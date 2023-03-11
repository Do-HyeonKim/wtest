package com.study.excel.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.study.excel.board.dto.ExcelDTO;

@Mapper
public interface ExcelMapper {
	
	public int insertExcel(ExcelDTO excel);

}
