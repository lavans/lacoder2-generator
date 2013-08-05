/* $Id: Attribute.java 526 2012-09-25 13:05:52Z dobashi $
 * create: 2004/12/28
 * (c)2004 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.model;

import com.lavans.lacoder2.generator.main.JavaType;
import com.lavans.lacoder2.lang.StringUtils;

/**
 * toGetParameter/toSetParameterにjspでやるべき事が混じってるような感じ。
 * やはりmap.put()あたりはjspに持って行くべきか？
 * @author dobashi
 * @version 1.00
 */
public class Attribute{
//	private static Log logger = LogFactory.getLog(Attribute.class);
	/** デフォルトの日付フォーマット TODO 設定ファイルから読めるように*/
	private static final String DEFALUT_DATETIME_FORMAT="yyyy/MM/dd HH:mm:ss";

	/** 親となるエンティティ */
	private Entity entity = null;
	/** 名前。CamelCase */
	private String name = null;
	/** 定数名。大文字スネークケース。DBのカラム名にも使用。 */
	private String dbName = null;

	/** タイトル */
	private String title = null;
	/** DB上の型 */
	private String dbType = null;
	/** javaの型 */
	private String javaType = null;
	/** 対応するクラス名 */
	private String className = "";
//	private String classAttrName = null;
	// list形式の場合
	private boolean isList = false;
	// 固定長リストの場合
	private int listSize=0;

	/** 日付入力用のformat。 */
	private String dateFormat = DEFALUT_DATETIME_FORMAT;

	/** sequence flag */
	private boolean isSequence = false;

	/** enum flag */
	private boolean isEnum = false;

	/** primary key flag */
	private boolean isPrimaryKey = false;

	// constraint 制約
	/** nullable flag. default = false. */
	private boolean isNullable = false;

	/** precision for BigDecimal */
	private Integer precision = null;
	/** scale for BigDecimal */
	private Integer scale = null;

	// 最小値最大値。
	// longで指定できる範囲ならvalidate()で判定処理を自動生成する。
	// 数値なら大きさ、文字列なら長さを指定する。
	/** min value */
	private Long min = null;
	/** max value */
	private Long max = null;

	/**
	 * 初期化値を返す。主にStringの初期値を""にして新規登録時にjspにnullが表示されないようにするため。
	 * @return
	 */
	public String getInitValue(){
//		logger.debug(arg0)
		String result = null;
		if(getJavaType().equals("int") ||
			getJavaType().equals("long") ||
			getJavaType().equals("double") ||
			getJavaType().equals("float") ){
			result = "0";
		}else if(getJavaType().equals("boolean")){
			result = "false";
		}else if(getName().equals("statusEnumId") ){
			// ステータスEnumのデフォルト値
			result = "\"on\"";
		}else if(getJavaType().equals("String")){
			result = "\"\"";
		}else if(!isEditable()){
			result = "null";
		}else if(getJavaType().equals("Date")){
//			result = "new Date()";
			result = "null";
		}else{
			result = "null";
		}
		return result;
	}

	/**
	 * jdbcでのset/getメソッドでの型名を返す。
	 * @return
	 */
	public String getJdbcMethodName(){
		// Oracleでbyte[]の時はBytesじゃなくてBlobにしないといけないかも
		return JavaType.getJdbcMethodName(javaType);
	}

	/**
	 * INSERT文でのvalue名を返す。
	 * @return
	 */
	public String getInsertSql(){
		return  ", :"+name;
	}

	/**
	 * UPDATE文でのフィールド名を返す。
	 * @return
	 */
	public String getUpdateSql(){
		String result = null;

		if(isPrimaryKey){
			// PKはWHERE句に含まれている
			result="";
		}else if(name.equals("insert_datetime")){
			// insert_dateは更新できない
			result = "";
		}else{
			result = ", "+ getDbName()+"=:"+name;
		}
		return result;
	}

	/**
	 * テーブルでのカラム名を返す。
	 * @return
	 */
//	public String getColumnName(){
//		return StringUtils.toUnderscore(name).toUpperCase();
//	}

