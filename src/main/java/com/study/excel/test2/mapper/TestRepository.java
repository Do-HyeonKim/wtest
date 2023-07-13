package com.study.excel.test2.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.study.excel.test2.dto.Test1DTO;

@Mapper
public interface TestRepository {
	
	public Test1DTO getTest1(Test1DTO test1);

}
