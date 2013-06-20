/* $Id: XMLReader.java 508 2012-09-20 14:41:55Z dobashi $
 * create: 2004/12/28
 * (c)2004 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.main;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lavans.lacoder2.generator.db.PostgresDialect;
import com.lavans.lacoder2.generator.db.TypeManager;
import com.lavans.lacoder2.generator.model.Attribute;
import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.generator.model.EnumClass;
import com.lavans.lacoder2.generator.model.EnumMember;
import com.lavans.lacoder2.generator.model.Package;
import com.lavans.lacoder2.generator.model.Service;
import com.lavans.lacoder2.lang.StringUtils;
import com.lavans.lacoder2.util.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


enum ConfigMain {
	FQDN, PROJECT, DATABASE_DIALECT, ENTITY_DEFAULT;
	public String getXmlName() {
		return name().toLowerCase().replace("_", "-");
	}
}

enum ConfigEntity {
	SKIP_CONFIRM, SKIP_RESULT, BACKUP, ROLE, PRIMARY_KEY, SCHEMA; // NAME,SHORTNAME,CACHE
	public String getXmlName() {
		return name().toLowerCase().replace("_", "-");
	}
}

enum ConfigAttribute {
	NAME,DB_NAME,;
	public String getXmlName() {
		return name().toLowerCase().replace("_", "-");
	}
	
}

/**
 * @author dobashi
 * @version 1.00
 */
public class XMLReader {
	private static Log logger = LogFactory.getLog(XMLReader.class);
	private Config lacoderConfig = null;

	/**
	 * 設定ファイル読み込み
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public void read(Target target, String filename)
			throws FileNotFoundException {
		Package.getAllList().clear();

		lacoderConfig = target.getGenerotorConfig();

		String fqdn = null;
		String project = null;
		fqdn = lacoderConfig.getNodeValue(ConfigMain.FQDN.getXmlName());
		project = lacoderConfig.getNodeValue(ConfigMain.PROJECT.getXmlName());

		Element root = target.getEntityConfig(filename).getRoot();

		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			// System.out.println(item.getNodeName() +":"+ item.getNodeType());

			if ((item.getNodeType() == Node.TEXT_NODE)
					|| (item.getNodeType() == Node.COMMENT_NODE)) {
				continue;
			}
			Element ele = (Element) item;
			// System.out.println(ele.getAttribute("name")
			// +":"+ele.getAttribute("value"));
			if (ele.getNodeName().equals("package")) {
				Package.addPackage(readPackage(fqdn, project, ele));
			}
		}
	}

	/**
	 * パッケージの読み出し
	 * 
	 * @param domain
	 * @param project
	 * @param pkgEle
	 * @return
	 */
	private Package readPackage(String domain, String project, Element pkgEle) {
		Package pkg = new Package();
		pkg.setName(domain, project, pkgEle.getAttribute("name"));

		// テーブル取得開始 -------------------------------
		NodeList tableList = pkgEle.getChildNodes();
		for (int i = 0; i < tableList.getLength(); i++) {
			Node item = tableList.item(i);
			// System.out.println(item.getNodeName() +":"+ item.getNodeType());

			if ((item.getNodeType() == Node.TEXT_NODE)
					|| (item.getNodeType() == Node.COMMENT_NODE)) {
				continue;
			}

			Element ele = (Element) item;
			if (ele.getNodeName().equals("entity")) {
				pkg.addEntity(readEntity(ele));
			} else if (ele.getNodeName().equals("enum")) {
				pkg.addEnum(readEnum(ele));
			} else if (ele.getNodeName().equals("service")) {
				pkg.addService(readService(ele));
			}

		}
		return pkg;

	}

