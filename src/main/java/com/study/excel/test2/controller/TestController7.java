package com.study.excel.test2.controller;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.build.AllowSysOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.excel.test2.dto.Movo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tc7")
@RequiredArgsConstructor
public class TestController7 {
	
	
	@RequestMapping("run")
	public List<Movo> run() throws Exception {
	    Map<Integer, Movo> map = new HashMap<>();
	    for (String target : getSearchList()) {
	        List<Movo> tempList = getList(target);
	        for (Movo movo : tempList) {
	            if (!map.containsKey(movo.getCid())) {
	                map.put(movo.getCid(), movo);
	            } else {
	                Movo existingMov = map.get(movo.getCid());
	                existingMov.merge(movo); // 기존 Movos 객체에 정보 추가
	            }
	        }
	    }

	    // Map의 values를 리스트로 변환하여 반환
	    List<Movo> returnList = new ArrayList<>(map.values());
	    return returnList;
	}
	
	//이렇게하면 cid별로 point가 뽑혀져서 나옴
//	public List<Movo> run() throws Exception {
//		 List<Movo> returnList = new ArrayList<>();
//		for(String target : getSearchList()) {
//			List<Movo> tempList = getList(target);
//			returnList.addAll(tempList);
//		}
//		return returnList;
//	}
	
	
	
	
	public List<Movo> getList(String target) throws Exception {
//	public JSONObject getList() throws Exception{	
	        List<Movo> movoList = new ArrayList<>();
	        Map<Integer, Movo> cidToMoVoMap = new HashMap<>();
	        JSONParser parser = new JSONParser();
	        Reader reader = new FileReader("C:\\Users\\cpflv\\OneDrive\\바탕 화면\\"+target+".json");
	        Object obj = parser.parse(reader);
	        JSONObject jsonObject = (JSONObject)obj;
            JSONObject annotations = (JSONObject) jsonObject.get("Annotations");
            JSONArray annotationArray = (JSONArray) annotations.get("Annotation");
//	        
            
      
	        for(Object obj2 : annotationArray) {
	        	JSONObject annotation = (JSONObject) obj2;
	        	JSONObject linkedRecords = (JSONObject) annotation.get("LinkedRecords");
	              
	              if(linkedRecords!=null) {
	              JSONArray cidArray = (JSONArray) linkedRecords.get("CID");
	              
	              for (Object cidObj : cidArray) {
	            	  int cid = ((Long) cidObj).intValue();
	            	  
	            	  
	            	  Movo moVo ;
	            	  if (cidToMoVoMap.containsKey(cid)) {
	                        moVo = cidToMoVoMap.get(cid);
	                    } else {
	                    	moVo = new Movo();
	                  	  	moVo.setCid(cid);
	                        cidToMoVoMap.put(cid, moVo);
	                        movoList.add(moVo);
	                    }
	            	  
	            	  
	            	  JSONArray dataArray = (JSONArray) annotation.get("Data");
	            	  
	            	    for (Object dataObj : dataArray) {
	                        JSONObject data = (JSONObject) dataObj;
	                        JSONObject value = (JSONObject) data.get("Value");
	                        JSONArray stringWithMarkupArray = (JSONArray) value.get("StringWithMarkup");

	                        if(stringWithMarkupArray!=null && stringWithMarkupArray.size() >0) {
	                        for (Object stringWithMarkupObj : stringWithMarkupArray) {
	                            JSONObject stringWithMarkup = (JSONObject) stringWithMarkupObj;
	                            String point = (String) stringWithMarkup.get("String");
	                            if(target.equals("Boiling Point")) {
	                            moVo.addBoilingPoint(point);
	                            }else if (target.equals("Melting point")) {
	                            moVo.addMeltingPoint(point);
	                            }
	                        	}
	                        }
	                    }
	              	}
	              }
	        }
	        
	        
		
		return movoList;
	}
	
	
	public List<String> getSearchList(){
		List<String> searchList = new ArrayList<>();
		searchList.add("Boiling Point");
		searchList.add("Melting point");
		
		return searchList;
	}
	

	
}
