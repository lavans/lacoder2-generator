package com.lavans.lacoder2.generator.action.main;

import java.util.ArrayList;
import java.util.List;

import com.lavans.lacoder2.controller.ActionSupport;
import com.lavans.lacoder2.util.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TopAction extends ActionSupport {
	public static final String CONFIG_FILE	="config_file";
	private static final Log logger = LogFactory.getLog(TopAction.class);

	public String execute(){
		logger.debug("");
		List<String> targetList = getTargetList();
		getRequest().setAttribute("targetList", targetList);
		return "Top.jsp";
	}

	private List<String> getTargetList(){
		Config configTarget = Config.getInstance("lacoder2.xml", true);
		NodeList nodeList = configTarget.getNodeList("target");
		List<String> targetList = new ArrayList<String>();
		for(int i=0; i<nodeList.getLength(); i++){
			Element node = (Element)nodeList.item(i);
			targetList.add(node.getAttribute("name"));
		}
		return targetList;
	}
}
//