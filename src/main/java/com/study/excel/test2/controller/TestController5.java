package com.study.excel.test2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

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
@RequestMapping("tc5")
@RequiredArgsConstructor
public class TestController5 {

	
	  @RequestMapping("readData")
	    public JSONObject readData() {
	         try {
	                // 엑셀 파일 읽기
	                FileInputStream fileInputStream = new FileInputStream("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\interp.xlsx");
	                Workbook workbook = new XSSFWorkbook(fileInputStream);

	                // 엑셀 시트에서 데이터 읽어오기
	                double[][] pointLo = readSheet(workbook.getSheet("point_lo"));
	                double[][] pointNo = readSheet(workbook.getSheet("point_no"));
	                double[][] pointValue = readSheet(workbook.getSheet("point_value"));
	                double[][] pointX = readSheet(workbook.getSheet("point_x"));
	                double[][] pointY = readSheet(workbook.getSheet("point_y"));
	                double[][] pointZ = readSheet(workbook.getSheet("point_z"));
	                double[][] valueT = readSheet(workbook.getSheet("value_T"));

	                // 시간 선택
	                int timeSelect = (int)maxValueTime(valueT);
	                

	                // 선택된 시간에 해당하는 데이터 행 추출
	                double[] selectedRow = valueT[timeSelect];
	                double[] slicedArray = Arrays.copyOfRange(selectedRow, 1, selectedRow.length);
	                // 3x3 행렬로 변환
	                double[][] matrix = new double[3][3];
	                int dataIndex = 1;
	                for (int i = 0; i < 3; i++) {
	                    for (int j = 0; j < 3; j++) {
	                    	pointValue[i][j] = selectedRow[dataIndex++];
	                    }
	                }
	                
	                
	                // 결과 출력
	                for (int i = 0; i < 3; i++) {
	                    for (int j = 0; j < 3; j++) {
	                        System.out.print(pointValue[i][j] + " ");
	                    }
	                    System.out.println();
	                }
	                // x, y, z 값의 최대 및 최소값 계산
	                double minReadX = minValue(pointLo, 3);
	                double maxReadX = maxValue(pointLo, 3);
	                double minReadY = minValue(pointLo, 4);
	                double maxReadY = maxValue(pointLo, 4);
	                double minReadZ = minValue(pointLo, 5);
	                double maxReadZ = maxValue(pointLo, 5);
	                
	                double maxTemp = Arrays.stream(slicedArray).max().getAsDouble();
	                double minTemp = Arrays.stream(slicedArray).min().getAsDouble();
	                
	                System.out.println(maxTemp);
	                System.out.println(minTemp);
	                // x, y, z 간격 계산
	                double intervalX = Math.abs(maxReadX - minReadX) / 100;
	                double intervalY = Math.abs(maxReadY - minReadY) / 100;
	                double intervalZ = Math.abs(maxReadZ - minReadZ) / 100;
	                double intervalTemp = Math.abs(maxTemp - minTemp) / 100;
	                
	                
	                // meshgrid 생성
	                double[] xq = meshGrid(minReadX, maxReadX, intervalX);
	                double[] yq = meshGrid(minReadY, maxReadY, intervalY);
	                double[] zq = meshGrid(minReadZ, maxReadZ, intervalZ);
	                double[] temp = meshGrid(minTemp, maxTemp, intervalTemp);
	                
	             // meshGrid 생성
	                JSONArray xqArray = new JSONArray();
	                for (double value : xq) {
	                    xqArray.add(value);
	                }

	                JSONArray yqArray = new JSONArray();
	                for (double value : yq) {
	                	if(yq.length==1) {
	                		for(double xqValue : xq) {
	                			yqArray.add(value);
	                		}
	                	}else {
	                		yqArray.add(value);
	                	}
	                }

	                JSONArray zqArray = new JSONArray();
	                for (double value : zq) {
	                    zqArray.add(value);
	                }
	                
	                

	                JSONArray tempArray = new JSONArray();
	                for (double value : temp) {
	                	tempArray.add(value);
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
	                jsonObject.put("temp", tempArray);
	                jsonObject.put("pointValue", pointValueArray);
	                
	                
	             // xq 배열 출력
//	                System.out.println("xq:");
//	                for (double value : xq) {
//	                    System.out.print(value + " ");
//	                }
//	                System.out.println();
//
//	                // yq 배열 출력
//	                System.out.println("yq:");
//	                for (double value : yq) {
//	                    System.out.print(value + " ");
//	                }
//	                System.out.println();
//
//	                // zq 배열 출력
//	                System.out.println("zq:");
//	                for (double value : zq) {
//	                    System.out.print(value + " ");
//	                }
	                
	                
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
	    private static double maxValueTime(double[][] matrix) {
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
	    
	    

	 // 행렬에서 최대값 찾기
	    private static double maxValue(double[][] matrix, int colIndex) {
	        double max = matrix[0][colIndex];
	        for (double[] row : matrix) {
	            if (row[colIndex] > max) {
	                max = row[colIndex];
	            }
	        }
	        return max;
	    }

	    // 행렬에서 최소값 찾기
	    private static double minValue(double[][] matrix, int colIndex) {
	        double min = matrix[0][colIndex];
	        for (double[] row : matrix) {
	            if (row[colIndex] < min) {
	                min = row[colIndex];
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
	    

	    public static double interp1(double[][] valueT, int timeIndex, int rowIndex, int colIndex) {
	        int numOfRows = valueT.length;
	        int numOfCols = valueT[0].length;

	        // 보간할 값의 행 인덱스가 행렬 범위를 벗어나는지 확인
	        if (rowIndex < 0 || rowIndex >= numOfRows) {
	            throw new IllegalArgumentException("Row index is out of range.");
	        }

	        // 보간할 값의 열 인덱스가 행렬 범위를 벗어나는지 확인
	        if (colIndex < 0 || colIndex >= numOfCols) {
	            throw new IllegalArgumentException("Column index is out of range.");
	        }

	        // 주어진 시간 인덱스가 행렬 범위를 벗어나는지 확인
	        if (timeIndex < 0 || timeIndex >= numOfCols) {
	            throw new IllegalArgumentException("Time index is out of range.");
	        }

	        // 보간할 값을 얻기 위해 사용할 시간 값
	        double timeSelect = valueT[0][timeIndex];

	        // 보간할 값의 바로 왼쪽과 오른쪽의 인덱스 찾기
	        int leftIndex = -1;
	        int rightIndex = -1;
	        for (int i = 1; i < numOfCols - 1; i++) {
	            if (valueT[0][i] <= timeSelect && valueT[0][i + 1] > timeSelect) {
	                leftIndex = i;
	                rightIndex = i + 1;
	                break;
	            }
	        }

	        // 보간할 값이 범위를 벗어나는지 확인
	        if (leftIndex == -1 || rightIndex == -1) {
	            throw new IllegalArgumentException("Time value is out of range.");
	        }

	        // 보간할 값의 바로 왼쪽과 오른쪽의 값을 얻기
	        double x0 = valueT[rowIndex][leftIndex];
	        double x1 = valueT[rowIndex][rightIndex];
	        double y0 = valueT[rowIndex][timeIndex];
	        double y1 = valueT[rowIndex][timeIndex + 1];

	        // 선형 보간하여 값을 반환
	        return y0 + ((timeSelect - x0) / (x1 - x0)) * (y1 - y0);
	    }
}
