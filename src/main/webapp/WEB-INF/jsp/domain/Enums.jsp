<!-- $Id: Enums.jsp 604 2012-12-11 10:40:02Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@page import="com.lavans.lacoder2.generator.writer.EnumWriter"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp" %>
<%@page import="java.util.*"%>
<%@ page import="java.text.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.Package" %>
<%@ page import="com.lavans.lacoder2.generator.model.*" %>
<%@page import="com.lavans.lacoder2.lang.StringUtils"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Package pkg = (Package)request.getAttribute("lacoder.package");
	EnumClass enumClass = (EnumClass)request.getAttribute("lacoder.enum");
	String className = enumClass.getClassName();
	EnumWriter writer = new EnumWriter(enumClass);

	List<String> fieldList = enumClass.getFieldList();
	List<String> booleanList = enumClass.getBooleanList();
	List<String> intList = enumClass.getBooleanList();%>
<html lang="ja">
<head>
<title>ソース</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
/* $Id: Enums.jsp 604 2012-12-11 10:40:02Z dobashi $
 *
 * Created by hsbi-const-generator on <%= sdf.format(new Date()) %>.
 * © SBI SECURITIES Co., Ltd. ALL Rights Reserved.
 */
package <%= pkg.getName() %>;

<%--import java.io.Serializable;
 --%>import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
<%--import com.lavans.lacoder2.util.IEnum; --%>
/**
 * <%= enumClass.getTitle() %>Enum。
 * @author
 */
public enum <%= className %> {
<%= writer.writeMembers() %>

	/** Enum ID */
	public static final String _ID	= "<%= enumClass.getName() %>";

<%= writer.writeMaps() %>

	/**
	 * 初期化
	 * <%-- 最終的にはタイトルはResourceBundleから取得？
	 * 動的に言語を切り替えられるようにするか? --%>
	 */
	static{
<%= writer.writeStatics() %>
	}

	/**
	 * IDから表示名を取得。
	 * @param id
	 * @return
	 */
	public static String getTitle(String name){
		return getTitle(name, "");
	}
	public static String getTitle(String name, String defaultStr){
		if(valueOf(name)!=null){
			valueOf(name).getTitle();
		}
		return defaultStr;
	}

<%
	for(int i=0; i<fieldList.size(); i++){
		String fieldName = (String)enumClass.getFieldList().get(i);
		if(fieldName.equals("int")){
%>	/**
	 * <%= fieldName %>からインスタンス取得。
	 * @param id
	 * @return
	 */
	public static <%= className %> valueOfInt(int id){
		return (<%= className %>)<%= fieldName %>Map.get(new Integer(id));
	}
<%		}else{
%>
	/**
	 * <%= fieldName %>からインスタンス取得。
	 * @param id
	 * @return
	 */
	public static <%= className %> valueOf<%= StringUtils.capitalize(fieldName) %>(String id){
		return (<%= className %>)<%= fieldName %>Map.get(id);
	}
<%		}
	}
%>
	/**
	 * デフォルトのインスタンスを返す。
	 * @param id
	 * @return
	 */
	public static <%= className %> getDefault(){
<%
	if(enumClass.getDefaultMember()!=null){
%>		return <%= enumClass.getDefaultMember().getConstName() %>;
<%	}else{
%>		return null;
<%	}
%>	}
	/**
	 * デフォルトのnameを返す。
	 * @param id
	 * @return
	 */
	public static String getDefaultName(){
<%
	if(enumClass.getDefaultMember()!=null){
%>		return <%= enumClass.getDefaultMember().getConstName() %>.name();
<%	}else{
%>		return "";
<%	}
%>	}

<%	for(int i=0; i<fieldList.size(); i++){
		String fieldName = (String)enumClass.getFieldList().get(i);
%>	/**
	 * <%= fieldName %>インスタンス一覧を返す。
	 */
	public static <%= className %>[] values<%= StringUtils.capitalize(fieldName) %>(){
		return (<%= className %>[])<%= fieldName %>List.toArray();
	}
<%	}	%>
	private String title = null;
<%= writer.writeInstanceVars() %>

	/**
	 * コンストラクタ。
	 */
	private <%= className %>(
		String title
<%= writer.writeConstructorArgs() %>
	){
		this.title = title;
<%= writer.writeConstructor() %>
	}
	public String getTitle(){
		return title;
	}
<%= writer.writeGetters() %>
<%--
	/**
	 * 同じenumかを比較するメソッド。
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(this==obj) return true;
		if(obj instanceof <%= className %>){
			if(id.equals(((<%= className %>)obj).getId())){
				return true;
			}
		}
		return false;
	}
--%>
	@Override
	public String toString(){
		return "<%= className %>:"+name()+":"+getTitle();
	}
}
</pre>
<!--
<%= debugStr %>
-->
</body>

<%--	//public static final String <%= member.getConstName() %>	= "<%= member.getName() %>"; --%>