package com.mentality.yun.util;/*
 * @author:一身都是月~
 * @date：2020/10/31 15:43
 * */

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private static DataSource dataSource;

    static {
        Properties properties = new Properties();
        try {
            properties.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
