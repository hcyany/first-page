package com.ischoolbar.programmer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.Teacher;
import com.ischoolbar.programmer.util.JDBC;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * TODO
 *教师表数据库操作
 */
public class TeacherDao extends BaseDao {
	JDBC jdbc = new JDBC();

	public boolean addTeacher(Teacher teacher) throws SQLException {
		String sql = "insert into s_teacher values(null,?,?,?,?,?,?,?,null)";
		return jdbc.prepareUpdate(sql,teacher.getSn(),teacher.getName(),teacher.getPassword(),teacher.getClazzId(),teacher.getSex(),teacher.getMobile(),teacher.getQq()) > 0;
	}

	public boolean editTeacher(Teacher teacher) throws SQLException {

		String sql = "update s_teacher set name = ?,sex = ?,mobile = ?,qq = ?,clazz_id = ? where id = ? ";
		return jdbc.prepareUpdate(sql,teacher.getName(),teacher.getSex(),teacher.getMobile(),teacher.getQq(),teacher.getClazzId(),teacher.getId()) > 0;

	}

	public boolean setTeacherPhoto(Teacher teacher) {
		String sql = "update s_teacher set photo = ? where id = ?";
		Connection connection = getConnection();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setBinaryStream(1, teacher.getPhoto());
			prepareStatement.setInt(2, teacher.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return update(sql);
	}

	public boolean deleteTeacher(String ids) throws SQLException {
		String sql = "delete from s_teacher where id in(?)";
		return jdbc.prepareUpdate(sql,ids) > 0;
	}

	public Teacher getTeacher(int id){
		String sql = "select * from s_teacher where id = ?";
		Teacher teacher = new Teacher();

		try {
			teacher = jdbc.selectTeacher(sql,id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teacher;
	}

	public List<Teacher> getTeacherList(Teacher teacher,Page page){
		List<Teacher> ret = new ArrayList<Teacher>();
		String sql = "select * from s_teacher ";
		if(!StringUtil.isEmpty(teacher.getName())){
			sql += "and name like '%" + teacher.getName() + "%' ";
		}
		if(teacher.getClazzId() != 0){
			sql += " and clazz_id = "+teacher.getClazzId();
		}
		if(teacher.getId() !=0 ){
			sql += " and id =  "+teacher.getId();
		}
		// sql += " limit " + page.getStart() + "," + page.getPageSize();
		sql = sql.replaceFirst("and", "where");


		try {
			ret = jdbc.queryTeacherPage(sql,page.getStart(),page.getPageSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}


	public int getTeacherListTotal(Teacher teacher) throws SQLException {
		int total = 0;
		String sql = "select count(*) as total from s_teacher ";
		if(!StringUtil.isEmpty(teacher.getName())){
			sql += "and name like '%" + teacher.getName() + "%' ";
		}
		if(teacher.getClazzId() != 0){
			sql += " and clazz_id =  "+teacher.getClazzId();
		}
		if(teacher.getId() !=0 ){
			sql += " and id =  "+teacher.getId();
		}
		sql = sql.replaceFirst("and", "where");

		try {
			total = jdbc.selectResultSet(sql);


		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}


	public Teacher login(String name ,String password){
		String sql = "select * from s_teacher where name = ? and password = ?";
		try {
			return jdbc.selectTeacher(sql,name,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean editPassword(Teacher teacher,String newPassword) throws SQLException {
		String sql = "update s_teacher set password = ? where id = ?" ;
		return jdbc.prepareUpdate(sql,newPassword,teacher.getId()) > 0;
	}
}
