package com.study.excel.test2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tc6")
@RequiredArgsConstructor
public class Test6Controller {
	
	
	@RequestMapping("makeExcelContour")
	public void makeExcelSheet() {
        try {
            
        	//point_no sheet 생성
        	Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("point_no");
            
            int[] Pointnumbers = {39, 42, 45, 48, 51, 54, 57, 60, 63};
            
            int rowNum = 0;
            for (int i = 0; i < Pointnumbers.length; i += 3) {
                Row row = sheet.createRow(rowNum++);
                for (int j = 0; j < 3; j++) {
                    Cell cell = row.createCell(j);
                    if (i + j < Pointnumbers.length) {
                        cell.setCellValue(Pointnumbers[i + j]);
                    } else {
                        cell.setCellValue(""); 
                    }
                }
            }
            
         	//point_x sheet 생성
            sheet = workbook.createSheet("point_x");
            
            double[] xValue = {0.1,	0,-0.1,0.1,	0,-0.1,0.1,	0,-0.1};
            
            rowNum = 0;
            for (int i = 0; i < xValue.length; i += 3) {
                Row row = sheet.createRow(rowNum++);
                for (int j = 0; j < 3; j++) {
                    Cell cell = row.createCell(j);
                    if (i + j < xValue.length) {
                        cell.setCellValue(xValue[i + j]);
                    } else {
                        cell.setCellValue(""); 
                    }
                }
            }
            
        	//point_y sheet 생성
            sheet = workbook.createSheet("point_y");
            
            double[] yValue = {0.03478,0.03478,0.03478,0.03478,0.03478,0.03478,0.03478,0.03478,0.03478};
            rowNum = 0;
            for (int i = 0; i < yValue.length; i += 3) {
                Row row = sheet.createRow(rowNum++);
                for (int j = 0; j < 3; j++) {
                    Cell cell = row.createCell(j);
                    if (i + j < yValue.length) {
                        cell.setCellValue(yValue[i + j]);
                    } else {
                        cell.setCellValue(""); 
                    }
                }
            }
            
        	//point_z sheet 생성
            sheet = workbook.createSheet("point_z");
            
            double[] zValue = {-0.01,	-0.01,	-0.01 ,-0.055	,-0.055	,-0.055 ,-0.1,	-0.1,	-0.1};   
            rowNum = 0;
            for (int i = 0; i < zValue.length; i += 3) {
                Row row = sheet.createRow(rowNum++);
                for (int j = 0; j < 3; j++) {
                    Cell cell = row.createCell(j);
                    if (i + j < zValue.length) {
                        cell.setCellValue(zValue[i + j]);
                    } else {
                        cell.setCellValue(""); 
                    }
                }
            }
            
         // point_lo 시트 생성
             sheet = workbook.createSheet("point_lo");

            // 데이터 채우기
             rowNum = 0; // 데이터는 첫 번째 행부터 시작합니다.

            // 데이터 배열을 순회하면서 시트에 채웁니다.
            for (int i = 0; i < Pointnumbers.length; i++) {
                Row row = sheet.createRow(rowNum++);
                int pointNumber = (i / 3) + 1; // 포인트 번호 계산
                int subPointNumber = (i % 3) + 1; // 하위 포인트 번호 계산
                int pointIndex = i; // 데이터 배열의 인덱스
                double x = xValue[i];
                double y = yValue[i];
                double z = zValue[i];
                
                // 각 열에 데이터를 추가합니다.
                row.createCell(0).setCellValue(pointNumber); // 첫 번째 열에는 1, 2, 3을 반복합니다.
                row.createCell(1).setCellValue(subPointNumber); // 두 번째 열에는 1, 2, 3을 반복합니다.
                row.createCell(2).setCellValue(Pointnumbers[pointIndex]); // 세 번째 열에는 Pointnumbers 배열의 값을 채웁니다.
                row.createCell(3).setCellValue(x); // 네 번째 열에는 xValue 배열의 값을 채웁니다.
                row.createCell(4).setCellValue(y); // 다섯 번째 열에는 yValue 배열의 값을 채웁니다.
                row.createCell(5).setCellValue(z); // 여섯 번째 열에는 zValue 배열의 값을 채웁니다.
            }
            
            // point_lo 시트 생성
            sheet = workbook.createSheet("value_T");
            makeValueSheet(workbook, sheet , Pointnumbers);
            
  

            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\contor_test.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("Excel 파일이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            System.out.println("오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	
	public JSONArray makeValueSheet(Workbook workbook,Sheet sheet , int [] Pointnumbers) {
		
		try {
 
         // 엑셀 파일 읽기
         FileInputStream fileInputStream = new FileInputStream("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\test.xlsx");
          workbook = new XSSFWorkbook(fileInputStream);


	               // 시트 2 처리 : 전체 데이터
	               Sheet sheet2 = workbook.getSheetAt(0);
	               JSONArray jsonArraySheet2 = convertSheetToJsonArray(sheet2);
	               
	               // "sheet1" 키를  가진 배열을 생성합니다.
	               JSONArray valueTJarr = new JSONArray();

	               // points 배열을  선언합니다.
	               List<String> points = new ArrayList<>();
	               for(int value : Pointnumbers) {
	            	   points.add("point"+value);

	               }
	               
	               System.out.println(points);
	               
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
	                    	   if (points.contains(key) ) {
	                           // points 배열에 포함되면 "sheet1" 객체에 추가합니다.
	                           sheet1Object.put(key, entry.getValue());
	                       }   else if (!key.startsWith("point")) {
	                    	   if(key.equals("time")) {
	                           sheet1Object.put(key, sheet2Object.get(key));
	                    	   }
	                       }
	                   }
        
	                   valueTJarr.add(sheet1Object);
	               }

	               writeDataToSheet(sheet, valueTJarr);
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
    
    
    private String cleanHeader(String header) {
	    header = header.replaceAll("\\[.*?\\]", "");

	    header = header.replaceAll("-", "");

	    header = header.toLowerCase();

	    return header.trim();
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
    
    

  
    
    private  void writeDataToSheet(Sheet sheet, JSONArray dataArray) {
   	 
        if (dataArray.size() > 0) {
               // 0번째 객체의 키값을 가져와서 헤더로 사용
               JSONObject firstObject = (JSONObject)dataArray.get(0);
               List<String> headers = new ArrayList<>();
               for(Object key  : firstObject.keySet()) {
            	   System.out.println(key);
               	headers.add(key.toString());
               }
               
//                헤더를 알파벳 순으로 정렬
//               Collections.sort(headers);
               
               Collections.sort(headers, (header1, header2) -> {
            	    if (header1.equals("time")) {
            	        return -1; // header1이 "time"일 때, header1을 더 앞에 위치하도록 합니다.
            	    } else if (header2.equals("time")) {
            	        return 1; // header2가 "time"일 때, header2를 더 앞에 위치하도록 합니다.
            	    } else {
            	        return header1.compareTo(header2); // 나머지 경우에는 알파벳 순으로 정렬합니다.
            	    }
            	});
               
               
               // 첫 번째 행에 헤더 추가 
               // 헤더 안쓸 경우엔 해당 코드 주석
               Row headerRow = sheet.createRow(0);
               for (int col = 0; col < headers.size(); col++) {
                   Cell cell = headerRow.createCell(col);
                   cell.setCellValue(headers.get(col));
               }
               
               // 나머지 행에 값 추가
               for (int row = 0; row < dataArray.size(); row++) {
                   JSONObject jsonObject = (JSONObject) dataArray.get(row);
                   Row dataRow = sheet.createRow(row + 1 ); // 헤더가 첫 번째 행이므로 행 인덱스는 row + 1
                   //헤더 없는 경우에 사용하는 코드 
//                   Row dataRow = sheet.createRow(row);
                   
                   
                   for (int col = 0; col < headers.size(); col++) {
                       Cell cell = dataRow.createCell(col);
                       cell.setCellValue(jsonObject.get(headers.get(col)).toString());
                   }
               }
               
           }
        
        
    

    }
    
    
}