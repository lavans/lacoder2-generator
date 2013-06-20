<%@tag import="org.slf4j.Logger"%><%@
tag import="org.slf4j.LoggerFactory"%><%@
tag import="com.lavans.lacoder2.lang.StringUtils"%><%@
tag import="com.lavans.lacoder2.lang.ArrayUtils"%><%@
tag import="java.lang.reflect.Method"%><%@
tag import="java.util.Arrays"%><%@
tag pageEncoding="UTF-8"%><%@
taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@
attribute name="name"	required="true" rtexprvalue="true"%><%@
attribute name="items"	required="true" rtexprvalue="true" type="java.util.List"%><%@
attribute name="attr"	required="false" rtexprvalue="true"%><%@
attribute name="title"	required="false" rtexprvalue="true"%><%@
attribute name="value"	required="false" rtexprvalue="true"%><%@
attribute name="firstItemTitle" required="false" rtexprvalue="true"%><%@
attribute name="firstItemValue" required="false" rtexprvalue="true"%><%@
attribute name="br"	required="false" rtexprvalue="true" type="java.lang.Boolean"%><%@
include file="utils.tag" %>
<%
//value
if(value==null) value="";
String values[] = value.split(",");
//items
if(items==null){
	Logger logger = LoggerFactory.getLogger(this.getClass());
	logger.error("radio.tag items==null.");
}
// br
if(br==null) br=false;
String brStr = br?"<br>":"";

// 選択肢の一つめに「すべて」「指定無し」などを置く場合
if(!StringUtils.isEmpty(firstItemTitle)){
	String checked = "";
	if(!StringUtils.isEmpty(value)){
		checked = value.equals(firstItemValue)?" checked":"";
	}
	String firstId = name +"."+ firstItemValue;
%> <input type="radio" name="${name}" value="<%= firstItemValue %>" id="<%= firstId %>"<%= checked %>><label for="<%= firstId %>"><%= firstItemTitle %></label><%
}
for(Object item: items){
	String itemValue = getValue(item, attr);
	String itemTitle;
	if(StringUtils.isEmpty(title)){
		itemTitle=itemValue;
	}else{
		itemTitle=getValue(item, title);
	}
	String checked = ArrayUtils.contains(values, itemValue)?" checked":"";
	String id = name + "." + itemValue;
%>
<input type="radio" name="${name}" value="<%= itemValue %>" id="<%= id %>"<%= checked %>><label for="<%= id %>"><%= itemTitle %></label><%= brStr %><%
}
%>
