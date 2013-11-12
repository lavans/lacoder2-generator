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
}
