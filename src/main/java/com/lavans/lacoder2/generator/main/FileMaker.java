/* $Id: FileMaker.java 508 2012-09-20 14:41:55Z dobashi $
 * created: 2005/09/08
 */
package com.lavans.lacoder2.generator.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.generator.model.EnumClass;
import com.lavans.lacoder2.generator.model.Package;
import com.lavans.lacoder2.lang.LogUtils;
import com.lavans.lacoder2.lang.StringEscapeUtils;


/**
 *
 * @author dobashi
 */
public class FileMaker {
	private static final Logger logger = LogUtils.getLogger();
	private static final String SRC_PATH = "/src/main/java/";
	private String urlBase = null;
	private String targetPath = null;
	private StringBuffer buf = new StringBuffer();
	/** ファイル存在時に上書きするかどうか */
	private boolean isOverride = false;

	public void makefile(Target target, String filetype, String urlBase) {
		this.urlBase = urlBase;
		targetPath = target.getPath();
		List<Package> pkgList = Package.getAllList();

		for (int i = 0; i < pkgList.size(); i++) {
			// パッケージディレクトリの作成
			Package pkg = (Package) pkgList.get(i);
			String dirPath = (targetPath + SRC_PATH + pkg.getModelSubPackagePath().replaceAll("\\.", "/"));
			buf.append("dir:" + dirPath + "<br>");

			// ファイル作成
			if (filetype.equals("enums")) {
				makeEnum(pkg);
			} else if (filetype.equals("base")) {
				// entityの場合
				buf.append("--Base作成:<br>");
				makeBase(pkg, dirPath);
			} else if (filetype.equals("entity")) {
				// entityの場合
				buf.append("--Entity作成:<br>");
				makeEntity(pkg, dirPath);
				makeService(pkg, dirPath);
			} else if (filetype.equals("action")) {
				// entityの場合
				buf.append("--action作成:<br>");
				makeAction(pkg);
				makeJsp(pkg);
			} else if (filetype.equals("sql")) {
				// sqlの場合enumは飛ばす
				if(pkg.getModelSubPackagePath().endsWith("enums")){
					continue;
				}
				buf.append("--SQL作成:<br>");
				makeSql(pkg);
			} else {
				buf.append("--err:" + filetype);
			}

		}
	}

	private void makeEnum(Package pkg) {
		String dirPath = (targetPath + SRC_PATH + pkg.getName().replaceAll("\\.", "/"));
		// enumの場合
		if (pkg.enumSize() > 0) {
			dirPath = makeDir(dirPath);
		}
		for (int j = 0; j < pkg.enumSize(); j++) {
			EnumClass enumClass = pkg.getEnum(j);
			String path = "Enums.do?package=" + pkg.getName() + "&enum=" + enumClass.getName();
			makeEntity(dirPath + "/" + enumClass.getClassName() + ".java", urlBase + path);
		}
	}

	private void makeBase(Package pkg, String dirPath) {
		// baseの場合
		if (pkg.entitySize() > 0) {
			makeDir(dirPath + "/entity/base");
			makeDir(dirPath + "/dao/base");
		}
		for (int j = 0; j < pkg.entitySize(); j++) {
			Entity entity = pkg.getEntity(j);
			String path;
			String name = entity.getClassName();
			path = "EntityBase.do?package=" + pkg.getName() + "&entity=" + entity.getName();
			makeEntity(dirPath + "/entity/base/" + name + "Base.java", urlBase + path);
			path = "DaoBaseXml.do?package=" + pkg.getName() + "&entity=" + entity.getName();
			makeEntity(dirPath + "/dao/base/" + name + "DaoBase.xml", urlBase + path);
			// path = "daoBase.do?package="+ pkg.getName() +"&entity="+
			// entity.getName();
			// makeEntity(dirPath+"/dao/base/"+name+"DaoBase.java",
			// urlBase+path);
			// path = "daoBaseXml.do?package="+ pkg.getName() +"&entity="+
			// entity.getName();
			// makeEntity(dirPath+"/dao/base/"+name+"DaoBase.xml",
			// urlBase+path);
		}
	}

