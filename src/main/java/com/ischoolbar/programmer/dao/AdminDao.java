package com.ischoolbar.programmer.dao;

import java.sql.SQLException;
import com.ischoolbar.programmer.model.Admin;
import com.ischoolbar.programmer.util.JDBC;


/**
 * ����Ա���ݷ�װ
 * @author �����
 */
public class AdminDao extends BaseDao{
	JDBC jdbc = new JDBC();

	//����Ա��¼
    public Admin login(String name,String password) throws SQLException {
    	String sql = "select * from s_admin where name = ? and password = ?";

    	//ResultSet ret = query(sql);
		Admin ret = jdbc.selectAdmin(sql,name,password);
    	try {
				Admin admin = new Admin();
				admin.setId(ret.getId());
				admin.setName(ret.getName());
				admin.setPassword(ret.getPassword());
				admin.setStatus(ret.getStatus());
				return admin;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }

	//�޸Ĺ���Ա����
    public boolean editPassword(Admin admin,String newPassword) throws SQLException {
    	String sql = "update s_admin set password = ? where id = ?";
    	// return update(sql);
    	return jdbc.prepareUpdate(sql,newPassword,admin.getId()) > 0;
    }


}
