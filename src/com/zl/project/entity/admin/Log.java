package com.zl.project.entity.admin;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * 系统日志实体
 * @author www65
 *
 */
@Component
public class Log {
private Long id;
private String content;//日志内容
private Date createTime;//创建时间
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date creatTime) {
	this.createTime = creatTime;
}


}
