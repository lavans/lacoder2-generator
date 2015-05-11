package com.lavans.lacoder2.generator.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.lavans.lacoder2.lang.StringUtils;
import com.lavans.lacoder2.util.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Target {
	/** logger */
	private static final Log logger = LogFactory.getLog(Target.class);
	/** generator config  */
	private static final String CONFIG_PATH	="/src/test/resources/";
	private static final String CONFIG_MAIN_PATH	="/src/main/resources/";
	private static final String GENERATOR_CONFIG_PATH = CONFIG_PATH + "lacoder2-generator/";
	private static final String TEMPLATE_CONFIG_PATH	= GENERATOR_CONFIG_PATH + "templates/";
	private static final String GENERATOR_CONFIG	="lacoder2-generator.xml";

	/** Target project info */
	private static Target instance = null;

	/** Select a target */
	public static Target select(String targetName, String realPath){
		logger.debug("targetName="+ targetName +",realPath="+realPath);

		instance = new Target();
		instance.name = targetName;
		// ファイル名指定の場合はターゲットとなるプロジェクトのlacoderフォルダを参照
		String pathes[] = realPath.split("\\"+File.separator);
		//String pathes[] = realPath.split("\\\\");
		logger.debug(StringUtils.join(pathes, " --- "));

		instance.path = StringUtils.join(Arrays.copyOf(pathes, pathes.length-6),"/")+"/"+targetName;
		return instance;
	}

	/**
	 *
	 * @return
	 */
	public static Target getSelectedTarget(){
		return instance;
	}

	/**
	 * De
	 */
	public static void deselect(){
		instance = null;
	}
	@Getter
	private String name;
	@Getter
	private String dao;
	@Getter
	private String path;

	public String getLacoderConfPath() {
		return path+GENERATOR_CONFIG_PATH;
	}


	/**
	 * テンプレートファイル置き場
	 * @return
	 */
	public String getTemplatePath() {
		return path + TEMPLATE_CONFIG_PATH;
	}

	/**
	 * ターゲットプロジェクトのメイン設定ファイルを返します。
	 * @param fileName
	 * @return
	 */
	public Config getTargetConfig() {
		Config config = null;
		try {
			config = Config.getInstance(path + CONFIG_PATH + Config.CONFIG_FILE, true);
		} catch (RuntimeException e) {
			config = Config.getInstance(path + CONFIG_MAIN_PATH + Config.CONFIG_FILE, true);
		}
		return config;
	}

	/**
	 * ジェネレーター用の設定ファイルを返します。
	 * @return
	 */
	public Config getGenerotorConfig() {
		return Config.getInstance(getLacoderConfPath()+GENERATOR_CONFIG, true);
	}

	/**
	 * Entity定義を記述した設定ファイルの読み込みクラスを返します。
	 *
	 * @param 指定したファイル名
	 */
	public Config getEntityConfig(String filename) {
		return Config.getInstance(getLacoderConfPath()+filename, true);
	}

	/**
	 * Generator用Enity定義設定ファイル名一覧を返す。
	 * @return
	 */
	public List<String> getConfList(){
		File dir = new File(getLacoderConfPath());
		logger.debug(dir.getAbsolutePath());
		File[] confFiles = dir.listFiles();
		List<String> list = new ArrayList<String>();
		for(File file: confFiles){
			if(file.getName().endsWith(".xml") && !file.getName().equals(GENERATOR_CONFIG)){
				list.add(file.getName());
			}
		}
		Collections.sort(list);
		return list;
	}
}

