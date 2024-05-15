package databaseConnection;

import java.sql.*;

public class PlatformDBConn implements DBconnection{

	private Connection conn;
	private Statement stat;
	private String query;
	
	public PlatformDBConn() {
		try{
			conn = DriverManager.getConnection(url, username, password);
			stat = conn.createStatement();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printTable() {
		try{
			query = "SELECT ID,	Name, Birth, Height, Position FROM `LE SSERAFIM`";
			stat.execute(query);// execute 給定的 SQL 語句，該語句可能返回多個結果。
			ResultSet result = stat.getResultSet(); // 以 ResultSet 物件的形式獲取當前結果。
			showResultSet(result); // ResultSet為表示資料庫結果集的資料表
			result.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showResultSet(ResultSet result) throws SQLException {
		ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			System.out.printf("%15s", metaData.getColumnLabel(i));
		}
		System.out.println();
		while (result.next()) {
			for (int i = 1; i <= columnCount; i++) {
				System.out.printf("%15s", result.getString(i));
			}
			System.out.println();
		}
	}
}

