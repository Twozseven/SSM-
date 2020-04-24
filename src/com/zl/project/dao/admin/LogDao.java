package com.zl.project.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zl.project.entity.admin.Authority;
import com.zl.project.entity.admin.Log;

/**
 * 系统日志dao
 * @author www65
 *
 */
@Repository
public interface LogDao {
	public int add(Log log);
	public List<Log> findList(Map<String,Object>queryMap);
	public int getTotal(Map<String,Object> queryMap);
	public int delete(String ids);
}
