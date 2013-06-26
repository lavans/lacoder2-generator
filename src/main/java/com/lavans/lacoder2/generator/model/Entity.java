/* $Id: Entity.java 508 2012-09-20 14:41:55Z dobashi $
 * create: 2004/12/28
 * (c)2004 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lavans.lacoder2.generator.db.TypeManager;
import com.lavans.lacoder2.lang.StringUtils;

/**
 * @author dobashi
 * @version 1.00
 */
public class Entity {
	private static Log logger = LogFactory.getLog(Entity.class);

	private TypeManager typeManager = null;	// 暫定。

	private String name = null;
	private String title = null;

	private String shortname = null;
	private List<Attribute> attrList = new ArrayList<Attribute>();
	private String primaryKey = null;
	private boolean isCached = false;
	private boolean isUser = false;
	// リスト属性を持っているか
	private boolean hasList = false;
	/** Blob属性を持っているか */
	private boolean hasBlob = false;
	/** バックアップ用テーブルを持っているか */
	private boolean hasBackup = false;
	/** スキーマ指定名 */
	private String Schema;

	/** 親パッケージ */
	private Package parentPackage = null;

	// behavior
	/** 確認画面をskipするかどうか */
	private boolean isSkipConfirm = false;
	private boolean isSkipDeleteConfirm = false;

	/** 登録処理の後、登録結果画面をSkipして検索画面に戻るか */
	private boolean isSkipResult = false;
	private boolean isSkipDeleteResult = false;

	// 編集するユーザーの一覧
	private List<String> userList = new ArrayList<String>();

	/**
	 * BigDecimalを持っているかどうか
	 * @return
	 */
	public boolean hasBigDecimal(){
		for(Attribute attr: attrList){
			if(attr.getJavaType().equals("BigDecimal")){
				return true;
			}
		}
		return false;
	}


	/**
	 *
	 * @return
	 */
	public List<Attribute> getPrimaryKeyList(){
		List<Attribute> list = new ArrayList<Attribute>();
		for(Attribute attr: attrList){
			if(attr.isPrimaryKey()){
				list.add(attr);
			}
		}
		return list;
	}