	/**
	 * エンティティの読み出し
	 * 
	 * @param entityEle
	 * @return
	 */
	private Entity readEntity(Element entityEle) {
		Entity entity = new Entity();

		// DBTypeの読込
		TypeManager typeManager = null;
		String dbType = null;
		dbType = lacoderConfig.getNodeValue(ConfigMain.DATABASE_DIALECT
				.getXmlName());
		typeManager = TypeManager.getInstance(dbType);
		if (typeManager == null) {
			// デフォルトはpostgreSQL
			logger.info("database-type error. use postgresType.");
			typeManager = new PostgresDialect();
		}
		entity.setTypeManager(typeManager);

		// デフォルト設定 skip
		String skipConfirm = ConfigEntity.SKIP_CONFIRM.getXmlName();
		String skipResult = ConfigEntity.SKIP_CONFIRM.getXmlName();
		String schema = ConfigEntity.SCHEMA.getXmlName();
		String entityDefault = "entity-default/";
		try {
			entity.setSkipConfirm(Boolean.parseBoolean(lacoderConfig
					.getNodeValue(entityDefault + skipConfirm)));
			entity.setSkipResult(Boolean.parseBoolean(lacoderConfig
					.getNodeValue(entityDefault + skipResult)));
			entity.setSchema(lacoderConfig.getNodeValue(entityDefault + schema));
		} catch (Exception e) {
			logger.error(e);
		}

		// 個別に上書き skip
		if (!StringUtils.isEmpty(entityEle.getAttribute(skipConfirm))) {
			entity.setSkipConfirm(Boolean.parseBoolean(entityEle
					.getAttribute(skipConfirm)));
		}
		if (!StringUtils.isEmpty(entityEle.getAttribute(skipResult))) {
			entity.setSkipResult(Boolean.parseBoolean(entityEle
					.getAttribute(skipResult)));
		}

		// 基本情報
		entity.setName(entityEle.getAttribute("name"));
		entity.setTitle(entityEle.getAttribute("title"));
		String shortName = entityEle.getAttribute("shortname");
		if (!shortName.equals("")) {
			entity.setShortname(shortName);
		} else {
			entity.setShortname(entityEle.getAttribute("name"));
		}
		entity.setCached(Boolean.parseBoolean(entityEle.getAttribute("cache")));
		if (entityEle.getAttribute(ConfigEntity.PRIMARY_KEY.getXmlName()) != null) {
			entity.setPrimaryKey(entityEle
					.getAttribute(ConfigEntity.PRIMARY_KEY.getXmlName()));
		}

		// プライマリーキー
		List<String> primaryKeyStrList = new ArrayList<String>(
				Arrays.asList(StringUtils.splitTrim(entity.getPrimaryKey(), ",")));

		// 編集可能ユーザー "member","admin"など
		String userStr = entityEle.getAttribute(ConfigEntity.ROLE.getXmlName());
		String users[] = userStr.split(",");
		for (int i = 0; i < users.length; i++) {
			if (!StringUtils.isEmpty(users[i].trim())) {
				entity.getUserList().add(users[i].trim());
			}
		}

		// バックアップをとるか
		entity.setHasBackup(Boolean.parseBoolean(entityEle
				.getAttribute(ConfigEntity.BACKUP.getXmlName())));

		// フィールド取得開始 -------------------------------
		NodeList nodeList = entityEle.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			// System.out.println(item.getNodeName() +":"+ item.getNodeType());

			if ((item.getNodeType() == Node.TEXT_NODE)
					|| (item.getNodeType() == Node.COMMENT_NODE)) {
				continue;
			}

			Element ele = (Element) item;
			Attribute attr = new Attribute();
			attr.setEntity(entity);
			attr.setName(ele.getAttribute("name"));

			// プライマリーキーか
			if (primaryKeyStrList.contains(attr.getName())) {
				attr.setPrimaryKey(true);
			}
			// シーケンスか
			if (ele.getAttribute("sequence").equals("true")) {
				attr.setSequence(true);
			}

			// タイトル
			attr.setTitle(ele.getAttribute("title"));

			// javaの型
			if (!ele.getAttribute("type").equals("")) {
				attr.setJavaType(ele.getAttribute("type"));
			}
			
			// 定数名
			if (!ele.getAttribute(ConfigAttribute.DB_NAME.getXmlName()).equals("")) {
				attr.setDbName(ele.getAttribute(ConfigAttribute.DB_NAME.getXmlName()));
			}else{
				attr.setDbName(StringUtils.toUnderscore(attr.getName()).toUpperCase());
			}

			// 参照クラス(optional)
			attr.setClassName(ele.getAttribute("class"));

			// enumか
			// 旧形式: enum="true" class="com.lavans.xxx.XXXEnum"
			// 新形式: enum="com.lavans.xxx.XXXEnum"
			if (!StringUtils.isEmpty(ele.getAttribute("enum"))) {
				attr.setEnum(true);
				if (!ele.getAttribute("enum").equals("true")) {
					attr.setClassName(ele.getAttribute("enum"));
				}
			}

			// DB上の型(optional)
			if (!ele.getAttribute("db-type").equals("")) {
				attr.setDbType(ele.getAttribute("db-type"));
			}

			// 日付型なら入力フォーマットの指定。未指定ならデフォルト"yyyy/MM/dd"。
			// logger.debug(attr.getName());
			String javaType = attr.getJavaType();
			if (javaType == null) {
				logger.info("xmlファイルのtype設定を見直してください。" + ele.getNodeName()
						+ ":" + ele.getAttribute("javaType"));
			}
			if (javaType.equals("Date")) {
				if (!ele.getAttribute("format").equals("")) {
					attr.setDateFormat(ele.getAttribute("format"));
				}
			}

			// NOT NULL指定
			// 設定が存在しない場合(default)はfalse
			try {
				attr.setNullable(Boolean.parseBoolean(ele.getAttribute("nullable")));
			} catch (Exception e) {
			}
			// constraint
			// defaultはnull
			try {
				attr.setMax(Long.parseLong(ele.getAttribute("max")));
			} catch (Exception e) {
			}
			try {
				attr.setMin(Long.parseLong(ele.getAttribute("min")));
			} catch (Exception e) {
			}
			try {
				attr.setPrecision(Integer.parseInt(ele
						.getAttribute("precision")));
			} catch (Exception e) {
			}
			try {
				attr.setScale(Integer.parseInt(ele.getAttribute("scale")));
			} catch (Exception e) {
			}

			// listの取得
			if (!ele.getAttribute("list").equals("")) {
				Boolean isList = false;
				try {
					isList = Boolean.parseBoolean(ele.getAttribute("list"));
				} catch (Exception e) {
				}
				if (isList) {
					attr.setList(true);
					entity.setHasList(true);
				}
			}

			// Blobを持っているかどうか
			if (attr.getJavaType().equals("byte[]")) {
				entity.setHasBlob(true);
			}

			entity.add(attr);
		}

