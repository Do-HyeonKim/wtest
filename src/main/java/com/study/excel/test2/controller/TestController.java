package com.study.excel.test2.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.Field;

import com.study.excel.test2.dto.Test1DTO;
import com.study.excel.test2.mapper.TestRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tc")
@RequiredArgsConstructor
public class TestController {
	
	
	private final TestRepository tRep;
	
	@RequestMapping("getTable")
	public  byte[]  getTable(@RequestBody Test1DTO test1) throws IOException {
//	public  Test1DTO  getTable(@RequestBody Test1DTO test1) throws IOException {
		
		// 계획 -> result save 호출하면 runId로 runtype찾은 다음에 그 타입에 따라서 
		// 그 테이블로 이동해서 runId 값 찾아서 object 형태로 가져옴 
		// 그럼 그 object의 변수 값으로 엑셀 헤더 생성 value 값으로 값 생성 
		// 마지막에 텍스트 멘트 추가해줌
		  Test1DTO test = tRep.getTest1(test1);

	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("테이블 정보");

	        //시트 만들어주는 부분
	        createExcelRows(sheet, test);

	        //엑셀 직접 쓰기 테스트
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
	        
	        //엑셀로 변환하는 부분 (한글 깨짐 확인 용)
	        saveExcelFile(excelBytes,filePath);
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
	
}
