<!-- $Id: Action.jsp 510 2012-09-20 14:58:49Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@page import="com.lavans.lacoder2.generator.main.Target"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp"
%><%@ page import="java.util.*"
%><%@ page import="java.text.*"
%><%@ page import="com.lavans.lacoder2.generator.model.Package"
%><%@ page import="com.lavans.lacoder2.generator.model.*"
%><%@page import="com.lavans.lacoder2.lang.StringUtils"
%><%!
	/**
	 * 基本方針
	 * とりあえずプロバイス用に手っ取り早く。
	 * 最終的にはjspをやめてファイルに直接書き出す。
	 * これをベースクラスとしてインクリメンタルな開発に耐えられるように。
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%><%
Package pkg = (Package)request.getAttribute("lacoder.package");
Entity entity = (Entity)request.getAttribute("lacoder.entity");
ActionWriter writer = new ActionWriter(entity);
String className = entity.getClassName();
String varName = entity.getShortname();
Role role = (Role)request.getAttribute("lacoder.role");

String jspPath=""; //pkg.getJspPath(role.getName());

if(!StringUtils.isEmpty(jspPath)){ jspPath = jspPath+="/"; }
%>


<%@page import="com.lavans.lacoder2.generator.writer.ActionWriter"%><html lang="ja">
<head>
<title>ソース</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
/* $Id: Action.jsp 510 2012-09-20 14:58:49Z dobashi $
 * created: <%= sdf.format(new Date()) %>
 *
 * This source code is generated by lacoder.
 * @see <%= entity.getClassName() %>Service
 * @see http://www.lavans.com/soft/lacoder/
 */
package <%= pkg.getActionPath(role.getName()) %>;
<%--import java.util.List; --%>
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lavans.lacoder2.di.BeanManager;
import com.lavans.lacoder2.controller.ActionSupport;
import com.lavans.lacoder2.sql.dao.Condition;
import com.lavans.lacoder2.sql.dao.DaoUtils;
import com.lavans.lacoder2.util.PageInfo;
import com.lavans.lacoder2.util.Pager;
import <%= pkg.getServiceSubPackagePath() %>.<%= className %>Service;
import <%= pkg.getModelSubPackagePath() %>.entity.<%= className %>;
<%-- = writer.writeImportsEnum() --%>

/**
 * <%= entity.getClassName() %> action.
 *
 * @author ${user}
 */

public class <%= className %>Action extends ActionSupport{
<%--	private static final long serialVersionUID = 1L;
--%>	/** logger */
	private static Log logger = LogFactory.getLog(<%= className %>Action.class);
	/** <%= entity.getTitle() %>Service */
	private <%= className %>Service <%= varName %>Service = ServiceManager.getService(<%= className %>Service.class);

<%--
	/**
	 * jsp用のパーツ作成
	 */
	private void makeUiParts(){
		uiParts.put("title", "<%= entity.getTitle() %>一覧");
<%= writer.writeUiPartsEnum() %>
		pager.setLinkParam(ParameterUtils.toStoreString(cond, "cond."));
	}
 --%>
 	/**
	 * 詳細
	 * @return
	 * @throws Exception
	 */
	public String read() throws Exception{
		<%= className %>.PK pk = parsePk();
		// コピーを取得
		<%= className %> <%= varName %> = <%= varName %>Service.read(pk);
		// リクエストにセット
		setAttribute("<%= varName %>", <%= varName %>);

		return "<%= jspPath %><%= className %>-read.jsp";
	}

	/**
	 * 一覧
	 * @return
	 * @throws Exception
	 */
<%-- @SuppressWarnings("unchecked")
--%>	public String list() throws Exception{
		// PageInfo
		PageInfo pageInfo = new PageInfo();
		pageInfo.setParameters(getRequest().getParameterMap(), "pageInfo.");

		// Search data
		Condition cond = DaoUtils.getCondition(getRequest().getParameterMap(), "cond.");
		Pager&lt;<%= className %>&gt; pager = <%= varName %>Service.pager(cond, pageInfo);

		// set to request scope
		setAttribute("cond", cond);
		setAttribute("pager", pager);
		setAttribute("rows", pageInfo.getRowsSelect());

		return "<%= jspPath %><%= className %>-list.jsp";
	}

