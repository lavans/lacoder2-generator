<!-- $Id: Dao.jsp 508 2012-09-20 14:41:55Z dobashi $ -->
<!doctype html public "-//w3c//dtd html 4.0 transitional//en"
   "http://www.w3.org/tr/rec-html40/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../common/common.jsp"
%><%@ page import="java.util.*"
%><%@ page import="java.text.*"
%><%@ page import="com.lavans.lacoder2.generator.model.Package"
%><%@ page import="com.lavans.lacoder2.generator.model.*"
%><%
	Package pkg = (Package)request.getAttribute("lacoder.package");
	Entity entity = (Entity)request.getAttribute("lacoder.entity");
	String className = entity.getClassName();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Attribute attrId = entity.get(0);
%>
<html lang="ja">
<head>
<title>ソース</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="red.css" rel="stylesheet" type="text/css">
</head>
<body text="#000000" leftmargin="10" topmargin="10" marginwidth="10" marginheight="10">
<pre>
/* $Id: Dao.jsp 508 2012-09-20 14:41:55Z dobashi $
 * created: <%= sdf.format(new Date()) %>
 *
 * This source code is generated by lacoder.
 * @see <%= entity.getClassName() %>Service
 * @see http://www.lavans.com/soft/lacoder/
 */
package <%= pkg.getModelSubPackagePath() %>.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import com.google.common.cache.CacheLoader;

import com.lavans.lacoder2.di.BeanManager;
import com.lavans.lacoder2.lang.LogUtils;
import com.lavans.lacoder2.sql.dao.CommonDao;
import com.lavans.lacoder2.sql.dao.Condition;
import com.lavans.lacoder2.sql.dao.DaoUtils;
import com.lavans.lacoder2.util.PageInfo;
import com.lavans.lacoder2.util.Pager;

import <%= pkg.getModelSubPackagePath() %>.entity.<%= className %>;
<% if(entity.hasBackup()){ %>import <%= pkg.getModelSubPackagePath() %>.entity.<%= className %>Bak;<% } %>

/**
 * <%= entity.getClassName() %>Daoクラス。
 * @author ${user}
 *
 */
public class <%= className %>Dao extends CacheLoader&lt;<%= className %>.PK, <%= className %>&gt; {
	private static Logger logger = LogUtils.getLogger();
	private CommonDao dao = BeanManager.getBean(CommonDao.class);

	/** Constructor */
	public <%= className %>Dao(){
	}

	/**
	 * load
	 */
	public <%= className %> load(<%= className %>.PK pk) throws SQLException{
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "load");
		List&lt;<%= className %>&gt; list = dao.list(<%= className %>.class, sql, pk.getAttributeMap());
		if(list.size()==0){
			logger.debug("target not found.");
			return null;
		}

		return list.get(0);
	}

	/**
	 * insert
	 * @param entity
	 * @return number of insert record
	 * @throws SQLException
	 */
	public int insert(<%= className %> entity) throws SQLException{
<% if(attrId.isSequence()){	// get nextseq
%>		// get next sequence
		String seqSql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "nextval");
		List&lt;Map&lt;String, Object&gt;&gt; seqResult = dao.executeQuery(seqSql);
		long seq = (Long)seqResult.get(0).values().toArray()[0];
		entity.<%= attrId.getSetterName() %>(seq);
<%	}
%>
		//
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "insert");
		int result = dao.executeUpdate(sql, entity.getAttributeMap());
		if(result!=1){
			logger.debug("insert failure.");
		}

		return result;
	}

	/**
	 * update
	 * @param entity
	 * @return number of update record
	 * @throws SQLException
	 */
	public int update(<%= className %> entity) throws SQLException{
		// update
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "update");
		int result = dao.executeUpdate(sql, entity.getAttributeMap());
		if(result!=1){
			logger.debug("update failure.["+ result +"]");
		}

		return result;
	}

	/**
	 * delete
	 *
	 * @param pk
	 * @return number of delete record
	 * @throws SQLException
	 */
	public int delete(<%= className %>.PK pk) throws SQLException{
		// delete
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "delete");
		int result = dao.executeUpdate(sql, pk.getAttributeMap());
		if(result!=1){
			logger.debug("delete failure.["+ result +"]");
		}

		return result;
	}

	/**
	 * list with conditions.
	 *
	 * @param Map&lt;String, String[]&gt;cond: query params
	 * @return List&lt;<%= className %>&gt; entityList
	 * @throws SQLException
	 */
	public List&lt;<%= className %>&gt; list(Condition cond) throws SQLException{
		// Copy entry. When fuzzy search, the value will be changed. ex) "value" to "%value%"
		Condition condWork = new Condition(cond);

		// list sql
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "list");
		sql += DaoUtils.makeWherePhrase(condWork);
		sql += DaoUtils.makeOrderByPhrase(condWork);

		List&lt;<%= className %>&gt; list = dao.list(<%= className %>.class, sql, DaoUtils.convertSearchCond(condWork, <%= className %>.getAttributeInfo()));

		return list;
	}

	/**
	 * pager.
	 *
	 * @param searchCondMap
	 * @return
	 * @throws SQLException
	 */
	public Pager&lt;<%= className %>&gt; pager(PageInfo pageInfo, Condition cond) throws SQLException{
		// Copy entry. When fuzzy search, the value will be changed. ex) "value" to "%value%"
		Condition condWork = new Condition(cond);

		// query condition
		String condition = DaoUtils.makeWherePhrase(condWork);
		String order = DaoUtils.makeOrderByPhrase(condWork);
		Map&lt;String, Object&gt; searchCond = DaoUtils.convertSearchCond(condWork, <%= className %>.getAttributeInfo());

		// count
		String seqSql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "count");
		seqSql = seqSql.replace("$condition",condition);
		List&lt;Map&lt;String, Object&gt;&gt; seqResult = dao.executeQuery(seqSql, searchCond);
		long count = (Long)seqResult.get(0).values().toArray()[0];

		// list
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "pager");
		sql = sql.replace("$condition",condition);
		sql = sql.replace("$order",order);
		searchCond.put("_rows", pageInfo.getRows());
		searchCond.put("_offset", pageInfo.getPage()*pageInfo.getRows());
		logger.debug(searchCond.toString());
		List&lt;<%= className %>&gt; list = dao.list(<%= className %>.class, sql, searchCond);

		// add to pager
		Pager&lt;<%= className %>&gt; pager = new Pager&lt;<%= className %>&gt;(pageInfo);
		pager.setTotalCount(count);
		for(<%= className %> entity: list){
			pager.add(entity);
		}

		return pager;
	}
