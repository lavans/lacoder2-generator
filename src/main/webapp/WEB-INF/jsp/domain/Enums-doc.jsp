<!-- $Id: Enums.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@page import="java.lang.reflect.Method"%>
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
	List<String> valueList = enumClass.getFieldList();%>
<html lang="ja">
<head>
<title>ソース</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>

<style type="text/css">
.title { background-color: #6699cc; color white; }
<!--
/*
body { background-color: #EEEEEE }
table { border: 1px solid #000000; width: 800px; border-collapse: collapse;}
th { background-color: #006699; color: white; }
td  { border: 1px solid #000000; }
*/
-->
</style>

</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<table class="table-striped table-bordered" style="width: 800px;">
	<tr class="info"> 
	  <th>名称</th><td><%= enumClass.getTitle() %></td>
	  <th>作成日</th><td><%= sdf.format(new Date()) %></td>
	</tr>
	<tr><th>パッケージ</th><td colspan="3"><%= pkg.getServiceSubPackagePath() %></td></tr>
	<tr><th>説明</th><td colspan="3"><%= enumClass.getComment() %></td></tr>
</table>
<br>
<table class="table-striped table-bordered" style="width: 800px;">
	<tr>
		<th>定義名</th>
		<th>タイトル</th>
	<% for(String valueTitle: enumClass.getTitleList()){ %>
		<th><%= valueTitle %></th>
	<% } %>
		<th>備考</th>
	</tr>
	<% for(EnumMember member: enumClass.getMemberList()){ %>
		<tr>
			<td><%= member.getName() %></td><td><%= member.getTitle() %></td>
			<% for(String key: enumClass.getFieldList()){ %>
				<td><%= member.getValue(key) %></td>
			<% } %>
			<td><%= member.getComment() %></td>
		</tr>
	<% } %>
</table>

</body>
</html>
