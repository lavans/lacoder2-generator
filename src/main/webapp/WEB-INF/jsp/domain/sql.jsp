<!-- $Id: sql.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.Attribute" %>
<%@ page import="com.lavans.lacoder2.generator.db.*" %>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	DecimalFormat df = new DecimalFormat("00");
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
	Attribute attrId = entity.get(0);
	TypeManager typeMan = entity.getTypeManager();
	String tableName = entity.getTableName();
	boolean isCreateOnly =false;
	if(!StringUtils.isEmpty(request.getParameter("createOnly"))){
//		logger.info("■□□■"+ request.getParameter("createOnly"));
		isCreateOnly = Boolean.parseBoolean(request.getParameter("createOnly"));
	}
//	logger.info("■■■■"+ request.getParameter("createOnly"));
%>

<%@page import="com.lavans.lacoder2.lang.StringUtils"%><html lang="ja">
<head>
<title>DDL</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
<% if(!isCreateOnly){ %>DROP TABLE <%= tableName %>;<% }
%>
CREATE TABLE <%= tableName %> (
<%
	logger.info("CREATE TABLE");
	for(int i=0; i<entity.size(); i++){
		Attribute attr = entity.get(i);
%>	<%= attr.getDbName() +"	"+ attr.getDbType().toUpperCase() %>,	-- <%= attr.getTitle() %>
<%	}
%>	CONSTRAINT <%= tableName %>_pk PRIMARY KEY(
		<%= StringUtils.toUnderscore(entity.getPrimaryKey()) %>
    ));

<%
	logger.info("CREATE SEQUENCE");
	for(int i=0; i<entity.size(); i++){
		Attribute attr = entity.get(i);
		if(attr.isSequence()){
%><%= typeMan.getSequenceSql(entity) %>
<%			break;
		}
	}

	if(entity.hasBackup()){
%>
<% if(!isCreateOnly){ %>DROP TABLE <%= tableName %>_BAK;<% }
%>
CREATE TABLE <%= tableName %>_BAK (
<%
	logger.info("CREATE BACKUP TABLE");
	for(int i=0; i<entity.size(); i++){
		Attribute attr = entity.get(i);
		// serialになるのを避けるため、javaTypeからdbTypeを取得
%>	<%= attr.getConstName() +"	"+ typeMan.getDbType(attr.getJavaType()).toUpperCase() %>,	-- <%= attr.getTitle() %>
<%	}
%>	BACKUP_DATETIME <%= typeMan.getDbType("Date").toUpperCase() %>, -- バックアップ日時
	CONSTRAINT <%= tableName %>_BAK_PKEY PRIMARY KEY(
		<%= StringUtils.toUnderscore(entity.getPrimaryKey()).toUpperCase() %>
    ));
<%= typeMan.getSequenceSql(tableName +"_BAK") %>

<%
	}
	if(!isCreateOnly){
		logger.info("INSERT");

	for(int j=1; j<31; j++){
%>INSERT INTO <%= tableName %> VALUES(<%
	//logger.info("INSERT");
	String officeName = entity.getName()+df.format(j);
	if(attrId.isSequence()){
		out.print(typeMan.getNextval(attrId));
	}else if(attrId.getJavaType().equals("int")){
//		out.print("1");
		out.print(""+j);
	}else{
		out.print("'"+officeName+"'");
	}

	for(int i=1; i<entity.size(); i++){
		Attribute attr = entity.get(i);
		if(attr.getName().equals("delete_flg")){
			out.print(", false");
		}else if(attr.isEnum()){
			out.print(", '"+attr.getName()+df.format(j)+"'");
		}else if(attr.getJavaType().equals("int")){
			out.print(", "+1);
//			out.print(", "+j);
		}else if(attr.getJavaType().equals("boolean")){
			out.print(", true");
		}else if(attr.getName().equals("pass")){
			out.print(", 'test'");
		}else if(attr.getName().equals("tel")){
			out.print(", '03-1234-5678'");
		}else if(attr.getName().equals("fax")){
			out.print(", '03-2345-6789'");
		}else if(attr.getName().equals("zip")){
			out.print(", '123-4567'");
		}else if(attr.getName().equals("url")){
			out.print(", 'http://www."+ officeName +".com'");
		}else if(attr.getName().contains("mail")){
			out.print(", 'mail"+ df.format(j)+"@"+officeName+".com'");
		}else if(attr.getName().equals("insert_date")){
			out.print(", "+ typeMan.getCurrentTime());
		}else if(attr.getName().equals("insert_user")){
			out.print(", 'SYSTEM'");
		}else if(attr.getName().equals("update_date")){
			out.print(", null");
		}else if(attr.getName().equals("update_user")){
			out.print(", null");
		}else if(attr.getJavaType().equals("Date")){
			out.print(", "+ typeMan.getCurrentTime());
		}else{
			out.print(", '"+attr.getTitle()+df.format(j)+"'");
		}
	}
%>);
<%		if(typeMan instanceof OracleDialect) { %>/<%	} %>
<%	} // for()
	} // if(!isCreateOnly)%>
</pre>
<!--
<%= debugStr %>
-->
</body>

