<!-- $Id: main.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*"%>
<%@ include file="../common/common.jsp" %>
<%@ page import="com.lavans.lacoder2.generator.model.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.Package" %>
<%@ page import="com.lavans.lacoder2.generator.action.*" %>
<%@ page import="com.lavans.lacoder2.generator.main.*" %>
<%
	List<Package> list =Package.getAllList();
	@SuppressWarnings("unchecked")
	List<String> fileList = (List<String>)request.getAttribute("fileList");
%>

<html lang="ja">
<head>
<title>オブジェクト一覧</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="<%= request.getContextPath() %>/css/lacoder.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

function fileMake(url, isWarn){
//	if(isWarn){	alert('上書きロック中'); return false; }
	if(isWarn){
		if(!confirm('既存ファイルを上書きします')){
			return false;
		}
	}

	w = window.open(url,"make","status=no,toolbar=no,resizable=yes,scrollbars=yes,height=200,width=1000,top=0");
	w.focus();
}
</script>
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<table border="0" cellspacing='3'>
<tr valign='top'>
<td>
<%
	Target target = Target.getSelectedTarget();
	for(int i=0; i<fileList.size(); i++){
		String filename = fileList.get(i);
%>
<a href="<%= request.getContextPath() %>/main.do?<%= MainAction.CONFIG_FILE %>=<%= filename %>&target=<%= target.getName() %>"><%= filename %></a><br>
<%	}	%>
</td>

<td>
<table border="1">
  <tr class="title"><td colspan="8">一括作成</td></tr>
  <tr class="">
    <td colspan="8">
      <a href="javascript:fileMake('fileMake.do?type=base&override=true', false)">EntityBase</a>
      <a href="javascript:fileMake('fileMake.do?type=entity', false)">Entity</a>(<a href="javascript:fileMake('fileMake.do?type=entity&override=true', true)">上書</a>)
      <a href="javascript:fileMake('fileMake.do?type=enums', false)">Enums</a>(<a href="javascript:fileMake('fileMake.do?type=enums&override=true', false)">上書</a>)
      <a href="javascript:fileMake('fileMake.do?type=action', false)">Presentation</a>(<a href="javascript:fileMake('fileMake.do?type=action&override=true', true)">上書</a>)
      <a href="javascript:fileMake('fileMake.do?type=sql&override=true', false)">SQL</a>
  </td>
  </tr>
