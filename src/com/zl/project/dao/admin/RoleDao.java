package com.zl.project.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zl.project.entity.admin.Role;

/**
 * 角色role dao
 * @author www65
 *
 */
@Repository//表示数据库操作层
public interface RoleDao {
	public int add(Role role);
	public int edit(Role role);
	public int delete(Long id);
	public List<Role> findList(Map<String,Object> queryMap);
	public int getTotal(Map<String,Object> queryMap);
	public Role find(Long id);
}
