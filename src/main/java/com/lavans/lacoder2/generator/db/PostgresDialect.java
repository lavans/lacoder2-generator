/* $Id: PostgresDialect.java 508 2012-09-20 14:41:55Z dobashi $
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
public class PostgresDialect extends TypeManager {
	private static Properties map = new Properties();
	static{
		map.put("int",		"int4");
		map.put("Integer",	"int4");
		map.put("long",		"int8");
		map.put("Long",		"int8");
		map.put("int-sequence",		"serial4");
		map.put("Integer-sequence","serial4");
		map.put("long-sequence",	"serial8");
		map.put("Long-sequence",	"serial8");
		map.put("BigDecimal","numeric");
		map.put("String",	"text");
		map.put("Date",		"timestamp");
		map.put("boolean",	"boolean");
		map.put("byte[]",	"bytea");
	}

	/**
	 * javaの型に対応したDBの型を取得。
	 * @return
	 */
	@Override
	public String getDbType(String javaType){
		return map.getProperty(javaType);
	}

	/**
	 * javaの型に対応したDBの型を取得。
	 * @return
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
		// Postgresはserial指定にすると自動的にシーケンスを作成するので不要。
		/* CREATE SEQUENCE ENTITY_ATTR_SEQ
		  INCREMENT BY 1
		  START WITH 98080
		  MAXVALUE 99999999
		  MINVALUE 1
		  CYCLE
		  NOCACHE
		*/

		return "";
	}

	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getNextval(com.lavans.lacoder2.generator.main.Entity, com.lavans.lacoder2.generator.main.Attribute)
	 */
	public String getNextval(Attribute attr) {
		return "SELECT NEXTVAL('"+ attr.getEntity().getTableName() +"_"+ attr.getConstName() +"_SEQ')";
	}

	/**
	 * backuptable
	 * @param attr
	 * @return
	 */
	public String getNextvalBak(Attribute attr) {
		return ("SELECT NEXTVAL('"+ attr.getEntity().getTableName() +"_BAK_BACKUP_ID_SEQ')");
	}

	/**
	 * シーケンスの型を取得。バックアップテーブル用。
	 * @return
	 */
	@Override
	public String getSequenceType(){
		return "INT8";
	}


	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getCurrentTime()
	 */
	public String getCurrentTime() {
		return "now()";		// "sysdate"
	}
}
