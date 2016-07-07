<%-- $Id: common.jsp 508 2012-09-20 14:41:55Z dobashi $
 --%><%--
  @ page import="com.lavans.util.browser.*" --%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%!
//	protected Logger logger = Logger.getLogger(this.getClass().getName());
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
%><%
//	Browser browser = (Browser)request.getAttribute("lavansutil.browser");
	String debugStr = (String)request.getAttribute("lavansutil.debugStr");
	if(debugStr==null){
	  debugStr="";
	}
%>
