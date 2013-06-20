package com.lavans.lacoder2.generator.writer;

import com.lavans.lacoder2.generator.model.EnumClass;
import com.lavans.lacoder2.generator.model.EnumMember;
import com.lavans.lacoder2.lang.StringUtils;


public class EnumWriter {
	private EnumClass enumClass;
	public EnumWriter(EnumClass enumClass){
		this.enumClass = enumClass;
	}
	
	/**
	 * Member declarations.
	 * @return
	 */
	public String writeMembers(){
		StringBuffer buf = new StringBuffer();
		for(EnumMember member: enumClass.getMemberList()){
			buf.append("\t"+toDeclare(member)+"\n");
		}
		buf.append(";");
		
		return buf.toString();
	}

	/**
	 * メンバー宣言を返します。
	 * @param member
	 * @return
	 */
	private String toDeclare(EnumMember member){
		StringBuilder buf = new StringBuilder();
		buf.append(member.getName() + "(\""+member.getTitle() +"\"");
		for(String field: enumClass.getFieldList()){
			buf.append(","+convertValueString(member, field));
		}
		buf.append("),");
		return buf.toString();
	}

	/**
	 * Stringは""で括る。それ以外ならそのまま。
	 * @return
	 */
	private String convertValueString(EnumMember member, String field){
		String value = member.getValue(field);
		if(!enumClass.isBoolean(field) && !enumClass.isInt(field)){
			value = "\""+ value + "\"";
		}
		return value;
	}

	/**
	 * valueOf検索用Map
	 * @return
	 */
	public String writeMaps(){
		StringBuffer buf = new StringBuffer();
		for(String field: enumClass.getFieldList()){
			buf.append(toMap(field));
		}
		
		return buf.toString();
	}
	
	/**
	 * Fieldから変数の型を返します。
	 * @param field
	 * @return
	 */
	private String getType(String field){
		String type = "String";
		if(enumClass.isBoolean(field)){
			type="Boolean";
		}else if(enumClass.isInt(field)){
			type="Integer";
		}
		return type;
	}
	
	/**
	 * Map個別
	 * @param field
	 * @return
	 */
	private static final String MAP_DECLARE="\tprivate static Map&lt;$type,$class&gt; $fieldMap = new HashMap&lt;&gt;($size);\n";
	private static final String LIST_DECLARE="\tprivate static List&lt;$class&gt; $fieldList = new ArrayList&lt;&gt;($size);\n";
	private String toMap(String field){
		String map = replaceDeclare(MAP_DECLARE, field);
		String list = replaceDeclare(LIST_DECLARE, field);

		String result = "\t/** "+ field +"格納用Map */\n"+ map;
		result += "\t/** "+ field +"格納用List */\n"+ list;
		return result;
	}
	/**
	 * 文字列変換
	 * @param src
	 * @param field
	 * @param type
	 * @return
	 */
	private String replaceDeclare(String src, String field){
		return src.replace("$field", field)
				.replace("$type", getType(field))
				.replace("$class", enumClass.getClassName())
				.replace("$size", String.valueOf(enumClass.getMemberList().size()));
	}
	
	/**
	 * static初期化
	 * @return
	 */
	public String writeStatics(){
		StringBuffer buf = new StringBuffer();
		for(String field: enumClass.getFieldList()){
			for(EnumMember member: enumClass.getMemberList()){
				buf.append(toStatic(member, field));
			}
		}
		
		return buf.toString();
	}
	
	private String replaceInit(String src, String field, String name, String value){
		return src.replace("$field", field).replace("$name", name).replace("$value", value);
	}
	private static final String MAP_INIT="\t\t$fieldMap.put($value, $name);\n";
	private static final String LIST_INIT="\t\t$fieldList.add($name);\n";
	public String toStatic(EnumMember member, String field){
		// 除外登録されていたら次へ
		if(member.isExclude(field)){
			return "";
		}
		String map = replaceInit(MAP_INIT, field, member.getName(), convertValueString(member, field));
		String list = replaceInit(LIST_INIT, field, member.getName(), convertValueString(member, field));
		
		return map + list;
	}
	
	/**
	 * インスタンス変数。
	 * @return
	 */
	public String writeInstanceVars(){
		StringBuffer buf = new StringBuffer();
		for(String field: enumClass.getFieldList()){
			buf.append("\tprivate " + getType(field) + " " + field +";\n");
		}
		
		return buf.toString();
	}
	/**
	 * コンストラクタ引数
	 * @return
	 */
	public String writeConstructorArgs(){
		StringBuffer buf = new StringBuffer();
		for(String field: enumClass.getFieldList()){
			buf.append("\t\t," + getType(field) + " " + field +"\n");
		}
		
		return buf.toString();
	}
	
	public String writeConstructor(){
		StringBuffer buf = new StringBuffer();
		for(String field: enumClass.getFieldList()){
			buf.append("\t\tthis."+field +"="+ field +";\n");
		}
		
		return buf.toString();
	}
	
	/**
	 * @return
	 */
	public String writeGetters(){
		StringBuffer buf = new StringBuffer();
		for(String field: enumClass.getFieldList()){
			buf.append(toGetter(field));
		}
		
		return buf.toString();
	}

	/**
	 * Getter method.
	 * @param field
	 * @return
	 */
	private String toGetter(String field){
		String result = "\tpublic "+ getType(field) +" get"+ StringUtils.toUpperCamelCase(field) +"(){\n";
		result += "\t\treturn "+ field+";\n";
		result += "\t}\n";
		
		return result;
	}
}
