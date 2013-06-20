package com.lavans.lacoder2.generator.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lavans.lacoder2.generator.model.Entity;
import com.lavans.lacoder2.generator.model.EnumClass;
import com.lavans.lacoder2.generator.model.Package;
import com.lavans.lacoder2.generator.model.Role;
import com.lavans.lacoder2.generator.model.Service;
import com.lavans.lacoder2.lang.LogUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;


public class ModelAction extends Action {
	private static Logger logger = LogUtils.getLogger();
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// パース済みの情報から取得
		Package pkg = Package.getPackage(request.getParameter("package"));
		if(pkg==null){
			logger.error("package選択無し");
			return mapping.findForward("main");
		}
		Entity entity = pkg.getEntity(request.getParameter("entity"));
		EnumClass enums = pkg.getEnum(request.getParameter("enum"));
		Service service = pkg.getService(request.getParameter("service"));
		// パラメータからroleを作成
		Role role = new Role(request.getParameter("role"));
		// パラメータからメソッド名(jsp作成用)を取得
		String method = request.getParameter("method");
		request.setAttribute("lacoder.package",pkg);
		request.setAttribute("lacoder.entity",entity);
		request.setAttribute("lacoder.enum",enums);
		request.setAttribute("lacoder.service",service);
		request.setAttribute("lacoder.role", role);
		request.setAttribute("lacoder.method", method);
		
		// enum-id
		if(enums!=null){
			String name = enums.getMemberList().get(0).getName();
			if(Character.isUpperCase(name.charAt(1))){
				return mapping.findForward("ok-id");
			}
		}
		return mapping.findForward("ok");
	}

}
