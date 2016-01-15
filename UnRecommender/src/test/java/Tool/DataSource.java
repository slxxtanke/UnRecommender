package Tool;

import java.sql.Connection;

public class DataSource {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost/TEST_YL2?useUnicode=true&characterEncoding=utf8";
	private static String user = "root";
	private static String pwd = "rootnouse";
	
	public static Connection getConn() throws Exception{		
		Class.forName(driver).newInstance();
		Connection conn = null;
		if(user == null || user.equals("")){
			conn = java.sql.DriverManager.getConnection(url);
		}else{
			conn = java.sql.DriverManager.getConnection(url, user, pwd);
		}
		return conn;
	}	
}