		return entity;
	}

	private EnumClass readEnum(Element enumEle) {
		EnumClass enumClass = new EnumClass();
		enumClass.setName(enumEle.getAttribute("name"));
		enumClass.setTitle(enumEle.getAttribute("title"));
		// valueListのセット
		if (enumEle.getAttribute("fields") != "") {
			enumClass.setFieldList(enumEle.getAttribute("fields"));
		}
		if (enumEle.getAttribute("values") != "") {
			enumClass.setFieldList(enumEle.getAttribute("values"));
		}
		// booleanListのセット
		if (enumEle.getAttribute("boolean-fields") != "") {
			enumClass.setBooleanList(enumEle.getAttribute("boolean-fields"));
		}
		// intListのセット
		if (enumEle.getAttribute("int-fields") != "") {
			enumClass.setIntList(enumEle.getAttribute("int-fields"));
		}

		// フィールド取得開始 -------------------------------
		NodeList nodeList = enumEle.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			// System.out.println(item.getNodeName() +":"+ item.getNodeType());

			if ((item.getNodeType() == Node.TEXT_NODE)
					|| (item.getNodeType() == Node.COMMENT_NODE)) {
				continue;
			}

			EnumMember member = readEnumMember(enumClass, (Element)item);
			enumClass.add(member);
		}

		return enumClass;
	}
	
	private EnumMember readEnumMember(EnumClass enumClass, Element ele){
		EnumMember member = new EnumMember();
		member.setName(ele.getAttribute("name"));
		try {
			member.setTitle(URLDecoder.decode(ele.getAttribute("title"),
					"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (ele.getAttribute("default").equals("true")) {
			member.setDefault(true);
		}
		// valueListのセット
		for (int j = 0; j < enumClass.getFieldList().size(); j++) {
			String listName = enumClass.getFieldList().get(j);
			if (ele.getAttribute(listName) != "") {
				try {
					member.putValue(listName, URLDecoder.decode(
							ele.getAttribute(listName), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				member.setExclude(listName);
			}
		}

		// Allリストからの除外登録
		if (ele.getAttribute("exclude") != "") {
			member.setExclude("all");
		}
		
		return member;
	}

	private Service readService(Element ele) {
		Service service = new Service();
		service.setName(ele.getAttribute("name"));
		return service;
	}
}