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
 * �༶��Ϣ���ݿ����
 * @author �����
 *
 */
public class ClazzDao extends BaseDao {
	JDBC jdbc = new JDBC();

	// ģ����ѯ + ��ҳ
	public List<Clazz> getClazzList(Clazz clazz,Page page) throws SQLException {
		List<Clazz> cl = new ArrayList<Clazz>();
		String sql = "select * from s_clazz ";
		if(!StringUtil.isEmpty(clazz.getName())){
			sql += "where name like '%"+clazz.getName()+"%'";  // '%?%' ����д�Ǵ����,��Ϊ��ʱ��?�����һ����ͨ�ַ���
		}
		// sql += " limit " + page.getStart() + "," + page.getPageSize();

		cl = jdbc.queryClazzPage(sql,page.getStart(),page.getPageSize());
		return cl;
	}

	//ģ����ѯ����
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

	//��Ӱ༶
	public boolean addClazz(Clazz clazz) throws SQLException {
		String sql = "insert into s_clazz values(null,?,?) ";
		return jdbc.prepareUpdate(sql, clazz.getName(), clazz.getInfo()) > 0;
	}

	// ɾ���༶
	public boolean deleteClazz(int id) throws SQLException {
		String sql = "delete from s_clazz where id = ?";
		return jdbc.prepareUpdate(sql,id) > 0;
	}

	// �޸İ༶
	public boolean editClazz(Clazz clazz) throws SQLException {
		String sql = "update s_clazz set name = ?,info = ? where id = ?";
		return jdbc.prepareUpdate(sql, clazz.getName(),clazz.getInfo(),clazz.getId()) > 0;
	}
	
}
