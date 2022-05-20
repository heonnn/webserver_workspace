package common;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JdbcTemplate
 * 
 * Jdbc driver 등록
 * Connection 객체 생성
 * commit, rollback, close 예외 처리
 *
 */
public class JdbcTemplate {
	static String driverClass;
	static String url;
	static String user;
	static String password;

	// jdbc driver 등록
	static {
		Properties prop = new Properties();
		try {
			// jdbc data(driver, connection 관련 data) 로드 후 값대입
			String fileName = JdbcTemplate.class.getResource("/jdbc/jdbcData.properties").getPath();
			prop.load(new FileReader(fileName));
			
			driverClass = prop.getProperty("driverClass");
			url= prop.getProperty("url");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			Class.forName(driverClass);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Connection 예외처리 
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	// commit & rollback 예외처리
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.commit();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// close 예외처리
	public static void close(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt != null && !pstmt.isClosed()) pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close(ResultSet rset) {
		try {
			if(rset != null && !rset.isClosed()) rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
