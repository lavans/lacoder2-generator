<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%
// @see http://java.sun.com/developer/technicalArticles/Servlets/servletapi2.3/
Throwable e = (Throwable)request.getAttribute("exception");
// drop ServletException wrapping
if(e.getCause()!=null){
	e = e.getCause();
}
StringWriter sw = new StringWriter();
PrintWriter st = new PrintWriter(sw);
e.printStackTrace(st);
String stackTrace = sw.getBuffer().toString().replace("\n","<br>").replace("\t","&nbsp;&nbsp;&nbsp;&nbsp;");
%>
<html>
<jsp:include page="./head.jsp" />
<body>
<div id="header">
  <h1>予期せぬエラー</h1>
</div>
<div id="contents">
${message}
<br>
<%= stackTrace %>
<br>
</div>

<jsp:include page="./footer.jsp" />

</body>
</html>
