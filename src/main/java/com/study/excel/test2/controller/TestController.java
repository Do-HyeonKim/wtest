package com.study.excel.test2.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.internal.build.AllowSysOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.study.excel.test2.dto.Test1DTO;
import com.study.excel.test2.mapper.TestRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tc")
@RequiredArgsConstructor
public class TestController {

	private final TestRepository tRep;

	@RequestMapping("getTable")
	public byte[] getTable(@RequestBody Test1DTO test1) throws IOException {
//	public  Test1DTO  getTable(@RequestBody Test1DTO test1) throws IOException {

		// 계획 -> result save 호출하면 runId로 runtype찾은 다음에 그 타입에 따라서
		// 그 테이블로 이동해서 runId 값 찾아서 object 형태로 가져옴
		// 그럼 그 object의 변수 값으로 엑셀 헤더 생성 value 값으로 값 생성
		// 마지막에 텍스트 멘트 추가해줌

		Test1DTO test = tRep.getTest1(test1);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("테이블 정보");

		// 시트 만들어주는 부분
		createExcelRows(sheet, test);

		// 엑셀 직접 쓰기 테스트
		String filePath = "C:\\Users\\cpflv\\Downloads\\test.xlsx";
//	        FileOutputStream outputStream = new FileOutputStream(filePath);
//	        workbook.write(outputStream);
//	        workbook.close();
//	        outputStream.close();
//	        return test;

		// 바이너리로 변환하는 부분
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		byte[] excelBytes = outputStream.toByteArray();

		// 엑셀로 변환하는 부분 (한글 깨짐 확인 용)
		saveExcelFile(excelBytes, filePath);
		return excelBytes;
	}

	public void createExcelRows(Sheet sheet, Test1DTO test1) {
		int rowIndex = 0;
		Row headerRow = sheet.createRow(rowIndex++);
		Row dataRow = sheet.createRow(rowIndex++);

		int cellIndex = 0;
		for (Field field : test1.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object fieldValue;
			try {
				fieldValue = field.get(test1);
			} catch (IllegalAccessException e) {
				fieldValue = null;
			}

			headerRow.createCell(cellIndex).setCellValue(fieldName);
			dataRow.createCell(cellIndex).setCellValue(String.valueOf(fieldValue));

			cellIndex++;
		}

		// 텍스트 추가해주는 부분
		List<String> textList = new ArrayList<>();
		textList.add("안녕하세요 오늘 날씨가 구려요");
		textList.add("비가 너무많이와 흑흑");
		textList.add("안녀엉 ?");

		// foreach 돌면서 추가해줌
		int textIndex = 0;

		for (String text : textList) {
			Row textRow = sheet.createRow(rowIndex++);
			textRow.createCell(textIndex).setCellValue(text);
		}

	}

