package com.ischoolbar.programmer.util;


import java.sql.*;

public class DBConn {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * 连接数据库
     */
    public static Connection getConnection() {
        try {
            Class.forName(Contents.driver);        //加载mysql驱动
            System.out.println(Contents.driver + "加载成功！");
        } catch (ClassNotFoundException e) {
            System.out.println(Contents.driver + "加载失败(╯﹏╰)b");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(Contents.url, Contents.username, Contents.password);        //连接数据库
            System.out.println(Contents.url + "连接成功！");
        } catch (SQLException e) {
            System.out.println(Contents.url + "连接失败(╯﹏╰)b");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     */
    public static void closeConnection() {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }        //关闭数据库
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}