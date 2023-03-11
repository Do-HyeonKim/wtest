package com.study.excel.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.excel.board.dto.ExcelTestDTO;
import com.study.excel.board.service.ExcelTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class ExcelTestController {
	
	private final ExcelTestService ets;
	
	@PostMapping("addExcel")
	 public String readExcel(@RequestParam("file") MultipartFile file, Model model)
		      throws IOException { 

			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		    XSSFSheet worksheet = workbook.getSheetAt(0);
		    
		    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
		        ExcelTestDTO excel = new ExcelTestDTO();
		           
		        
		        DataFormatter formatter = new DataFormatter();		        
		        XSSFRow row = worksheet.getRow(i);
		        	    	
		        String userName = formatter.formatCellValue(row.getCell(0));
		        String userEmail = formatter.formatCellValue(row.getCell(1));
		        String userId = formatter.formatCellValue(row.getCell(2));
		        String userPhone = formatter.formatCellValue(row.getCell(3));
		        String userType = formatter.formatCellValue(row.getCell(4));
		        
		        excel.setUserName(userName);
		        excel.setUserEmail(userEmail);
		        excel.setUserId(userId);
		        excel.setUserPhone(userPhone);
		        excel.setUserType(userType);
      
		        ets.insertExcel(excel);
		    } 
		    return "redirect:/success"; //성공 시 

		  }
	
	
	
	@PostMapping("testJson")
	public String jsonTest() throws Exception{
		JSONObject mainJobj = new JSONObject();
		mainJobj.put("nargout",1 );
		
		JSONArray rhsJarray = new JSONArray();
		JSONObject rhsJobj1 = new JSONObject();
		List<Integer> intArr = new ArrayList<>();
		intArr.add(1);	intArr.add(2);	intArr.add(3);
		List<String> stringArr = new ArrayList<>();
		stringArr.add("11"); stringArr.add("22"); stringArr.add("33");
		rhsJobj1.put("mwdata", intArr);
		rhsJobj1.put("mwsize", stringArr);
		rhsJobj1.put("mwtype", "uint8");
		rhsJarray.put(rhsJobj1);
		
		JSONObject rhsJobj2 = new JSONObject();
		List<Integer> intArr2 = new ArrayList<>();
		intArr2.add(11);	intArr2.add(22);	intArr2.add(33);
		List<String> stringArr2 = new ArrayList<>();
		stringArr2.add("??"); stringArr2.add("!!"); stringArr2.add("~~");
		rhsJobj2.put("mwdata", intArr2);
		rhsJobj2.put("mwsize", stringArr2);
		rhsJobj2.put("mwtype", "uint8");
		rhsJarray.put(rhsJobj2);
		mainJobj.put("rhs",rhsJarray);
		
		
		JSONObject outputFormatJobj = new JSONObject();
		outputFormatJobj.put("mode", "large");
		outputFormatJobj.put("nanInfFormat","string");
		mainJobj.put("outputFormat", outputFormatJobj);

		
		System.out.println(mainJobj);
		return "성공";
		
	}
	

}
