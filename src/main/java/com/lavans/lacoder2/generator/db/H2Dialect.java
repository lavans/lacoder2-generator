/* $Id: H2Dialect.java 508 2012-09-20 14:41:55Z dobashi $
 * create: 2004/12/28
 * (c)2004 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.db;

import java.util.Properties;

import com.lavans.lacoder2.generator.model.Attribute;

/**
 * @author dobashi
 * @version 1.00
 */
public class H2Dialect extends TypeManager {
	private static Properties map = new Properties();
	static{
		map.put("int",		"INT");
		map.put("Integer",	"INT");
		map.put("long",		"BIGINT");
		map.put("Long",		"BIGINT");
		// IDENTITYによるSEQの自動生成ではなく自前でわかりやすい名前で用意するのでBIGINT
		map.put("int-sequence",		"BIGINT");
		map.put("Integer-sequence","BIGINT");
		map.put("long-sequence",	"BIGINT");
		map.put("Long-sequence",	"BIGINT");
		map.put("BigDecimal","DECIMAL");
		map.put("String",	"VARCHAR");
		map.put("Date",		"TIMESTAMP");
		map.put("boolean",	"BOOLEAN");
		map.put("byte[]",	"BINARY");
	}

	/**
	 * シーケンスの型を取得。バックアップテーブル用。
	 * @return
	 */
	@Override
	public String getSequenceType(){
		return "BIGINT";
	}

	/**
	 * javaの型に対応したDBの型を取得。
	 * @return
	 */
	@Override
	public String getDbType(String javaType){
		return map.getProperty(javaType);
	}

	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getJavaType(java.lang.String)
	 */
	public String getDbType(Attribute attr) {
		String javaType = attr.getJavaType();
		// シーケンスの場合
		if(attr.isSequence()){
			javaType += "-sequence";
		}
		String dbType = map.getProperty(javaType);

		// BigDecimalでprecisionとscaleの指定がある場合
		if(javaType.equals("BigDecimal")){
			if(attr.getPrecision()!=null && attr.getScale()!=null){
				dbType +="("+attr.getPrecision()+","+attr.getScale()+")";
			}else 	if(attr.getPrecision()!=null && attr.getScale()==null){
				dbType +="("+attr.getPrecision()+")";
			}
			// scaleのみ指定の場合や指定無しの場合はnumericとだけ宣言する。
		}

		return dbType;
	}


	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getSequenceType()
	 */
//	public String getSequenceType() {
//		return "serial";	// number(10)?
//	}

	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getSequenceSql(com.lavans.lacoder2.generator.main.Entity, com.lavans.lacoder2.generator.main.Attribute)
	 */
	public String getSequenceSql(String entity) {
		// sequenceはPKでしか使わないので、"_PK_SEQ"とする。
		String result = "CREATE SEQUENCE "+ entity +"_PK_SEQ;";
		return result;
	}

	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getNextval(com.lavans.lacoder2.generator.main.Entity, com.lavans.lacoder2.generator.main.Attribute)
	 */
	public String getNextval(Attribute attr) {
		return "SELECT "+ attr.getEntity().getTableName() +"_PK_SEQ.NEXTVAL";
	}

	/**
	 * sequence update
	 */
	public String getSequenceUpdateSql(Attribute attr){
		// TODO
		return "";
	}

	/**
	 * backuptable
	 * @param attr
	 * @return
	 */
	public String getNextvalBak(Attribute attr) {
		return "SELECT "+ attr.getEntity().getTableName() +"_BAK_PK_SEQ.NEXTVAL";
	}
	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getCurrentTime()
	 */
	public String getCurrentTime() {
		return "now()";		// "sysdate"
	}
}
