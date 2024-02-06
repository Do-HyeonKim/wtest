package com.study.excel.test2.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;



@Service
public class jdbcConnect {

	
	public static class DatabaseConnection {
	    private static Connection connection;

	    public  Connection getConnection() {
	        if (connection == null) {
	            try {
	                String url = "jdbc:postgresql://localhost:5432/your_database";
	                String username = "your_username";
	                String password = "your_password";

	                connection = DriverManager.getConnection(url, username, password);
	                return connection; // 커넥션 성공 시 true 대신 커넥션을 리턴
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return null; // 커넥션 생성 실패 시 null 리턴
	    }
	}


//		1. 쿼리 실행
//		@PostMapping("/connect-db")
//	    public JSONArray connectToDatabase(@RequestBody DatabaseInfo databaseInfo) {
		public JSONArray executeQuery() {
//	        String host = databaseInfo.getHost();
//	        String username = databaseInfo.getUsername();
//	        String password = databaseInfo.getPassword();

			
            String host = "localhost:5432";
            String database = "";
            String username = "your_username";
            String password = "your_password";
            
            
	        JSONArray jsonArray = new JSONArray();

	        try {
	            String jdbcUrl = "jdbc:mysql://" + host + "/" +database;
	            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT * FROM your_table");

	            ResultSetMetaData metaData = resultSet.getMetaData();
	            int columnCount = metaData.getColumnCount();

	            while (resultSet.next()) {
	                JSONObject jsonObject = new JSONObject();
	                for (int i = 1; i <= columnCount; i++) {
	                    String columnName = metaData.getColumnName(i);
	                    String columnValue = resultSet.getString(i);
	                    jsonObject.put(columnName, columnValue);
	                }
	                jsonArray.add(jsonObject);
	            }

	            resultSet.close();
	            statement.close();
	            connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // 오류 처리 필요
	        }

	        return jsonArray;
	    }


//	2. map list로 리턴
	    public static List<Map<String, Object>> fetchDataFromDatabase() {
	        List<Map<String, Object>> result = new ArrayList<>();
	        String query = "SELECT column_name FROM your_table";

	        try (
	            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/your_database", "your_username", "your_password");
	            PreparedStatement statement = connection.prepareStatement(query);
	            ResultSet resultSet = statement.executeQuery();
	        ) {
	            ResultSetMetaData metaData = resultSet.getMetaData();
	            int columnCount = metaData.getColumnCount();

	            while (resultSet.next()) {
	                Map<String, Object> row = new HashMap<>();
	                for (int i = 1; i <= columnCount; i++) {
	                    String columnName = metaData.getColumnName(i);
	                    Object columnValue = resultSet.getObject(i);
	                    row.put(columnName, columnValue);
	                }
	                result.add(row);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return result;
	    }
	}



