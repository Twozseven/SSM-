 package com.zl.project.controller.admin;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.*;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.github.pagehelper.util.StringUtil;
import com.zl.project.entity.admin.Authority;
import com.zl.project.entity.admin.Menu;
import com.zl.project.entity.admin.Role;
import com.zl.project.entity.admin.User;
import com.zl.project.service.admin.AuthorityService;
import com.zl.project.service.admin.LogService;
import com.zl.project.service.admin.MenuService;
import com.zl.project.service.admin.RoleService;
import com.zl.project.service.admin.UserService;
import com.zl.project.util.CpachaUtil;
import com.zl.project.util.MenuUtil;
/**
 * 系统操作类控制器
 * 
 */
@Controller
@RequestMapping("/system")
public class SystemController {

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private  LogService logService;

	/**
	 * 系统登录后的主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model,HttpServletRequest request) {
		List<Menu> userMenus = (List<Menu>)request.getSession().getAttribute("userMenus");
		model.addObject("topMenuList",MenuUtil.getAllTopMenu(userMenus));	
		model.addObject("secondMenuList",MenuUtil.getAllSecondTopMenu(userMenus));
		model.setViewName("system/index");
		model.addObject("name","zhangli");
		return model;///WEB-INF/views/+system/index+.jsp==WEB-INF/views/system/index.jsp
	}
	//系统登陆后后台管理网站的欢迎页
	@RequestMapping(value="/welcome",method=RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model) {
		model.setViewName("system/welcome");
		
		return model;
	}
	
	
	//登录页面
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {		
		model.setViewName("system/login");
		return model;
	}
	/**
	 * 登录表单提交处控制器
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)	
	@ResponseBody
	public Map<String,String> loginAct(User user,String cpacha,HttpServletRequest request){
		Map<String,String> ret = new HashMap<String,String>();
		if(user==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写用户信息！");
			return ret;
		}
		if(StringUtil.isEmpty(cpacha)) {
			ret.put("type", "error");
			ret.put("msg", "请填写验证码！");
			return ret;
		}
		if(StringUtil.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "请填写用户名！");
			return ret;
		}
		if(StringUtil.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "请填写密码！");
			return ret;
		}
		Object loginCpacha = request.getSession().getAttribute("loginCpacha");
		
		if(loginCpacha == null) {
			ret.put("type", "error");
			ret.put("msg", "会话超时，请刷新页面！");
			return ret;
		}
		if(!cpacha.toUpperCase().equals(loginCpacha.toString().toUpperCase())){
			ret.put("type", "error");
			ret.put("msg", "验证码错误！");
			//添加日志
			logService.add("用户名为"+user.getUsername()+"的用户输入验证码错误");
			return ret;		
		}
		User findByUsername=userService.findByUsername(user.getUsername());
		if(findByUsername==null) {
			ret.put("type", "error");
			ret.put("msg", "该用户名不存在！");
			logService.add("登录时用户名为"+user.getUsername()+"的用户名不存在");
			return ret;
		}
		if(!user.getPassword().equals(findByUsername.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "密码错误！");
			logService.add("用户名为"+user.getUsername()+"的用户输入密码错误");
			return ret;
		}
		//此时用户名，密码，验证码都正确
		//此时需要查询用户的角色权限
		Role role = roleService.find(findByUsername.getRoleId());
		List<Authority> authorityList = authorityService.findListByRoleId(role.getId());//根据角色获取权限列表
		String menuIds = "";
		for(Authority authority:authorityList) {
			menuIds +=authority.getMenuId() + ",";
		}
		if(!StringUtils.isEmpty(menuIds)) {
			menuIds = menuIds.substring(0,menuIds.length()-1);
			
		}
		List<Menu> userMenus = menuService.findListByIds(menuIds);
		//把角色信息，菜单信息放大session中
		request.getSession().setAttribute("admin", findByUsername);
		request.getSession().setAttribute("role", role);
		request.getSession().setAttribute("userMenus", userMenus);
		ret.put("type", "success");
		ret.put("msg", "登录成功！");
		logService.add("用户名为"+user.getUsername()+",角色为"+role.getName()+"的用户登录成功");
		return ret;
	}
	
	/**
	 * 后台退出注销登录功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout",method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("admin", null);
		session.setAttribute("role", null);
		request.getSession().setAttribute("userMenus", null);
		return "redirect:login";//重定向到登录页面
	}
	
	/**
	 * 修改密码页面
	 * @param model
	 * @return
	 */
		@RequestMapping(value="/edit_password",method=RequestMethod.GET)
		public ModelAndView editPassword(ModelAndView model) {		
			model.setViewName("system/edit_password");
			return model;
		}
	
		@RequestMapping(value="/edit_password",method=RequestMethod.POST)	
		@ResponseBody
		public Map<String,String> editPasswordAct(String newpassword,String oldpassword,HttpServletRequest request){
			Map<String,String> ret = new HashMap<String,String>();
			if(StringUtils.isEmpty(newpassword)) {
				ret.put("type", "error");
				ret.put("msg", "请填写新密码！");
				return ret;
			}
			
			User user = (User)request.getSession().getAttribute("admin");
			if(!user.getPassword().equals(oldpassword)) {
				ret.put("type", "error");
				ret.put("msg", "原密码错误！");
				return ret;
			}
			user.setPassword(newpassword);
			if(userService.editPassword(user)<=0) {
				ret.put("type", "error");
				ret.put("msg", "密码修改失败请联系管理员！");
				return ret;
			}
			ret.put("type", "success");
			ret.put("msg", "密码修改成功！");
			logService.add("用户名为"+user.getUsername()+"的用户修改密码成功");
			return ret;
		}
		
		
	/**
	 * 本系统所有验证码都用这个方法
	 * @param vcodeLen
	 * @param width
	 * @param height
	 * @param cpachaType :用来区别验证码类型，传字符串
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/get_cpacha",method=RequestMethod.GET)
	public void generateCpacha(
			//添加参数
			@RequestParam(name="vl",required=false,defaultValue="4") Integer vcodeLen,
			@RequestParam(name="w",required=false,defaultValue="100") Integer width,
			@RequestParam(name="h",required=false,defaultValue="30") Integer height,
			@RequestParam(name="type",required=false,defaultValue="loginCpacha") String cpachaType,
			HttpServletRequest request,
			HttpServletResponse response) {
		CpachaUtil cpachaUtil = new CpachaUtil(vcodeLen, width, height);
		String generatorVCode = cpachaUtil.generatorVCode();
		request.getSession().setAttribute(cpachaType, generatorVCode);
		BufferedImage generatorRotateVCodeImage= cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			ImageIO.write(generatorRotateVCodeImage, "gif",   response.getOutputStream());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
}
