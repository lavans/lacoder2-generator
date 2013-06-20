<!-- $Id: Service.jsp 510 2012-09-20 14:58:49Z dobashi $ -->
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp" %>
<%@ page import="java.text.*" %>
<%@page import="com.lavans.lacoder2.generator.model.Package"%>
<%@ page import="com.lavans.lacoder2.generator.model.Entity" %>
<%!
	/**
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%>
<%
	Package pkg = (Package)request.getAttribute("lacoder.package");
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
	String className = entity.getClassName();
	String entityName = entity.getShortname();
%>
<head>
<title>service</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
/* $Id: Service.jsp 510 2012-09-20 14:58:49Z dobashi $
 * 作成日: <%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>
 *
 */
package <%= pkg.getServiceSubPackagePath() %>;

import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;

import com.lavans.lacoder2.di.BeanManager;
import com.lavans.lacoder2.lang.LogUtils;
import com.lavans.lacoder2.sql.dao.Condition;
import com.lavans.lacoder2.util.PageInfo;
import com.lavans.lacoder2.util.Pager;

import <%= pkg.getModelSubPackagePath() %>.manager.<%= className %>Manager;
import <%= pkg.getModelSubPackagePath() %>.entity.<%= className %>;

/**
 * Service for <%= entity.getTitle() %>.
 * @author dobashi
 *
 */
public class <%= className %>Service {
	/** logger */
	private static Logger logger = LogUtils.getLogger();

	/** EntityManager */
	private <%= className %>Manager <%= entityName %>Manager = BeanManager.getBean(<%= className %>Manager.class);
<%--
	/**
	 * コンストラクタ。
	 */
	private <%= className %>Service(){
		logger.debug("created.");
	}
 --%>
	/**
	 * 初期化処理。
	 *
	 */
	public void init(){
		logger.info("");
	}

	/**
	 * 更新用クラスを返す。
	 * @param datatype
	 * @return
	 */
	public <%= className %> read(<%= className %>.PK pk) throws SQLException{
		if(pk==null) return null;

		return <%= entityName %>Manager.get(pk);
	}

	/**
	 * <%= entity.getTitle() %>登録処理。
	 */
	public <%= className %> create(<%= className %> <%= entityName %>) throws SQLException{
		return <%= entityName %>Manager.insert(<%= entityName %>);
	}

	/**
	 * <%= entity.getTitle() %>更新処理。
	 */
	public <%= className %> update(<%= className %> <%= entityName %>) throws SQLException{
		return <%= entityName %>Manager.update(<%= entityName %>);
	}

	/**
	 * <%= entity.getTitle() %>削除
	 * @param <%= entityName %>
	 * @return
	 * @throws SQLException
	 */
	public int delete(<%= className %>.PK pk) throws SQLException{
		return <%= entityName %>Manager.delete(pk);
	}

	/**
	 * 一覧処理。
	 * @param searchCondMap
	 * @return
	 * @throws SQLException
	 */
	public List&lt;<%= className %>&gt; list(Condition cond) throws SQLException{
		return <%= entityName %>Manager.list(cond);
	}

	/**
	 * ページング一覧処理。
	 * @param pageInfo
	 * @param cond
	 * @return
	 * @throws SQLException
	 */
	public Pager&lt;<%= className %>&gt; pager( Condition cond,PageInfo pageInfo) throws SQLException{
		return <%= entityName %>Manager.pager(cond, pageInfo);
	}
}
</pre>
<!--
<%= debugStr %>
-->
</body>
