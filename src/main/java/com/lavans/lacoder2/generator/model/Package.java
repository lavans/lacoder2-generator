/* $Id: Package.java 522 2012-09-24 17:24:41Z dobashi $
 * create: 2004/12/28
 * (c)2004 Lavans Networks Inc. All Rights Reserved.
 */
package com.lavans.lacoder2.generator.model;

import java.util.ArrayList;
import java.util.List;

import com.lavans.lacoder2.generator.main.Target;
import com.lavans.lacoder2.lang.StringUtils;

/**
 * @author dobashi
 * @version 1.00
 */
public class Package {
	private static final String ACTION  = "action";
	private static final String DOMAIN  = "domain";
	private static final String SERVICE = "service";
	private static List<Package> allList = new ArrayList<Package>();

	public static boolean addPackage(Package p) {
		return allList.add(p);
	}
	public static List<Package> getAllList(){
		return allList;
	}
	public static Package getPackage(String name){
		Package result = null;
		for(int i=0; i<allList.size(); i++){
			Package pkg = allList.get(i);
			if(pkg.getName().equals(name)){
				result=pkg;
				break;
			}
		}
		return result;
	}

	private String fqdn = null;
	private String project = null;
	private String subname=null;

	private List<Entity> entityList = new ArrayList<Entity>();
	private List<EnumClass> enumList= new ArrayList<EnumClass>();
	private List<Service> serviceList= new ArrayList<Service>();

	public Entity getEntity(String name){
		Entity result = null;
		for(int i=0; i<entityList.size(); i++){
			Entity table = entityList.get(i);
			if(table.getName().equals(name)){
				result=table;
				break;
			}
		}
		return result;
	}
	public EnumClass getEnum(String name){
		EnumClass result = null;
		for(int i=0; i<enumList.size(); i++){
			EnumClass enums = enumList.get(i);
			if(enums.getName().equals(name)){
				result=enums;
				break;
			}
		}
		return result;
	}
	public Service getService(String name){
		Service result = null;
		for(int i=0; i<serviceList.size(); i++){
			Service service = serviceList.get(i);
			if(service.getName().equals(name)){
				result=service;
				break;
			}
		}
		return result;
	}

	/**
	 *
	 * @return fullname(domain.project.package) を戻します。
	 * パッケージを特定するためのキーとしてのみ利用します。
	 * lacoderが生成するクラスではこの組み合わせ（projectの次にsubname)のjavaパッケージは存在しません。
	 *
	 */
	public String getName() {
		return fqdn+"."+project+"."+subname;
	}
	/**
	 * @return fullname(domain.project.package) を戻します。
	 */
	public String getDomainPath() {
		return fqdn+"."+project+"."+DOMAIN ;
	}

	/** com.lavans.lacoder2.generator.domain.account */
	public String getModelSubPackagePath() {
		return fqdn+"."+project+"."+DOMAIN +makeDotSubname(subname);
	}
	/** com.lavans.lacoder2.generator.service.account */
	public String getServiceSubPackagePath() {
		return fqdn+"."+project+"."+SERVICE +makeDotSubname(subname);
	}
	/** com.lavans.lacoder2.generator.presentation */
	public String getActionPath() {
		return fqdn+"."+project+"."+ACTION;
	}
	
	private String makeDotSubname(String value){
		return StringUtils.isEmpty(value)?"":"."+value;
	}
	private static final String ACTION_PATH="/root/web-app/action-path";
	private static final String JSP_PATH="/root/web-app/jsp-path";
	/**
	 * Actionのpathを取得します。Actionはroleの下にあるので引数でroleをとります。
	 * @return
	 */
	public String getActionPath(String role) {
		String actionPath=Target.getSelectedTarget().getTargetConfig().getNodeValue(ACTION_PATH);
		if(!StringUtils.isEmpty(actionPath)){ actionPath = "."+actionPath; }

		return getActionPath()+"."+ role +makeDotSubname(subname) + actionPath;
	}
	/**
	 * jspのpathを取得します。Actionはroleの下にあるので引数でroleをとります。
	 * @return
	 */
	public String getJspPath(String role) {
		String jspPath=Target.getSelectedTarget().getTargetConfig().getNodeValue(JSP_PATH);
		if(!StringUtils.isEmpty(jspPath)){ jspPath = "."+jspPath; }

		return jspPath+"."+ role + makeDotSubname(subname);
	}

	/**
	 * @return package名のみを戻します。
	 */
	public String getSubPackageName() {
		return subname;
	}

	/**
	 * @param name name を設定。
	 */
	public void setName(String fqdn, String project, String name) {
		this.subname = name;
		this.fqdn = fqdn;
		this.project = project;
	}
	/**
	 * @param arg0
	 * @return
	 */
	public boolean addEntity(Entity entity) {
		entity.setParentPackage(this);
		return entityList.add(entity);
	}
	/**
	 * @param arg0
	 * @return
	 */
	public Entity getEntity(int arg0) {
		return entityList.get(arg0);
	}
	public List<Entity> getEntityList() {
		return entityList;
	}
	/**
	 * @param arg0
	 * @return
	 */
	public boolean removeEntity(Object arg0) {
		return entityList.remove(arg0);
	}
	/**
	 * @return
	 */
	public int entitySize() {
		return entityList.size();
	}

	/**
	 * @param arg0
	 * @return
	 */
	public boolean addEnum(EnumClass arg0) {
		return enumList.add(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 */
	public EnumClass getEnum(int arg0) {
		return enumList.get(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 */
	public boolean removeEnum(Object arg0) {
		return enumList.remove(arg0);
	}
	/**
	 * @return
	 */
	public int enumSize() {
		return enumList.size();
	}

	public boolean addService(Service arg0) {
		return serviceList.add(arg0);
	}
	public Service getService(int arg0) {
		return serviceList.get(arg0);
	}
	public Service removeService(int arg0) {
		return serviceList.remove(arg0);
	}
	/**
	 * @return
	 */
	public int serviceSize() {
		return serviceList.size();
	}

	/**
	 * @return domain を戻します。
	 */
	public String getFqdn() {
		return fqdn;
	}
	/**
	 * @param domain domain を設定。
	 */
	public void setFqdn(String domain) {
		this.fqdn = domain;
	}
	/**
	 * @return project を戻します。
	 */
	public String getProject() {
		return project;
	}
	/**
	 * @param project project を設定。
	 */
	public void setProject(String project) {
		this.project = project;
	}
}
