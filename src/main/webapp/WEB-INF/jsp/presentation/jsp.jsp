<!-- $Id: jsp.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.Entity" %>
<%@ page import="com.lavans.lacoder2.generator.model.Attribute" %>
<%@page import="com.lavans.lacoder2.generator.model.Package"%>
<%@page import="com.lavans.lacoder2.generator.model.Role"%>
<%@page import="com.lavans.lacoder2.generator.writer.JspTemplateEngine"%><html>
<%
/*
	Package pkg = (Package)request.getAttribute("lacoder.package");
	String className = entity.getClassName();
	Attribute attrId = entity.get(0);
*/
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
	Role role = (Role)request.getAttribute("lacoder.role");
JspTemplateEngine writer = new JspTemplateEngine(entity, role);
String method = (String)request.getAttribute("lacoder.method");
%>
<body>
<pre>
<%=writer.writeJsp(method) %>
</pre>
<!--
<%= debugStr %>
-->
</body>
</html>

