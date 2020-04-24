package com.zl.project.dao.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zl.project.entity.admin.Authority;

/**
 * »®œﬁ¿‡dao
 * @author www65
 *
 */
@Repository
public interface AuthorityDao {
	public int add(Authority authority);
	public int deleteByRoleId(Long roleId);
	public List<Authority> findListByRoleId(Long roleId);
}
