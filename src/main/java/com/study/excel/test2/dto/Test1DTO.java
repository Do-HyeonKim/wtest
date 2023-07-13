package com.study.excel.test2.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("test1")
public class Test1DTO {

	
	private String id;
	private String name;
	private String age;
}