	public void saveExcelFile(byte[] excelBytes, String filePath) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(excelBytes);
			System.out.println("Excel file saved successfully!");
		} catch (IOException e) {
			System.out.println("Error saving Excel file: " + e.getMessage());
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
	
	@RequestMapping("test")
	public void convetorExcel() {
	     try {
	            File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
	            FileInputStream fis = new FileInputStream(file);
	            Workbook workbook = new XSSFWorkbook(fis);

	            JSONObject resultJson = new JSONObject();

	            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
	                Sheet sheet = workbook.getSheetAt(sheetIndex);

	                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	                    Row row = sheet.getRow(rowIndex);

	                    if (row != null) {
	                        JSONObject pointJson = new JSONObject();
	                        String stack = row.getCell(0).getStringCellValue();
	                        String category = row.getCell(1).getStringCellValue();

	                        pointJson.put("no", row.getCell(2).getNumericCellValue());
	                        pointJson.put("x", row.getCell(3).getNumericCellValue());
	                        pointJson.put("y", row.getCell(4).getNumericCellValue());
	                        pointJson.put("z", row.getCell(5).getNumericCellValue());

	                        if (!resultJson.containsKey(stack)) {
	                            resultJson.put(stack, new JSONObject());
	                        }

	                        JSONObject stackObject = (JSONObject) resultJson.get(stack);

	                        if (!stackObject.containsKey(category)) {
	                            stackObject.put(category, new JSONArray());
	                        }

	                        JSONArray categoryArray = (JSONArray) stackObject.get(category);
	                        categoryArray.add(pointJson);
	                    }
	                }
	            }

	            System.out.println(resultJson.toJSONString());

	            workbook.close();
	            fis.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
	@RequestMapping("test2")
	public void test2() throws Exception {
		 File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
		    FileInputStream fis = new FileInputStream(file);
		    Workbook workbook = new XSSFWorkbook(fis);

		    int rowindex = 0;
		    int columnindex = 0;
		    // 첫번쨰 시트만 조회
		    Sheet sheet = workbook.getSheetAt(0);

		    // 마지막 행
		    int rows = (sheet.getLastRowNum() + 1);
		    // 마지막 셀
		    int maxCells = 0;
		    for (rowindex = 0; rowindex < rows; rowindex++) { // 세로
		        Row row = sheet.getRow(rowindex);
		        if (row != null) {
		            int cells = (row.getLastCellNum());
		            if (cells > maxCells)
		                maxCells = cells;
		        }

		    }

		    // 셀의 내용 저장
		    String[][] text = new String[rows][maxCells];
		    for (rowindex = 0; rowindex < rows; rowindex++) { // Row

		        Row row = sheet.getRow(rowindex);
		        if (row != null) {
		            int cells = row.getLastCellNum();
		            for (columnindex = 0; columnindex < cells; columnindex++) { // Col

		                Cell cell = row.getCell(columnindex);

		                String value = "";
		                // 셀이 빈값일경우를 위한 널체크
		                if (cell == null) {
		                    continue;
		                } else {
		                    // 타입별로 내용 조회
		                    switch (cell.getCellType()) {
		                        case Cell.CELL_TYPE_FORMULA:
		                            value = cell.getNumericCellValue() + " ";
		                            break;
		                        case Cell.CELL_TYPE_NUMERIC:
		                            value = cell.getNumericCellValue() + "";
		                            break;
		                        case Cell.CELL_TYPE_STRING:
		                            value = cell.getStringCellValue() + "";
		                            break;
		                        case Cell.CELL_TYPE_BLANK:
		                            value = "";
		                            break;
		                        case Cell.CELL_TYPE_ERROR:
		                            value = cell.getErrorCellValue() + "";
		                            break;
		                    }
		                }
		                // 내용 저장
		                text[rowindex][columnindex] = value;
		            }

		        }
		    }
		    
		    for (rowindex = 0; rowindex < rows; rowindex++) { // Row
		    	   Row row = sheet.getRow(rowindex);
		    	   if (row != null) {
		    	       int cells = row.getLastCellNum();
		    	       for (columnindex = 0; columnindex <= cells; columnindex++) { // Col
		    	           Cell cell = row.getCell(columnindex);

		    	           String value = "";
		    	           // 셀이 빈값일경우를 위한 널체크
		    	           if (cell == null) {
		    	               continue;
		    	           } else {
		    	               // 타입별로 내용 조회
		    	               switch (cell.getCellType()) {
		    	                  case Cell.CELL_TYPE_FORMULA:
		    	                      value = cell.getNumericCellValue() + " ";
		    	                      break;
		    	                  case Cell.CELL_TYPE_NUMERIC:
		    	                      value = cell.getNumericCellValue() + "";
		    	                      break;
		    	                  case Cell.CELL_TYPE_STRING:
		    	                      value = cell.getStringCellValue() + "";
		    	                      break;
		    	                  case Cell.CELL_TYPE_BLANK:
		    	                      value = "";
		    	                      break;
		    	                  case Cell.CELL_TYPE_ERROR:
		    	                      value = cell.getErrorCellValue() + "";
		    	                      break;
		    	               }
		    	           }
		    	           //내용 저장
		    	           text[rowindex][columnindex] = value;
		    	       }
		    	   }
		    	}

		 // JSONObject 생성
		    JSONObject result = new JSONObject();
		     JSONArray subTypeArr = new JSONArray();
		     String firstCategory = "";
		    for (int i = 5; i < rows; i++) { // 시작 행은 4번째 행부터
		      String category = text[i][2]; // 3번째 열은 category 값
		      String type = text[i][3]; // 4번째 열은 type 값
		      String subType = text[i][4]; // 5번째 열은 subType 값

		      
//		      // category, type, subType 값이 존재하지 않을 경우 생성
//		      JSONObject categoryObj = (JSONObject) result.getOrDefault(category, new JSONObject());
//		      JSONObject typeObj = (JSONObject) categoryObj.getOrDefault(type, new JSONObject());
//		      JSONArray subTypeArr = (JSONArray) typeObj.getOrDefault(subType, new JSONArray());
		      

		      // subType 리스트에 값 추가
		      JSONObject subTypeMap = new JSONObject();
		      subTypeMap.put("no", text[i][5]); // 6번째 열은 no 값
		      subTypeMap.put("x", Double.parseDouble(text[i][6])); // 7번째 열은 x 값
		      subTypeMap.put("y", Double.parseDouble(text[i][7])); // 8번째 열은 y 값
		      subTypeMap.put("z", Double.parseDouble(text[i][8])); // 9번째 열은 z 값
		      
		      subTypeArr.add(subTypeMap);
//
//		      // 수정된 값을 다시 넣음
//		      typeObj.put(subType, subTypeArr);
//		      categoryObj.put(type, typeObj);
		      result.put("stack", subTypeArr);
		    }

		    System.out.println(result.toString());
//		    	// 데이터 구조화
//		    	Map<String, Map<String, Map<String, List<Map<String, Object>>>>> result = new HashMap<>();
//
//		    	for (int i = 5; i < rows; i++) { // 시작 행은 4번째 행부터
//		    		System.out.println(text[i][2]);
//		    		System.out.println(text[i][3]);
//		    		System.out.println(text[i][4]);
//		    		System.out.println(text[i][5]);
//		    	   String category = text[i][2]; // 3번째 열은 category 값
//		    	   String type = text[i][3]; // 4번째 열은 type 값
//		    	   String subType = text[i][4]; // 5번째 열은 subType 값
//
//		    	   if (!result.containsKey(category)) {
//		    	       result.put(category, new HashMap<>());
//		    	   }
//
//		    	   Map<String, Map<String, List<Map<String, Object>>>> categoryMap = result.get(category);
//
//		    	   if (!categoryMap.containsKey(type)) {
//		    	       categoryMap.put(type, new HashMap<>());
//		    	   }
//
//		    	   Map<String, List<Map<String, Object>>> typeMap = categoryMap.get(type);
//
//		    	   if (!typeMap.containsKey(subType)) {
//		    	       typeMap.put(subType, new ArrayList<>());
//		    	   }
//
//		    	   List<Map<String, Object>> subTypeList = typeMap.get(subType);
//
//		    	   Map<String, Object> subTypeMap = new HashMap<>();
//		    	   subTypeMap.put("no", text[i][5]); // 6번째 열은 no 값
//		    	   subTypeMap.put("x", Double.parseDouble(text[i][6])); // 7번째 열은 x 값
//		    	   subTypeMap.put("y", Double.parseDouble(text[i][7])); // 8번째 열은 y 값
//		    	   subTypeMap.put("z", Double.parseDouble(text[i][8])); // 9번째 열은 z 값
//
//		    	   subTypeList.add(subTypeMap);
//		    	}
//
//		    	System.out.println(result);
		    	
		   //type1 
//		    Map<String, Map<String, Map<String, List<Map<String, Object>>>>> result = new HashMap<>();
//
//		    for (int i = 5; i < rows; i++) { // 시작 행은 4번째 행부터
//		        String category = text[i][2]; // 3번째 열은 category 값
//		        String type = text[i][3]; // 4번째 열은 type 값
//		        String subType = text[i][4]; // 5번째 열은 subType 값
//		        System.out.println(category);
//		        System.out.println(type);
//		        System.out.println(subType);
//
//		        if (!result.containsKey(category)) {
//		            result.put(category, new HashMap<>());
//		        }
//
//		        Map<String, Map<String, List<Map<String, Object>>>> categoryMap = result.get(category);
//
//		        if (!categoryMap.containsKey(type)) {
//		            categoryMap.put(type, new HashMap<>());
//		        }
//
//		        Map<String, List<Map<String, Object>>> typeMap = categoryMap.get(type);
//
//		        if (!typeMap.containsKey(subType)) {
//		            typeMap.put(subType, new ArrayList<>());
//		        }
//
//		        List<Map<String, Object>> subTypeList = typeMap.get(subType);
//
//		        Map<String, Object> subTypeMap = new HashMap<>();
//		        subTypeMap.put("no", text[i][5]); // 6번째 열은 no 값
//		        subTypeMap.put("x", Double.parseDouble(text[i][6])); // 7번째 열은 x 값
//		        subTypeMap.put("y", Double.parseDouble(text[i][7])); // 8번째 열은 y 값
//		        subTypeMap.put("z", Double.parseDouble(text[i][8])); // 9번째 열은 z 값
//
//		        subTypeList.add(subTypeMap);
//		    }
//
//		    // Convert the result map to JSON using Jackson ObjectMapper
//		    ObjectMapper objectMapper = new ObjectMapper();
//		    String jsonResult = objectMapper.writeValueAsString(result);
//
//		    System.out.println(jsonResult);
		
		    //type2
//		    // 데이터 구조화
//		    Map<String, Map<String, Map<String, List<Map<String, Object>>>>> result = new HashMap<>();
//
//		    for (int i = 5; i < rows; i++) { // 시작 행은 4번째 행부터
//		        String category = text[i][2]; // 3번째 열은 category 값
//		        String type = text[i][3]; // 4번째 열은 type 값
//		        String subType = text[i][4]; // 5번째 열은 subType 값
//
//		        if (!result.containsKey(category)) {
//		            result.put(category, new HashMap<>());
//		        }
//
//		        Map<String, Map<String, List<Map<String, Object>>>> categoryMap = result.get(category);
//
//		        if (!categoryMap.containsKey(type)) {
//		            categoryMap.put(type, new HashMap<>());
//		        }
//
//		        Map<String, List<Map<String, Object>>> typeMap = categoryMap.get(type);
//
//		        if (!typeMap.containsKey(subType)) {
//		            typeMap.put(subType, new ArrayList<>());
//		        }
//
//		        List<Map<String, Object>> subTypeList = typeMap.get(subType);
//
//		        Map<String, Object> subTypeMap = new HashMap<>();
//		        subTypeMap.put("no", text[i][5]); // 6번째 열은 no 값
//		        subTypeMap.put("x", Double.parseDouble(text[i][6])); // 7번째 열은 x 값
//		        subTypeMap.put("y", Double.parseDouble(text[i][7])); // 8번째 열은 y 값
//		        subTypeMap.put("z", Double.parseDouble(text[i][8])); // 9번째 열은 z 값
//
//		        subTypeList.add(subTypeMap);
//		    }
//
//		    System.out.println(result);
//		 File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
//         FileInputStream fis = new FileInputStream(file);
//         Workbook workbook = new XSSFWorkbook(fis);
//         
//		int rowindex = 0;
//		int columnindex = 0;
//		// 첫번쨰 시트만 조회
//		Sheet sheet = workbook.getSheetAt(0);
//		
//		//마지막 행
//		int rows = (sheet.getLastRowNum()+1);
//		//마지막 셀
//		int maxCells = 0;
//		for (rowindex = 0; rowindex < rows; rowindex++) { // 세로
//			Row row = sheet.getRow(rowindex);
//			if(row!=null) {
//				int cells = (row.getLastCellNum());
//				if (cells > maxCells)
//					maxCells = cells;	
//			}
//			
//		}
//		
//		//셀의 내용 저장
//		String[][] text = new String[rows][maxCells];
//		for (rowindex = 0; rowindex < rows; rowindex++) { // Row
//		
//			Row row = sheet.getRow(rowindex);
//			if (row != null) {
//				int cells = row.getLastCellNum();
//				for (columnindex = 0; columnindex <= cells; columnindex++) { // Col
//
//					Cell cell = row.getCell(columnindex);
//
//					String value = "";
//					// 셀이 빈값일경우를 위한 널체크
//					if (cell == null) {
//						continue;
//					} else {
//						// 타입별로 내용 조회
//						switch (cell.getCellType()) {
//						case Cell.CELL_TYPE_FORMULA:
//							value = cell.getNumericCellValue() + " ";
//							break;
//						case Cell.CELL_TYPE_NUMERIC:
//							value = cell.getNumericCellValue() + "";
//							break;
//						case Cell.CELL_TYPE_STRING:
//							value = cell.getStringCellValue() + "";
//							break;
//						case Cell.CELL_TYPE_BLANK:
//							value =  "";
//							break;
//						case Cell.CELL_TYPE_ERROR:
//							value = cell.getErrorCellValue() + "";
//							break;
//						}
//					}
//					//내용 저장
//					text[rowindex][columnindex] = value;
//				}
//
//			}
//		}
//		
//		for(int i = 0 ; i < rows ; i++) {
//			for(int j = 0 ; j < maxCells;  j++) {
//				if(text[i][j]!=null) {
//					System.out.println("i==>" + i + "  j==>"+ j + "  text[i][j] ==> " + text[i][j]);
//				}
//			}
//		}
	}
	
	
	   @RequestMapping("test7")
	    public JSONArray  convertExcelToJson() {
	        List<String> keys = new ArrayList<>();
	        List<List<Object>> data = new ArrayList<>();

	        try {
	            FileInputStream fis = new FileInputStream(new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx"));

	            // 엑셀 파일을 읽어옴
	            XSSFWorkbook workbook = new XSSFWorkbook(fis);
	            XSSFSheet sheet = workbook.getSheetAt(1); // 두 번째 시트를 사용

	            // 헤더 찾기
	            Row headerRow = findHeaderRow(sheet);
	            Iterator<Cell> headerCellIterator = headerRow.cellIterator();
	            while (headerCellIterator.hasNext()) {
	                Cell cell = headerCellIterator.next();
	                keys.add(cell.getStringCellValue());
	            }

	            // 데이터 읽기
	            Iterator<Row> rowIterator = sheet.iterator();
	            rowIterator.next(); 
	            while (rowIterator.hasNext()) {
	                Row row = rowIterator.next();
	                Iterator<Cell> cellIterator = row.cellIterator();
	                List<Object> rowData = new ArrayList<>();

	                while (cellIterator.hasNext()) {
	                    Cell cell = cellIterator.next();
	                    switch (cell.getCellType()) {
	                        case Cell.CELL_TYPE_STRING:
	                            rowData.add(cell.getStringCellValue());
	                            break;
	                        case Cell.CELL_TYPE_NUMERIC:
	                            rowData.add(cell.getNumericCellValue());
	                            break;
	                        case Cell.CELL_TYPE_BOOLEAN:
	                            rowData.add(cell.getBooleanCellValue());
	                            break;
	                        default:
	                            rowData.add(null);
	                            break;
	                    }
	                }

	                data.add(rowData);
	            }

	            // JSON 형식으로 변환
	            JSONArray jsonArray = convertToJSON(keys, data);

	            workbook.close();
	            fis.close();

	            return jsonArray;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	    private Row findHeaderRow(XSSFSheet sheet) {
	        Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	                    return row; // 문자열이 있는 첫 번째 셀을 헤더로 사용
	                }
	            }
	        }
	        return null;
	    }

	    private JSONArray convertToJSON(List<String> keys, List<List<Object>> data) {
	        JSONArray jsonArray = new JSONArray();

	        for (List<Object> rowData : data) {
	        	System.out.println(rowData);
	            JSONObject jsonObject = new JSONObject();
	            for (int i = 0; i < keys.size(); i++) {
	                String key = keys.get(i);
	                Object value = rowData.get(i);
	                jsonObject.put(key, value);
	            }
	            jsonArray.add(jsonObject);
	        }

	        return jsonArray;
	    }
	    

    @RequestMapping("test4")
    public void test4() throws Exception {
    	   File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
    	    FileInputStream fis = new FileInputStream(file);
    	    Workbook workbook = new XSSFWorkbook(fis);

    	    int rowindex = 0;
    	    int columnindex = 0;
    	    // 첫 번째 시트만 조회
    	    Sheet sheet = workbook.getSheetAt(0);

    	    // 마지막 행
    	    int rows = sheet.getLastRowNum() + 1;
    	    // 마지막 셀
    	    int maxCells = 0;
    	    for (rowindex = 0; rowindex < rows; rowindex++) {
    	        Row row = sheet.getRow(rowindex);
    	        if (row != null) {
    	            int cells = row.getLastCellNum();
    	            if (cells > maxCells)
    	                maxCells = cells;
    	        }
    	    }

    	    Map<String, Object> result = new HashMap<>();
    	    Map<String, Object> topMap = new HashMap<>();
    	    Map<String, Object> middleMap = new HashMap<>();
    	    Map<String, Object> bottomMap = new HashMap<>();

    	    String currentCategory = "";
    	    Map<String, Object> currentMap = null;
    	    for (rowindex = 5; rowindex < rows; rowindex++) {
    	        Row row = sheet.getRow(rowindex);
    	        if (row != null) {
    	            Map<String, Object> point = new HashMap<>();
    	            int cells = row.getLastCellNum();
    	            for (columnindex = 0; columnindex <= cells; columnindex++) {
    	                Cell cell = row.getCell(columnindex);

    	                String value = "";
    	                if (cell != null) {
    	                    switch (cell.getCellType()) {
    	                        case Cell.CELL_TYPE_FORMULA:
    	                            value = cell.getNumericCellValue() + " ";
    	                            break;
    	                        case Cell.CELL_TYPE_NUMERIC:
    	                            value = cell.getNumericCellValue() + "";
    	                            break;
    	                        case Cell.CELL_TYPE_STRING:
    	                            value = cell.getStringCellValue() + "";
    	                            break;
    	                        case Cell.CELL_TYPE_BLANK:
    	                            value = "";
    	                            break;
    	                        case Cell.CELL_TYPE_ERROR:
    	                            value = cell.getErrorCellValue() + "";
    	                            break;
    	                    }
    	                }
    	                if (columnindex == 2) {
    	                    currentCategory = value.toLowerCase();
    	                    if (currentCategory.contains("top")) {
    	                        currentMap = topMap;
    	                    } else if (currentCategory.contains("middle")) {
    	                        currentMap = middleMap;
    	                    } else if (currentCategory.contains("bottom")) {
    	                        currentMap = bottomMap;
    	                    }
    	                } else if (columnindex >= 5 && columnindex <= 8) {
    	                    switch (columnindex) {
    	                        case 5:
    	                            point.put("no", value);
    	                            break;
    	                        case 6:
    	                            point.put("x", value);
    	                            break;
    	                        case 7:
    	                            point.put("y", value);
    	                            break;
    	                        case 8:
    	                            point.put("z", value);
    	                            break;
    	                    }
    	                }
    	            }
    	            // 현재 카테고리에 따라 리스트에 추가
    	            if (currentCategory.contains("top") || currentCategory.contains("middle") || currentCategory.contains("bottom")) {
    	                currentMap.computeIfAbsent(currentCategory, k -> new ArrayList<>());
    	                ((List<Map<String, Object>>) currentMap.get(currentCategory)).add(point);
    	            }
    	        }
    	    }

    	    result.put("top", topMap);
    	    result.put("middle", middleMap);
    	    result.put("bottom", bottomMap);

    	    // result를 JSON 형태로 출력
    	    ObjectMapper objectMapper = new ObjectMapper();
    	    String jsonResult = objectMapper.writeValueAsString(result);

    	    System.out.println(jsonResult);
//        File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
//        FileInputStream fis = new FileInputStream(file);
//        Workbook workbook = new XSSFWorkbook(fis);
//
//        int rowindex = 0;
//        int columnindex = 0;
//        // 첫 번째 시트만 조회
//        Sheet sheet = workbook.getSheetAt(0);
//
//        // 마지막 행
//        int rows = sheet.getLastRowNum() + 1;
//        // 마지막 셀
//        int maxCells = 0;
//        for (rowindex = 0; rowindex < rows; rowindex++) {
//            Row row = sheet.getRow(rowindex);
//            if (row != null) {
//                int cells = row.getLastCellNum();
//                if (cells > maxCells)
//                    maxCells = cells;
//            }
//        }
//
//        // 셀의 내용 저장
//        List<Map<String, Object>> negList = new ArrayList<>();
//        List<Map<String, Object>> centerList = new ArrayList<>();
//        List<Map<String, Object>> posList = new ArrayList<>();
//
//        for (rowindex = 5; rowindex < rows; rowindex++) {
//            Row row = sheet.getRow(rowindex);
//            if (row != null) {
//                Map<String, Object> point = new HashMap<>();
//                int cells = row.getLastCellNum();
//                for (columnindex = 0; columnindex <= cells; columnindex++) {
//                    Cell cell = row.getCell(columnindex);
//
//                    String value = "";
//                    if (cell != null) {
//                        switch (cell.getCellType()) {
//                            case Cell.CELL_TYPE_FORMULA:
//                                value = cell.getNumericCellValue() + " ";
//                                break;
//                            case Cell.CELL_TYPE_NUMERIC:
//                                value = cell.getNumericCellValue() + "";
//                                break;
//                            case Cell.CELL_TYPE_STRING:
//                                value = cell.getStringCellValue() + "";
//                                break;
//                            case Cell.CELL_TYPE_BLANK:
//                                value = "";
//                                break;
//                            case Cell.CELL_TYPE_ERROR:
//                                value = cell.getErrorCellValue() + "";
//                                break;
//                        }
//                    }
//
//                    if (columnindex == 5) {
//                        point.put("no",value);
//                    } else if (columnindex >= 5 && columnindex <= 8) {
//                        switch (columnindex) {
//                            case 6:
//                                point.put("x", Double.parseDouble(value));
//                                break;
//                            case 7:
//                                point.put("y", Double.parseDouble(value));
//                                break;
//                            case 8:
//                                point.put("z", Double.parseDouble(value));
//                                break;
//                        }
//                    }
//                }
//
//                // 분류에 따라 리스트에 추가
//                if (rowindex >= 0 && rowindex < 10) {
//                    negList.add(point);
//                } else if (rowindex >= 10 && rowindex < 20) {
//                    centerList.add(point);
//                } else if (rowindex >= 20 && rowindex < 30) {
//                    posList.add(point);
//                }
//            }
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("stack2", Map.of("top", Map.of("neg", negList, "center", centerList, "pos", posList)));
//        
//        // result를 JSON 형태로 출력
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonResult = objectMapper.writeValueAsString(result);
//        
//        System.out.println(jsonResult);
    }
    
    
    @RequestMapping("test5")
	public void test5() throws Exception {
    	 File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
    	  FileInputStream fis = new FileInputStream(file);
    	  Workbook workbook = new XSSFWorkbook(fis);

    	  int rowindex = 0;
    	  int columnindex = 0;
    	  // 첫번쨰 시트만 조회
    	  Sheet sheet = workbook.getSheetAt(0);

    	  //마지막 행
    	  int rows = (sheet.getLastRowNum()+1);
    	  //마지막 셀
    	  int maxCells = 0;
    	  for (rowindex = 0; rowindex < rows; rowindex++) { // 세로
    	      Row row = sheet.getRow(rowindex);
    	      if(row!=null) {
    	          int cells = (row.getLastCellNum());
    	          if (cells > maxCells)
    	            maxCells = cells;
    	      }
    	  }

    	  
    	  //셀의 내용 저장
    	  String[][] text = new String[rows][maxCells];
    	  for (rowindex = 0; rowindex < rows; rowindex++) { // Row

    	      Row row = sheet.getRow(rowindex);
    	      if (row != null) {
    	          int cells = row.getLastCellNum();
    	          for (columnindex = 0; columnindex <= cells; columnindex++) { // Col

    	            Cell cell = row.getCell(columnindex);

    	            String value = "";
    	            // 셀이 빈값일경우를 위한 널체크
    	            if (cell == null) {
    	               continue;
    	            } else {
    	               // 타입별로 내용 조회
    	               switch (cell.getCellType()) {
    	                  case Cell.CELL_TYPE_FORMULA:
    	                      value = cell.getNumericCellValue() + " ";
    	                      break;
    	                  case Cell.CELL_TYPE_NUMERIC:
    	                      value = cell.getNumericCellValue() + "";
    	                      break;
    	                  case Cell.CELL_TYPE_STRING:
    	                      value = cell.getStringCellValue() + "";
    	                      break;
    	                  case Cell.CELL_TYPE_BLANK:
    	                      value = "";
    	                      break;
    	                  case Cell.CELL_TYPE_ERROR:
    	                      value = cell.getErrorCellValue() + "";
    	                      break;
    	               }
    	            }
    	            //내용 저장
    	            text[rowindex][columnindex] = value;
    	          }
    	      }
    	  }
    
    	  //셀의 내용 저장
    	  Map<String, Map<String, Map<String, Map<String, List<Map<String, Object>>>>>> result = new HashMap<>();
    	  String currentStackName = "";
    	  String currentPositionName = "";
    	  String currentSubPositionName = "";
    	  String currentCoordinateName = "";

    	  for (rowindex = 0; rowindex < rows; rowindex++) { // Row
    	      Row row = sheet.getRow(rowindex);
    	      if (row != null) {
    	          int cells = row.getLastCellNum();
    	          for (columnindex = 0; columnindex <= cells; columnindex++) { // Col
    	            Cell cell = row.getCell(columnindex);
    	            String value = "";
    	            // 셀이 빈값일경우를 위한 널체크
    	            if (cell == null) {
    	               continue;
    	            } else {
    	               // 타입별로 내용 조회
    	               switch (cell.getCellType()) {
    	                  case Cell.CELL_TYPE_FORMULA:
    	                      value = cell.getNumericCellValue() + " ";
    	                      break;
    	                  case Cell.CELL_TYPE_NUMERIC:
    	                      value = cell.getNumericCellValue() + "";
    	                      break;
    	                  case Cell.CELL_TYPE_STRING:
    	                      value = cell.getStringCellValue() + "";
    	                      break;
    	                  case Cell.CELL_TYPE_BLANK:
    	                      value = "";
    	                      break;
    	                  case Cell.CELL_TYPE_ERROR:
    	                      value = cell.getErrorCellValue() + "";
    	                      break;
    	               }
    	            }
    	            //내용 저장
    	            if (columnindex == 2) {
    	               // stack 이름을 저장
    	               currentStackName = value;
    	               if (!result.containsKey(currentStackName)) {
    	                  result.put(currentStackName, new HashMap<>());
    	               }
    	            } else if (columnindex == 3) {
    	               // position 이름을 저장
    	               currentPositionName = value;
    	               if (!result.get(currentStackName).containsKey(currentPositionName)) {
    	                  result.get(currentStackName).put(currentPositionName, new HashMap<>());
    	               }
    	            } else if (columnindex == 4) {
    	               // subposition 이름을 저장
    	               currentSubPositionName = value;
    	               if (!result.get(currentStackName).get(currentPositionName).containsKey(currentSubPositionName)) {
    	                  result.get(currentStackName).get(currentPositionName).put(currentSubPositionName, new HashMap<>());
    	               }
    	            } else if (columnindex == 5) {
    	            	   // coordinate 이름을 저장
    	            	   currentCoordinateName = value;
    	            	   if (!result.get(currentStackName).get(currentPositionName).get(currentSubPositionName).containsKey(currentCoordinateName)) {
    	            	       result.get(currentStackName).get(currentPositionName).get(currentSubPositionName).put(currentCoordinateName, new ArrayList<>());
    	            	   }
    	            	} else if (columnindex >= 6 && columnindex <= 8) {
    	            	   // 좌표 값을 저장
    	            	   Map<String, Object> point = new HashMap<>();
    	            	   point.put("no", text[rowindex][5]);
    	            	   point.put("x", Double.parseDouble(text[rowindex][6]));
    	            	   point.put("y", Double.parseDouble(text[rowindex][7]));
    	            	   point.put("z", Double.parseDouble(text[rowindex][8]));
    	            	   result.get(currentStackName).get(currentPositionName).get(currentSubPositionName).get(currentCoordinateName).add(point);
    	            	}
    	          }
    	      }
}
    }
    
    
    @RequestMapping("test22")
	public void test22() throws Exception {
        File file = new File("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\parser_json.xlsx");
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        int rowindex = 0;
        int columnindex = 0;
        Sheet sheet = workbook.getSheetAt(0);
        int rows = (sheet.getLastRowNum() + 1);
        int maxCells = 0;

        for (rowindex = 0; rowindex < rows; rowindex++) {
            Row row = sheet.getRow(rowindex);
            if (row != null) {
                int cells = (row.getLastCellNum());
                if (cells > maxCells)
                    maxCells = cells;
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        for (rowindex = 5; rowindex < rows; rowindex++) {
            Row row = sheet.getRow(rowindex);
            if (row != null) {
                int cells = row.getLastCellNum();

                Map<String, Object> map = new HashMap<>();

                for (columnindex = 0; columnindex < cells; columnindex++) {
                    Cell cell = row.getCell(columnindex);
                    String value = dataFormatter.formatCellValue(cell);


                    if (cell == null) {
                        continue;
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_FORMULA:
                                value = cell.getNumericCellValue() + " ";
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case Cell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                value = "";
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                    }

                    if (columnindex == 2) {
                    	System.out.println(value);
                        map.put("stack", value);
                    } else if (columnindex == 3) {
                    	System.out.println(value);
                        map.put("top", value.equals("top"));
                        map.put("middle", value.equals("middle"));
                        map.put("bottom", value.equals("bottom"));
                    } else if (columnindex >= 5) {
                        Map<String, Object> objectMap = new HashMap<>();
                        System.out.println(value);
                        System.out.println(dataFormatter.formatCellValue(row.getCell(columnindex + 1)));
                        System.out.println(dataFormatter.formatCellValue(row.getCell(columnindex + 2)));
                        System.out.println(dataFormatter.formatCellValue(row.getCell(columnindex + 3)));
                        objectMap.put("no", value);
                        objectMap.put("x", dataFormatter.formatCellValue(row.getCell(columnindex + 1)));
                        objectMap.put("y", dataFormatter.formatCellValue(row.getCell(columnindex + 2)));
                        objectMap.put("z", dataFormatter.formatCellValue(row.getCell(columnindex + 3)));

                        if (value.equals("neg")) {
                            map.put("neg", objectMap);
                        } else if (value.equals("pos")) {
                            map.put("pos", objectMap);
                        } else if (value.equals("center")) {
                            map.put("center", objectMap);
                        }

                        // Move to the next set of values
                        columnindex += 3;
                    }
                }

                result.add(map);
            }
        }

        // Convert the list to a JSON format
        String json = result.toString();
        System.out.println(json);
    }
}
    
