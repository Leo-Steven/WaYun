package com.mentality.yun.web.servlet;


import com.mentality.yun.domain.ResultInfo;
import com.mentality.yun.domain.TransferInfo;
import com.mentality.yun.domain.User;
import com.mentality.yun.domain.UserFile;
import com.mentality.yun.service.FileService;
import com.mentality.yun.service.UserService;
import com.mentality.yun.service.impl.FileServiceImpl;
import com.mentality.yun.service.impl.UserServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

@WebServlet(name = "FileServlet", urlPatterns = "/file/*")
public class FileServlet extends BaseServlet {
    private final FileService fileService = new FileServiceImpl();
    private final UserService userService = new UserServiceImpl();
    List<TransferInfo> downloadList = new ArrayList<>();
    List<TransferInfo> uploadList = new ArrayList<>();
    private int downloadTaskId;
    private int uploadTaskId;

    /**
     * 用于文件上传的servleet
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 这里采取在用户本地创建一个上传文件的副本，即先将接收到的二进制文件在用户本地创建，然后将路径封装为对象

        ResultInfo resultInfo = new ResultInfo();
        ServletContext servletContext = request.getServletContext();
        // 获取当前用户对象
        User current_user = (User) request.getSession().getAttribute("current_user");

        UserFile userFile = new UserFile();
        DiskFileItemFactory fc = new DiskFileItemFactory();
        ServletFileUpload su = new ServletFileUpload(fc);
        try {
            // 1. 文件本地副本拷贝
            List<FileItem> list = su.parseRequest(request);
            for (FileItem fileItem : list) {
                if (!fileItem.isFormField()) {
                    InputStream inputStream = fileItem.getInputStream();

                    // 1.1 将上传文件夹放在桌面
                    String sep = File.separator;
                    //  String path = servletContext.getRealPath(sep+"upload"+sep+current_user.getUid());
                    String path = "C:\\Users\\Lenovo\\Desktop" + sep + "WaYun" + sep + "uploading" + sep + current_user.getUid();
                    String servicePath = "C:\\Users\\Lenovo\\Desktop" + sep + "upload" + sep + current_user.getUid();
                    System.out.println(path);

                    // 2. 封装userFile对象


                    /*
                    2.1 对应设置文件的类别id，并设置分类存储
                    获取文件类型，大类型  ----  MimeType 前半部分
                    image  ---------  1
                    audio  ---------  2
                    video  ---------  3
                    text/application   ---------  4
                    other --------  5
                    */

                    String mimeType = servletContext.getMimeType(fileItem.getName());
                    String fType = mimeType.split("/")[0];
                    if ("image".equals(fType)) {
                        userFile.setCid(1);
                        path += sep + "images";
                        servicePath += sep + "images";
                    } else if ("audio".equals(fType)) {
                        userFile.setCid(2);
                        path += sep + "audios";
                        servicePath += sep + "audios";
                    } else if ("video".equals(fType)) {
                        userFile.setCid(3);
                        path += sep + "videos";
                        servicePath += sep + "videos";
                    } else if ("text".equals(fType) || "application".equals(fType)) {
                        userFile.setCid(4);
                        path += sep + "documents";
                        servicePath += sep + "documents";
                    } else {
                        userFile.setCid(5);
                        path += sep + "others";
                        servicePath += sep + "others";
                    }


                    File parent = new File(path);
                    // 创建的是本地文件的副本
                    File file = new File(parent, fileItem.getName());
                    servicePath = servicePath + sep + fileItem.getName();

                    // 1.2 如果不存在用户对应的文件夹，并创建对应的文件夹
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    // 1.3 判断文件是否存在
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    OutputStream outputStream = new FileOutputStream(file);

                    // 2.2 设置文件名字
                    userFile.setFname(fileItem.getName());

                    // 2.3 设置文件上传后存储的服务器地址
                    userFile.setFaddress(servicePath.substring(servicePath.indexOf("upload")));


                    //  2.4 设置对应的用户id
                    userFile.setUid(current_user.getUid());

                    // 2.5 设置文件的上传日期
                    DateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月-dd日 hh:mm:ss");
                    userFile.setDate(dateFormat.format(new Date()));

                    // 3. 调用servlet进行数据库相关信息存储
                    boolean flag = fileService.save(userFile);

