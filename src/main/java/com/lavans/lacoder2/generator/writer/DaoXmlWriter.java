package com.lavans.lacoder2.generator.writer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lavans.lacoder2.generator.db.OracleDialect;
import com.lavans.lacoder2.generator.db.TypeManager;
import com.lavans.lacoder2.generator.model.Attribute;
import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.lang.StringUtils;

public class DaoXmlWriter {
	private static Log logger = LogFactory.getLog(DaoXmlWriter.class);
	private Entity entity;
	private String pkWhere;
	String tableName;

	private String pkSelectColumns;
	private String allSelectColumns;
	private String allInsertColumns;
	private String allInsertValues;
	private String allUpdateColumns;

	public DaoXmlWriter(Entity entity){
		this.entity = entity;
		parse();
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
		parse();
	}

	private void parse(){
		tableName = makeTableName(entity);
		// primaryKeyが2つ以上の場合はとりあえず両方ともStringとして扱う？。
		List<Attribute> primaryKeyList = entity.getPrimaryKeyList();
		logger.info("--pk数:"+primaryKeyList.size());

		// WHERE句における更新条件作成
		StringBuilder whereBuf = new StringBuilder();
		StringBuilder pkBuf = new StringBuilder();
		// 全カラム名
		StringBuilder allSelectBuf = new StringBuilder();
		StringBuilder allInsertCBuf = new StringBuilder();
		StringBuilder allInsertVBuf = new StringBuilder();
		StringBuilder allUpdateBuf = new StringBuilder();
		for(Attribute attr: entity.getAttrList()){
			if(attr.isPrimaryKey()){
				whereBuf.append(" AND "+ attr.getConstName() +"=:"+ attr.getName());
				pkBuf.append(", ")/*.append(tableName).append(".")*/.append(attr.getConstName());
			}
			allSelectBuf.append(", ")/*.append(tableName).append(".")*/.append(attr.getConstName());
			allInsertCBuf.append(", "+attr.getConstName());
			allInsertVBuf.append(attr.getInsertSql());
			allUpdateBuf.append(attr.getUpdateSql());
		}
		pkWhere = whereBuf.substring(5);
		pkSelectColumns  = pkBuf.substring(2);
		allSelectColumns = allSelectBuf.substring(2);
		allInsertColumns = allInsertCBuf.substring(2);
		allInsertValues  = allInsertVBuf.substring(2);
		allUpdateColumns = allUpdateBuf.substring(2);
	}
	
	/**
	 * テーブル名作成。スキーマが指定されている場合は"スキーマ.テーブル"となる
	 * @param entity
	 * @return
	 */
	private String makeTableName(Entity entity){
		String result = StringUtils.toUnderscore(entity.getName());
		if(!StringUtils.isEmpty(entity.getSchema())){
			result = entity.getSchema()+"."+result;
		}
		return result.toUpperCase();
	}

	public String writeSelectSql(){
		return "SELECT "+ allSelectColumns +" FROM "+ tableName +" WHERE "+ pkWhere;
	}

	public String writeNextValSql(){
		return entity.getTypeManager().getNextval(entity.get(0));
	}