	/**
	 * パラメータ化する時の値を返す。Stringならそのまま。
	 * それ以外のクラスならgetId()を返す。プリミティブ型なら文字列に変換。
	 * @return
	 */
	public String toGetParameter(){
		// insert_date/userはパラメータ化の必要なし。
		if(!isEditable()){
			return "";
		}

		String result  = null;
//		if(!className.equals("")){
//			if(entity.getTypeManager().getJavaType(type).equals("int")){
//				result = "new Integer("+ getVarName() + ".getId())";
//			}else{
//				result = getVarName() + ".getId()";
//			}
//		}else
		if(isList){
			result = name;
			//return ""; // とりあえずリストはOutputパラメータだけなので無視
		}else if(getJavaType().equals("String")){
			result = name;
		}else if(getJavaType().equals("Date")){
			// date型は値が入っていない場合NullPo抑制が必要
			result = name + "==null?\"\":"+ "String.valueOf("+ name +".getTime())";
		}else if(getJavaType().equals("byte[]")){
			// Base64で埋められるけど、バイナリデータをformに入れて持ち回りたいことはないのでなにもしない。
			// でも後で入れるかもしれないからコメントで残しておく
			//result = "StringUtils.encodeBase64Url( + name + ")";
		}else{
			result = "String.valueOf("+ getVarName() +")";
		}
		result = "map.put(prefix+"+ getConstName() +", new String[]{"+ result +"});";

		return result;
	}

	/**
	 * mapからインスタンス変数へ代入。
	 * @return
	 */
	public String toSetParameter(){
		// insert_date/userはパラメータ化の必要なし。
		if(!isEditable()){
			return "";
		}

		StringBuffer buf = new StringBuffer();
		buf.append("if(map.get(prefix+"+ getConstName() +")!=null) "+ getVarName() +" = ");

		if(isList){
//			return "";	// とりあえずリストはOutputパラメータだけなので無視
			buf.append(getClassLastName() +"StringUtils.join(map.get(prefix+"+ getConstName() +"),\",\");");
		}else if(getJavaType().equals("int") || getJavaType().equals("Integer")){
			buf.append("Integer.parseInt(map.get(prefix+"+ getConstName() +")[0]);");
		}else if(getJavaType().equals("long") || getJavaType().equals("Long")){
			buf.append("Long.parseLong(map.get(prefix+"+ getConstName() +")[0]);");
		}else if(getJavaType().equals("double") || getJavaType().equals("Daouble")){
			buf.append("Double.parseDouble(map.get(prefix+"+ getConstName() +")[0]);");
		}else if(getJavaType().equals("boolean") || getJavaType().equals("Boolean")){
			buf.append("Boolean.parseBoolean(map.get(prefix+"+ getConstName() +")[0]);");
		}else if(getJavaType().equals("Date")){
			buf.append("new Date(Long.parseLong(map.get(prefix+"+ getConstName() +")[0]));");
		}else if(getJavaType().equals("BigDecimal")){
			buf.append("new BigDecimal(map.get(prefix+"+ getConstName() +")[0]);");
		}else if(getJavaType().equals("byte[]")){
			//buf.append("StringUtils.decodeBase64Url(map.get(prefix+"+ getConstName() +"));");
		}else{	// それ以外(String)
			buf.append("map.get(prefix+"+ getConstName() +")[0];");
		}

		return "try{ "+ buf.toString() +"}catch(Exception e){}";
	}

