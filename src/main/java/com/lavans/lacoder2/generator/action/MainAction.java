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

import com.lavans.lacoder2.generator.main.Target;
import com.lavans.lacoder2.generator.main.XMLReader;
import com.lavans.lacoder2.lang.StringUtils;
import com.lavans.lacoder2.util.Config;

public class MainAction extends Action {
	public static final String CONFIG_FILE	="config_file";
	private static final Log logger = LogFactory.getLog(MainAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// target選択
		if(request.getParameter("selectTarget")!=null){
			Target.deselect();
		}

		// 選択されたTargetを取得
		String targetName = request.getParameter("target");
		Target target = null;
		if(!StringUtils.isEmpty(targetName)){
			// target指定時
			if(targetName.equals("null")){
				target = null;
			}else{
				target = Target.select(targetName, getServlet().getServletContext().getRealPath(""));
			}
		}else{
			target = Target.getSelectedTarget();
		}
		// target未指定なら選択画面へ
		if(target==null){
			Config configTarget = Config.getInstance("lacoder2.xml", true);
			try {
				List<String> targetList = configTarget.getNodeValueList("target");
				request.setAttribute("targetList", targetList);
			} catch (Exception e) {
				logger.error("target error. Check lacoder/conf/target.xml");
			}
			return mapping.findForward("selectTarget");
		}

		// ファイル一覧の読み込み
		request.setAttribute("fileList", target.getConfList());

		// 設定ファイルの読込
		String configFile = request.getParameter(CONFIG_FILE);
		if(configFile!=null){
			XMLReader reader = new XMLReader();
			reader.read(target, configFile);
		}

		return mapping.findForward("ok");
	}

}