	public String writeInsertSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("INSERT INTO "+ tableName +" ( "+ allInsertColumns +" ) VALUES ( ");
		buf.append(""+ allInsertValues +")");
		return buf.toString();
	}
	public String writeUpdateSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("UPDATE "+ tableName +" SET ");
		buf.append(""+ allUpdateColumns +" WHERE "+ pkWhere);
		return buf.toString();
	}

	// 削除
	public String writeDeleteSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("DELETE FROM "+ tableName +" WHERE "+ pkWhere);
		return buf.toString();
	}
	public String writeDeleteAnySql(){
		StringBuilder buf = new StringBuilder();
		buf.append("DELETE FROM "+ tableName +" $condition");
		return buf.toString();
	}

	// 一覧
	public String writeListSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT "+ allSelectColumns +" FROM "+ tableName);	// $condition\n$order"
		return buf.toString();
	}

	public String writeListPkSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT "+ pkSelectColumns +" FROM "+ tableName);	// $condition\n$order"
		return buf.toString();
	}
	public String writeListCountSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT COUNT(1) FROM "+ tableName +" $condition");
		return buf.toString();
	}

	public String writeListPagerSql(){
		TypeManager typeManager = entity.getTypeManager();
		StringBuilder buf = new StringBuilder();
		if(typeManager instanceof OracleDialect){
			buf.append("SELECT * FROM (SELECT A.*, ROWNUM AS NUM FROM (\n ");
		}
		buf.append(writeListSql()).append(" $condition $order");
		if(typeManager instanceof OracleDialect){
			buf.append("\n) A ) WHERE NUM>:_start AND NUM<=:_end");
		}else{
			// Postgres/H2DB用 MySQLもOK?
			buf.append(" LIMIT :_limit OFFSET :_offset");
		}
		return buf.toString();
	}
	public String writeListPagerPkSql(){
		TypeManager typeManager = entity.getTypeManager();
		StringBuilder buf = new StringBuilder();
		if(typeManager instanceof OracleDialect){
			buf.append("SELECT * FROM (SELECT A.*, ROWNUM AS NUM FROM (\n ");
		}
		buf.append(writeListPkSql()).append(" $condition $order");
		if(typeManager instanceof OracleDialect){
			buf.append("\n) A ) WHERE NUM>:_start AND NUM<=:_end");
		}else{
			// Postgres用 MySQLもOK?
			buf.append(" LIMIT :_limit OFFSET :_offset");
		}
		return buf.toString();
	}

	// for _BAK table
	public String writeNextValBakSql(){
		String str = entity.getTypeManager().getNextvalBak(entity.get(0));
		return str;
	}

	public String writeBackupSql(){
		TypeManager typeManager = entity.getTypeManager();
		StringBuilder buf = new StringBuilder();
		buf.append("INSERT INTO "+ tableName +"_BAK \n");
		buf.append("  SELECT "+ allSelectColumns +", "+ typeManager.getCurrentTime() +"\n  FROM "+ tableName +"\n  WHERE "+ pkWhere);	// $condition\n$order"
		return buf.toString();
	}

	public String writeRestoreSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("INSERT INTO "+ tableName +" \n");
		buf.append("  SELECT "+ allSelectColumns +"\n  FROM "+ tableName +"_BAK \n  WHERE "+ pkWhere);
		return buf.toString();
	}

	public String writeSelectBakSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT "+ allSelectColumns +", BACKUP_DATETIME FROM "+ tableName +"_BAK"+" WHERE "+ pkWhere);
		return buf.toString();
	}

	// 削除
	public String writeDeleteBakSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("DELETE FROM "+ tableName +"_BAK WHERE "+ pkWhere);
		return buf.toString();
	}

	public String writeDeleteAnyBakSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("DELETE FROM "+ tableName +"_BAK $condition");
		return buf.toString();
	}

	public String writeListBakSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT "+ allSelectColumns +", BACKUP_DATETIME FROM "+ tableName +"_BAK");	// $condition\n$order"
		return buf.toString();
	}

	public String writeCountBakSql(){
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT COUNT(1) FROM "+ tableName +"_BAK $condition");
		return buf.toString();
	}

	public String writePagerBakSql(){
		TypeManager typeManager = entity.getTypeManager();
		StringBuilder buf = new StringBuilder();
		if(typeManager instanceof OracleDialect){
			buf.append("SELECT * FROM (SELECT A.*, ROWNUM AS NUM FROM (\n ");
		}
		buf.append("SELECT "+ allSelectColumns +", BACKUP_DATETIME \nFROM "+ tableName +"_BAK" );
		buf.append("\n$condition $order\n");
		if(typeManager instanceof OracleDialect){
			buf.append(") A ) WHERE NUM>:_start AND NUM<=:_end");
		}else{
			// Postgres/H2DB用 MySQLもOK?
			buf.append("LIMIT :_limit OFFSET :_offset");
		}
		return buf.toString();
	}

}
