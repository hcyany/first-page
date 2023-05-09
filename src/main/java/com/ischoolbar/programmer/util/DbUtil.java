package com.ischoolbar.programmer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private String dbUrl = "jdbc:mysql://localhost:3306/student?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true";
    private String dbUser = "root";
    private String dbPassword = "hcy080325...";
    private String jdbcName = "com.mysql.cj.jdbc.Driver";
    private Connection connection = null;
    //????????????
    public Connection getConnection(){
    	try {
			Class.forName(jdbcName);
			connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
			System.out.println("----------------数据库开启成功------------------");
		} catch (Exception e) {
			System.out.println("----------------数据库开启失败------------------");
			e.printStackTrace();
		}
    	return connection;
    }
    //??????????
    public void closeCon(){
    	if(connection != null)
			try {
				connection.close();
				System.out.println("----------------数据库关闭----------------");
			} catch (SQLException e) {
				e.printStackTrace();
			}
    }
	public static void main(String[] args) {
		//????????????
		DbUtil dbUtil = new DbUtil();
		dbUtil.getConnection();
		dbUtil.closeCon();

	}

}
