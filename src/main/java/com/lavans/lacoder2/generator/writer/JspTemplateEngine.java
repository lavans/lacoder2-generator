package com.lavans.lacoder2.generator.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;

import com.lavans.lacoder2.generator.main.Target;
import com.lavans.lacoder2.generator.model.Attribute;
import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.generator.model.Role;
import com.lavans.lacoder2.lang.LogUtils;
import com.lavans.lacoder2.lang.StringUtils;

public class JspTemplateEngine {
	private static Logger logger = LogUtils.getLogger();
	
	private Entity entity;
	private Role role;
	//private String varName;
	private Target target = Target.getSelectedTarget();
	public JspTemplateEngine(Entity entity, Role role){
		this.entity = entity;
		this.role = role;
		//varName = entity.getShortname();
	}

	private StringBuilder getTemplate(String fileName){
		// roleの下から取得
		String rolePath = target.getTemplatePath()+ role +"/"+ fileName;
		logger.info(rolePath);
		File file = new File(rolePath);
		if(!file.exists()){
			// 存在しないなら親から
			String parentPath = target.getTemplatePath() + fileName;
			logger.debug(parentPath);
			file = new File(parentPath);
			// これも存在しないならエラー
			if(!file.exists()){
				logger.error("テンプレートファイルが存在しない:"+ fileName);
			}
		}
		StringBuilder buf = new StringBuilder((int)file.length());
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line;
			while((line = in.readLine()) != null){
				buf.append(line+"\n");
			}
			in.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}

		return buf;
	}

	private class TagSplitter{
		String before;
		String body;
		String after;
		String[] attrs;
		/**
		 * 空要素タグの事は<del>明日考えよう</del>考えた
		 * @param buf
		 * @param tagName
		 * @return
		 */
		public boolean find(StringBuilder buf, String tagName){
			String startTag = "<#"+tagName +" ";
			String endTag   = "</#"+tagName +">";
			int startTagIndex = buf.indexOf(startTag);
			if(startTagIndex<0) return false;

			// タグの前
			before = buf.substring(0, startTagIndex);
			before = StringUtils.chomp(before);
			logger.debug("before:"+before);

			int startTagEnd = buf.indexOf(">", startTagIndex);
			String tagAttrStr = buf.substring(startTagIndex+2, startTagEnd);
			logger.debug("tagAttrStr:"+ tagAttrStr);

			if(tagAttrStr.endsWith("/")){
				// 空要素タグなら
				body="";
				tagAttrStr = tagAttrStr.substring(0, tagAttrStr.length()-1);
				after = buf.substring(startTagEnd+1);
				logger.debug("after:"+after);
			}else{
				int endTagIndex =  buf.indexOf(endTag);
				body = buf.substring(startTagEnd+1, endTagIndex);
				body = StringUtils.chomp(body);
				logger.debug("body:"+body);

				after = buf.substring(endTagIndex+endTag.length());
				logger.debug("after:"+after);
			}
			attrs = tagAttrStr.split(" ");

			return true;
		}
	}
	public String writeJsp(String method){
		// ファイルの読み込み
		StringBuilder buf = getTemplate(method+".tmpl");
		TagSplitter splitter = new TagSplitter();
		while(splitter.find(buf, "foreach")){
			StringBuilder bufwork = new StringBuilder();
			bufwork.append(splitter.before);
			for(Attribute attr: entity.getAttrList()){
				// pkのみ
				if(splitter.attrs.length==4 && splitter.attrs[2].equals("only") && splitter.attrs[3].equals("pk")){
					if(!attr.isPrimaryKey()){ continue; }
				}
				// pk以外
				if(splitter.attrs.length==4 && splitter.attrs[2].equals("except") && splitter.attrs[3].equals("pk")){
					if(attr.isPrimaryKey()){ continue; }
				}
				String body = splitter.body;
				body = body.replace("<#attr title>", attr.getTitle());
				body = body.replace("<#attr name>", attr.getVarName());
				body = body.replace("<#attr getterName>", attr.getGetterName());
				body = body.replace("<#attr constName>", attr.getConstName());
				bufwork.append(body);
			}
			bufwork.append(splitter.after);
			// 入れ替え
			buf = bufwork;
		}

		while(splitter.find(buf, "if")){
			StringBuilder bufwork = new StringBuilder();
			bufwork.append(splitter.before);
			bufwork.append(splitter.body);
			bufwork.append(splitter.after);
			// 入れ替え
			buf = bufwork;
		}

		//
		String result = buf.toString();
		result = result.replace("<#entity title>", entity.getTitle());
		result = result.replace("<#entity name>", entity.getShortname());
		result = result.replace("<#entity className>", entity.getClassName());
		result = result.replace("<#entity classNameFull>", entity.getClassNameFull());
		result = result.replace("<#role>", role.toString());
		// foreach attr attriute
//		entity.geta
//		buf.indexOf()
		return result;
	}

	/**
	 * PKをentityからgetするメソッドをカンマ区切りで列挙する。
	 * @return
	 */
	public String writePrimaryKeyGetter(){
		return new DaoWriter(entity).writePrimaryKeyGetter();
	}
}
