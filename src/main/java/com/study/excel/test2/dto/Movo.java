package com.study.excel.test2.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("test7")
public class Movo {

	private Integer cid;
	private List<String> boilingPointList = new ArrayList<>();
	private List<String> meltingPointList = new ArrayList<>();

	public List<String> getBoilingPointList() {
		return boilingPointList;
	}

	public void addBoilingPoint(String boilingPoint) {
		this.boilingPointList.add(boilingPoint);
	}

	public List<String> getMeltingPointList() {
		return meltingPointList;
	}

	public void addMeltingPoint(String meltingPoint) {
		this.meltingPointList.add(meltingPoint);
	}
	
    public void merge(Movo other) {
        this.boilingPointList.addAll(other.getBoilingPointList());
        this.meltingPointList.addAll(other.getMeltingPointList());
    }
    
    

}
