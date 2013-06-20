<%@tag import="com.lavans.lacoder2.util.ParameterUtils"
%><%@tag pageEncoding="UTF-8"
%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@attribute name="value"	required="true" rtexprvalue="true" type="com.lavans.lacoder2.util.Parameterizable"
%><%@attribute name="prefix"	required="false" rtexprvalue="true"
%><% if(prefix==null) prefix="";
%><%= ParameterUtils.toStoreString(value, prefix) %>