	/**
	 * パラメータ化する時の値を返す。Stringならそのまま。
	 * それ以外のクラスならgetId()を返す。プリミティブ型なら文字列に変換。
	 * @return
	 */
	public String toGetAttributeInfo(){
		StringBuilder buf = new StringBuilder();
		buf.append("map.put("+ getConstName() +", ");

		if(getJavaType().equals("int")){
			buf.append("Integer.class);");
		}else if(getJavaType().equals("long")){
			buf.append("Long.class);");
		}else if(getJavaType().equals("double")){
			buf.append("Double.class);");
		}else if(getJavaType().equals("boolean")){
			buf.append("Boolean.class);");
		}else if(getJavaType().equals("Date")){
			buf.append("Date.class);");
//			buf.append("java.sql.Date.class);");
//		}else if(getJavaType().equals("BigDecimal")){
//			buf.append("BigDecimal.class;");
//		}else if(getJavaType().equals("byte[]")){
//			buf.append("byte[].class;");
		}else{	// それ以外(String, BigDecimal, byt[])
			buf.append(getJavaType()+".class);");
		}

		return buf.toString();
	}

	/**
	 * パラメータ化する時の値を返す。Stringならそのまま。
	 * それ以外のクラスならgetId()を返す。プリミティブ型なら文字列に変換。
	 * @return
	 */
	public String toGetAttributeMap(){
		String result  = null;
//		if(isList){
//			// リストは後で考える。Enumの配列とか？カンマ区切りStringの処理？
//			return "";
//		}
		result = "map.put("+ getConstName() +","+ getVarName() +");";

		return result;
	}

	public String toClassGetter(){
		if(!hasClass()){ return ""; }

		StringBuffer buf = new StringBuffer();
		buf.append("	/**\n");
		buf.append("	 * @return "+ getClassVarName() +"を戻します。\n");
		buf.append("	 */\n");
		buf.append("	public "+ getClassLastName() +" get"+ StringUtils.capitalize(getClassVarName()) +"(){\n");
		if(isEnum){
			if(getJavaType().equals("boolean")){
				buf.append("		return "+ getClassLastName() +".valueOf"+ StringUtils.capitalize(getVarName()) +"(Boolean.toString("+ getVarName() +"));\n");
			}else if(getJavaType().equals("int")){
				buf.append("		return "+ getClassLastName() +".valueOfInt("+ getVarName() +");\n");
			}else{
				buf.append("		return "+ getClassLastName() +".valueOf("+ getVarName() +");\n");
			}
		}else{
			buf.append("		return "+ getClassVarName() +";\n");
		}
		buf.append("	}\n");
		buf.append("	\n");
		// enumの時はtitleも返す
		if(isEnum){
			buf.append("	/**\n");
			buf.append("	 * @return "+ getClassVarName() +"Titleを戻します。\n");
			buf.append("	 */\n");
			buf.append("	public String get"+ StringUtils.capitalize(getClassVarName()) +"Title(){\n");
			buf.append("		if(get"+ StringUtils.capitalize(getClassVarName()) +"()==null) return \"\";\n");
			buf.append("		return get"+ StringUtils.capitalize(getClassVarName()) +"().getTitle();\n");
			buf.append("	}\n");
			buf.append("	\n");
		}
		return buf.toString();
	}

	public String toClassSetter(){
		if(!hasClass()){ return ""; }

		StringBuffer buf = new StringBuffer();
		buf.append("	/**\n");
		buf.append("	 * "+ getClassLastName() +"を設定します。\n");
		buf.append("	 */\n");
		buf.append("	public void set"+ StringUtils.capitalize(getClassVarName()) +"("+ getClassLastName() +" "+ getClassVarName() +"){\n");
		if(isEnum){
			if(getJavaType().equals("boolean")){
				buf.append("		this."+ getVarName() +"=Boolean.parseBoolean("+ getClassVarName() +".name());\n");
			}else if(getJavaType().equals("int")){
				buf.append("		this."+ getVarName() +"="+ getClassVarName() +".getInt();\n");
			}else{
				buf.append("		this."+ getVarName() +"="+ getClassVarName() +".name();\n");
			}
		}else{
			buf.append("		this."+ getClassVarName() +"="+ getClassVarName() +";\n");
		}
		buf.append("	}\n");
		buf.append("	\n");

		return buf.toString();
	}

