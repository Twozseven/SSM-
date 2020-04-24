package com.zl.project.util;

import java.util.ArrayList;
import java.util.List;

import com.zl.project.entity.admin.Menu;

/**
 * ���ڲ˵�������һЩ���÷���
 * @author www65
 *
 */
public class MenuUtil {
	/**
	 * �Ӹ����Ĳ˵��з������ж����˵�
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
 * ��ȡ���ж����˵�
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
 * ��ȡĳ�������˵��µİ�ť
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
