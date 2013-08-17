/* $Id: OracleDialect.java 508 2012-09-20 14:41:55Z dobashi $
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
public class OracleDialect extends TypeManager {
	private static Properties map = new Properties();
	static{
		map.put("int",		"NUMBER(10)");
		map.put("Integer",	"NUMBER(10)");
		map.put("long",		"NUMBER(20)");
		map.put("Long",		"NUMBER(20)");
		map.put("BigDecimal","NUMBER");
		map.put("String",	"VARCHR2");
		map.put("Date",		"DATE");
		map.put("boolean",	"NUMBER(1)");
		map.put("byte[]",	"BLOB");


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
		String dbType = map.getProperty(attr.getJavaType());

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
		// sequenceはPKでしか使わないので、"PK_SEQ"とする。
		String result =
		  "CREATE SEQUENCE "+ entity +"_PK_SEQ "
		  +"INCREMENT BY 1 START WITH 1 MAXVALUE 99999999 MINVALUE 1 CYCLE NOCACHE ";
		return result;
	}

	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getNextval(com.lavans.lacoder2.generator.main.Entity, com.lavans.lacoder2.generator.main.Attribute)
	 */
	public String getNextval(Attribute attr) {
		return attr.getEntity().getTableName() +"_PK_SEQ.NEXTVAL";
	}

	/**
	 * backuptable
	 * @param attr
	 * @return
	 */
	public String getNextvalBak(Attribute attr) {
		return attr.getEntity().getTableName() +"_BAK_PK_SEQ.NEXTVAL";
	}

	/**
	 * sequence update
	 */
	public String getSequenceUpdateSql(Attribute attr){
		// TODO
		return "";
	}


	/**
	 * シーケンスの型を取得。バックアップテーブル用。
	 * @return
	 */
	@Override
	public String getSequenceType(){
		return "NUMBER(20)";
	}


	/* (非 Javadoc)
	 * @see com.lavans.lacoder2.generator.main.TypeManager#getCurrentTime()
	 */
	public String getCurrentTime() {
		return "sysdate";		// "sysdate"
	}
}
