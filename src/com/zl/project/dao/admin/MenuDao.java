package com.zl.project.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zl.project.entity.admin.Menu;

/**
 * ≤Àµ•π‹¿Ìdao
 * @author www65
 *
 */
@Repository
public interface MenuDao {
	public int add (Menu menu);
	public List<Menu>findList(Map<String,Object>queryMap);
	public List<Menu>findTopList();
	public int getTotal(Map<String,Object>queryMap);
	public int edit (Menu menu);
	public int delete(Long id);
	public List<Menu>findchildrenList(Long parentId);
	public List<Menu>findListByIds(String ids);
}
