package com.study.excel.board.controller;

import java.io.IOException;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;



import com.study.excel.board.dto.ExcelDTO;
import com.study.excel.board.mapper.ExcelMapper;

@Controller
@RequestMapping("/excel")
public class ExcelController {

	
	@Autowired
	ExcelMapper em;
	
	@PostMapping("add")
	 public String readExcel(@RequestParam("file") MultipartFile file, Model model)
		      throws IOException { 

			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		    XSSFSheet worksheet = workbook.getSheetAt(0);
		    
		    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
		        ExcelDTO excel = new ExcelDTO();
		           
		        
		        DataFormatter formatter = new DataFormatter();		        
		        XSSFRow row = worksheet.getRow(i);
		        
		        String sNum =  formatter.formatCellValue(row.getCell(0));
		        String sName = formatter.formatCellValue(row.getCell(1));
		        String sId = formatter.formatCellValue(row.getCell(2));
		        int iNum = Integer.parseInt(sNum);
		        
		        excel.setNum(iNum);
		        excel.setName(sName);
		        excel.setId(sId);
		            
//		        excel.setId(row.getCell(0).getStringCellValue()); 
//		        excel.setName(row.getCell(1).getStringCellValue());
//		        excel.setId(row.getCell(2).getStringCellValue());
		        
		        em.insertExcel(excel);
		    }
		    return "redirect:/";

		  }
	
	
	
	@PostMapping("addExcel")
	 public String readExcel2(@RequestParam("file") MultipartFile file, Model model)
		      throws IOException { 

			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		    XSSFSheet worksheet = workbook.getSheetAt(0);
		    
		    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
		        ExcelDTO excel = new ExcelDTO();
		           
		        
		        DataFormatter formatter = new DataFormatter();		        
		        XSSFRow row = worksheet.getRow(i);
		        
		        String sNum =  formatter.formatCellValue(row.getCell(0));
		        String sName = formatter.formatCellValue(row.getCell(1));
		        String sId = formatter.formatCellValue(row.getCell(2));
		        int iNum = Integer.parseInt(sNum);
		        
		        excel.setNum(iNum);
		        excel.setName(sName);
		        excel.setId(sId);
		            
//		        excel.setId(row.getCell(0).getStringCellValue()); 
//		        excel.setName(row.getCell(1).getStringCellValue());
//		        excel.setId(row.getCell(2).getStringCellValue());
		        
		        em.insertExcel(excel);
		    }
		    return "redirect:/";

		  }
	

}
