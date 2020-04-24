package com.zl.project.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zl.project.entity.admin.Authority;

/**
 * Ȩ��service�ӿ�
 * @author www65
 *
 */
@Service
public interface AuthorityService {
public int add(Authority authority);
public int deleteByRoleId(Long roleId);
public List<Authority> findListByRoleId(Long roleId);
}
