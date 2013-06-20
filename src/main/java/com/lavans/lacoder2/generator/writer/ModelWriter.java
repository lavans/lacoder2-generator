package com.lavans.lacoder2.generator.writer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lavans.lacoder2.generator.model.Attribute;
import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.lang.StringUtils;

public class ModelWriter {
	private static Log logger = LogFactory.getLog(ModelWriter.class);

	private Entity entity;

	private Attribute backupAttr = new Attribute(){{
		setName("backupDatetime");
		setJavaType("Date");
		setTitle("バックアップ日時");
	}};


	public ModelWriter(Entity entity){
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * このEntityに必要なimportを列挙する。
	 * @return
	 */
	public String writeImports(){
		StringBuffer buf = new StringBuffer();
		if(entity.hasBigDecimal()){
			buf.append("import java.math.BigDecimal;\n");
		}
		if(entity.hasDate()){
			buf.append("import java.util.Date;\n");
			buf.append("import java.text.DateFormat;\n");
			buf.append("import java.text.ParseException;\n");
			buf.append("import java.text.SimpleDateFormat;\n");
		}
		if(entity.hasList()){
			buf.append("import com.lavans.lacoder2.lang.StringUtils;\n");
		}

		Set<String> classSet = new HashSet<String>();
		for(Attribute attr: entity.getAttrList()){
			if((!attr.getClassName().equals(""))){
				classSet.add(attr.getClassName());
			}
		}

		for(String className: classSet){
			buf.append("import "+ className +";\n");
		}
		return buf.toString();
	}

	/**
	 * 定数定義書き出し。
	 * @return
	 */
	public String writeConsts(){
		StringBuffer buf = new StringBuffer();
		buf.append("	public static final String _NAME = \""+ entity.getName() +"\";\n");

		for(Attribute attr: entity.getAttrList()){
			buf.append("	public static final String "+ attr.getConstName() +" = \""+ attr.getName() +"\";\n");
		}
		return buf.toString();
	}

	/**
	 * インスタンス変数書きだし。
	 * @return
	 */
	public String writeInstanceVarsPK(){
		return StringUtils.indent(writeInstanceVars(entity.getPrimaryKeyList()),1);
	}
	public String writeInstanceVars(){
		return writeInstanceVars(entity.getAttrList());
	}
	private String writeInstanceVars(List<Attribute> list){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: list){
			buf.append("	/** "+ attr.getTitle() +" */\n");
			buf.append("	private "+ attr.getJavaType() +" "+ attr.getVarName() +" = "+ attr.getInitValue() +";\n");
			if(!attr.getClassName().equals("") && !attr.isEnum()){
				// クラスを持つ場合
				buf.append("	private "+ attr.getClassLastName() +" "+ attr.getClassVarName() +" = null;\n");
			}
		}
		// enumはsetter/getterで直接参照するのでクラス型は必要ない

		// バックアップありなら
		if(entity.hasBackup()){
			buf.append("	/** "+ backupAttr.getTitle() +" */\n");
			buf.append("	private Date backupDatetime = null;\n");
		}

		return buf.toString();
	}

	/**
	 * getParameters()の各属性の書き出し
	 * @return
	 */
	public String writeGetParameters(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getAttrList()){
//			if(org.apache.commons.lang.StringUtils)
			buf.append("\t\t"+attr.toGetParameter()).append("\n");
		}
		return buf.toString();
	}

	/**
	 * setParameters()の各属性の書き出し
	 * @return
	 */
	public String writeSetParameters(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getAttrList()){
//			if(org.apache.commons.lang.StringUtils)
			buf.append("\t\t"+attr.toSetParameter()).append("\n");
		}
		return buf.toString();
	}

	/**
	 * getAttributeMap()の各属性の書き出し
	 * @return
	 */
	public String writeGetAttributeInfo(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getAttrList()){
			buf.append("\t\t"+attr.toGetAttributeInfo()).append("\n");
		}
		return buf.toString();
	}

	/**
	 * getAttributeMap()の各属性の書き出し
	 * @return
	 */
	public String writeGetAttributeMap(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getAttrList()){
			buf.append("\t\t"+attr.toGetAttributeMap()).append("\n");
		}
		return buf.toString();
	}

	/**
	 * アクセッサの書き出し
	 * @return
	 */
	/**
	 * インスタンス変数書きだし。
	 * @return
	 */
