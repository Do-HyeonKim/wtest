package com.study.excel.test2.controller;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tc4")
@RequiredArgsConstructor
public class TestController4 {
	
	
	@RequestMapping("readData")
	public JSONObject readData() {
		 try {
	            // 엑셀 파일 읽기
	            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\interp.xlsx");
	            Workbook workbook = new XSSFWorkbook(fileInputStream);

	            // 엑셀 시트에서 데이터 읽어오기
	            double[][] pointNo = readSheet(workbook.getSheet("point_no"));
	            double[][] pointValue = readSheet(workbook.getSheet("point_value"));
	            double[][] pointX = readSheet(workbook.getSheet("point_x"));
	            double[][] pointY = readSheet(workbook.getSheet("point_y"));
	            double[][] pointZ = readSheet(workbook.getSheet("point_z"));
	            double[][] valueT = readSheet(workbook.getSheet("value_T"));

	            // 시간 선택
	            double timeSelect = maxValue(valueT);
	            

	            // 보간 알고리즘을 사용하여 값 보간
	            double[][] valueSelect = new double[pointNo.length][pointNo[0].length];
	            for (int i = 0; i < pointNo.length; i++) {
	                for (int j = 0; j < pointNo[0].length; j++) {
	                    int nop = i * pointNo[0].length + j + 1;
	                    valueSelect[i][j] = interp1(valueT, 0, nop, timeSelect);
	                    pointValue[i][j] = valueSelect[i][j];
	                }
	            }
	            
	            
	       
	            // x, y, z 값의 최대 및 최소값 계산
	            double minReadX = minValue(pointX);
	            double maxReadX = maxValue(pointX);
	            double minReadY = minValue(pointY);
	            double maxReadY = maxValue(pointY);
	            double minReadZ = minValue(pointZ);
	            double maxReadZ = maxValue(pointZ);

	            // x, y, z 간격 계산
	            double intervalX = Math.abs(maxReadX - minReadX) / 100;
	            double intervalY = Math.abs(maxReadY - minReadY) / 100;
	            double intervalZ = Math.abs(maxReadZ - minReadZ) / 100;

	            // meshgrid 생성maxReadX
	            double[] xq = meshGrid(minReadX, maxReadX, intervalX);
	            double[] yq = meshGrid(minReadY, maxReadY, intervalY);
	            double[] zq = meshGrid(minReadZ, maxReadZ, intervalZ);
	            
	            
	         // meshGrid 생성
	            JSONArray xqArray = new JSONArray();
	            for (double value : xq) {
	                xqArray.add(value);
	            }

	            JSONArray yqArray = new JSONArray();
	            for (double value : yq) {
	                yqArray.add(value);
	            }

	            JSONArray zqArray = new JSONArray();
	            for (double value : zq) {
	                zqArray.add(value);
	            }
	            

	            // pointValue 배열을 JSON 배열로 변환
	            JSONArray pointValueArray = new JSONArray();
	            for (double[] row : pointValue) {
	                JSONArray rowArray = new JSONArray();
	                for (double value : row) {
	                    rowArray.add(value);
	                }
	                pointValueArray.add(rowArray);
	            }

	            

	            // JSONObject로 변환
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("xq", xqArray);
	            jsonObject.put("yq", yqArray);
	            jsonObject.put("zq", zqArray);
	            jsonObject.put("pointValue", pointValueArray);
	            
	            
	         // xq 배열 출력
	            System.out.println("xq:");
	            for (double value : xq) {
	                System.out.print(value + " ");
	            }
	            System.out.println();

	            // yq 배열 출력
	            System.out.println("yq:");
	            for (double value : yq) {
	                System.out.print(value + " ");
	            }
	            System.out.println();

	            // zq 배열 출력
	            System.out.println("zq:");
	            for (double value : zq) {
	                System.out.print(value + " ");
	            }
	            
	            
	            workbook.close();
	            fileInputStream.close();
	            
	            return jsonObject;

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 return null;
	}
	
	
	// 엑셀 시트에서 데이터 읽어오기
    private static double[][] readSheet(Sheet sheet) {
        int rows = sheet.getPhysicalNumberOfRows();
        int cols = sheet.getRow(0).getPhysicalNumberOfCells();
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < cols; j++) {
                Cell cell = row.getCell(j);
                data[i][j] = cell.getNumericCellValue();
            }
        }
        return data;
    }

 // 행렬에서 최대값 찾기
    private static double maxValue(double[][] matrix) {
        double max = matrix[0][0];
        for (double[] row : matrix) {
            for (double val : row) {
                if (val > max) {
                    max = val;
                }
            }
        }
        return max;
    }

    // 행렬에서 최소값 찾기
    private static double minValue(double[][] matrix) {
        double min = matrix[0][0];
        for (double[] row : matrix) {
            for (double val : row) {
                if (val < min) {
                    min = val;
                }
            }
        }
        return min;
    }

    // meshgrid 생성
    private static double[] meshGrid(double min, double max, double interval) {
        int numPoints = (int) Math.ceil((max - min) / interval) + 1;
        double[] grid = new double[numPoints];
        for (int i = 0; i < numPoints; i++) {
            grid[i] = min + i * interval;
        }
        return grid;
    }

    // 보간 알고리즘 구현
    private static double interp1(double[][] valueT, int timeIndex, int nop, double timeSelect) {
        // 예시로 선형 보간 알고리즘을 구현합니다.
        double[] timeColumn = valueT[0]; // 첫 번째 열은 시간 정보입니다.
        double[] valueColumn = valueT[nop]; // nop에 해당하는 열은 보간할 값입니다.

        // 시간 값의 최소와 최대 범위 확인
        double minTime = timeColumn[0];
        double maxTime = timeColumn[timeColumn.length - 1];

        // 시간이 주어진 범위를 벗어나면 가장 가까운 값으로 보간합니다.
        if (timeSelect <= minTime) {
            return valueColumn[0];
        }
        if (timeSelect >= maxTime) {
            return valueColumn[valueColumn.length - 1];
        }

        // 주어진 시간 값에 가장 가까운 인덱스를 찾습니다.
        int index = 0;
        for (int i = 0; i < timeColumn.length - 1; i++) {
            if (timeSelect >= timeColumn[i] && timeSelect <= timeColumn[i + 1]) {
                index = i;
                break;
            }
        }

        // 선형 보간을 수행합니다.
        double t0 = timeColumn[index];
        double t1 = timeColumn[index + 1];
        double v0 = valueColumn[index];
        double v1 = valueColumn[index + 1];
        return v0 + (v1 - v0) * (timeSelect - t0) / (t1 - t0);
    }

}
