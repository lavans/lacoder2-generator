<%-- $Id: common.jsp 508 2012-09-20 14:41:55Z dobashi $
 --%><%--
  @ page import="com.lavans.util.browser.*" --%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%!
//	protected Logger logger = Logger.getLogger(this.getClass().getName());
	protected Log logger = LogFactory.getLog(this.getClass());
%><%
//	Browser browser = (Browser)request.getAttribute("lavansutil.browser");
	String debugStr = (String)request.getAttribute("lavansutil.debugStr");
	if(debugStr==null){
	  debugStr="";
	}
%>