//	public String writeAccesssorPK(){
//		return StringUtils.indent(writeAccesssor(entity.getPrimaryKeyList()),1);
//	}
//	public String writeAccesssor(){
//		return writeAccesssor(entity.getAttrList());
//	}
//	private String writeAccesssor(List<Attribute> list){
//		StringBuffer buf = new StringBuffer();
//		for(Attribute attr: list){
////			if(org.apache.commons.lang.StringUtils)
//			buf.append(attr.toGetter()).append("\n");
//			buf.append(attr.toSetter()).append("\n");
//		}
//
//		// backup
//		if(entity.hasBackup()){
//			buf.append(backupAttr.toGetter()).append("\n");
//			buf.append(backupAttr.toSetter()).append("\n");
//		}
//		return buf.toString();
//	}

	/**
	 * PKコンストラクタ呼び出し
	 * @return
	 */
	public String writePKConstructorCall(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			buf.append(","+ attr.getVarName() );
		}
		return buf.substring(1);
	}
//	/**
//	 * PKコンストラクタ引数宣言
//	 * @return
//	 */
//	public String writePKConstructorArgs(){
//		StringBuffer buf = new StringBuffer();
//		for(Attribute attr: entity.getPrimaryKeyList()){
//			buf.append(","+ attr.getJavaType()+ " "+ attr.getVarName() );
//		}
//		return buf.substring(1);
//	}
	/**
	 * PK作成用
	 * @return
	 */
//	public String writePKSetter(){
//		StringBuffer buf = new StringBuffer();
//		for(Attribute attr: entity.getPrimaryKeyList()){
////			if(org.apache.commons.lang.StringUtils)
//			buf.append("this."+ attr.getVarName() +"="+ attr.getVarName() +";\n");
//		}
//		return StringUtils.indent(buf.toString(),3);
//	}

	/**
	 * getParameters()の各属性の書き出し
	 * @return
	 */
	public String writeGetParametersPK(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			buf.append(attr.toGetParameter()).append("\n");
		}
		return StringUtils.indent(buf.toString(),3);
	}

	/**
	 * setParameters()の各属性の書き出し
	 * @return
	 */
	public String writeSetParametersPK(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			buf.append(attr.toSetParameter()).append("\n");
		}
		return StringUtils.indent(buf.toString(),3);
	}

	/**
	 * getAttributeMap()の各属性の書き出し
	 * @return
	 */
	public String writeGetAttributeMapPK(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			buf.append(attr.toGetAttributeMap()).append("\n");
		}
		return StringUtils.indent(buf.toString(),3);
	}

	/**
	 * メンバーを文字列に書き出し
	 * @return
	 */
	public String writeStringMembersPK(){
		return writeStringMembers(entity.getPrimaryKeyList());
	}
	public String writeStringMembers(){
		List<Attribute> list = new ArrayList<Attribute>();
		// PKと名前(あれば)を入れる
		for(Attribute attr: entity.getAttrList()){
			if(attr.isPrimaryKey()){
				list.add(attr);
			}else if(attr.getName().matches("^"+entity.getName().toLowerCase() +".*Name$")){
				list.add(attr);
			}
		}
		return writeStringMembers(list);
	}
	private String writeStringMembers(List<Attribute> list){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: list){
			if(attr.getJavaType().equals("String")){
				buf.append(" +\":\"+ "+ attr.getGetterName()+"()");
			}else{
				buf.append(" +\":\"+ String.valueOf("+ attr.getGetterName() +"())");
			}
		}
		if(buf.length()==0){
			logger.error("プライマリキー無し");
			return "";
		}
		return buf.substring(7);
	}
}
