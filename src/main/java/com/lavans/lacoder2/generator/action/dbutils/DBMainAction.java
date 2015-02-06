package com.lavans.lacoder2.generator.action.dbutils;

import java.util.List;

import lombok.val;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import com.lavans.lacoder2.controller.ActionSupport;
import com.lavans.lacoder2.lang.LogUtils;
import com.lavans.lacoder2.sql.dbutils.model.ConnectInfo;
import com.lavans.lacoder2.sql.dbutils.model.Database;

public class DBMainAction extends ActionSupport{
	private static final Logger logger = LogUtils.getLogger();

	private static final Database database;

	static{
    val c = new ConnectInfo("lacoder2.xml", "default");
    val d = new Database(c);
    database = d;
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
