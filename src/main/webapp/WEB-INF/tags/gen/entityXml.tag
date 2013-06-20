<%@tag import="com.lavans.lacoder2.lang.StringUtils"%>
<%@tag import="com.lavans.lacoder2.sql.dbutils.model.Column"%>
<%@tag pageEncoding="UTF-8"
%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@attribute name="table"	required="true" rtexprvalue="true" type="com.lavans.lacoder2.sql.dbutils.model.Table"
%>
<%!
private String makeDbName(String dbName, String attrName){
	// Const名に戻して元の名前と同じなら必要ない
	if(StringUtils.toUnderscore(attrName).toUpperCase().equals(dbName)){
		return "";
	}
	// 異なるときは元の名前をdb-nameとしてセット
	return "db-name=\""+ dbName +"\" ";
}
%>
<%
	if(table==null){
		return;
	}
%>
<pre>
&lt;entity name="<%= StringUtils.toUpperCamelCase(table.getName()) %>" title="" primary-key=""&gt;
<%	for(Column column: table.getColumnList()){
	String attrName = StringUtils.toCamelCase(column.getName());
	String dbName = makeDbName(column.getName(), attrName);
%>	&lt;attr name="<%= attrName %>" <%= dbName %>type="<%= column.getJavaType() %>" db-type="<%= column.getDbType() %>" title=""/&gt;
<% } 
%>&lt;/entity&gt;
</pre>

