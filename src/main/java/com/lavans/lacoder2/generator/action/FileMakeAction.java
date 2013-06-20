package com.lavans.lacoder2.generator.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.lavans.lacoder2.generator.main.FileMaker;
import com.lavans.lacoder2.generator.main.Target;
import com.lavans.lacoder2.util.Config;

public class FileMakeAction extends Action {
	private static final Log logger = LogFactory.getLog(FileMakeAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 選択されたTargetを取得
		Target target = Target.getSelectedTarget();
		// target未指定なら選択画面へ
		if(target==null){
			Config configTarget = Config.getInstance("target.xml", true);
			List<String> targetList = configTarget.getNodeValueList("/lacoder/target");
			request.setAttribute("targetList", targetList);
			return mapping.findForward("selectTarget");
		}

		String filetype = request.getParameter("type");
		boolean isOverride = Boolean.parseBoolean(request.getParameter("override"));
		FileMaker fileMaker = new FileMaker();
		fileMaker.setOverride(isOverride);

		// 実際のパスを分解して遡る
		logger.debug(request.getRequestURI());
		logger.debug(request.getRequestURL());
		logger.debug(request.getServerName());
		logger.debug(request.getServletPath());
		String url = request.getRequestURL().toString();
		url = url.substring(0, url.lastIndexOf("/")+1);
		fileMaker.makefile(target, filetype, url);
		request.setAttribute("lacoder.fileMaker", fileMaker);
		return mapping.findForward("ok");
	}

}
