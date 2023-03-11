package com.study.excel.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.study.excel.board.dto.ExcelTestDTO;

@Mapper
public interface ExcelTestMapper {
	
	// 엑셀 등록
	public int insertExcel(ExcelTestDTO excel) ;

}
