/* $Id: EnumMember.java 604 2012-12-11 10:40:02Z dobashi $
 * create: 2005/01/20
 * (c)2005 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lavans.lacoder2.lang.StringUtils;

/**
 * @author dobashi
 * 
 */
public class EnumMember {
	private String name = null;
	private String title = null, comment = null;
	private boolean isDefault = false;

	/** 各リスト値保存用のMap */
	private Map<String, String> valueMap = new HashMap<String, String>();

	/** 各リスト除外用のSet */
	private Set<String> excludeSet = new HashSet<String>();

	/**
	 * @return isDefault を戻します。
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            isDefault を設定。
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return name を戻します。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return name を戻します。
	 */
	public String getConstName() {
		// 最初の１文字が大文字ならnameはCONST形式と判断する。
		if (Character.isUpperCase(name.charAt(0))) {
			return name;
		}
		// 小文字スタートなら旧形式(nameを小文字、enum名が大文字)
		return StringUtils.toUnderscore(name).toUpperCase();
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
	 * @param arg0
	 * @return
	 */
	public String getValue(Object arg0) {
		return valueMap.get(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public Object putValue(String arg0, String arg1) {
		return valueMap.put(arg0, arg1);
	}

	/**
	 * リスト除外設定。
	 * 
	 * @param 除外するリスト名
	 *            。
	 * @return
	 */
	public void setExclude(String excludeStr) {
		excludeSet.add(excludeStr);
		// String[] excludes = excludeStr.split(",");
		// for(int i=0; i<excludes.length; i++){
		// excludeSet.add(excludes[i].trim());
		// }
	}

	/**
	 * このリスト一覧から除外するか
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public boolean isExclude(String listName) {
		return excludeSet.contains(listName);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