	/**
	 * Entity作成
	 * @param pkg
	 * @param dirPath
	 */
	private void makeEntity(Package pkg, String dirPath) {
		// ディレクトリ作成
		if (pkg.entitySize() > 0) {
			makeDir(dirPath + "/entity");
			makeDir(dirPath + "/dao");
		}
		for (int j = 0; j < pkg.entitySize(); j++) {
			Entity entity = pkg.getEntity(j);
			String path;
			String name = entity.getClassName();
			path = "Entity.do?package=" + pkg.getName() + "&entity=" + entity.getName();
			makeEntity(dirPath + "/entity/" + name + ".java", urlBase + path);

//			path = "Dao.do?package=" + pkg.getName() + "&entity=" + entity.getName();
//			makeEntity(dirPath + "/dao/" + name + "Dao.java", urlBase + path);
//			path = "DaoXml.do?package=" + pkg.getName() + "&entity=" + entity.getName();
//			makeEntity(dirPath + "/dao/" + name + "Dao.xml", urlBase + path);

			// for backup
			if(entity.hasBackup()){
				path = "EntityBak.do?package=" + pkg.getName() + "&entity=" + entity.getName();
				makeEntity(dirPath + "/entity/" + name + "Bak.java", urlBase + path);
			}
}
	}

	/**
	 * サービス、EntityManager
	 * @param pkg
	 * @param dirPath
	 */
	private void makeService(Package pkg, String dirPath) {
		// ディレクトリ作成
		String servicePath = (targetPath + SRC_PATH + pkg.getServiceSubPackagePath().replaceAll("\\.", "/"));
		String managerPath = dirPath + "/manager";
		if (pkg.entitySize() > 0) {
			makeDir(managerPath);
			makeDir(servicePath);
		}
		// serviceの場合
		for (int j = 0; j < pkg.entitySize(); j++) {
			Entity entity = pkg.getEntity(j);
			String path;
			String name = entity.getClassName();

			// サービスの時はdomainをserviceにする
			path = "Service.do?package=" + pkg.getName() + "&entity=" + entity.getName();
			makeEntity(servicePath + "/" + name + "Service.java", urlBase + path);

			// EntityManager
			path = "EntityManager.do?package=" + pkg.getName() + "&entity=" + entity.getName();
			makeEntity(managerPath +"/" + name + "Manager.java", urlBase + path);
		}
	}

	/**
	 * アクション生成
	 *
	 * @param pkg
	 */
	private void makeAction(Package pkg) {
		// jspの場合
		for (int j = 0; j < pkg.entitySize(); j++) {
			Entity entity = pkg.getEntity(j);
			// presentation パス
			String path, filename;
			for (String role : entity.getUserList()) {
				String dirPath = targetPath + SRC_PATH + pkg.getActionPath(role).replaceAll("\\.", "/");
				makeDir(dirPath);
				// Action取得
				path = "Action.do?package=" + pkg.getName() + "&entity=" + entity.getName() + "&role=" + role;
				filename = dirPath + "/" + entity.getClassName() + "Action.java";
				logger.debug(filename + "\n" + urlBase + path);
				makeEntity(filename, urlBase + path);
			}
		}
	}

	private static List<String> methodList;
	static {
		methodList = new ArrayList<String>();
		methodList.add("list");
		methodList.add("read");
		methodList.add("createInput");
		methodList.add("createConfirm");
		methodList.add("createResult");
		methodList.add("updateInput");
		methodList.add("updateConfirm");
		methodList.add("updateResult");
		methodList.add("logicalDeleteConfirm");
		methodList.add("logicalDeleteResult");
		methodList.add("deleteConfirm");
		methodList.add("deleteResult");
		methodList.add("bakList");
		methodList.add("bakRead");
	}

	private void makeJsp(Package pkg) {
		// jspの場合
		for (int j = 0; j < pkg.entitySize(); j++) {
			Entity entity = pkg.getEntity(j);
			// presentation パス
			String path, filename;
			for (String role : entity.getUserList()) {
				String dirPath = targetPath + SRC_PATH + pkg.getJspPath(role).replaceAll("\\.", "/");
				makeDir(dirPath);
				String filenameBase = entity.getName();
				// 一覧
				String pathBase = "jsp.do?package=" + pkg.getName() + "&entity=" + entity.getName() + "&role=" + role;
				for (String method : methodList) {
					if(entity.isSkipConfirm() && method.endsWith("Confirm")) continue;
					if(entity.isSkipResult() && method.endsWith("Result")) continue;
					if(!entity.hasBackup() && method.startsWith("bak")) continue;
					path = pathBase + "&method=" + method;
					filename = dirPath + "/" + filenameBase + "-" + method + ".jsp";
					logger.debug(filename + "\n" + urlBase + path);
					makeEntity(filename, urlBase + path);
				}
			}
		}
	}

	/**
	 * ディレクトリ作成
	 * @param dirPath
	 * @return
	 */
	private String makeDir(String dirPath) {
		buf.append("mkdir:" + dirPath + "<br>");
		File file = new File(dirPath);
		if (!file.mkdirs()) {
			// すでに作成済みの場合でもfalse
			logger.debug(file.getPath() + " create false");
		}
		return dirPath;
	}

