package com.lavans.lacoder2.generator.writer;

import java.util.Set;
import java.util.TreeSet;

import com.lavans.lacoder2.generator.model.Attribute;
import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.lang.StringUtils;

public class ActionWriter {
	//private static Log logger = LogFactory.getLog(DaoWriter.class);
	private Entity entity;

	private String varName;
	public ActionWriter(Entity entity){
		this.entity = entity;
		varName = entity.getShortname();
	}

	/**
	 * このEntityで使用しているEnumのimportを列挙する。
	 * @return
	 */
	public String writeImportsEnum(){
		StringBuffer buf = new StringBuffer();

		// 重複を回避するために一旦Setに格納
		Set<String> classSet = new TreeSet<String>();
		for(Attribute attr: entity.getAttrList()){
			if(attr.isEnum()){
				classSet.add(attr.getClassName());
			}
		}

		// Setからimport文を作成
		for(String className: classSet){
			// stausEnumは参照不要(暫定)
			//if(className.endsWith("StatusEnum")) continue;
			buf.append("import "+ className +";\n");
		}
		return buf.toString();
	}

	/**
	 * このEntityで使用しているEnumのuiParts作成。
	 * @return
	 */
	public String writeUiPartsEnum(){
		StringBuffer buf = new StringBuffer();

		Set<String> classSet = new TreeSet<String>();
		for(Attribute attr: entity.getAttrList()){
			if(attr.isEnum()){
				classSet.add(attr.getClassLastName());
			}
		}

		for(String className: classSet){
			buf.append("uiParts.put(\""+ StringUtils.uncapitalize(className) +"List\", "+ className +".getAllList());\n");
		}
		return StringUtils.chomp(StringUtils.indent(buf.toString(), 2));
	}
	/**
	 * Resultアノテーションリダイレクト用PK書き出し
	 * @return
	 */
	public String writeResults(String jspPath){
		StringBuffer buf = new StringBuffer();
		buf.append(writeResultsInsert(jspPath, "create"));
		buf.append(writeResultsInsert(jspPath, "update"));
		buf.append(writeResultsDelete(jspPath, "logicalDelete"));
		buf.append(writeResultsDelete(jspPath, "delete"));

		return StringUtils.indent(buf.toString(), 2);
	}

	private String writeResultsInsert(String jspPath, String crud){
		String constName = crud.toUpperCase();
		StringBuffer buf = new StringBuffer();
		buf.append("// "+ crud +"\n");
		buf.append("@Result(name = ActionSupport."+ constName +"_INPUT,	location = \""+ jspPath +"/"+ varName +"-"+ crud +"Input.jsp\"),\n");
		if(!entity.isSkipConfirm()){
			buf.append("@Result(name = ActionSupport."+ constName +"_CONFIRM,location = \""+ jspPath +"/"+ varName +"-"+ crud +"Confirm.jsp\"),\n");
		}
		if(!entity.isSkipResult()){
			// TODO Cookie確認
			buf.append("// これだとCookie非対応機種の時にセッションが維持されなくね？\n");
			buf.append("@Result(name = ActionSupport."+ constName +"_EXECUTE,location = \""+ varName +"-crud!"+ crud +"Result.action?"+ writeRedirectPK() +"\", type=\"redirect\"),\n");
			buf.append("@Result(name = ActionSupport."+ constName +"_RESULT,	location = \""+ jspPath +"/"+ varName +"-"+ crud +"Result.jsp\"),\n");
		}else{
			buf.append("@Result(name = ActionSupport."+ constName +"_EXECUTE,location = \""+ varName +"List\", type=\"redirectAction\"),\n");
		}

		return buf.toString();
	}

	private String writeResultsDelete(String jspPath, String crud){
		String constName = StringUtils.toUnderscore(crud).toUpperCase();
		StringBuffer buf = new StringBuffer();
		buf.append("// "+ crud +"\n");
		if(!entity.isSkipDeleteConfirm()){
			buf.append("@Result(name = ActionSupport."+ constName +"_CONFIRM,location = \""+ jspPath +"/"+ varName +"-"+ crud +"Confirm.jsp\"),\n");
		}
		if(!entity.isSkipDeleteResult()){
			buf.append("@Result(name = ActionSupport."+ constName +"_EXECUTE,location = \""+ varName +"-crud\", type=\"redirectAction\", params={\"method\",ActionSupport."+ constName +"_RESULT}),\n");
			buf.append("@Result(name = ActionSupport."+ constName +"_RESULT,	location = \""+ jspPath +"/"+ varName +"-"+ crud +"Result.jsp\"),\n");
		}else{
			buf.append("@Result(name = ActionSupport."+ constName +"_EXECUTE,location = \""+ varName +"List\", type=\"redirectAction\"),\n");
		}

		return buf.toString();
	}

	/**
	 * PKをentityからgetするメソッドをカンマ区切りで列挙する。
	 * @return
	 */
	public String writePrimaryKeyGetter(){
		return new DaoWriter(entity).writePrimaryKeyGetter();
	}

	/**
	 * Resultアノテーションリダイレクト用PK書き出し
	 * @return
	 */
	public String writeRedirectPK(){
		StringBuffer buf = new StringBuffer();
		for(Attribute attr: entity.getPrimaryKeyList()){
			buf.append(entity.getShortname()+"."+attr.getVarName()+"="+"${"+entity.getShortname()+"."+attr.getVarName()+"}");
		}
		return buf.toString();
	}
}
