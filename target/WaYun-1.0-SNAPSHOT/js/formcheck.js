/*
    需要校验的项目
    1. 用户名校验  ---- 8~20位字符
    2. 密码   - --- 8~20位
    3. 邮件  ---- 邮件格式
    4. 姓名 ----非空
    5. 手机  --- 11位数字
    6. 出生日期  --- 非空
    7. 验证码  --- 非空
     */

function checkUserName() {
    var name = $("#username").val();
    var reg_name = /^\w{8,20}$/;
    var flag = reg_name.test(name);
    if (!flag) {
        // 如果格式不正确，则将输入框边框变红
        $("#username").css("border", "1px solid red");
        return flag;
    } else {
        // 格式正确则显示正常
        $("#username").css("border", "");
    }
    // 通过异步请求查询数据库中是否已经存在该用户名
    $.get("user/checkedUsername", {username: name}, function (data) {
        // data 为一个resultInfo 对象
        flag = data.flag;
        $("#errorMsg").html(data.errorMsg);
        if (!flag) {
            // 如果用户名已经存在，则将输入框边框变红
            $("#username").css("border", "1px solid red");
        } else {
            // 用户名可用
            $("#username").css("border", "");
        }
    })
    return flag;
}

function checkName() {
    var name = $("#name").val();
    var reg_name = /^\S+$/;

    var flag = reg_name.test(name);
    if (!flag) {
        $("#name").css("border", "1px solid red");
    } else {
        $("#name").css("border", "");
    }
    return flag;
}

function checkPassword() {
    var password = $("#password").val();
    var reg_password = /^\w{8,20}$/;
    var flag = reg_password.test(password);
    if (flag) {
        $("#password").css("border", "");
    } else {
        $("#password").css("border", "1px solid red");
    }
    return flag;
}

function checkEmail() {
    var email = $("#email").val();
    // 邮箱正则： “+”表示至少出现一次   “\.”表示出现一次点，这里表示转义字符
    var reg_email = /^\w+@\w+\.\w+$/;
    var flag = reg_email.test(email);
    if (flag) {
        $("#email").css("border", "");
    } else {
        $("#email").css("border", "1px solid red");
    }
    return flag;
}

function checkTelephone() {
    var telephone = $("#telephone").val();
    var reg_tel = /^\d{11,11}$/;
    var flag = reg_tel.test(telephone);
    if (flag) {
        $("#telephone").css("border", "");
    } else {
        $("#telephone").css("border", "1px solid red");
    }
    return flag;
}

function checkBirthday() {
    var bir = $("#birthday").val();
    var reg_bir = /^\S+$/;

    var flag = reg_bir.test(bir);
    if (!flag) {
        $("#birthday").css("border", "1px solid red");
    } else {
        $("#birthday").css("border", "");
    }
    return flag;
}

function checkCheck() {
    var check = $("#check").val();
    var reg_check = /^\S+$/;
    var flag = reg_check.test(check);
    if (!flag) {
        $("#check").css("border", "1px solid red");
    } else {
        $("#check").css("border", "");
    }
    return flag;
}