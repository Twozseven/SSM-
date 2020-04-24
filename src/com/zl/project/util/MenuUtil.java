package com.zl.project.util;

import java.util.ArrayList;
import java.util.List;

import com.zl.project.entity.admin.Menu;

/**
 * 关于菜单操作的一些共用方法
 * @author www65
 *
 */
public class MenuUtil {
	/**
	 * 从给定的菜单中返回所有顶级菜单
	 * @param menuList
	 * @return
	 */
public static List<Menu> getAllTopMenu(List<Menu> menuList){
	List<Menu> ret = new ArrayList<Menu>();
	for(Menu menu:menuList) {
		if(menu.get_parentId()==0) {
			ret.add(menu);
		}
	}
	return ret;
}
/**
 * 获取所有二级菜单
 * @param menuList
 * @return
 */
public static List<Menu> getAllSecondTopMenu(List<Menu> menuList){
	List<Menu> ret = new ArrayList<Menu>();
	 List<Menu> allTopMenu = getAllTopMenu(menuList);
	
	for(Menu menu:menuList) {
		for(Menu topMenu:allTopMenu) {
			if(menu.get_parentId()== topMenu.getId()) {
				ret.add(menu);
				break;
			}
		}
		
	}
	return ret;
}
/**
 * 获取某个二级菜单下的按钮
 * @param menuList
 * @param url
 * @return
 */
public static List<Menu> getAllThirdMenu(List<Menu> menuList,Long secondMenuId){
	List<Menu> ret = new ArrayList<Menu>();	
	for(Menu menu:menuList) {
		if(menu.get_parentId() == secondMenuId) {
			ret.add(menu);
		}
		
	}
	return ret;
}

}
