package com.study.excel.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.study.excel.board.dto.ParamLogVO;
import com.study.excel.board.dto.ProjectDTO;

@Mapper
public interface ParamRepository {

	public int insertParamKey(ParamLogVO vo);
	
	
	void insertCsvParam(String id, ProjectDTO project,  List<Map<String, String>> csvList);
}
