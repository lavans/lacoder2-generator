<!DOCTYPE html>
<!-- $Id: selectTarget.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<%@ page contentType="text/html; charset=Windows-31J" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.*"%>
<%@ include file="../common/common.jsp" %>
<%@ page import="com.lavans.lacoder2.generator.model.Package" %>
<%
	List<Package> list =Package.getAllList();
	@SuppressWarnings("unchecked")
	List<String> targetList = (List<String>)request.getAttribute("targetList");
%>

<html lang="ja">
<head>
<jsp:include page="../include/head.jsp" >
		<jsp:param name="title" value="lacoder2-generator トップ" />
</jsp:include>
<LINK href="<%= request.getContextPath() %>/css/lacoder.css" rel="stylesheet" type="text/css">
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
</head>

<body>
<div class="container-fluid">
	<jsp:include page="../include/header.jsp" />
      <div class="hero-unit visible-desktop" style=";">
        <h1 style="color: #ffffff; text-shadow: 1px 3px 1px #000000;">lacoder2-generator</h1>
      </div>

	<div class="row-fluid">
		<div class="span4" style="background-color: #DDDDDD; -webkit-border-radius: 10px;">


			<ul id="names" class="nav nav-list">
				<li class="nav-header">Projects</li>
				<c:forEach var="target" items="${targetList}">
					<li><a href="${pageContext.request.contextPath}/main.do?target=${target}">${target}</a></li>
				</c:forEach>
			</ul>
		</div>
		<div class="span8" style="background-color: lightgray;-webkit-border-radius: 10px; padding: 10px;" >
			<div ><p id="title" class="lead">
				<a href="${pageContext.request.contextPath}/as/dbutils/DBMain.html">DBUtils</a><br>
			</p></div>
		</div>
	</div>
	<jsp:include page="../include/footer.jsp" />
</div>

<%= debugStr %>
</body>

</html>