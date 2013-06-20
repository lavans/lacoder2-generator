<%@tag import="com.lavans.lacoder2.lang.ObjectUtils"%>
<%@tag import="com.lavans.lacoder2.lang.StringUtils"%>
<%@tag import="java.util.Collection"
%><%@tag pageEncoding="UTF-8"
%><%@tag import="java.lang.reflect.Method"
%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@ attribute name="name"	required="true" rtexprvalue="true"
%><%@ attribute name="items"	required="true" rtexprvalue="true" type="java.lang.Object"
%><%@ attribute name="attr"	required="false" rtexprvalue="true"
%><%@ attribute name="title"	required="false" rtexprvalue="true"
%><%@ attribute name="value"	required="false" rtexprvalue="true"
%><%@ attribute name="onchange" required="false" rtexprvalue="true"
%><%@ attribute name="firstItemTitle" required="false" rtexprvalue="true"
%><%@ attribute name="firstItemValue" required="false" rtexprvalue="true"
%><%@include file="utils.tag"
%><%
//System.out.println(items);
if(value==null){
	value="";
}
if(items==null){
	throw new NullPointerException("'items' must be set.");
}
String onchangeStr="";
if(!StringUtils.isEmpty(onchange)){
	onchangeStr="onchange='"+ onchange +"'";
}
%>
<select name="${name}"  <%= onchangeStr %>>
<% // 選択肢の一つめに「すべて」「指定無し」などを置く場合
if(!StringUtils.isEmpty(firstItemTitle)){
%>  <option value="<%= ObjectUtils.toString(firstItemValue) %>" ><%= firstItemTitle %></option>
<%
}

// Collection/Arrasy both available
Object[] itemsArray = null;
if(items instanceof Collection){
	itemsArray = ((Collection<?>)items).toArray();
}else{
	itemsArray = (Object[])items;
}

for(Object item: itemsArray){
	String itemValue = getValue(item, attr);
	String itemTitle;
	if(StringUtils.isEmpty(title)){
		itemTitle=itemValue;
	}else{
		itemTitle=getValue(item, title);
	}

	String selected = value.equals(itemValue)?" selected":"";
%>  <option value="<%= itemValue %>" <%= selected %>><%= itemTitle %></option>
<%
}
%>
</select>