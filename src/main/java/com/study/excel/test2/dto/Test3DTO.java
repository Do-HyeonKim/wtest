package com.study.excel.test2.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("test3")
public class Test3DTO {

	private String id;
	private String city;
	private String country;

}
