package com.zl.project.page.admin;

import org.springframework.stereotype.Component;

/**
 * 分页基本信息
 * @author www65
 *
 */
@Component
public class page {
private int page = 1;//当前页码
private int rows;//每页显示数量
private int offset;//对应数据库中偏移量
public int getPage() {
	return page;
}
public void setPage(int page) {
	this.page = page;
}
public int getRows() {
	return rows;
}
public void setRows(int rows) {
	this.rows = rows;
}
public int getOffset() {
	this.offset = (page -1)*rows;
	return offset;
}
public void setOffset(int offset) {
	
	this.offset = offset;
}
}