                    // 1.4 实现用户本地副本的创建
                    IOUtils.copy(inputStream, outputStream);
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);

                    // 调用方法进行文件上传到服务器端
                    TransferInfo transferInfo = new TransferInfo(0, file.length(), file, new File(servicePath), false);
                    transferInfo.setTaskId(uploadTaskId++);
                    uploadList.add(transferInfo);
                    transfer(transferInfo, getLimits(current_user.getVip()));

                    if (flag) {
                        resultInfo.setFlag(flag);
                    } else {
                        resultInfo.setFlag(flag);
                        resultInfo.setErrorMsg("上传失败！！");
                    }
                }
            }
        } catch (FileUploadException e) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("服务器出现错误");
            System.out.println("error error error~~");
            e.printStackTrace();
        }
    }

    /**
     * 用于文件下载的servlet
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vip = ((User) request.getSession().getAttribute("current_user")).getVip();
        int limits = getLimits(vip);

        // 获取需要进行操作的 文件id
        int fid = Integer.parseInt(request.getParameter("fid"));

        // 调用service 获取对应地址
        UserFile file = fileService.getPathByFid(fid);
        // 将需要下载的文件封装为TransferInfo加入到  downloadFiles 集合中
            /*
            这里将文件下载的目录结构设置为与上传时的目录一样，并放在桌面
             数据库中存储的地址格式为   upload\9\images\5be3af2550C3OK1.jpg， 这里新文件的路径需要截取upload后面的
             */
        String sep = File.separator;
        String path = "C:" + sep + "Users" + sep + "Lenovo" + sep + "Desktop" + sep;
        String realPath = path + file.getFaddress();
        File oldFile = new File(realPath);
        File newFile = new File(path + "WaYun" + sep + "download" + file.getFaddress().substring(file.getFaddress().indexOf('\\')));
        TransferInfo transferInfo = new TransferInfo(0, oldFile.length(), oldFile, newFile, true);
        transferInfo.setTaskId(downloadTaskId++);
        if (newFile.exists()) {
            // 如果文件需要下载的文件已经存在
            transferInfo.setFinished(true);
            transferInfo.setTransfer(false);
            downloadList.add(transferInfo);
        } else {
            downloadList.add(transferInfo);
            // 添加到下载任务的集合中
            transfer(transferInfo, limits);
        }
    }

    /**
     * 用于查询用户文件的servlet
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserFile> list = null;
        // 1. 获取当前用户id
        User user = (User) request.getSession().getAttribute("current_user");
        int uid = user.getUid();
        // 1. 获取请求文件类型的cid
        int cid = Integer.parseInt(request.getParameter("cid"));

        // 2. 调用对应的service方法，获取list集合
        if (cid == 0) {
            list = fileService.findAll(uid);
        } else {
            list = fileService.findAllByCid(uid, cid);
        }

        // 3. 解析为json数据返回前台
        this.writeValue(response, list);
    }

    /**
     * 用于删除所选文件的servlet
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取选中的文件
        int fid = Integer.parseInt(request.getParameter("fid"));

        // 调用fileService获取文件地址，并将数据库中对应的储存进行删除
        String sep = File.separator;
        String path = "C:" + sep + "Users" + sep + "Lenovo" + sep + "Desktop" + sep;
        // 需要删除服务器端的文件
        UserFile userFile = fileService.findByFid(fid);
        fileService.delByFid(fid);

        // 将本地文件进行删除
        path += userFile.getFaddress();
        File file = new File(path);
        boolean delete = file.delete();
        if (delete) {
            System.out.println("文件删除成功");
        } else {
            System.out.println("文件删除失败！");
        }
    }

    /**
     * 下载和上传调用的 文件传输方法
     *
     * @param transferInfo 将文件传送封装为一个 TransferInfo对象
     * @param limits       最高下载/上传速度
     * @throws IOException
     */
    public void transfer(TransferInfo transferInfo, int limits) throws IOException {
        File newFile = transferInfo.getNewFile();
        File parent = new File(newFile.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }
        newFile.createNewFile();
        // 关联文件
        RandomAccessFile oldFileAccess = new RandomAccessFile(transferInfo.getOldFile(), "r");
        RandomAccessFile newFileAccess = new RandomAccessFile(transferInfo.getNewFile(), "rw");
        System.out.println("length:" + transferInfo.getOldFile().length());
        System.out.println("endindex:" + transferInfo.getEndIndex());

        // 对应输出位置和查询位置
        oldFileAccess.seek(transferInfo.getStartIndex());
        newFileAccess.seek(transferInfo.getStartIndex());

        // 开始读取
        byte[] bytes = new byte[1024];
        int len, count = 0, i = 0;
        // 当文件还未读到末尾时进入循环
        // 进行获取时间毫秒值
        while (transferInfo.isTransfer() && (len = oldFileAccess.read(bytes)) != -1) {
            // 判断是否超过endIndex
            if (oldFileAccess.getFilePointer() > transferInfo.getEndIndex()) {
                // 超过时，则将未超过部分传输，并跳出循环
                len = Math.toIntExact(len - oldFileAccess.getFilePointer() - transferInfo.getEndIndex());
                transferInfo.setCount(transferInfo.getCount() + len);
                newFileAccess.write(bytes, 0, len);
                break;
            }
            // 未超过则直接进行读取
            newFileAccess.write(bytes, 0, len);
            transferInfo.setCount(transferInfo.getCount() + len);
            count++;
            // 由于计算机读取 1024个字节使用的时间可以忽略不记，而 1mb/s 又近似等于 1024字节/ms，所以通过读取的1024字节的次数来进行睡眠 1 ms
            if (count >= limits / 1024) {
                try {
                    // 为了效果比较明显，就 sleep 200毫秒
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                count = 0;
            }
        }
        if (transferInfo.getCount() == transferInfo.getEndIndex()) {
            // 如果传输的总字节数等于文件的任务的总字节数，则传输完成
            transferInfo.setFinished(true);
            System.out.println("文件传输完成");

            // 如果是文件上传文件，则还需要删除对应的本地上传列表中对应的文件
            if (!transferInfo.isDownload()) {
                transferInfo.getOldFile().delete();
            }

        } else {
            // 设置下次文件传输的开始位置 为 已经传输的字节末的下标
            transferInfo.setStartIndex(transferInfo.getCount());
            System.out.println("文件下载暂停");
        }
    }

    /**
     * 获取最高下载/上传速度
     *
     * @param vip 是否为vip
     * @return 最高速度
     */
    public int getLimits(String vip) {
        // 0.5mb/s 约等于 524字节/ms  ------  1mb/s 约等于 1024字节/ms
        int limits = 0;
        boolean flag = "Y".equals(vip);
        if (flag) {
            limits = 2048;
        } else {
            limits = 1024;
        }
        return limits;
    }

    public void changeProcess(HttpServletRequest request, HttpServletResponse response) {
        String vip = ((User) request.getSession().getAttribute("current_user")).getVip();
        // 获取到文件信息id
        int fid = Integer.parseInt(request.getParameter("fid"));
        int cid = Integer.parseInt(request.getParameter("cid"));
        List<TransferInfo> list = cid == 0 ? downloadList : uploadList;
        list.forEach((transferInfo) -> {
                    // 进行文件传输暂停的切换
                    if (transferInfo.getTaskId() == fid) {
                        transferInfo.setTransfer(!transferInfo.isTransfer());

                        // 如果是切换为继续，则调用tansfer方法
                        if (transferInfo.isTransfer()) {
                            try {
                                transfer(transferInfo, getLimits(vip));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    /**
     * 通过传入的任务类别ID，将对应的集合封装 json 返回
     *
     * @param request  request
     * @param response respinse
     * @throws ServletException
     * @throws IOException
     */
    public void findTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取当前所要查询的任务类别
        int cid = Integer.parseInt(request.getParameter("cid"));

        // 2. 将当前请求的集合写回前台页面
        if (cid == 0) {
            // 请求下载任务
            this.writeValue(response, downloadList);
        } else {
            // 请求上传任务
            this.writeValue(response, uploadList);
        }
    }

    public void delTask(HttpServletRequest request, HttpServletResponse response) {
        /*
        首先都是删除对应的List中的transferInfo对象
        1. 上传任务
        如果是未完成的任务则是删除 数据库中的记录，以及服务器上的未完成任务
        2. 下载任务
        如果未完成，则是删除本地的文件，

        已完成的任务则是单纯的删除任务记录
         */
        int fid = Integer.parseInt(request.getParameter("fid"));
        int cid = Integer.parseInt(request.getParameter("cid"));

        User user = (User) request.getSession().getAttribute("current_user");
        List<TransferInfo> list = cid == 0 ? downloadList : uploadList;
        TransferInfo task = null;
        for (TransferInfo transferInfo : list) {
            if (transferInfo.getTaskId() == fid) {
                // 1. 获取需要删除的任务信息
                task = transferInfo;
                break;
            }
        }

        // 2. 如果任务未完成则删除服务器/用户文件
        list.remove(task);
        if (!task.isFinished()) {
            task.getNewFile().delete();
            // 如果是上传文件中的则需要删除数据库中的文件信息以及用户预备上传文件夹中对应的文件
            if (!task.isDownload()) {
                boolean delete = task.getOldFile().delete();
                if (delete) {
                    System.out.println("文件删除成功");
                } else {
                    System.out.println("文件删除失败");
                }
                fileService.delByUidAndFilename(user.getUid(), task.getNewFile().getName());
            }
        }
    }


}


    /*public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        //.1.获取请求参数
        String[] checkeds = request.getParameterValues("checked");
        int[] fids = new int[checkeds.length];
        for (int i = 0; i < checkeds.length; i++) {
            fids[i] = Integer.parseInt(checkeds[i]);
        }
        // 调用service 获取对应地址
        UserFile[] files = fileService.getPathByFids(fids);
        // 2.2 用字节流关联
        String sep = File.separator;
        String path = "C:"+sep+"Users"+sep+"Lenovo"+sep+"Desktop"+sep;
        for (int i = 0; i < files.length; i++) {
            String realPath = path+files[i].getFaddress();
            FileInputStream fileInputStream=new FileInputStream(realPath);
            String filename = files[i].getFname();
            //3. 设置response响应头
            // 3.1 设置响应头类型：content-type
            String mimeType = servletContext.getMimeType(filename);
            response.setHeader("content-type",filename);
            // 3.2 设置响应头打开方式：content-disposition
            response.setHeader("content-disposition","attachment;filename="+filename);
            //4. 将输入流的数据输出到输出流中
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buff=new byte[1024*8];
            int len=0;
            while((len=fileInputStream.read(buff))!=-1){
                outputStream.write(buff,0,len);
            }
            fileInputStream.close();
        }

    }*/
