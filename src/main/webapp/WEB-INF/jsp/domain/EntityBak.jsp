<!-- $Id: EntityBak.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp" %>
<%@page import="java.util.*"%>
<%@ page import="java.text.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.*" %>
<%@ page import="com.lavans.lacoder2.generator.model.Package" %>
<%!
	/**
	 * 基本方針
	 * validate()はBaseに移してxmlで指定したconstraintに応じて自動チェックするようにする。
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%>
<%
	Package pkg = (Package)request.getAttribute("lacoder.package");
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
	String className = entity.getClassName();
	ModelWriter writer = new ModelWriter(entity);
%>

<%@page import="com.lavans.lacoder2.generator.writer.ModelWriter"%>
<html lang="ja">
<head>
<title>ソース</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
/* $Id: EntityBak.jsp 508 2012-09-20 14:41:55Z dobashi $
 * 作成日: <%= sdf.format(new Date()) %>
 *
 */
package <%= pkg.getModelSubPackagePath() %>.entity;
<%--
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.io.Serializable;
 --%>
import <%= pkg.getModelSubPackagePath() %>.entity.<%= className %>;
import java.util.Date;
<%--
import <%= pkg.getName() %>.<%= pkg.getProject() %>.http.IUser;
import <%= pkg.getName() %>.<%= pkg.getProject() %>.http.Cp932;
import <%= pkg.getName() %>.<%= pkg.getProject() %>.common.UserManager;
--%>
/**
 * <%= entity.getTitle() %>クラス。
 *
 * @author ${user}
 */
public class <%= className %>Bak extends <%= className %>{
	/** serialID */
	private static final long serialVersionUID = 1L;

	/** backupda date */
	private Date backupDatetime = null;

	/**
	 * @return backupDatetime
	 */
	public Date getBackupDatetime() {
		return backupDatetime;
	}

	/**
	 * set backupDatetime
	 * @param backupDatetime
	 */
	public void setBackupDatetime(Date backupDatetime) {
		this.backupDatetime = backupDatetime;
	}

	/**
	 * 文字列表現。
	 * PKの各属性と名前を":"で連結して返す。
	 */
	@Override
	public String toString() {
		return <%= writer.writeStringMembers() %>;
	}
}
</pre>
<!--
<%= debugStr %>
-->
</body>

