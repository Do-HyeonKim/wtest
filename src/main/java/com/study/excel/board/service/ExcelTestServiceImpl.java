package com.study.excel.board.service;

import org.springframework.stereotype.Service;

import com.study.excel.board.dto.ExcelTestDTO;
import com.study.excel.board.mapper.ExcelTestMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelTestServiceImpl implements ExcelTestService {

	private final ExcelTestMapper etm;

	@Override
	public int insertExcel(ExcelTestDTO excel) {
		
		return etm.insertExcel(excel);
	} 
	
	
}