<% if(entity.hasBackup()){ %>
	/**
	 * backup entity to _BAK table.
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	public int backup(<%= className %>.PK pk) throws SQLException{
		// get backup next sequence
		String seqSql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "backupNextval");
		List&lt;Map&lt;String, Object&gt;&gt; seqResult = dao.executeQuery(seqSql);
		long seq = (Long)seqResult.get(0).values().toArray()[0];

		// insert to backup table
				String sql = DaoUtils.getSql(MailSendLogDao.class.getName()+"Base", "backup");
		Condition cond = new Condition();
		cond.equal("backupId", String.valueOf(seq));
		cond.equal("<%= attrId.getName() %>", pk.<%= attrId.getGetterName() %>());
		Map&lt;String, Object&gt; condWork = CollectionUtils.copy(cond.getMap());
		int result = dao.executeUpdate(sql, DaoUtils.convertSearchCond(condWork, MailSendLog.getAttributeInfo()));

		if(result!=1){
			logger.debug("backup failure.["+ result +"]");
		}

		return result;
	}

	/**
	 * pager for backup table.
	 *
	 * @param searchCondMap
	 * @return
	 * @throws SQLException
	 */
	public Pager&lt;<%= className %>Bak&gt; pagerBak(PageInfo pageInfo, Condition cond) throws SQLException{
		// Copy entry. When fuzzy search, the value will be changed. ex) "value" to "%value%"
		Condition condWork = new Condition(cond);

		// query condition
		String condition = DaoUtils.getWherePhrase(condWork);
		if(condition.length()&gt;4){
			condition = "WHERE "+ condition.substring(4);
		}
		Map&lt;String, Object&gt; searchCond = DaoUtils.convertSearchCond(condWork, <%= className %>.getAttributeInfo());

		// count
		String seqSql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "count");
		seqSql = seqSql.replace("$condition",condition);
		List&lt;Map&lt;String, Object&gt;&gt; seqResult = dao.executeQuery(seqSql, searchCond);
		long count = (Long)seqResult.get(0).values().toArray()[0];

		// data
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "pager");
		sql = sql.replace("$condition",condition);
		searchCond.put("_rows", pageInfo.getRows());
		searchCond.put("_offset", pageInfo.getPage()*pageInfo.getRows());
		logger.debug(searchCond);
		List&lt;<%= className %>Bak&gt; list = dao.list(<%= className %>Bak.class, sql, searchCond);

		// add to pager
		Pager&lt;<%= className %>Bak&gt; pager = new Pager&lt;<%= className %>Bak&gt;(pageInfo);
		pager.setTotalCount(count);
		for(<%= className %>Bak entity: list){
			pager.add(entity);
		}

		return pager;
	}

	/**
	 * restore entity from _BAK table.
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	public int restore(<%= className %>.PK pk) throws SQLException{
		// insert to backup table
		String sql = DaoUtils.getSql(<%= className %>Dao.class.getName()+"Base", "restore");
		Condition cond = new Condition();
		cond.equal("<%= attrId.getName() %>", pk.<%= attrId.getGetterName() %>());

		int result = dao.executeUpdate(sql, searchCond);
		if(result!=1){
			logger.debug("backup failure.["+ result +"]");
		}

		return result;
	}
<%	} %>
}
</pre>
<!-- <%= debugStr %> -->
</body>

