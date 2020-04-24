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
 * �û����������
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
 * �û��б����
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
 * ��ȡ�û��б�
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
 * ����û�
 * @param user
 * @return
 */
@RequestMapping(value="/add",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> add(User user){
	Map<String ,String> ret = new HashMap<String,String>();
	if(user == null) {
		ret.put("type", "error");
		ret.put("msg", "����д��ȷ�û���Ϣ��");
		return ret;
	}
	if(StringUtils.isEmpty(user.getUsername())) {
		ret.put("type", "error");
		ret.put("msg", "����д�û�����");
		return ret;
	}
	if(StringUtils.isEmpty(user.getPassword())) {
		ret.put("type", "error");
		ret.put("msg", "����д���룡");
		return ret;
	}
	if(user.getRoleId() == null) {
		ret.put("type", "error");
		ret.put("msg", "��ѡ��������ɫ��");
		return ret;
	}
	if(isExist(user.getUsername(),0l)) {
		ret.put("type", "error");
		ret.put("msg", "���û����Ѵ������������룡");
		return ret;
	}
	if(userService.add(user)<= 0) {
		ret.put("type", "error");
		ret.put("msg", "�û����ʧ������ϵ����Ա��");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "��ӳɹ���");
	return ret;
}

/**
 * �༭�û�
 * @param user
 * @return
 */
@RequestMapping(value="/edit",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> edit(User user){
	Map<String ,String> ret = new HashMap<String,String>();
	if(user == null) {
		ret.put("type", "error");
		ret.put("msg", "����д��ȷ�û���Ϣ��");
		return ret;
	}
	if(StringUtils.isEmpty(user.getUsername())) {
		ret.put("type", "error");
		ret.put("msg", "����д�û�����");
		return ret;
	}
//	if(StringUtils.isEmpty(user.getPassword())) {
//		ret.put("type", "error");
//		ret.put("msg", "����д���룡");
//		return ret;
//	}
	if(user.getRoleId() == null) {
		ret.put("type", "error");
		ret.put("msg", "��ѡ��������ɫ��");
		return ret;
	}
	if(isExist(user.getUsername(),user.getId())) {
		ret.put("type", "error");
		ret.put("msg", "���û����Ѵ������������룡");
		return ret;
	}
	if(userService.edit(user)<= 0) {
		ret.put("type", "error");
		ret.put("msg", "�û��޸�ʧ������ϵ����Ա��");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "�޸ĳɹ���");
	return ret;
}

/**
 * ����ɾ���û�
 * @param ids
 * @return
 */
@RequestMapping(value="/delete",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> delete(String ids){
	Map<String ,String> ret = new HashMap<String,String>();
	
	if(StringUtils.isEmpty(ids)) {
		ret.put("type", "error");
		ret.put("msg", "��ѡ��Ҫɾ�������ݣ�");
		return ret;
	}
	if(ids.contains(",")) {
		ids = ids.substring(0,ids.length()-1);
	}

	if(userService.delete(ids)<= 0) {
		ret.put("type", "error");
		ret.put("msg", "�û�ɾ��ʧ������ϵ����Ա��");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "ɾ���ɹ���");
	return ret;
}
/**
 * �ϴ�ͼƬ
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
		ret.put("msg", "��ѡ��Ҫ�ϴ���ͼƬ��");
		return ret;
	}
	if(photo.getSize() > 1024*1024*1024) {
		ret.put("type", "error");
		ret.put("msg", "ͼƬ��С���ܳ���10M��");
		return ret;
	}
	//abcasj.add.ada.jpg,��ȡ���һ�������ĺ�׺
	String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
	if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
		ret.put("type", "error");
		ret.put("msg", "ͼƬ��ʽ������ѡ��jpg,jpeg,gif,png���͵�ͼƬ");
		return ret;
	}
//�õ���Ŀ��Ŀ¼�ľ��Ե�ַ
	String savePath = request.getServletContext().getRealPath("/")+"/resource/upload/";
	File savePathFile = new File(savePath);
	if(!savePathFile.exists()) {
		//�������ڸ�Ŀ¼�򴴽���Ŀ¼
		savePathFile.mkdir();
	}
	String filename = new Date().getTime()+"."+suffix;
	try {
		//���ļ����浽ָ��Ŀ¼
		photo.transferTo(new File(savePath + filename));
	}  catch (Exception e) {
		// TODO Auto-generated catch block
		ret.put("type", "error");
		ret.put("msg", "�����ļ��쳣��");
		return ret;
	}
	ret.put("type", "success");
	ret.put("msg", "�ϴ��ɹ���");
	ret.put("filePath",request.getServletContext().getContextPath()+"/resource/upload/" + filename );
	return ret;
}
/**
 * �жϸ��û����Ƿ����
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
