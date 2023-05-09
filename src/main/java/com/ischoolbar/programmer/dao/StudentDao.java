package com.ischoolbar.programmer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Admin;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.Student;
import com.ischoolbar.programmer.util.JDBC;
import com.ischoolbar.programmer.util.StringUtil;

public class StudentDao extends BaseDao {

	JDBC jdbc = new JDBC();
	//添加学生
	public boolean addStudent(Student student) throws SQLException {
//		String sql = "insert into s_student values(null,'"+student.getSn()+"','"+student.getName()+"'";
//		sql += ",'" + student.getPassword() + "'," + student.getClazzId();
//		sql += ",'" + student.getSex() + "','" + student.getMobile() + "'";
//		sql += ",'" + student.getQq() + "',null)";

		String sql = "insert into s_student values(null,?,?,?,?,?,?,?,null)";

		return jdbc.prepareUpdate(sql,student.getSn(),student.getName(),
				student.getPassword(),student.getClazzId(),student.getSex(),
				student.getMobile(),student.getQq()) > 0;
	}

	//修改学生
	public boolean editStudent(Student student) throws SQLException {

//		String sql = "update s_student set name = '"+student.getName()+"'";
//		sql += ",sex = '" + student.getSex() + "'";
//		sql += ",mobile = '" + student.getMobile() + "'";
//		sql += ",qq = '" + student.getQq() + "'";
//		sql += ",clazz_id = " + student.getClazzId();
//		sql += " where id = " + student.getId();

		String sql = "update s_student set name = ?, sex = ?, mobile = ?, qq = ?, clazz_id = ? where id = ?";

		return jdbc.prepareUpdate(sql,student.getName(),student.getSex(),
				student.getMobile(),student.getQq(),student.getClazzId(),
				student.getId()) > 0;
	}

	//  修改学生照片
	public boolean setStudentPhoto(Student student) throws SQLException {
		String sql = "update s_student set photo = ? where id = ?";
		Connection connection = getConnection();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setBinaryStream(1, student.getPhoto());
			prepareStatement.setInt(2, student.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jdbc.prepareUpdate(sql) > 0;
	}

	// 批量删除
	public boolean deleteStudent(String ids) throws SQLException {
		String sql = "delete from s_student where id in("+ids+")";
		return jdbc.prepareUpdate(sql) > 0;
	}

	//  查询学生信息
	public Student getStudent(int id) throws SQLException {
		String sql = "select * from s_student where id = ?";
		Student student = new Student();
		student = jdbc.select(sql,id);
		return student;
	}

	// 分页查询
	public List<Student> getStudentList(Student student,Page page){
		List<Student> ret = new ArrayList<Student>();
		String sql = "select * from s_student ";
		if(!StringUtil.isEmpty(student.getName())){
			sql += "and name like '%" + student.getName() + "%'";
		}
		if(student.getClazzId() != 0){
			sql += " and clazz_id = " + student.getClazzId();
		}
		if(student.getId() != 0){
			sql += " and id = " + student.getId();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				Student s = new Student();
				s.setId(resultSet.getInt("id"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				s.setMobile(resultSet.getString("mobile"));
				s.setName(resultSet.getString("name"));
				s.setPassword(resultSet.getString("password"));
				s.setPhoto(resultSet.getBinaryStream("photo"));
				s.setQq(resultSet.getString("qq"));
				s.setSex(resultSet.getString("sex"));
				s.setSn(resultSet.getString("sn"));
				ret.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	// 模糊查询
	public int getStudentListTotal(Student student){
		int total = 0;
		String sql = "select count(*) as total from s_student ";
		if(!StringUtil.isEmpty(student.getName())){
			sql += "and name like '%" + student.getName() + "%'";
		}
		if(student.getClazzId() != 0){
			sql += " and clazz_id = " + student.getClazzId();
		}
		if(student.getId() != 0){
			sql += " and id = " + student.getId();
		}
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	//  学生登录
	public Student login(String name ,String password) throws SQLException {
		String sql = "select * from s_student where name = ? and password = ?";
		Student student = new Student();
		try {
			student = jdbc.select(sql,name,password);
				return student;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 修改密码
	public boolean editPassword(Student student,String newPassword) throws SQLException {
		String sql = "update s_student set password = ? where id = ? ";
		return jdbc.prepareUpdate(sql, newPassword, student.getId()) > 0;
	}
}
