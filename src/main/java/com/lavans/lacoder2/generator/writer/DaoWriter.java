package com.lavans.lacoder2.generator.writer;

import com.lavans.lacoder2.generator.model.Attribute;
import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.lang.StringUtils;

public class DaoWriter {
	//private static Log logger = LogFactory.getLog(DaoWriter.class);
	private Entity entity;

	public DaoWriter(Entity entity){
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}


	public String writeBindPk(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			String constStr = entity.getClassName() +"."+ attr.getConstName();

			if(attr.getJavaType().equals("Date")){
				buf.append("st.setTimestamp("+ constStr +",new Timestamp("+ attr.getVarName() +".getTime())));\n");
			}else{
				buf.append("st.set"+ attr.getJdbcMethodName()+"("+ constStr +","+ attr.getVarName() +");\n");
			}
		}
		return StringUtils.indent(buf.toString(),3);
	}

	/**
	 * selectに必要なキーがそろっているか確認する。
	 * Stringが空でないかどうかだけチェック。
	 * @return
	 */
	public String writeSelectCheck(){
		StringBuffer buf = new StringBuffer();

		for(Attribute attr: entity.getPrimaryKeyList()){
			if(attr.getJavaType().equals("String")){
				buf.append("if("+ attr.getVarName() +"==null || "+ attr.getVarName() +".equals(\"\")){;\n");
				buf.append("	logger.info(\""+ attr.getVarName() +" is empty\");\n");
				buf.append("	return null;");
				buf.append("}");
			}
		}

		return StringUtils.indent(buf.toString(),2);
	}


	public String writeBindInsert(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getAttrList()){
			buf.append(bindStr(attr));
		}
		return StringUtils.indent(buf.toString(),3);
	}

	public String writeBindUpdate(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getAttrList()){
			if(attr.getName().equals("insertDatetime")){
				// 登録日時は更新しない
				continue;
			}
			buf.append(bindStr(attr));
		}
		return StringUtils.indent(buf.toString(),3);
	}

	public String writeBindPK(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			buf.append(bindStr(attr));
		}
		return StringUtils.indent(buf.toString(),3);
	}

	private String bindStr(Attribute attr){
		StringBuffer buf = new StringBuffer();

		String getMethodStr = entity.getShortname() +"."+ attr.getGetterName() +"()";
		String constStr = entity.getClassName() +"."+ attr.getConstName();

		if(attr.getJavaType().equals("Date")){
			buf.append("if("+ getMethodStr +"!=null){\n");
			buf.append("	st.setTimestamp("+ constStr +",new Timestamp("+ getMethodStr +".getTime()));\n");
			buf.append("}else{\n");
			buf.append("	st.setTimestamp("+ constStr +", null);\n");
			// Oracle 9以前ではsetNullが使えない
			//buf.append("				st.setNull("+ attr.getName() +",Types.NULL); --+"
			buf.append("}\n");

		}else{
			buf.append("st.set"+ attr.getJdbcMethodName()+"("+ constStr +","+ getMethodStr +");\n");
		}

		return buf.toString();

		// Oracleの時に以下の処理が必要かも
		//
//			}else if(attr.getJavaType().equals("byte[]")){
//				buf.append("if("+ getMethodStr +"!=null){\n");
//				buf.append("	st.setBlob("+ constStr +", new SerialBlob("+ getMethodStr +")));\n");
//				buf.append("}else{\n");
//				buf.append("st.setTimestamp("+ constStr +", null);\n");
//				buf.append("}\n");
	}

	/**
	 * PKをentityからgetするメソッドをカンマ区切りで列挙する。
	 * @return
	 */
	public String writePrimaryKeyGetter(){
		return writePrimaryKeyGetter(entity.getShortname());
	}

	/**
	 * PKをpkからgetするメソッドをカンマ区切りで列挙する。
	 * @return
	 */
	public String writePrimaryKeyGetterFromPK(){
		return writePrimaryKeyGetter("pk");
	}

	/**
	 * PKをentityからgetするメソッドをカンマ区切りで列挙する。
	 * @return
	 */
	private String writePrimaryKeyGetter(String name){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			buf.append(", "+ name +"."+ attr.getGetterName() +"()");
		}
		return buf.substring(2);
	}

	public String writeMakeTarget(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getAttrList()){
			String setter = attr.getSetterName();
			String columnName = "\""+ attr.getConstName() +"\"";
			buf.append("if(columnNames.contains("+ columnName +")) target."+ setter +"(rs.get"+ attr.getJdbcMethodName() +"("+ columnName +"));\n" );
		}
		return StringUtils.indent(buf.toString(), 2);
	}


	public String writeMakeTargetPK(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			String setter = attr.getSetterName();
			String columnName = "\""+ attr.getConstName() +"\"";
			buf.append("target."+ setter +"(rs.get"+ attr.getJdbcMethodName() +"("+ columnName +"));\n" );
		}
		return StringUtils.indent(buf.toString(), 2);
	}
}
