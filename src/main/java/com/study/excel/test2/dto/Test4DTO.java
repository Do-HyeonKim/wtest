package com.study.excel.test2.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("test4")
public class Test4DTO {

	private String id;
	private String department;
	private String position;

}
