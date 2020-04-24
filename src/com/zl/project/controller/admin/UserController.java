package com.zl.project.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zl.project.entity.admin.Role;
import com.zl.project.entity.admin.User;
import com.zl.project.page.admin.page;
import com.zl.project.service.admin.RoleService;
import com.zl.project.service.admin.UserService;

/**
 * 用户管理控制器
 * @author www65
 *
 */
@RequestMapping("/admin/user")
@Controller
public class UserController {
@Autowired
private UserService userService;
@Autowired
private RoleService roleService;
/**
 * 用户列表界面
 * @param model
 * @return
 */
@RequestMapping(value = "/list",method = RequestMethod.GET)
public ModelAndView List(ModelAndView model) {
	Map<String,Object>queryMap = new HashMap<String,Object>();
	model.addObject("roleList",roleService.findList(queryMap));
	roleService.findList(queryMap);
	model.setViewName("user/list");
	return model;
}
/**
 * 获取用户列表
 * @param page
 * @param username
 * @param roleId
 * @param sex
 * @return
 */
@RequestMapping(value="/list",method=RequestMethod.POST)
@ResponseBody
public Map<String, Object> getList(page page,
		@RequestParam(name="username",required=false,defaultValue="") String username,
		@RequestParam(name="roleId",required=false) Long roleId,
		@RequestParam(name="sex",required=false) Integer sex
		){
	Map<String, Object> ret = new HashMap<String, Object>();
	Map<String, Object> queryMap = new HashMap<String, Object>();
	queryMap.put("username", username);
	queryMap.put("roleId", roleId);
	queryMap.put("sex", sex);
	queryMap.put("offset", page.getOffset());
	queryMap.put("pageSize", page.getRows());
	ret.put("rows", userService.findList(queryMap));
	ret.put("total", userService.getTotal(queryMap));
	return ret;
}
/**
 * 添加用户
 * @param user
 * @return
 */
@RequestMapping(value="/add",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> add(User user){
	Map<String ,String> ret = new HashMap<String,String>();
	if(user == null) {
		ret.put("type", "error");
		ret.put("msg", "请填写正确用户信息！");
		return ret;
	}
	if(StringUtils.isEmpty(user.getUsername())) {
		ret.put("type", "error");
		ret.put("msg", "请填写用户名！");
		return ret;
	}
	if(StringUtils.isEmpty(user.getPassword())) {
		ret.put("type", "error");
		ret.put("msg", "请填写密码！");
		return ret;
	}
	if(user.getRoleId() == null) {
		ret.put("type", "error");
		ret.put("msg", "请选择所属角色！");
		return ret;
	}
	if(isExist(user.getUsername(),0l)) {
		ret.put("type", "error");
		ret.put("msg", "该用户名已存在请重新输入！");
		return ret;
	}
	if(userService.add(user)<= 0) {
		ret.put("type", "error");
		ret.put("msg", "用户添加失败请联系管理员！");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "添加成功！");
	return ret;
}

/**
 * 编辑用户
 * @param user
 * @return
 */
@RequestMapping(value="/edit",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> edit(User user){
	Map<String ,String> ret = new HashMap<String,String>();
	if(user == null) {
		ret.put("type", "error");
		ret.put("msg", "请填写正确用户信息！");
		return ret;
	}
	if(StringUtils.isEmpty(user.getUsername())) {
		ret.put("type", "error");
		ret.put("msg", "请填写用户名！");
		return ret;
	}
//	if(StringUtils.isEmpty(user.getPassword())) {
//		ret.put("type", "error");
//		ret.put("msg", "请填写密码！");
//		return ret;
//	}
	if(user.getRoleId() == null) {
		ret.put("type", "error");
		ret.put("msg", "请选择所属角色！");
		return ret;
	}
	if(isExist(user.getUsername(),user.getId())) {
		ret.put("type", "error");
		ret.put("msg", "该用户名已存在请重新输入！");
		return ret;
	}
	if(userService.edit(user)<= 0) {
		ret.put("type", "error");
		ret.put("msg", "用户修改失败请联系管理员！");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "修改成功！");
	return ret;
}

/**
 * 批量删除用户
 * @param ids
 * @return
 */
@RequestMapping(value="/delete",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> delete(String ids){
	Map<String ,String> ret = new HashMap<String,String>();
	
	if(StringUtils.isEmpty(ids)) {
		ret.put("type", "error");
		ret.put("msg", "请选择要删除的数据！");
		return ret;
	}
	if(ids.contains(",")) {
		ids = ids.substring(0,ids.length()-1);
	}

	if(userService.delete(ids)<= 0) {
		ret.put("type", "error");
		ret.put("msg", "用户删除失败请联系管理员！");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "删除成功！");
	return ret;
}
/**
 * 上传图片
 * @param photo
 * @param request
 * @return
 */

@RequestMapping(value="/upload_photo",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> uploadphoto(MultipartFile photo,HttpServletRequest request){
	Map<String ,String> ret = new HashMap<String,String>();
	
	if(photo == null) {
		ret.put("type", "error");
		ret.put("msg", "请选择要上传的图片！");
		return ret;
	}
	if(photo.getSize() > 1024*1024*1024) {
		ret.put("type", "error");
		ret.put("msg", "图片大小不能超过10M！");
		return ret;
	}
	//abcasj.add.ada.jpg,获取最后一个点后面的后缀
	String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
	if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
		ret.put("type", "error");
		ret.put("msg", "图片格式错误，请选择jpg,jpeg,gif,png类型的图片");
		return ret;
	}
//拿到项目根目录的绝对地址
	String savePath = request.getServletContext().getRealPath("/")+"/resource/upload/";
	File savePathFile = new File(savePath);
	if(!savePathFile.exists()) {
		//若不存在该目录则创建该目录
		savePathFile.mkdir();
	}
	String filename = new Date().getTime()+"."+suffix;
	try {
		//将文件保存到指定目录
		photo.transferTo(new File(savePath + filename));
	}  catch (Exception e) {
		// TODO Auto-generated catch block
		ret.put("type", "error");
		ret.put("msg", "保存文件异常！");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "上传成功！");
	ret.put("filePath",request.getServletContext().getContextPath()+"/resource/upload/" + filename );
	return ret;
}
/**
 * 判断该用户名是否存在
 * @param username
 * @param id
 * @return
 */
private boolean isExist(String username,Long id) {
	User user = userService.findByUsername(username);
	if(user == null)return false;
	if(user.getId().longValue() == id.longValue())return false;
	return true;
}
}
