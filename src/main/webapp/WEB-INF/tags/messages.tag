<%@tag import="com.lavans.lacoder2.lang.StringUtils"%>
<%@tag pageEncoding="UTF-8"%>
<%@attribute name="value" required="true" rtexprvalue="true" type="java.util.Collection" %>
<% if(value==null) return; %>
<div class="messages" style="color: #003399; margin: 5px;">
<%= StringUtils.join(value, "<br>") %>
</div>