	/**
	 *
	 * @return
	 */
//	public String toGetter(){
//		StringBuffer buf = new StringBuffer();
//		buf.append("	/**\n");
//		buf.append("	 * @return "+ getTitle() +"を戻します。\n");
//		buf.append("	 */\n");
//		buf.append("	public "+ getJavaType() +" "+ getGetterName() +"(){\n");
//		buf.append("		return "+ getVarName() +";\n");
//		buf.append("	}\n");
//
//		if(getJavaType().equals("Date")){
//			buf.append("	/**\n");
//			buf.append("	 * @return "+ getTitle() +"の日付フォーマットを戻します。\n");
//			buf.append("	 */\n");
//			buf.append("	public DateFormat "+ getGetterName() +"DateFormat(){\n");
//			buf.append("		return new SimpleDateFormat(\""+ getDateFormat() +"\");\n");
//			buf.append("	}\n");
//			buf.append("	/**\n");
//			buf.append("	 * @return "+ getTitle() +"の日付文字列を戻します。\n");
//			buf.append("	 */\n");
//			buf.append("	public String "+ getGetterName() +"Str(){\n");
//			buf.append("		if("+ getVarName() +"==null) return \"\";\n");
//			buf.append("		return "+ getGetterName() + "DateFormat().format("+ getVarName() +");\n");
//			buf.append("	}\n");
//		}
//
//		return buf.toString();
//	}
//
//	/**
//	 * setterの生成
//	 * @return
//	 */
//	public String toSetter(){
//		StringBuffer buf = new StringBuffer();
//		buf.append("	/**\n");
//		buf.append("	 *  "+ getTitle() +"を設定します。\n");
//		buf.append("	 */\n");
//		buf.append("	public void "+ getSetterName() +"("+ getJavaType() +" "+ getVarName() +"){\n");
//		buf.append("		this."+ getVarName() +"="+ getVarName() +";\n");
//		buf.append("	}\n");
//
//		if(getJavaType().equals("Date")){
//			buf.append("	/**\n");
//			buf.append("	 * "+ getTitle() +"を日付文字列で設定します。\n");
//			buf.append("	 */\n");
//			buf.append("	public void "+ getSetterName() +"Str(String "+ getVarName() +"Str) throws ParseException{\n");
//			buf.append("		this."+ getVarName() +"="+ getGetterName() + "DateFormat().parse("+ getVarName() +"Str);\n");
//			buf.append("	}\n");
//		}
//
//		return buf.toString();
//	}

	/**
	 *  insert_date/user等は入力不可。
	 * @return
	 */
	public boolean isEditable(){
		// insert_date/userはパラメータ化の必要なし。
		if(name.equals("insert_datetime") || name.equals("update_datetime") ){
			return false;
		}
		return true;
	}
	/**
	 * @return entity を戻します。
	 */
	public Entity getEntity() {
		return entity;
	}
	/**
	 * @param entity entity を設定。
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

//	/**
//	 * @return classAttrName を戻します。
//	 */
//	public String getClassAttrName() {
//		return classAttrName;
//	}
//	/**
//	 * @param classAttrName classAttrName を設定。
//	 */
//	public void setClassAttrName(String attrName) {
//		this.classAttrName = attrName;
//	}
	/**
	 * @return className を戻します。
	 */
	public String getClassName() {
		// クラス名が.で始まる場合はドメインパスまでを省略している
		if((className!=null) && className.startsWith(".")){
			className = entity.getParentPackage().getDomainPath()+className;
		}
		return className;
	}
	/**
	 * @return className を戻します。
	 */
	public String getClassLastName() {
		String names[] = className.split("\\.");
		return names[names.length-1];
	}
	/**
	 * @param className className を設定。
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return name を戻します。
	 */
	public String getName() {
		return name;
	}
	/**
	 * DB上のカラム名を設定します。
	 * @param dbName
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	/**
	 * DB上のカラム名を取得します。
	 * @return dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * Const定義名を取得します。スネークケースで全大文字です。。
	 * @return dbName
	 */
	public String getConstName() {
		return StringUtils.toSnakeCase(name).toUpperCase();
	}
	/**
	 * Entity内での変数名を返す。
	 * classを持っている場合、末尾がIdで終わらなければIdをつける。
	 * @return
	 */
	public String getVarName() {
		return name;
	}
	/**
	 * 最初を大文字にした名前を返す。
	 * @return
	 */
	public String getCapitalizeName() {
		return StringUtils.capitalize(getVarName());
	}

