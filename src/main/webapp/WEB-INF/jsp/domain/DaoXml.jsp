<!-- $Id: DaoXml.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
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
	$Id: DaoXml.jsp 508 2012-09-20 14:41:55Z dobashi $
	created: &lt;%= sdf.format(new Date()) %&gt;
 --&gt;
&lt;dao&gt;
	&lt;!-- sample --&gt;
	&lt;sql name=&quot;sample&quot;&gt;
		&lt;![CDATA[
INPUT YOUR SQL
		]]&gt;
	&lt;/sql&gt;
&lt;/dao&gt;
</pre>

<!-- <%= debugStr %> -->
</body>

