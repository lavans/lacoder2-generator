/* $Id: EnumClass.java 604 2012-12-11 10:40:02Z dobashi $
 * create: 2005/01/20
 * (c)2005 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lavans.lacoder2.lang.StringUtils;

/**
 * @author dobashi
 *
 */
public class EnumClass {
	private String name = null;
	private String title = null;
	private String comment = null;
	private List<EnumMember> memberList = new ArrayList<EnumMember>();
	private EnumMember defaultMember = null;
	/** 各値を保持するリストの名称一覧 */
	private List<String> fieldList = new ArrayList<String>();
	/** fieldListに対応するタイトル一覧 */
	private List<String> titleList = new ArrayList<String>();
	/** int値フィールド一覧 */
	private List<String> intList = new ArrayList<String>();
	/** boolean値フィールド一覧 */
	private List<String> booleanList = new ArrayList<String>();

	/**
	 * @return name を戻します。
	 */
	public String getName() {
		return name;
	}

	public String getClassName() {
		return StringUtils.capitalize(name);
	}

	/**
	 * @param name
	 *            name を設定。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return title を戻します。
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            title を設定。
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param member
	 * @return
	 */
	public boolean add(EnumMember member) {
		if (member.isDefault()) {
			defaultMember = member;
		}
		return memberList.add(member);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public EnumMember get(int member) {
		return memberList.get(member);
	}

	public List<EnumMember> getMemberList() {
		return memberList;
	}

	/**
	 * @param member
	 * @return
	 */
	public boolean remove(EnumMember member) {
		return memberList.remove(member);
	}

	/**
	 * @return defaultMemger を戻します。
	 */
	public EnumMember getDefaultMember() {
		return defaultMember;
	}

	/**
	 * @param defaultMemger
	 *            defaultMemger を設定。
	 */
	public void setDefaultMember(EnumMember defaultMember) {
		this.defaultMember = defaultMember;
	}

	/**
	 * fieldListをcsv形式で追加
	 *
	 * @param listStr
	 */
	public void setFieldList(String listStr) {
		Collections.addAll(fieldList,  StringUtils.splitTrim(listStr, ","));
	}

	/**
	 * @return fieldList を戻します。
	 */
	public List<String> getFieldList() {
		return fieldList;
	}

	public List<String> getBooleanList() {
		return booleanList;
	}

	public void setBooleanList(String listStr) {
		Collections.addAll(booleanList,  StringUtils.splitTrim(listStr, ","));
	}

	/**
	 * Return whether the field is boolean.
	 *
	 * @param fieldName
	 * @return
	 */
	public boolean isBoolean(String fieldName){
		return booleanList.contains(fieldName);
	}

	/**
	 * Get field list which has int value.
	 *
	 * @return
	 */
	public List<String> getIntList() {
		return intList;
	}

	/**
	 * Set field list which has int value.
	 *
	 * @param listStr
	 */
	public void setIntList(String listStr) {
		Collections.addAll(intList,  StringUtils.splitTrim(listStr, ","));
	}

	/**
	 * Return whether the field is int.
	 *
	 * @param fieldName
	 * @return
	 */
	public boolean isInt(String fieldName){
		return intList.contains(fieldName) || fieldName.equals("int");
	}


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(String listStr) {
		String[] lists = listStr.split(",");
		for (int i = 0; i < lists.length; i++) {
			titleList.add(lists[i].trim());
		}
	}
}
