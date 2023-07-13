package com.study.excel.test2.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("test2")
public class Test2DTO {

	private String id;
	private String address;
	private String phone;

}
