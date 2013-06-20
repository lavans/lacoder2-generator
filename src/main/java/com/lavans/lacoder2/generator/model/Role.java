package com.lavans.lacoder2.generator.model;

import java.util.ArrayList;
import java.util.List;

import com.lavans.lacoder2.lang.StringUtils;

/**
 * 
 * @author Yuki
 *
 */
public class Role {
	private static List<String> list = new ArrayList<String>();
	
	public static void addUserType(String userType){
		list.add(userType);
	}
	
	public static List<String> getList(){
		return list;
	}

	public static void setList(List<String> userTypeList) {
		Role.list = userTypeList;
	}
	
	private String name;
	public Role(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public String getClassName(){
		return StringUtils.capitalize(name);
	}
	@Override
	public String toString(){
		return name;
	}
	
}
