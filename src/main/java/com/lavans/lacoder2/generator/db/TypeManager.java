/* $Id: TypeManager.java 508 2012-09-20 14:41:55Z dobashi $
 * create: 2004/12/28
 * (c)2004 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.db;

import java.util.HashMap;
import java.util.Map;

import com.lavans.lacoder2.generator.model.Attribute;
import com.lavans.lacoder2.generator.model.Entity;

/**
 * @author dobashi
 * @version 1.00
 */
public abstract class TypeManager {
	public static final String SEQUENCE="sequence";
	private static Map<String, TypeManager> typeMap = new HashMap<String, TypeManager>();
	static{
		typeMap.put("oracle", new OracleDialect());
		typeMap.put("postgres", new PostgresDialect());
		typeMap.put("h2", new H2Dialect());
	}
	public static TypeManager getInstance(String dbType){
		return typeMap.get(dbType);
	}

	/**
	 * シーケンスの実際の型を取得。
	 * @return
	 */
//	public String getSequenceType();

	/**
	 * Javaの型からDBの型を取得
	 * @param dbType
	 * @return
	 */
	public abstract String getDbType(String javaType);
	public abstract String getDbType(Attribute attr);

	/**
	 * シーケンス作成用のSQL取得。
	 * @param entity
	 * @param attr
	 * @return
	 */
	public String getSequenceSql(Entity entity){
		return getSequenceSql(entity.getTableName().toUpperCase());
	}
	public abstract String getSequenceSql(String entity);
	/**
	 * シーケンスの次の値を取得。
	 * @param entity
	 * @param attr
	 * @return
	 */
	public abstract String getNextval(Attribute attr);
	public abstract String getNextvalBak(Attribute attr);
	/**
	 * シーケンスを最後の値に更新
	 * @param attr
	 * @return
	 */
	public abstract String getSequenceUpdateSql(Attribute attr);

	/**
	 * シーケンスの型を取得。バックアップテーブル用。
	 * @return
	 */
	public abstract String getSequenceType();

	/**
	 * 現在時刻を取得。
	 * @return
	 */
	public abstract String getCurrentTime();

}
