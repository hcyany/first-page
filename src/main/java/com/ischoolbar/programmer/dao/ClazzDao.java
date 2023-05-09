package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Clazz;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.util.JDBC;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * 班级信息数据库操作
 * @author 赵燕军
 *
 */
public class ClazzDao extends BaseDao {
	JDBC jdbc = new JDBC();

	// 模糊查询 + 分页
	public List<Clazz> getClazzList(Clazz clazz,Page page) throws SQLException {
		List<Clazz> cl = new ArrayList<Clazz>();
		String sql = "select * from s_clazz ";
		if(!StringUtil.isEmpty(clazz.getName())){
			sql += "where name like '%"+clazz.getName()+"%'";  // '%?%' 这样写是错误的,因为此时的?变成了一个普通字符。
		}
		// sql += " limit " + page.getStart() + "," + page.getPageSize();

		cl = jdbc.queryClazzPage(sql,page.getStart(),page.getPageSize());
		return cl;
	}

	//模糊查询个数
	public int getClazzListTotal(Clazz clazz) throws SQLException {
		int total = 0;
		String sql = "select count(*) as total from s_clazz ";
		if(!StringUtil.isEmpty(clazz.getName())){
			sql += "where name like '%" + clazz.getName() + "%' ";
		}

		try {
			total = jdbc.selectResultSet(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	//添加班级
	public boolean addClazz(Clazz clazz) throws SQLException {
		String sql = "insert into s_clazz values(null,?,?) ";
		return jdbc.prepareUpdate(sql, clazz.getName(), clazz.getInfo()) > 0;
	}

	// 删除班级
	public boolean deleteClazz(int id) throws SQLException {
		String sql = "delete from s_clazz where id = ?";
		return jdbc.prepareUpdate(sql,id) > 0;
	}

	// 修改班级
	public boolean editClazz(Clazz clazz) throws SQLException {
		String sql = "update s_clazz set name = ?,info = ? where id = ?";
		return jdbc.prepareUpdate(sql, clazz.getName(),clazz.getInfo(),clazz.getId()) > 0;
	}
	
}
