<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="gen" tagdir="/WEB-INF/tags/gen"%>
<html>
<head>
<jsp:include page="../include/head.jsp" >
		<jsp:param name="title" value="DBUtils" />
</jsp:include>
<script src="${pageContext.request.contextPath}/js/search.js" ></script>

<script type="text/javascript">
// for filtering seach
var queryBoxName = 'query';
var listName = '#names';
var countName = '#count';
var noneName = '#noneName';

String.prototype.toCamel = function(){
	return this.replace(/(\_[a-z])/g, function($1){return $1.toUpperCase().replace('_','');});
};

function getTable(tableName){
	location.href="DBMain-info.json?tableName="+tableName;
}
/* 	$.get("DBMain-info.json?tableName="+tableName, function(table){
		$('#title').html(table.name);
		$('#body').html(JSON.stringify(table).toLowerCase());
	});
 */

</script>

</head>
<body>
<div id="wrapper">
<div class="container-fluid">
	<jsp:include page="../include/header.jsp">
		<jsp:param name="title" value="DBUtils" />
	</jsp:include>

	<div class="row-fluid">
		<div class="span4" style="background-color: #DDDDDD; -webkit-border-radius: 10px;">

			<!--  search box -->
			<p style="padding: 10px;">
				Search: <input type="text" name="query" value=""><br>
				<span id="count"></span>
			</p>
			<p id="none" style="display: none">No match.</p>
			<!--  search box -->

			<ul id="names" class="nav nav-list">
				<c:forEach var="tableName" items="${tableNames}">
					<li><a href="javascript:getTable('${tableName}')">${tableName}</a></li>
				</c:forEach>
			</ul>
		</div>
		<div class="span8" style="background-color: lightgray;-webkit-border-radius: 10px; padding: 10px;" >
			<div ><p id="title" class="lead">${table.name}</p></div>
			<div><p id="body" class="text-left">
				<gen:entityXml table="${table}" />
			</p></div>
		</div>
	</div>
</div>
</div>
<jsp:include page="../include/footer.jsp" />

</body>
</html>