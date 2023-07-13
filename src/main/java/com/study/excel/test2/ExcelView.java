package com.study.excel.test2;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.study.excel.test2.dto.Test1DTO;

public class ExcelView extends AbstractXlsView  {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        Test1DTO test1 = (Test1DTO) model.get("test1");

        // 시트 생성
        Sheet sheet = workbook.createSheet("테이블 정보");

        // 헤더 행 생성
        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        // 데이터 행 생성
        Row dataRow = sheet.createRow(1);
        createDataRow(dataRow, test1);
    }

    private void createHeaderRow(Row headerRow) {
        int cellIndex = 0;
        headerRow.createCell(cellIndex++).setCellValue("아이디");
        headerRow.createCell(cellIndex++).setCellValue("이름");
        headerRow.createCell(cellIndex).setCellValue("나이");
    }

    private void createDataRow(Row dataRow, Test1DTO test1) {
        int cellIndex = 0;
        dataRow.createCell(cellIndex++).setCellValue(test1.getId());
        dataRow.createCell(cellIndex++).setCellValue(test1.getName());
        dataRow.createCell(cellIndex).setCellValue(test1.getAge());
    }
}