	/**
	 * プライマリキー一覧をメソッドの引数形式で返す。
	 * pkeyがcompany_id::string, detail_id::integerなら"String companyId, int detailId"
	 * @return
	 */
	public String getPrimaryKeyArg(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: getPrimaryKeyList()){
			buf.append(", "+ attr.getJavaType()+" "+ attr.getVarName());
		}
		if(buf.length()>2){
			return buf.substring(2);
		}
		// primaryKeyの設定が間違っているとエラーとなる。
		logger.info("エラー: PKが存在しない。"+getName()+ " PK:"+ primaryKey);
		return "";
	}

	/**
	 * プライマリキー一覧を変数名形式で返す。
	 * pkeyがcompany_id::string, detail_id::integerなら"companyId, detailId"
	 * @return
	 */
	public String getPrimaryKeyVar(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: getPrimaryKeyList()){
			buf.append(", "+ attr.getVarName());
		}
		return buf.substring(2);
	}

	/**
	 * @return isCached を戻します。
	 */
	public boolean isCached() {
		return isCached;
	}
	/**
	 * @param isCached isCached を設定。
	 */
	public void setCached(boolean isCached) {
		this.isCached = isCached;
	}
	/**
	 * @return name を戻します。
	 */
	public String getName() {
		return name;
	}
	public String getTableName() {
		return StringUtils.toUnderscore(name);
	}
	public String getClassName() {
		return StringUtils.capitalize(name);
	}
	public String getClassNameFull() {
		return getParentPackage().getModelSubPackagePath() + ".entity."+ StringUtils.capitalize(name);
	}
	/**
	 * @param name name を設定。
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return primaryKey を戻します。
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}
	/**
	 * @param primaryKey primaryKey を設定。
	 */
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	/**
	 *
	 * @return
	 */
	public List<Attribute> getAttrList(){
		return attrList;
	}
	/**
	 * @param arg0
	 * @return
	 */
	public boolean add(Attribute attr) {

		return attrList.add(attr);
	}
	/**
	 * @param arg0
	 * @return
	 */
	public Attribute get(int arg0) {
		return attrList.get(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 */
	public boolean remove(Object arg0) {
		return attrList.remove(arg0);
	}
	/**
	 * @return
	 */
	public int size() {
		return attrList.size();
	}
	/**
	 * @return typeManager を戻します。
	 */
	public TypeManager getTypeManager() {
		return typeManager;
	}
	/**
	 * @param typeManager typeManager を設定。
	 */
	public void setTypeManager(TypeManager typeManager) {
		this.typeManager = typeManager;
	}
	/**
	 * @return shortname を戻します。
	 */
	public String getShortname() {
		return shortname;
	}
	/**
	 * @param shortname shortname を設定。
	 */
	public void setShortname(String shortname) {
		// 一文字目は小文字にする
		this.shortname = StringUtils.uncapitalize(shortname);
	}
	/**
	 * @return title を戻します。
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title title を設定。
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return isUser を戻します。
	 */
	public boolean isUser() {
		return isUser;
	}
	/**
	 * @param isUser isUser を設定。
	 */
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}
	/**
	 * @return hasList を戻します。
	 */
	public boolean hasList() {
		return hasList;
	}
	/**
	 * @param hasList hasList を設定。
	 */
	public void setHasList(boolean hasList) {
		this.hasList = hasList;
	}

	/**
	 * 日付型を持っているか
	 * @return
	 */
	public boolean hasDate(){
		boolean hasDate = false;
		for(int i=1; i<size(); i++){
			Attribute attr = get(i);
			if(attr.getJavaType().equals("Date")){
				hasDate = true;
				break;
			}
		}
		return hasDate;
	}

	/**
	 * 日付型を持っているか
	 * @return
	 */
	public boolean hasInsertDatetime(){
		return hasAttrName("insertDatetime");
	}

	public boolean hasUpdateDatetime(){
		return hasAttrName("updateDatetime");
	}

	private boolean hasAttrName(String name){
		boolean hasDate = false;
		for(int i=1; i<size(); i++){
			Attribute attr = get(i);
			if(attr.getName().equals(name)){
				hasDate = true;
				break;
			}
		}
		return hasDate;
	}

	/**
	 * @return hasBlob
	 */
	public boolean isHasBlob() {
		return hasBlob;
	}

	/**
	 * @param hasBlob 設定する hasBlob
	 */
	public void setHasBlob(boolean hasBlob) {
		this.hasBlob = hasBlob;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public Package getParentPackage() {
		return parentPackage;
	}

	public void setParentPackage(Package parentPackage) {
		this.parentPackage = parentPackage;
	}

	public boolean isSkipConfirm() {
		return isSkipConfirm;
	}

	public void setSkipConfirm(boolean isSkipConfirm) {
		this.isSkipConfirm = isSkipConfirm;
	}

	public boolean isSkipDeleteConfirm() {
		return isSkipDeleteConfirm;
	}

	public void setSkipDeleteConfirm(boolean isSkipDeleteConfirm) {
		this.isSkipDeleteConfirm = isSkipDeleteConfirm;
	}

	public boolean isSkipResult() {
		return isSkipResult;
	}

	public void setSkipResult(boolean isSkipResult) {
		this.isSkipResult = isSkipResult;
	}
	public boolean isSkipDeleteResult() {
		return isSkipDeleteResult;
	}

	public void setSkipDeleteResult(boolean isSkipDeleteResult) {
		this.isSkipDeleteResult = isSkipDeleteResult;
	}

	public boolean hasBackup() {
		return hasBackup;
	}

	public void setHasBackup(boolean hasBackup) {
		this.hasBackup = hasBackup;
	}

	public String getSchema() {
		return Schema;
	}

	public void setSchema(String schema) {
		Schema = schema;
	}

}
