<!-- $Id: EntityManager.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<%@page import="com.lavans.lacoder2.generator.writer.EntityManagerWriter"%>
<%@page import="com.lavans.lacoder2.generator.model.Attribute"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="../common/common.jsp" %>
<%@page import="java.text.*" %>
<%@page import="com.lavans.lacoder2.generator.model.Package"%>
<%@page import="com.lavans.lacoder2.generator.model.Entity" %>
<%!
	/**
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%>
<%
	Package pkg = (Package)request.getAttribute("lacoder.package");
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
	EntityManagerWriter writer = new EntityManagerWriter(entity);
	String className = entity.getClassName();
%>
<head>
<title>service</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
/* $Id: EntityManager.jsp 508 2012-09-20 14:41:55Z dobashi $
 * 作成日: <%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>
 *
 */
package <%= pkg.getModelSubPackagePath() %>.manager;

<%= writer.writeImports() %>

/**
 * EntityManager for <%= entity.getTitle() %>.
 * @author SBI
 *
 */
public class <%= className %>Manager {
<%= writer.writeFielsds() %>
	/**
	 * Constructor.
	 */
	public <%= className %>Manager(){
<%= writer.writeConstructor() %>
	}

	/**
	 * インスタンスを返します。
	 * <% if(entity.isCached()){ %>キャッシュに無い場合はDBからloadされキャッシュされます。 <% } %>
	 * 
	 * @param pk PrimaryKey
	 * @return entity instance.
	 */
	public <%= className %> get(<%= className %>.PK pk){
		if(pk==null) return null;
<% if(entity.isCached()){ 
%>		return cacheManager.get(pk).orNull();
<% }else{ 
%>		return baseDao.load(<%= className %>.class, pk);
<% }
%>	}

	/**
	 * <%= entity.getTitle() %>登録処理。
	 * @return entity.
	 */
	public <%= className %> insert(<%= className %> entity){
		baseDao.insert(entity);
<% if(entity.isCached()){ %>		cacheManager.put(entity.getPk(), Optional.of(entity));<% } %>
		return entity;
	}

	/**
	 * <%= entity.getTitle() %>更新処理。
	 * @return E 更新時間をセットしたEntityそのものを返す。
	 */
	public <%= className %> update(<%= className %> entity){
		baseDao.update(entity);
<% if(entity.isCached()){ %>		cacheManager.put(entity.getPk(), Optional.of(entity));<% } %>
		return entity;
	}

	/**
	 * <%= entity.getTitle() %>削除
	 * @param E
	 * @return
	 * @throws SQLException
	 */
	public int delete(<%= className %>.PK pk){
		// DBから削除
		int result = baseDao.delete(<%= className %>.class, pk);
<% if(entity.isCached()){ %>		cacheManager.invalidate(pk);<% } %>
		return result;
	}

	/**
	 * 一覧処理。
	 * @param pageInfo
	 * @param cond
	 * @return
	 * @throws SQLException
	 */
	public List&lt;<%= className %>&gt; list(Condition cond) {
		return baseDao.list(<%= className %>.class, cond);
	}

	/**
	 * ページング一覧処理。
	 * @param pageInfo
	 * @param cond
	 * @return
	 * @throws SQLException
	 */
	public Pager&lt;<%= className %>&gt; pager(Condition cond, PageInfo pageInfo) {
		return baseDao.pager(<%= className %>.class, cond, pageInfo);
	}
<% if(entity.isCached()){ %>
	/**
	 * キャッシュハンドラー
	 * @author sbisec
	 */
	private class <%= className %>CacheHandler implements CacheHandler&lt;<%= className %>.PK, Optional&lt;<%= className %>&gt;&gt; {
		/** cache size */
		private static final int MAX_CACHE_SIZE=100;
		@Override
		public int getMaxCacheSize() {
			return MAX_CACHE_SIZE;
		}

		@Override
		public <%= className %>.PK decode(String key) {
			return JSON.decode(key,<%= className %>.PK.class);
		}

		@Override
		public Optional&lt;<%= className %>&gt; load(<%= className %>.PK in) {
			return Optional.fromNullable(baseDao.load(<%= className %>.class, in));
		}

		@Override
		public ValidTerm getValidTerm(<%= className %>.PK in) {
			return new ValidTerm(1, TimeUnit.DAYS);
		}
	}
<%	} %>}
</pre>
<!--
<%= debugStr %>
-->
</body>