	/**
	 * Entity内でのクラス型の変数名を返す。
	 * 変数名が必ずIdで終わっているので、最後の2文字を削除した物をクラス型の変数名とする。
	 * "Id"で終わっていない場合は最後に"Class"または"Enum"をつける
	 * @return
	 */
	public String getClassVarName() {
		String classVarName = getVarName();
		if(!getClassName().equals("") && !name.endsWith("Id")){
			if(isEnum){
				classVarName +="Enum";
			}else{
				classVarName +="Class";
			}
		}else{
			classVarName = classVarName.substring(0, classVarName.length()-2);
		}
		return classVarName;
	}

	/**
	 * getterのメソッド名。customerId -> getCustomerId
	 * booleanでis/can/hasの場合はgetIsBoolとならずにisBoolだけとしたいところだけど
	 * struts2のActionの自動設定で失敗するのでgetIsBoolとする。
	 * @return
	 */
	public String getGetterName(){
//		if(getJavaType().equals("boolean")){
//			if(name.startsWith("is") || name.startsWith("can") || name.startsWith("has")){
//				return name;
//			}
//		}

		return "get" + StringUtils.capitalize(getVarName());
	}

	/**
	 * setterのメソッド名。customerId -> setCustomerId
	 * booleanでis/can/hasの場合はgetIsBoolとならずにisBoolだけとする
	 * @return
	 */
	public String getSetterName(){
		return "set" + StringUtils.capitalize(getVarName());

	}
	/**
	 * 変数名を頭大文字にして返す。getXX(),setXX()等で使用。
	 * @return
	 */
//	public String getVarNameUpperFirst(){
//		return StringUtils.upperFirst(name);
//	}

	/**
	 * @param name name を設定。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return isSequence を戻します。
	 */
	public boolean isSequence() {
		return isSequence;
	}
	/**
	 * @param isSequence isSequence を設定。
	 */
	public void setSequence(boolean isSequence) {
		this.isSequence = isSequence;
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
	 * @return dateFormat を戻します。
	 */
	public String getDateFormat() {
		return dateFormat;
	}
	/**
	 * @param dateFormat dateFormat を設定。
	 */
	public void setDateFormat(String format) {
		this.dateFormat = format;
	}
	/**
	 * @return isEnum を戻します。
	 */
	public boolean isEnum() {
		return isEnum;
	}
	/**
	 * @param isEnum isEnum を設定。
	 */
	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public boolean hasClass() {
		return !getClassName().equals("");
	}

	/**
	 * EntityクラスにおけるJavaの型。
	 * @return
	 */
	public String getJavaType(){
		return javaType;
	}

	/**
	 * @param javaType javaType を設定。
	 */
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	/**
	 * @return isList を戻します。
	 */
	public boolean isList() {
		return isList;
	}
	/**
	 * @param isList isList を設定。
	 */
	public void setList(boolean isList) {
		this.isList = isList;
	}
	/**
	 * @return listSize を戻します。
	 */
	public int getListSize() {
		return listSize;
	}
	/**
	 * @param listSize listSize を設定。
	 */
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}
	/**
	 * @return isPrimaryKey を戻します。
	 */
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	/**
	 * @param isPrimaryKey isPrimaryKey を設定。
	 */
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getDbType() {
		// xmlで指定済みならそれを返す
		if(dbType!=null){
			return dbType;
		}
		// 未指定ならTypeManagerから取得。
		return entity.getTypeManager().getDbType(this);
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public Long getMin() {
		return min;
	}

	public void setMin(Long min) {
		this.min = min;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}
}
