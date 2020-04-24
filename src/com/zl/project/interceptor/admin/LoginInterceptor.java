package com.zl.project.interceptor.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zl.project.entity.admin.Menu;
import com.zl.project.util.MenuUtil;

import net.sf.json.JSONObject;
/**
 * 后台登录拦截器
 * @author www65
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String requestURI = request.getRequestURI();
		Object admin = request.getSession().getAttribute("admin");
		if(admin==null) {
			//表示未登录或者登录失效
			System.out.print("链接"+requestURI+"进入拦截器");
			String header = request.getHeader("X-Requested-With");
			//判断是否是ajax请求
			if("".equals(header)) {
				//是ajax请求
				Map<String,String> ret = new HashMap<String,String>();
				ret.put("type", "error");
				ret.put("msg", "登录会话超时或未登录，请重新登录");
				response.getWriter().write(JSONObject.fromObject(ret).toString());
				return false;
			}
			//表示普通链接跳转，直接重定向到登录页面
			response.sendRedirect(request.getServletContext().getContextPath()+"/system/login");
			return false;
		}
		//获取按钮id
		String mid = request.getParameter("_mid");
		if(!StringUtils.isEmpty(mid)) {
			List<Menu> allThirdMenu =MenuUtil.getAllThirdMenu((List<Menu>)request.getSession().getAttribute("userMenus"), Long.valueOf(mid)); 
		request.setAttribute("thirdMenuList", allThirdMenu);
		}
		return true;
	}

}
