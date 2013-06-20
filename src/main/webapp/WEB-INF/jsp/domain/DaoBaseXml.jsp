<!-- $Id: DaoBaseXml.jsp 524 2012-09-25 11:38:08Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp"
%><%@ page import="java.util.*"
%><%@ page import="java.text.*"
%><%@ page import="com.lavans.lacoder2.generator.*"
%><%@ page import="com.lavans.lacoder2.generator.model.Package"
%><%@ page import="com.lavans.lacoder2.generator.model.Attribute"
%><%@ page import="com.lavans.lacoder2.generator.model.Entity"
%><%@ page import="com.lavans.lacoder2.generator.db.*"
%><%@ page import="com.lavans.lacoder2.generator.main.*"
%><%@ page import="com.lavans.lacoder2.lang.StringUtils"
%><%@page import="com.lavans.lacoder2.generator.writer.DaoXmlWriter"%>
<%!
	/**
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	String escapeBackSlash="\\\\";
%><%
Package pkg = (Package)request.getAttribute("lacoder.package");
Entity entity = (Entity)request.getAttribute("lacoder.entity");
DaoXmlWriter writer = new DaoXmlWriter(entity);
String className = entity.getClassName();
%>

<%@page import="com.sun.java.swing.plaf.windows.resources.windows"%><html lang="ja">
<head>
<title>ソース</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
&lt;?xml version="1.0" encoding="UTF-8" ?&gt;
&lt;!DOCTYPE well-formed&gt;
&lt;!--
	This source code is generated by lacoder.
	DO NOT EDIT.<%--
	@see http://www.lavans.com/soft/lacoder/
--%>
 --&gt;
&lt;dao&gt;
	&lt;!-- select 1 record --&gt;
	&lt;sql name=&quot;load"&gt;
		&lt;![CDATA[
<%= writer.writeSelectSql() %>
		]]&gt;
	&lt;/sql&gt;

<%	// ID(プライマリキー)がsequenceの時
	if(entity.get(0).isSequence()){
%>	&lt;sql name=&quot;nextval&quot;&gt;
		&lt;![CDATA[
<%= writer.writeNextValSql() %>
		]]&gt;
	&lt;/sql&gt;

<%
	}
%>	&lt;sql name=&quot;insert&quot;&gt;
		&lt;![CDATA[
<%= writer.writeInsertSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;update&quot;&gt;
		&lt;![CDATA[
<%= writer.writeUpdateSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;delete&quot;&gt;
		&lt;![CDATA[
<%= writer.writeDeleteSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;deleteAny&quot;&gt;
		&lt;![CDATA[
<%= writer.writeDeleteAnySql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;list&quot;&gt;
		&lt;![CDATA[
<%= writer.writeListSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;listPk&quot;&gt;
		&lt;![CDATA[
<%= writer.writeListPkSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;count&quot;&gt;
		&lt;![CDATA[
<%= writer.writeListCountSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;pager&quot;&gt;
		&lt;![CDATA[
<%= writer.writeListPagerSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;pagerPk&quot;&gt;
		&lt;![CDATA[
<%= writer.writeListPagerPkSql() %>
		]]&gt;
	&lt;/sql&gt;

<%	if(entity.hasBackup()){ %>
	&lt;sql name=&quot;backup&quot;&gt;
		&lt;![CDATA[
<%= writer.writeBackupSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;restore&quot;&gt;
		&lt;![CDATA[
<%= writer.writeRestoreSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;loadBak"&gt;
		&lt;![CDATA[
<%= writer.writeSelectBakSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;deleteBak&quot;&gt;
		&lt;![CDATA[
<%= writer.writeDeleteBakSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;deleteAnyBak&quot;&gt;
		&lt;![CDATA[
<%= writer.writeDeleteAnyBakSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;listBak&quot;&gt;
		&lt;![CDATA[
<%= writer.writeListBakSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;countBak&quot;&gt;
		&lt;![CDATA[
<%= writer.writeCountBakSql() %>
		]]&gt;
	&lt;/sql&gt;

	&lt;sql name=&quot;pagerBak&quot;&gt;
		&lt;![CDATA[
<%= writer.writePagerBakSql() %>
		]]&gt;
	&lt;/sql&gt;

<% } %>
&lt;/dao&gt;
</pre>

<!-- <%= debugStr %> -->
</body>

