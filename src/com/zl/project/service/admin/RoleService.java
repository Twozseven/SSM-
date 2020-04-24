package com.zl.project.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zl.project.entity.admin.Role;

/**
 * 角色role service
 * @author www65
 *
 */
@Service//自动加载到容器之中
public interface RoleService {
public int add(Role role);
public int edit(Role role);
public int delete(Long id);
public List<Role> findList(Map<String,Object> queryMap);
public int getTotal(Map<String,Object> queryMap);
public Role find(Long id);
}
