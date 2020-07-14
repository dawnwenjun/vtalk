package com.talk.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
	private static final String driver="com.mysql.jdbc.Driver";
	private static final String url="jdbc:mysql://localhost:3306/talk?useUnicode=true&characterEncoding=UTF-8";
	private static final String username="root";
	private static final String password="1234";
	
	private static Connection conn=null;
	

	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		if(conn==null) {
			conn=DriverManager.getConnection(url,username,password);
			return conn;
		}
		return conn;
	}
	public static void main(String[] args) {
		try {
			Connection conn = DBHelper.getConnection();
			if(conn!=null) {
				System.out.println("连接数据库成功");
			}else
			{
				System.out.println("连接数据库失败");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
}