	/**
	 * 登録
	 * @return
	 */
	public String createInput(){
		<%= className %> <%= varName %> = new <%= className %>();
		setAttribute("<%= varName %>", <%= varName %>);
		return "<%= jspPath %><%= className %>-createInput.jsp";
	}

	/**
	 * 登録完了
	 * @return
	 */
	public String createResult() throws Exception{
		<%= className %> <%= varName %> = parseEntity();
		Map&lt;String, String&gt; errors = <%= varName %>.validate();
		if(errors.size()&gt;0){
			addActionErrors(errors.values());
			setAttribute("<%= varName %>", <%= varName %>);
			return "<%= jspPath %><%= className %>-createInput.jsp";
		}

		// 登録処理
		try {
			<%= varName %>Service.create(<%= varName %>);
		} catch (Exception e) {
			logger.debug("", e);
			addActionError(e.getClass().getSimpleName() +":"+ e.getMessage());
			setAttribute("<%= varName %>", <%= varName %>);
			return "<%= jspPath %><%= className %>-createInput.jsp";
		}

		// 登録成功
<%	if(entity.isSkipResult()){ %>
		// 一覧アクションへ
		redirect("<%= className %>!list");
		return null;
<%	}else{ %>
		// 完了画面
		return "<%= jspPath %><%= className %>-createResult.jsp";
<%	} %>
	}

	/**
	 * 編集
	 * @return
	 */
	public String updateInput() throws Exception{
		<%= className %>.PK pk = parsePk();
		// コピーを取得
		<%= className %> <%= varName %> = <%= varName %>Service.read(pk);
		// リクエストにセット
		setAttribute("<%= varName %>", <%= varName %>);

		return "<%= jspPath %><%= className %>-updateInput.jsp";
	}

	/**
	 * 編集完了
	 * @return
	 */
	public String updateResult() throws Exception{
		<%= className %>.PK pk = parsePk();
		<%= className %> <%= varName %> = <%= varName %>Service.read(pk);
		// 入力したパラメータをセット
		parseEntity(<%= varName %>);
		Map&lt;String, String&gt; errors = <%= varName %>.validate();
		if(errors.size()&gt;0){
			addActionErrors(errors.values());
			setAttribute("<%= varName %>", <%= varName %>);

			return "<%= jspPath %><%= className %>-updateInput.jsp";
		}

		// 登録処理
		try {
			<%= varName %>Service.update(<%= varName %>);
		} catch (Exception e) {
			logger.debug("", e);
			addActionError(e.getMessage());
			setAttribute("<%= varName %>", <%= varName %>);

			return "<%= jspPath %><%= className %>-updateInput.jsp";
		}

		// 登録成功
		// 一覧アクションへ
		chain("<%= className %>!list");
		return null;
	}

	/**
	 * 削除
	 * @return
	 */
	public String delete() throws Exception{
		<%= className %>.PK pk = parsePk();

		<%= varName %>Service.delete(pk);
		redirect("<%= className %>!list");
		return null;
	}

	/**
	 * PK情報の読出
	 * @return
	 */
	private <%= className %>.PK parsePk(){
		<%= className %>.PK pk = new <%= className %>.PK();
		pk.setParameters(getRequest().getParameterMap(),"<%= varName %>.");
		return pk;
	}

	/**
	 * Entity情報の読出。
	 * 新規Entity作成。
	 * @return
	 */
	private <%= className %> parseEntity(){
		return parseEntity(new <%= className %>());
	}

	/**
	 * Entity情報の読出。
	 * 既存Entityに情報を上書き。
	 * @return
	 */
	private <%= className %> parseEntity(<%= className %> entity){
		entity.setParameters(getRequest().getParameterMap(),"<%= varName %>.");
		return entity;
	}
}
</pre>
<!-- <%= debugStr %> -->
</body>
</html>
