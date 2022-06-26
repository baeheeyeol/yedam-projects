package com.yedam.app.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DAO {
	// Oracle DB 정보
	private String jdbc_driver;
	private String oracle_url;
	private String connectedId;
	private String connectedPwd;

	// 공통으로 사용되는 필드
	protected Connection conn;
	protected Statement stmt;
	protected PreparedStatement pstmt;
	protected ResultSet rs;

	public DAO() {
		dbConfig();
	}

	public void connect() {
		try {
			Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(oracle_url, connectedId, connectedPwd);
//		conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr","hr");
//			conn = DriverManager.getConnection(oracle_url, "hr","hr");
		} catch (ClassNotFoundException e) {
			System.out.println("jdbc driver 로딩 실패");
		} catch (SQLException e) {
			System.out.println("DB 연결실패");
		}
	}

	private void dbConfig() {
		String resource = "config/db.properties";
		Properties properties = new Properties();
		try {
			String filePath = ClassLoader.getSystemClassLoader().getResource(resource).getPath();
			properties.load(new FileInputStream(filePath));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		properties.setProperty(resource, resource);
		jdbc_driver = properties.getProperty("driver");
		oracle_url = properties.getProperty("url");
		connectedId = properties.getProperty("id");
		connectedPwd = properties.getProperty("password");
	}

	// DB 접속을 해제하는 메소드
	public void disconnect() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
