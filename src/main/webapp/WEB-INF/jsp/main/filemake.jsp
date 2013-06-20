<!-- $Id: filemake.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp" %>
<%@ page import="com.lavans.lacoder2.generator.main.*" %>
<%
	FileMaker fileMaker = (FileMaker)request.getAttribute("lacoder.fileMaker");
%>
<html lang="ja">
<head>
<title>FileMakaer</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<table border="1">
<tr><td><%= fileMaker.getTarget() %></td></tr>
<tr><td><%= fileMaker.getUrlBase() %></td></tr>
<tr><td><%= fileMaker.getBuf().toString() %></td></tr>
<tr><td>
</td></tr>
</table>


<!--
<%= debugStr %>
-->
</body>
</html>

