package com.study.excel.test2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.internal.build.AllowSysOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.excel.test2.dto.TC2;
import com.study.excel.test2.mapper.TestRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tc1")
@RequiredArgsConstructor
public class TestController2 {

	
	@RequestMapping("test1")
	public JSONArray test1() throws Exception {


		        File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
		    	  FileInputStream fis = new FileInputStream(file);
		    	  Workbook workbook = new XSSFWorkbook(fis);
		    	  Sheet sheet = workbook.getSheetAt(1);
		          Iterator<Row> rows = sheet.iterator();
		          JSONArray jsonArray = new JSONArray();
		          int startRowIndex = 3; // Start from 4th row
		          int startKeyColIndex = 2; // Start from 3rd column
		          int startValColIndex = 0; // Start from 1st column
		          String[] keys = null;
		          while (rows.hasNext()) {
		              Row nextRow = rows.next();
		              if (nextRow.getRowNum() == startRowIndex) {
		                  keys = new String[nextRow.getLastCellNum()];
		                  for (int i = startKeyColIndex; i < nextRow.getLastCellNum(); i++) {
		                    keys[i] = getCellValue(nextRow.getCell(i)).toString();
		                  }
		              } else if (nextRow.getRowNum() > startRowIndex && keys != null) {
		                  Map<String, Object> map = new LinkedHashMap<>();
		                  for (int i = startKeyColIndex; i < nextRow.getLastCellNum(); i++) {
		                    map.put(keys[i], getCellValue(nextRow.getCell(i)));
		                  }
		                  List<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());
		                  Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
		                    public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
		                        return o1.getKey().compareTo(o2.getKey());
		                    }
		                  });
		                  Map<String, Object> sortedMap = new LinkedHashMap<>();
		                  for (Map.Entry<String, Object> entry : list) {
		                    sortedMap.put(entry.getKey(), entry.getValue());
		                  }
		                  JSONObject jsonObject = new JSONObject(sortedMap);
		                  jsonArray.add(jsonObject);
		              }
		          }
		          System.out.println(jsonArray.toJSONString());
		          return jsonArray;
		      }
	
	
	 private static Object getCellValue(Cell cell) {
	      switch (cell.getCellType()) {
	          case Cell.CELL_TYPE_STRING:
	              return cell.getStringCellValue();
	          case Cell.CELL_TYPE_NUMERIC:
	              return cell.getNumericCellValue();
	          case Cell.CELL_TYPE_BOOLEAN:
	              return cell.getBooleanCellValue();
	          case Cell.CELL_TYPE_FORMULA:
	              return cell.getNumericCellValue();
	          case Cell.CELL_TYPE_BLANK:
	              return "";
	          default:
	              return cell.toString();
	      }
	  }
	
	@RequestMapping("test2")
	public JSONArray test2(@RequestBody TC2 tc2) throws Exception{
		
		
		JSONArray array1 = tc2.getJarr1();
		JSONArray array2 = tc2.getJarr2();

        // 결과를 담을 JSON 배열
        JSONArray resultArray = new JSONArray();
        
        JSONParser jsonParser = new JSONParser();
        ObjectMapper mapper = new ObjectMapper();

        // 첫 번째 배열의 각 객체에 대해 반복
        for (int i = 0; i < array1.size(); i++) {
            // 첫 번째 배열에서 객체 가져오기
            JSONObject obj1 = (JSONObject) jsonParser.parse(mapper.writeValueAsString(array1.get(i)));

            // 첫 번째 배열의 객체에서 'no' 키의 값 가져오기
            String noValue = (String)obj1.get("no");

            // 두 번째 배열에서 해당하는 키의 값 가져오기
            String matchingValue = getMatchingValue(array2, noValue, tc2.getTime());

            // 새로운 객체 생성
            JSONObject newObject = new JSONObject();
            newObject.put("no", noValue);
            newObject.put("x", obj1.get("x"));
            newObject.put("y", obj1.get("y"));
            newObject.put("z", obj1.get("z"));
            newObject.put("voltage", Double.parseDouble(matchingValue));

            // 결과 배열에 추가
            resultArray.add(newObject);
        }

        // 결과 출력
        System.out.println(resultArray);
        
        return resultArray;
		
	}
	
	   // 두 번째 배열에서 특정 키의 값 찾기
    public String getMatchingValue(JSONArray array, String key, String time) throws Exception  {
    	  JSONParser jsonParser = new JSONParser();
          ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < array.size(); i++) {
        	if(time!=null) {
            JSONObject obj = (JSONObject) jsonParser.parse(mapper.writeValueAsString(array.get(i)));
            if (obj.containsKey(key)) {
                if (time.equals(obj.get("time"))) {
                    return (String) obj.get(key);
                }
                }
            }else {
            	  JSONObject obj = (JSONObject) jsonParser.parse(mapper.writeValueAsString(array.get(array.size()-1)));
                  if (obj.containsKey(key)) {
                	  return (String) obj.get(key);
                  }
            }
        }
        return null; // 일치하는 값이 없을 경우
    }
	
}
	

		