<%	for(int i=0; i<list.size(); i++){
		Package pkg = (Package)list.get(i);
%>
<tr class="title"><td colspan="5">
<%= pkg.getSubPackageName() %>
</td></tr>
<%
		for(int j=0; j<pkg.serviceSize(); j++){
			Service service = pkg.getService(j);
%>
<tr>
<td><%= service.getName() %></td>
<%--
<td><a href="main?cmd=<%=MainController.C_SERVICE_BASE %>&package=<%= pkg.getName() %>&service=<%= service.getName() %>">service_base</a></td>
<td><a href="main?cmd=<%=MainController.C_SERVICE_AP   %>&package=<%= pkg.getName() %>&service=<%= service.getName() %>">service_ap</a></td>
<td><a href="main?cmd=<%=MainController.C_SERVICE_WEB  %>&package=<%= pkg.getName() %>&service=<%= service.getName() %>">service_web</a></td>
<td><a href="main?cmd=<%=MainController.C_SERVICE_REMOTE%>&package=<%= pkg.getName() %>&service=<%= service.getName() %>">service_remote</a></td>
--%>
</tr>
<%
		}
		for(Entity entity: pkg.getEntityList()){
			// primary-keyが設定されていないEntityはエラー
			if(entity.getPrimaryKeyList().size()==0){ %>
<tr>
	<td rowspan="1" colspan="2" class="entity"><%= entity.getName() %></td>
	<td colspan="3"><div style="color:#CC0033;">primary-keyを設定してください。</div></td>
</tr>
<%
				continue;
			}
			String entityName = entity.getName();
			if(entityName.length()>25){
				entityName = entityName.substring(0,25)+"<br>"+entityName.substring(25);
			}
			String param = "package="+ pkg.getName() +"&entity="+entity.getName();
%>
<tr>
<td rowspan="1" colspan="2" class="entity"><%= entityName %></td>
  <td>
    <a href="Entity.do?<%= param %>">Entity</a><br>
    <a href="EntityBase.do?<%= param %>">EntityBase</a><br>
    <a href="EntityManager.do?<%= param %>">EntityManager</a><br>
    <a href="EntityBak.do?<%= param %>">EntityBak</a><br>
  </td>
  <td>
    <a href="Dao.do?<%= param %>">Dao</a><br>
    <a href="DaoXml.do?<%= param %>">DaoXml</a><br>
    <a href="DaoBaseXml.do?<%= param %>">DaoBaseXml</a><br>
  </td>
  <td>
    <a href="Service.do?<%= param %>">Service</a><br>
    <a href="sql.do?<%= param %>">SQL</a><br>
    <a href="sqlCopy.do?<%= param %>">SQL Copy</a><br>
  </td>
  <%--
  <td><a href="main?cmd=<%=MainController.C_MESSAGE   %>&<%= param %>">Message</a></td>
<!--   <td><a href="main?cmd=<%=MainController.C_DSERVER_XML %>&<%= param %>"><%= MainController.C_DSERVER_XML %></a></td>
-->
--%>
</tr>

<%-- presen start --%>
<tr><td rowspan="<%= entity.getUserList().size()+1 %>" class="jsp">presen</td></tr>
<%			for(String role: entity.getUserList()){
				String paramJsp = param+"&role="+role;
%>
<tr>
  <td><%= role %></td>
  <td>
    <a href="Action.do?<%= paramJsp %>">Action</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=list">一覧jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=read">詳細jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=bakList">BAK一覧jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=bakRead">BAK詳細jsp</a><br>
  </td>
  <td>
    <a href="jsp.do?<%= paramJsp %>&method=createInput">登録jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=createConfirm">登録確認jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=createResult">登録完了jsp</a><br>
  </td>
  <td>
    <a href="jsp.do?<%= paramJsp %>&method=updateInput">編集jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=updateConfirm">編集確認jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=updateResult">編集完了jsp</a><br>
  </td>
  <td>
    <a href="jsp.do?<%= paramJsp %>&method=deleteConfirm">削除確認jsp</a><br>
    <a href="jsp.do?<%= paramJsp %>&method=deleteResult">削除完了jsp</a><br>
  </td>
</tr>
<%			} %>
<%		}
		for(int j=0; j<pkg.enumSize(); j++){
			EnumClass enums = pkg.getEnum(j);
%>
<tr>
<td><%= enums.getName() %></td>
<td><a href="Enums.do?package=<%= pkg.getName() %>&enum=<%= enums.getName() %>">enum</a></td>
<%-- <td><a href="main?cmd=<%=MainController.C_ENUM_INT  %>&package=<%= pkg.getName() %>&enum=<%= enum.getName() %>">enum_int</a></td>
--%>
</tr>
<%
		}
	}
%>

</table>

</td>
</tr>
</table>
<br>
<a href="as/main/Top.html">プロジェクト選択</a><br>
<!-- -------------------------------------------------------------- -->
<%--
	for(int i=0; i<list.size(); i++){
		Package pkg = (Package)list.get(i);
		for(int j=0; j<pkg.entitySize(); j++){
			Entity entity = pkg.getEntity(j);
		String entityName = entity.getName();
		if(entityName.length()>25){
			entityName = entityName.substring(0,25)+"<br>"+entityName.substring(25);
		}
%>
	list.add("main?cmd=<%=MainController.C_APImodel    %>&<<%= param %>);
<%		}
	}
--%>


<%= debugStr %>
</body>

</html>