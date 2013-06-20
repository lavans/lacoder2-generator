<!-- $Id: error.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=Windows-31J" pageEncoding="UTF-8"%>
<%@ include file="../common/common.jsp" %>

<%
	// Model Classes
	Exception e = (Exception)request.getAttribute("luz.exception");
	e.printStackTrace();
	String url = (String)request.getAttribute("luz.login_url");
	if(url==null){
		url = "main";
	}
%>
<html lang="ja">
<head>
<title>error</title>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS">
</head>
<body bgcolor="#CCCCFF" text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<%
    if(e!=null){
%>
<br><br>
<table>
  <tr><td>error</td>
  <tr><td><%= e.getMessage() %></td>
</table>
<%
	}

%>
<a href="javascript:history.back()">戻る</a><br>
<a href="<%= url %>" target="_top">top</a>
<%= debugStr %>
</body>
<%
	logger.error("err", e);
%>
