package com.study.excel.test2.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tc3")
@RequiredArgsConstructor
public class TestController3 {
	
	@RequestMapping("/test")
    public  JSONArray test(@RequestPart("file") MultipartFile file) {
        JSONArray jsonArray = new JSONArray();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")  && !line.trim().isEmpty()) {
                    String[] parts = line.split("\\s+"); // " " 공백으로 자르기
                    System.out.println(parts.length);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("key1", Integer.parseInt(parts[0]));
                    jsonObject.put("key2", Integer.parseInt(parts[1]));
                    jsonObject.put("key3", Integer.parseInt(parts[2]));
                    jsonObject.put("key4", Double.parseDouble(parts[3]));
                    jsonObject.put("key5", Integer.parseInt(parts[4]));
                    jsonArray.add(jsonObject);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(jsonArray.toString());
        
        return jsonArray;
    }
	
	
	@RequestMapping("write")
	 public boolean write(@RequestBody List<Object> jsonData) throws IOException {
		System.out.println(jsonData);
	        // JSON 데이터를 .inp 파일로 작성
	        try (FileWriter writer = new FileWriter("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\new_file.inp")) {
	            for (Object data : jsonData) {
	            	 LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) data;
	                 StringBuilder line = new StringBuilder();
	                 line.append(map.get("key1").toString()).append(" ");
	                 line.append(map.get("key2").toString()).append(" ");
	                 line.append(map.get("key3").toString()).append(" ");
	                 line.append(map.get("key4").toString()).append(" ");
	                 line.append(map.get("key5").toString());
	                 writer.write(line.toString() + "\n");
	                }
	            }
	            return true;
	        }
	 
	 
	
	@RequestMapping("test2")
    public  JSONArray test2() {
        JSONArray jsonArray = new JSONArray();

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\test.inp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")  && !line.trim().isEmpty()) {
                    String[] parts = line.split("\\s+"); // " " 공백으로 자르기
                    System.out.println(parts.length);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("key1", Integer.parseInt(parts[0]));
                    jsonObject.put("key2", Integer.parseInt(parts[1]));
                    jsonObject.put("key3", Integer.parseInt(parts[2]));
                    jsonObject.put("key4", Double.parseDouble(parts[3]));
                    jsonObject.put("key5", Integer.parseInt(parts[4]));
                    jsonArray.add(jsonObject);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(jsonArray.toString());
        
        return jsonArray;
    }

}
