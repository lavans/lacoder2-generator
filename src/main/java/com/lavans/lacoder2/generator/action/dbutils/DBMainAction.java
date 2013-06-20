package com.lavans.lacoder2.generator.action.dbutils;

import java.util.List;

import com.lavans.lacoder2.controller.ActionSupport;
import com.lavans.lacoder2.lang.LogUtils;
import com.lavans.lacoder2.sql.dbutils.enums.DbmsType;
import com.lavans.lacoder2.sql.dbutils.model.Database;
import com.lavans.lacoder2.sql.dbutils.model.DbmsConnectInfo;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

public class DBMainAction extends ActionSupport{
	private static final Logger logger = LogUtils.getLogger();
	
	private static Database database = Database.connect(createConnectInfo());

	
	/** todo */
	private static DbmsConnectInfo createConnectInfo() {
		DbmsConnectInfo connectInfo = new DbmsConnectInfo();
		connectInfo.setDbmsType(DbmsType.ORACLE);
		connectInfo.setIp("192.168.100.118");
		connectInfo.setDBName("et93");
		connectInfo.setUser("etrade");
		connectInfo.setPass("etrade");
		connectInfo.setStatistics(true);
		logger.info(connectInfo.getName());
		return connectInfo;
	}

	public String execute(){
		logger.debug("");
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		List<String> tableNames = database.getTableNames();
		logger.debug(tableNames.toString());

		getRequest().setAttribute("tableNames", tableNames);
		getRequest().setAttribute("version", database.getVersion());

		stopWatch.stop();
		logger.info("execute time "+ stopWatch.getTime()+ "ms");
		
		return "DBMain.jsp";
	}
	
	public String info(){
		getRequest().setAttribute("tableNames", database.getTableNames());
		String tableName = getRequest().getParameter("tableName");
		getRequest().setAttribute("table", database.getTable(tableName));
		return "DBMain-info.jsp";
	}
}
