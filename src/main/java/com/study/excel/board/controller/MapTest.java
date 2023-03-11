package com.study.excel.board.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.excel.board.dto.ParamDTO;
import com.study.excel.board.dto.ParamLogVO;
import com.study.excel.board.dto.ProjectDTO;
import com.study.excel.board.mapper.ParamRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("test2")
@RequiredArgsConstructor
public class MapTest {

	private final ParamRepository pRep;

	@RequestMapping("map")
	public void test(@RequestBody ParamDTO param) throws IOException {
		File filePath = new File("C:\\Users\\cpflv\\Downloads\\maptest.txt");
		File newFilePath = new File("C:\\Users\\cpflv\\Downloads\\maptest" + 4 + ".txt");

		try {
			// 파일 읽기
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			StringBuilder content = new StringBuilder();

			FileInputStream fis = new FileInputStream(filePath);
			FileOutputStream fos = new FileOutputStream(newFilePath);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fis.close();
			fos.close();

			newFilePath.renameTo(filePath);
			String line = "";
			String key = "";
			String value = "";
			// 파일 내용 한 줄씩 읽어오기
			while ((line = reader.readLine()) != null) {
				String[] token = line.split("=");
				key = token[0].trim();
				value = token[1].trim();
				Map<String, Object> map = new HashMap<String, Object>();
				if (value.contains(".") && value.contains("e")) {
					map.put(key, new BigDecimal(Double.parseDouble(value)));
				} else {
					map.put(key, value);
				}

				for (String keyword : map.keySet()) {
					if (keyword.equals(param.getKey())) {
						if (param.getValue().contains(".") && param.getValue().contains("e")) {
							content.append(keyword).append("=")
									.append(new BigDecimal(Double.parseDouble(param.getValue()))).append("\n");
							System.out.println(new BigDecimal(Double.parseDouble(param.getValue())));
						} else {
							content.append(keyword).append("=").append(param.getValue()).append("\n");
						}
					} else {
						content.append(key).append("=").append(map.get(key)).append("\n");
					}
				}
				ParamLogVO vo = new ParamLogVO();
				vo.setFileName(filePath.getName());
				vo.setFileKey(key);
				pRep.insertParamKey(vo);
			}

			// 파일 쓰기
			FileWriter writer = new FileWriter(filePath);
			writer.write(content.toString());
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("list")
	public void filetest() throws IOException {
		File fileToModify = new File("C:\\Users\\cpflv\\Downloads\\listtest.txt");
		FileReader fileInputStream = new FileReader(fileToModify);
		BufferedReader reader = new BufferedReader(fileInputStream);

		List<String> fileContent = new ArrayList<>();

		String line = "";
		// 파일 내용을 리스트에 저장
		while ((line = reader.readLine()) != null) {
			line = reader.readLine();
			fileContent.add(line);
		}

		// 파일 내용 수정
		String lineToModify = fileContent.get(2); // 3번째 라인
		String[] tokens = lineToModify.split(" ");
		tokens[2] = "8"; // key2의 1행 3열 수정

		// 수정된 라인으로 교체
		String modifiedLine = String.join(" ", tokens);
		fileContent.set(2, modifiedLine);

		// 파일 저장
		FileOutputStream fileOutputStream = new FileOutputStream(fileToModify);
		PrintWriter printWriter = new PrintWriter(fileOutputStream);
		for (String saveLine : fileContent) {
			printWriter.println(saveLine);
			System.out.println(printWriter);
		}
		printWriter.flush();
		printWriter.close();
		fileOutputStream.close();
	}

	@RequestMapping("list2")
	public void filetest2() throws IOException {
		File filename = new File("C:\\Users\\cpflv\\Downloads\\listtest.txt");
		File newFilename = new File("C:\\Users\\cpflv\\Downloads\\listtest_new.txt");
		// 파일 읽기

		try (BufferedReader br = new BufferedReader(new FileReader(filename));
				BufferedWriter bw = new BufferedWriter(new FileWriter(newFilename))) {

			// 파일 내용을 읽어서 2차원 배열로 저장
			String line;
			int row = 0;
			int col = 0;
			String[][] values = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					// 헤더는 건너뛰기
					continue;
				}
				String[] parts = line.split("\\s+");
				if (values == null) {
					values = new String[parts.length][];
				}
				values[row] = new String[parts.length];
				for (int i = 0; i < parts.length; i++) {
					values[row][i] = parts[i];
				}
				row++;
			}

			// 수정할 위치의 값을 변경
			int rowIndex = 2;
			int colIndex = 1;
			values[rowIndex][colIndex] = "4";

			// 수정된 값을 파일에 쓰기
			bw.write("#==============================");
			bw.newLine();
			bw.write("#key 1 key 2 key3 key4 key5");
			bw.newLine();
			bw.write("#==============================");
			bw.newLine();
			bw.write("#this is key");
			bw.newLine();
			bw.write("#description");
			bw.newLine();
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < values[i].length; j++) {
					bw.write(values[i][j] + " ");
				}
				bw.newLine();
			}
			bw.flush();
			System.out.println("파일 수정 완료: " + newFilename);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("test4")
	public String editTxtFile(@RequestBody ProjectDTO project) throws IOException {
		String id = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < project.getParameter().size(); i++) {
			File oldFile = new File(
					"C:\\Users\\cpflv\\Downloads\\newtemp\\" + project.getParameter().get(i).getFileName() + ".txt");
			File newFile = new File("C:\\Users\\cpflv\\Downloads\\newtemp\\" + project.getParameter().get(i).getFileName()
					+ "_" + id + ".txt");
			
			File temp = new File("C:\\Users\\cpflv\\Downloads\\newtemp\\temp.txt");
			
			FileInputStream fis = new FileInputStream(oldFile);
			FileOutputStream fos = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fis.close();
			fos.close();


			List<ParamDTO> parameter = project.getParameter();

			try (BufferedReader reader = new BufferedReader(new FileReader(temp));
					BufferedWriter writer = new BufferedWriter(new FileWriter(oldFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split("=");
					String key = parts[0].trim();
					String value = parts[1].trim();
					for (ParamDTO param : parameter) {
						if (param.getKey().equals(key)) {
							value = param.getValue();
							break;
						}
					}
					writer.write(key + " = " + value);
					writer.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			temp.renameTo(newFile);
			temp.delete();
		}
		   return "redirect:/";
		}
	
	@RequestMapping("test5")
	public String editcsv() {
        ProjectDTO project = new ProjectDTO();
        List<ParamDTO> params = new ArrayList<>();
        project.setParameter(params);
        String csvFile = "C:\\Users\\cpflv\\Downloads\\listtest.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;
            List<String> headers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue; // 주석 무시
                }
                StringTokenizer st = new StringTokenizer(line);
                if (isHeader) {
                    while (st.hasMoreTokens()) {
                        headers.add(st.nextToken());
                    }
                    isHeader = false;
                } else {
                    ParamDTO param = new ParamDTO();
                    List<List<String>> csv = new ArrayList<>();
                    int rowIndex = 0;
                    while (st.hasMoreTokens()) {
                        List<String> row = new ArrayList<>();
                        for (String value : st.nextToken().split("\\s+")) {
                            row.add(value);
                        }
                        csv.add(row);
                        rowIndex++;
                    }
                    param.setCsv(csv);
                    params.add(param);
                 //   System.out.println(param);
                    System.out.println(param.getCsv());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

	public String editcsv2() {	
	     String filePath = "example.csv";
	        List<List<String>> csvData = new ArrayList<>();

	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                if (!line.startsWith("#")) {  // 주석 제외
	                    String[] values = line.split(",");
	                    List<String> row = new ArrayList<>();
	                    for (String value : values) {
	                        row.add(value.trim());
	                    }
	                    csvData.add(row);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // csvData를 parameter에 저장
	        ProjectDTO projectDTO = new ProjectDTO();
	        ParamDTO paramDTO = new ParamDTO();
	        paramDTO.setCsv(csvData);
	        projectDTO.getParameter().add(paramDTO);
			return "redirect:/";
	}

	@RequestMapping("test6")
	public String editcsv3(@RequestBody ProjectDTO project) throws IOException {
		
//		String id = UUID.randomUUID().toString().replace("-", "");
//		
//		File orgFile = new File("C:\\Users\\cpflv\\Downloads\\temp\\list\\listtest.txt");
//			
//		
//		boolean isCsvChanged = false;
//		String line;
//
//		BufferedReader br = new BufferedReader(new FileReader(orgFile));
//		List<List<String>> fileContent = new ArrayList<>();
//
//		while ((line = br.readLine()) != null) {
//			if (line.startsWith("#")) {
//				continue;
//			}
//			String[] values = line.split(" ");
//			fileContent.add(Arrays.asList(values));
//		}
//		System.out.println("fileContent====" + fileContent);
//		br.close();
//		
//		
//		List<ParamDTO> orgParamList = new ArrayList<>();
//		ProjectDTO orgProject = new ProjectDTO();
//		ParamDTO orgParam = new ParamDTO();
//		
//		orgParam.setCsv(fileContent);
//		orgParamList.add(orgParam);
//	    orgProject.setParameter(orgParamList);
//
//		System.out.println("orgProject====" + orgProject);
//		
//		//@requestbody로 받은 부분
//		List<ParamDTO> newParamList = project.getParameter();
//		List<List<String>> csv = newParamList.get(0).getCsv();
//		ParamDTO newParam = new ParamDTO();
//		
//		System.out.println("csv====" + csv);
//
//		if (!csv.equals(fileContent)) {
//			isCsvChanged = true;
//			//파일복사
//			File newFile = new File("C:\\Users\\cpflv\\Downloads\\temp\\list\\listtest_"+id+".txt");
//			FileInputStream fis = new FileInputStream(orgFile);
//			FileOutputStream fos = new FileOutputStream(newFile);
//			byte[] buffer = new byte[1024];
//			int length;
//			while ((length = fis.read(buffer)) > 0) {
//				fos.write(buffer, 0, length);
//			}
//			fis.close();
//			fos.close();
//
//			orgFile.renameTo(newFile);
//		
//			//파일 다시 쓰기
//			FileWriter fw = new FileWriter(orgFile);
//			fw.write("#==============================\n");
//			fw.write("#key 1 key 2 key3 key4 key5\n");
//			fw.write("#==============================\n");
//			fw.write("#this is key\n");
//			fw.write("#description\n");
//			for (List<String> row : csv) {
//				fw.write(String.join(" ", row) + "\n");
//			}
//			fw.close();
//			
//			//db insert
//			System.out.println("newProject====" + project);
//			
//			
//			pRep.insertCsvParam(project);
//		}
		  String id = UUID.randomUUID().toString().replace("-", "");
		  for(int ii =0; ii< project.getParameter().size() ; ii++) {
	        File orgFile = new File("C:\\Users\\cpflv\\Downloads\\newtemp\\"+project.getParameter().get(ii).getFileName()+".txt");

	        boolean isCsvChanged = false;
	        String line;

	        BufferedReader br = new BufferedReader(new FileReader(orgFile));
	        List<List<String>> fileContent = new ArrayList<>();

	        while ((line = br.readLine()) != null) {
	            if (line.startsWith("#")) {
	                continue;
	            }
	            String[] values = line.split(" ");
	            fileContent.add(Arrays.asList(values));
	        }
	        System.out.println("fileContent====" + fileContent);
	        br.close();

	        List<ParamDTO> orgParamList = new ArrayList<>();
	        ProjectDTO orgProject = new ProjectDTO();
	        ParamDTO orgParam = new ParamDTO();

	        orgParam.setCsv(fileContent);
	        orgParamList.add(orgParam);
	        orgProject.setParameter(orgParamList);

	        System.out.println("orgProject====" + orgProject);

	        //@requestbody로 받은 부분
	        List<ParamDTO> newParamList = project.getParameter();
	        List<List<String>> csv = newParamList.get(0).getCsv();
	        ParamDTO newParam = new ParamDTO();

	        System.out.println("csv====" + csv);

	        if (!csv.equals(fileContent)) {
	            isCsvChanged = true;
	            //파일복사
	            File newFile = new File("C:\\Users\\cpflv\\Downloads\\newtemp\\"+project.getParameter().get(ii).getFileName()+"_"+id+".txt");
	            FileInputStream fis = new FileInputStream(orgFile);
	            FileOutputStream fos = new FileOutputStream(newFile);
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = fis.read(buffer)) > 0) {
	                fos.write(buffer, 0, length);
	            }
	            fis.close();
	            fos.close();

	            orgFile.renameTo(newFile);

	            //파일 다시 쓰기
	            FileWriter fw = new FileWriter(orgFile);
	            fw.write("#==============================\n");
	            fw.write("#key 1 key 2 key3 key4 key5\n");
	            fw.write("#==============================\n");
	            fw.write("#this is key\n");
	            fw.write("#description\n");
	            for (List<String> row : csv) {
	                fw.write(String.join(" ", row) + "\n");
	            }
	            fw.close();

	            //db insert
	            System.out.println("newProject====" + project);

	            // csvList 생성 및 DB insert
	            List<Map<String, String>> csvList = new ArrayList<>();
	            for (int i = 0; i < csv.size(); i++) {
	                Map<String, String> map = new HashMap<>();
	                for (int j = 0; j < csv.get(i).size(); j++) {
	                    map.put("col" + j, csv.get(i).get(j));
	                }
	                csvList.add(map);
	            }

	            // csvList를 파라미터로 하는 DB insert 메소드 호출
	            pRep.insertCsvParam(id,project,csvList);
        }
		  }
		return "redirect:/";

}
}