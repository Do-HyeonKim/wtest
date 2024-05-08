package com.study.excel.test2.controller;

import java.awt.SystemColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.internal.build.AllowSysOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
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
		System.out.println("zz");
		
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
            Object matchingValue = getMatchingValue(array2, noValue, tc2.getTime());
            // 새로운 객체 생성
            JSONObject newObject = new JSONObject();
            newObject.put("no", noValue);
            newObject.put("x", obj1.get("x"));
            newObject.put("y", obj1.get("y"));
            newObject.put("z", obj1.get("z"));
            newObject.put("voltage", matchingValue);
            
            // 결과 배열에 추가
            resultArray.add(newObject);
        }

        
        
        // 결과 출력
        System.out.println(resultArray);
       
        
        // 추가 데이터를 생성합니다.
        
        JSONArray addData = new JSONArray();
        
        
        addData = interpolateZLevels(resultArray,100);
//        generateAddedData(resultArray, addData);
//        addAddedDataToOriginalData(resultArray, addData);


        
        saveJsonToFile(addData, "C:\\Users\\cpflv\\Downloads\\output.json");
        
//        return resultArray;
        return addData;
      
		
	}
	
	
	
	
	   // 두 번째 배열에서 특정 키의 값 찾기
    public Object getMatchingValue(JSONArray array, String key, String time) throws Exception  {
    	  JSONParser jsonParser = new JSONParser();
          ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < array.size(); i++) {
        	if(time!=null) {
            JSONObject obj = (JSONObject) jsonParser.parse(mapper.writeValueAsString(array.get(i)));
            if (obj.containsKey(key)) {
                if (Double.parseDouble(time)==(Double)obj.get("time")) {
                    return  obj.get(key);
                }
                }
            }else {
            	  JSONObject obj = (JSONObject) jsonParser.parse(mapper.writeValueAsString(array.get(array.size()-1)));
                  if (obj.containsKey(key)) {
                	  return obj.get(key);
                  }
            }
        }
        return null; // 일치하는 값이 없을 경우
    }
    
    
    private static void saveJsonToFile(JSONArray jsonArray, String fileName) {
        Path filePath = Paths.get(fileName);
        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
            fileWriter.write(jsonArray.toJSONString());
            System.out.println("JSON 데이터가 성공적으로 파일에 저장되었습니다. 파일 경로: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("JSON 데이터를 파일에 저장하는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    
    
    // 0113테스트 실패 
    private static void generateAddedData(JSONArray originalData, JSONArray addedData) {
    	   // 먼저 각 데이터 포인트 사이의 선형 보간을 수행합니다.
    	   for (int i = 0; i < originalData.size() - 1; i++) {
    	       JSONObject currentPoint = (JSONObject) originalData.get(i);
    	       JSONObject nextPoint = (JSONObject) originalData.get(i+1);
    	       double x1 = (double)currentPoint.get("x");
    	       double x2 = (double)nextPoint.get("x");
    	       double y1 = (double)currentPoint.get("y");
    	       double y2 = (double)nextPoint.get("y");
    	       double z1 = (double)currentPoint.get("z");
    	       double z2 = (double)nextPoint.get("z");
    	       double v1 = (double)currentPoint.get("voltage");
    	       double v2 = (double)nextPoint.get("voltage");
    	       for (int j = 0; j <= 100; j++) {
    	           double t = j / 100.0;
    	           double tempX = x1 + t * (x2 - x1);
    	           double tempY = y1 + t * (y2 - y1);
    	           double tempZ = z1 + t * (z2 - z1);
    	           double tempV = v1 + t * (v2 - v1);
    	           JSONObject tempPoint = new JSONObject();
    	           tempPoint.put("no", "temp" + (i * 100 + j));
    	           tempPoint.put("x", String.format("%.8f", tempX));
    	           tempPoint.put("y", String.format("%.8f", tempY));
    	           tempPoint.put("z", String.format("%.8f", tempZ));
    	           tempPoint.put("voltage", String.format("%.8f", tempV));
    	           addedData.add(tempPoint);
    	       }
    	   }
    	   // 그런 다음 각 데이터 포인트 사이의 선형 보간을 수행하여 직사각형 형태의 데이터를 생성합니다.
    	   for (int i = 0; i < addedData.size() - 1; i++) {
    	       JSONObject currentPoint = (JSONObject) addedData.get(i);
    	       JSONObject nextPoint = (JSONObject) addedData.get(i+1);
    	       double x1 = Double.parseDouble(currentPoint.get("x").toString());
    	       double x2 = Double.parseDouble(nextPoint.get("x").toString());
    	       double y1 =  Double.parseDouble(currentPoint.get("y").toString());
    	       double y2 = Double.parseDouble(nextPoint.get("y").toString());
    	       double z1 = Double.parseDouble(currentPoint.get("z").toString());
    	       double z2 = Double.parseDouble(nextPoint.get("z").toString());
    	       double v1 = Double.parseDouble(currentPoint.get("voltage").toString());
    	       double v2 = Double.parseDouble(nextPoint.get("voltage").toString());
    	       for (int j = 0; j <= 100; j++) {
    	           double t = j / 100.0;
    	           double tempX = x1 + t * (x2 - x1);
    	           double tempY = y1 + t * (y2 - y1);
    	           double tempZ = z1 + t * (z2 - z1);
    	           double tempV = v1 + t * (v2 - v1);
    	           JSONObject tempPoint = new JSONObject();
    	           tempPoint.put("no", "rect" + (i * 100 + j));
    	           tempPoint.put("x", String.format("%.8f", tempX));
    	           tempPoint.put("y", String.format("%.8f", tempY));
    	           tempPoint.put("z", String.format("%.8f", tempZ));
    	           tempPoint.put("voltage", String.format("%.8f", tempV));
    	           addedData.add(tempPoint);
    	       }
    	   }
    	}
    private static void addAddedDataToOriginalData(JSONArray originalData, JSONArray addedData) {
    	  int i = 0;
    	  while (i < originalData.size()) {
    	      int endIndex = (i + 1) * 6000 > addedData.size() ? addedData.size() : (i + 1) * 6000;
    	      if (i * 6000 >= endIndex) {
    	          break;
    	      }
    	      originalData.addAll(addedData.subList(i * 6000, endIndex));
    	      i++;
    	  }
    	}
    

    
    
    //0113 정렬 코드 추가한거ㅠ 
    public JSONArray interpolate(JSONObject point1, JSONObject point2, int steps) {
        JSONArray interpolatedPoints = new JSONArray();
        for (int i = 1; i < steps; i++) {
            double t = (double) i / steps;
            double x = (double)point1.get("x") + t * ((double)point2.get("x") - (double)point1.get("x"));
            double y = (double)point1.get("y") + t * ((double)point2.get("y") - (double)point1.get("y"));
            double z = (double)point1.get("z") + t * ((double)point2.get("z") - (double)point1.get("z"));
            double voltage = (double)point1.get("voltage") + t * ((double)point2.get("voltage") - (double)point1.get("voltage"));
            
            JSONObject interpolatedPoint = new JSONObject();
            interpolatedPoint.put("x", x);
            interpolatedPoint.put("y", y);
            interpolatedPoint.put("z", z);
            interpolatedPoint.put("voltage", voltage);
            
            interpolatedPoints.add(interpolatedPoint);
        }
        return interpolatedPoints;
    }
    
    

	public JSONArray interpolateZLevels(JSONArray initList, int steps) {
		List<JSONObject> list = new ArrayList<>();
		for (int i = 0; i < initList.size(); i++) {
			list.add((JSONObject) initList.get(i));
		}

		// Sort by z
		list.sort(Comparator.comparingDouble(a -> (double) a.get("z")));

		// Group by z and interpolate
		List<JSONObject> extendedInitList = new ArrayList<>();
		double lastZ = Double.MIN_VALUE;
		List<JSONObject> group = new ArrayList<>();

		for (JSONObject point : list) {
			double currentZ = (double) point.get("z");
			if (currentZ != lastZ) {
				if (!group.isEmpty()) {
					// Interpolate the group
					extendedInitList.addAll(interpolateGroup(group, steps));
					group.clear();
				}
				lastZ = currentZ;
			}
			group.add(point);
		}
		// Interpolate the last group
		if (!group.isEmpty()) {
			extendedInitList.addAll(interpolateGroup(group, steps));
		}

		Collections.sort(extendedInitList, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject a, JSONObject b) {
				int zCompare = Double.compare((double) b.get("z"), (double) a.get("z"));
				if (zCompare != 0)
					return zCompare;

				int yCompare = Double.compare((double) b.get("y"), (double) a.get("y"));
				if (yCompare != 0)
					return yCompare;

				return Double.compare((double) b.get("x"), (double) a.get("x"));
			}
		});

		JSONArray finalList = new JSONArray();
		for (JSONObject point : extendedInitList) {
			finalList.add(point);
		}

		return finalList;
	}

	private List<JSONObject> interpolateGroup(List<JSONObject> group, int steps) {
		List<JSONObject> interpolatedGroup = new ArrayList<>();

// Sort the group by x
		group.sort(Comparator.comparingDouble(a -> (double) a.get("x")));

// Interpolate within the group
		for (int i = 0; i < group.size() - 1; i++) {
			interpolatedGroup.add(group.get(i));
			JSONArray interpolatedPointsX = interpolate(group.get(i), group.get(i + 1), steps);
			for (int j = 0; j < interpolatedPointsX.size(); j++) {
				interpolatedGroup.add((JSONObject) interpolatedPointsX.get(j));
			}
		}
// Add the last point of the group
		interpolatedGroup.add(group.get(group.size() - 1));

		return interpolatedGroup;
	}
	
	@RequestMapping("test3")
	public JSONObject test3() {
		
		 File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\test.xlsx");
		 
		 
	        try (FileInputStream fis = new FileInputStream(file);
	                Workbook workbook = new XSSFWorkbook(fis)) {

	               // 시트 2 처리 : 전체 데이터
	               Sheet sheet2 = workbook.getSheetAt(0);
	               JSONArray jsonArraySheet2 = convertSheetToJsonArray(sheet2);

	               // 결과 JSON 객체 생성
	               JSONObject resultObject = new JSONObject();
	               resultObject.put("sheet2", jsonArraySheet2);

	               
	               // "sheet1" 키를  가진 배열을 생성합니다.
	               JSONArray jsonArraySheet1 = new JSONArray();

	               // points 배열을  선언합니다.
	               List<String> points = new ArrayList<>();
	               points.add("point39");
	               points.add("point40");
	               points.add("point41");
	               points.add("point42");
	               points.add("point43");
	               points.add("point44");
	               points.add("point45");
	               points.add("point46");
	               points.add("point47");
	               points.add("point48");
	               points.add("point49");
          
	               
	               
	               //이부분은 mps에서 처리할지 simulator에서 처리할지 생각해봐야함 
//	               for (int i =  0; i < jsonArraySheet2.size(); i++) {
	               for(Object object : jsonArraySheet2) {
//	                   JSONObject sheet2Object = (JSONObject) jsonArraySheet2.get(i);
	            	   JSONObject sheet2Object = (JSONObject) object; 
	                   JSONObject sheet1Object = new JSONObject();

	                   //객체 키 순회 
	                   Iterator<Map.Entry<String, Object>> iterator = sheet2Object.entrySet().iterator();
	                   while (iterator.hasNext()) {
	                       Map.Entry<String, Object> entry = iterator.next();
	                       String key = entry.getKey();
	                       // 키가 points 배열에 포함되는지 확인합니다.
//	                       if (Arrays.asList(points).contains(key)) {
	                    	   if (points.contains(key)) {
	                           // points 배열에 포함되면 "sheet1" 객체에 추가합니다.
	                           sheet1Object.put(key, entry.getValue());
	                       }
	                       // points 배열에 포함되지 않는 키는 "sheet1" 객체에  그대로 추가합니다.
	                        else if (!key.startsWith("point")) {
	                           sheet1Object.put(key, sheet2Object.get(key));
	                       }
	                   }

	                  
	                   jsonArraySheet1.add(sheet1Object);
	               }

	               // "sheet1" 배열을  원래의 JSON 객체에 추가합니다.
	               resultObject.put("sheet1", jsonArraySheet1);


	             return resultObject;
	             }
	        
	 catch (IOException e) {
	               e.printStackTrace();
	           }
	        return null;
	       }
	
		// sheet -> json 생성
	     private JSONArray convertSheetToJsonArray(Sheet sheet) {
	         JSONArray jsonArray = new JSONArray();
	         Row headerRow = sheet.getRow(0);

	         for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	             Row currentRow = sheet.getRow(i);
	             JSONObject jsonObject = new JSONObject();

	             for (int j = 0; j < headerRow.getLastCellNum(); j++) {
	                 Cell currentCell = currentRow.getCell(j);
	                 Cell headerCell = headerRow.getCell(j);
	                 jsonObject.put(cleanHeader(headerCell.getStringCellValue()), getCellValue2(currentCell));
	             }

	             jsonArray.add(jsonObject);
	         }

	         return jsonArray;
	     }


		 
	     //셀 가져오는거
	     private  Object getCellValue2(Cell cell) {
	         if (cell == null) {
	             return null;
	         }

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
	     
	     
	     private String cleanHeader(String header) {
	    	    header = header.replaceAll("\\[.*?\\]", "");

	    	    header = header.replaceAll("-", "");

	    	    header = header.toLowerCase();

	    	    return header.trim();
	    	}
	     
	     @RequestMapping("makeExcel")
	     public void makeExcel(@RequestBody JSONObject jboj) {

	    	 //파싱 안해주면 오류남 
	    	 JSONObject obj=(JSONObject)JSONValue.parse(jboj.toString()); 
	         
	    	 JSONArray sheet1Array = (JSONArray)obj.get("sheet1");
	         JSONArray sheet2Array = (JSONArray)obj.get("sheet2");

	         Workbook workbook = new XSSFWorkbook();

	         Sheet sheet1 = workbook.createSheet("Sheet1");
	         writeDataToSheet(sheet1, sheet1Array);

	         Sheet sheet2 = workbook.createSheet("Sheet2");
	         writeDataToSheet(sheet2, sheet2Array);

	         try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\cpflv\\workbook.xlsx")) {
	             workbook.write(fileOut);
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	     }
	     
	     private  void writeDataToSheet(Sheet sheet, JSONArray dataArray) {
	 
//	         JSONArray dataArray = (JSONArray)jboj.get("sheet1");
	         
//	         JSONObject headerObj = (JSONObject)dataArray.get(0);

	         if (dataArray.size() > 0) {
	                // 0번째 객체의 키값을 가져와서 헤더로 사용
	                JSONObject firstObject = (JSONObject)dataArray.get(0);
	                List<String> headers = new ArrayList<>();
	                for(Object key  : firstObject.keySet()) {
	                	headers.add(key.toString());
	                }
	                
	                
	                // 헤더를 알파벳 순으로 정렬
	                Collections.sort(headers);
	                
	                // 첫 번째 행에 헤더 추가
	                Row headerRow = sheet.createRow(0);
	                for (int col = 0; col < headers.size(); col++) {
	                    Cell cell = headerRow.createCell(col);
	                    cell.setCellValue(headers.get(col));
	                }
	                
	                
	                // 나머지 행에 값 추가
	                for (int row = 0; row < dataArray.size(); row++) {
	                    JSONObject jsonObject = (JSONObject) dataArray.get(row);
	                    Row dataRow = sheet.createRow(row + 1); // 헤더가 첫 번째 행이므로 행 인덱스는 row + 1

	                    for (int col = 0; col < headers.size(); col++) {
	                        Cell cell = dataRow.createCell(col);
	                        cell.setCellValue(jsonObject.get(headers.get(col)).toString());
	                    }
	                }
	                
	                System.out.println("성공 ㅇㅇ");
	            }
	         
	         
	     

	     }
	
}
    
    
    

	

		
