/* $Id: JavaType.java 508 2012-09-20 14:41:55Z dobashi $
 * create: 2005/01/04
 * (c)2005 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.main;

import java.util.Properties;

/**
 * @author dobashi
 * @version 1.00
 */
public class JavaType{
	private static Properties jdbcMap = new Properties();
	static{
		jdbcMap.put("int",		"Int");
		jdbcMap.put("Integer",	"Int");
		jdbcMap.put("double",	"Double");
		jdbcMap.put("Double",	"Double");
		jdbcMap.put("long",		"Long");
		jdbcMap.put("Long",		"Long");
		jdbcMap.put("String",	"String");
		jdbcMap.put("Date",		"Timestamp");
		jdbcMap.put("boolean",	"Boolean");
		jdbcMap.put("Boolean",	"Boolean");
		jdbcMap.put("BigDecimal",	"BigDecimal");
		jdbcMap.put("byte[]",	"Bytes");
		//jdbcMap.put("byte[]",	"Blob");

	}
	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getJavaType(java.lang.String)
	 */
	public static String getJdbcMethodName(String type) {
		return jdbcMap.getProperty(type);
	}

//	private static Properties wrapperMap = new Properties();
//	static{
//		wrapperMap.put("int",	"Integer");
//		wrapperMap.put("long",	"Long");
//		wrapperMap.put("double","Double");
//		wrapperMap.put("float","Float");
//		wrapperMap.put("boolean","Boolean");
//	}
//
//
//	/* (非 Javadoc)
//	 * @see com.lavans.lacoder2.generator.main.TypeManager#getJavaType(java.lang.String)
//	 */
//	public static String getWrapperName(String type) {
//		return wrapperMap.getProperty(type);
//	}

}
