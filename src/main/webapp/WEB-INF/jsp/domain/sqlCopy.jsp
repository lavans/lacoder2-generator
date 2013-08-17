<!-- $Id: sqlCopy.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@page import="com.lavans.lacoder2.lang.StringUtils"%>
<%@ page contentType="text/html; charset=windows-31j" %>
<%@ include file="../common/common.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.Attribute" %>
<%@ page import="com.lavans.lacoder2.generator.db.*" %>
<%
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
	Attribute attrId = entity.get(0);
	TypeManager typeMan = entity.getTypeManager();
	String tableName = entity.getTableName();
	StringBuffer buf = new StringBuffer();

	for(int i=0; i<entity.size(); i++){
		String str = StringUtils.toSnakeCase(entity.get(i).getName());
		if(!str.equals("")){
			buf.append(", "+str);
		}
	}
	String allCols = buf.substring(2);

%>
<html lang="ja">
<head>
<title>DDL</title>
<meta http-equiv="Content-Type" content="text/html; charset=windows-31j">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
-- copy
CREATE TABLE <%= tableName %>2 as SELECT * FROM <%= tableName %>;

-- create
DROP TABLE <%= tableName %>;

CREATE TABLE <%= tableName %> (
<%
	logger.info("CREATE TABLE");
	for(int i=0; i<entity.size(); i++){
		Attribute attr = entity.get(i);
%>	<%= attr.getDbName() +"	"+ attr.getDbType().toUpperCase() %>,	-- <%= attr.getTitle() %>
<%	}
%>	CONSTRAINT <%= tableName %>_PKEY PRIMARY KEY(
		<%= StringUtils.toUnderscore(entity.getPrimaryKey()).toUpperCase() %>
    ));
<%
	logger.info("CREATE SEQUENCE");
	for(int i=0; i<entity.size(); i++){
		Attribute attr = entity.get(i);
		if(attr.isSequence()){
%><%= typeMan.getSequenceSql(entity) %>
<%		}
	}
%>
--insert
INSERT INTO  <%= tableName %> (
  SELECT <%= allCols %>
  FROM <%= tableName %>2
);

DROP TABLE <%= tableName %>2;

<% if(attrId.isSequence()){ %>
<%= typeMan.getSequenceUpdateSql(attrId) %>
<% } %>

</pre>
<!--
<%= debugStr %>
-->
</body>

