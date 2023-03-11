package com.study.excel.board.dto;


import java.util.List;

import lombok.Data;

@Data
public class ParamDTO {

	private String key;
	private String value;
	private String fileName;
	
	private List<List<String>> csv;
}
