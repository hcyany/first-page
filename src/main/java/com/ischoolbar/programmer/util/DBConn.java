package com.ischoolbar.programmer.util;


import java.sql.*;

public class DBConn {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * �������ݿ�
     */
    public static Connection getConnection() {
        try {
            Class.forName(Contents.driver);        //����mysql����
            System.out.println(Contents.driver + "���سɹ���");
        } catch (ClassNotFoundException e) {
            System.out.println(Contents.driver + "����ʧ��(�s�n�t)b");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(Contents.url, Contents.username, Contents.password);        //�������ݿ�
            System.out.println(Contents.url + "���ӳɹ���");
        } catch (SQLException e) {
            System.out.println(Contents.url + "����ʧ��(�s�n�t)b");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * �ر����ݿ�����
     *
     */
    public static void closeConnection() {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }        //�ر����ݿ�
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