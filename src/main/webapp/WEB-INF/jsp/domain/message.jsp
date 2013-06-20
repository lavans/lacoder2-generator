<!-- $Id: message.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.Attribute" %>
<%!
	/**
	 * 基本方針
	 * とりあえずプロバイス用に手っ取り早く。
	 * 最終的にはjspをやめてファイルに直接書き出す。
	 * これをベースクラスとしてインクリメンタルな開発に耐えられるように。
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%>
<%
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
%>
<html lang="ja">
<head>
<title>service</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
	/** <%= entity.getClassName() %> */
<%
	for(int i=0; i<entity.size(); i++){
		Attribute attr = entity.get(i);
		if(!attr.isEditable()){
			continue;
		}
		if(attr.isSequence()){
			continue;
		}
		String def = "ERR_NO_"+attr.getConstName();
		String key = def.replaceAll("_", ".").toLowerCase();
%>	public static final String <%= def %>		= "<%= key %>";
<%	}
%>
# <%= entity.getClassName() %>
<%	for(int i=0; i<entity.size(); i++){
		Attribute attr = entity.get(i);
		if(!attr.isEditable()){
			continue;
		}
		if(attr.isSequence()){
			continue;
		}
		String def = "ERR_NO_"+attr.getConstName();
		String key = def.replaceAll("_", ".").toLowerCase();
%><%= key %>=<%= attr.getTitle() %>を入力してください。
<%	}
%>
</pre>
<!--
<%= debugStr %>
-->
</body>