	/**
	 * Entity(dao,entity)の作成
	 *
	 * @param pkg
	 * @param filetype
	 * @param urlBase
	 * @param dirPath
	 */
	private void makeEntity(String filename, String uri) {
		buf.append("makeEntity:" + filename + "<br>");

		// ファイル存在チェック
		if (!isOverride && new File(filename).exists()) {
			buf.append("ファイルが存在するため作成しない<br>");
			return;
		}

		URL url;
		try {
			url = new URL(uri);
			logger.info(url.getContent().toString());
			BufferedInputStream is = new BufferedInputStream(url.openStream());

			// 1000バイトずつ読み込んでみる
			int length = 1000;
			byte[] predata = new byte[0];
			byte[] data = null;
			byte[] buf = new byte[length];
			// StringBuffer sbuf = new StringBuffer();
			while (true) {
				int readLength = is.read(buf);
				if (readLength < 0) {
					break;
				}
				// log.debug(new String(buf, "JISAutoDetect"));
				data = new byte[predata.length + readLength];
				System.arraycopy(predata, 0, data, 0, predata.length);
				System.arraycopy(buf, 0, data, predata.length, readLength);
				predata = new byte[data.length];
				System.arraycopy(data, 0, predata, 0, data.length);

			}
			String sourceCode = new String(data, "UTF-8");
			sourceCode = StringEscapeUtils.unescapeHtml4(sourceCode);
			int pre = sourceCode.indexOf("<pre>") + 7;
			int presla = sourceCode.indexOf("</pre>");
			sourceCode = sourceCode.substring(pre, presla);
			write(filename, sourceCode);
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	/**
	 * SQL作成
	 * @param pkg
	 */
	private void makeSql(Package pkg) {
		StringBuilder builder = new StringBuilder();
		for (int j = 0; j < pkg.entitySize(); j++) {
			Entity entity = pkg.getEntity(j);
			String path = "sql.do?package=" + pkg.getName() + "&entity=" + entity.getName() +"&createOnly=true";
			builder.append(getSql(urlBase + path));//.append("\n");
		}
		String filename = targetPath + "/sql/"+ pkg.getSubPackageName() +".sql";

		write(filename, builder.toString());
	}

	/**
	 * Entity(dao,entity)の作成
	 *
	 * @param pkg
	 * @param filetype
	 * @param urlBase
	 * @param dirPath
	 */
	private String getSql(String uri) {
		buf.append("getSql:" + uri + "<br>");

		URL url;
		StringWriter out = null;
		try {
			url = new URL(uri);
			logger.info(url.getContent().toString());
			BufferedInputStream is = new BufferedInputStream(url.openStream());

			out = new StringWriter();
			// 1000バイトずつ読み込んでみる
			int length = 1000;
			byte[] predata = new byte[0];
			byte[] data = null;
			byte[] buf = new byte[length];
			// StringBuffer sbuf = new StringBuffer();
			while (true) {
				int readLength = is.read(buf);
				if (readLength < 0) {
					break;
				}
				// log.debug(new String(buf, "JISAutoDetect"));
				data = new byte[predata.length + readLength];
				System.arraycopy(predata, 0, data, 0, predata.length);
				System.arraycopy(buf, 0, data, predata.length, readLength);
				predata = new byte[data.length];
				System.arraycopy(data, 0, predata, 0, data.length);

			}
			String sourceCode = new String(data, "UTF-8");
			sourceCode = StringEscapeUtils.unescapeHtml4(sourceCode);
			int pre = sourceCode.indexOf("<pre>") + 7;
			int presla = sourceCode.indexOf("</pre>");
			sourceCode = sourceCode.substring(pre, presla);
			out.write(new String(sourceCode));
			out.close();
			// url.get
		} catch (Exception e) {
			logger.error("",e);
		}

		return out.toString();
	}
	/**
	 * ログ出力先の指定
	 **/
	private static final boolean APPEND_FILE=false;
	private static final boolean AUTO_FLUSH=true;
	private void write(String filename, String body) {
		PrintWriter out = null;
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(filename, APPEND_FILE));
			out = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), AUTO_FLUSH);
			out.print(body);
			out.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Can't open logfile.[" + filename + "] log is set to [System.out]");
			System.err.println(e.getMessage());
			out = new PrintWriter(System.out);
			out.print(body);
		} catch (UnsupportedEncodingException e) {
			System.err.print(e.getMessage());
		}finally{
			try { if(os!=null) os.close(); }catch(Exception e) {}
			if(out!=null) out.close();
		}
	}

	public StringBuffer getBuf() {
		return buf;
	}

	public String getTarget() {
		return targetPath;
	}

	public String getUrlBase() {
		return urlBase;
	}

	public boolean isOverride() {
		return isOverride;
	}

	public void setOverride(boolean isOverride) {
		this.isOverride = isOverride;
	}
}
