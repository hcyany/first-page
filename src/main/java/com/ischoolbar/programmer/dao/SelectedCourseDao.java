package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.SelectedCourse;
import com.ischoolbar.programmer.util.JDBC;


//选课表数据库操作封装

public class SelectedCourseDao extends BaseDao {
	
	JDBC jdbc = new JDBC();
	public List<SelectedCourse> getSelectedCourseList(SelectedCourse selectedCourse,Page page){
		List<SelectedCourse> ret = new ArrayList<SelectedCourse>();
		String sql = "select * from s_selected_course ";
		if(selectedCourse.getStudentId() != 0){
			sql += " and student_id =  "+selectedCourse.getStudentId();
		}
		if(selectedCourse.getCourseId() != 0){
			sql += " and course_id =  "+selectedCourse.getCourseId();
		}
		// sql += " limit " + page.getStart() + "," + page.getPageSize();
		sql = sql.replaceFirst("and", "where");

		try {
				ret = jdbc.querySelectedCoursePage(sql, page.getStart(),page.getPageSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public int getSelectedCourseListTotal(SelectedCourse selectedCourse) throws SQLException {
		int total = 0;
		String sql = "select count(*) as total from s_selected_course ";
		if(selectedCourse.getStudentId() != 0){
			sql += " and student_id = ? ";
		}
		if(selectedCourse.getCourseId() != 0){
			sql += " and course_id = ? ";
		}
		sql = sql.replaceFirst("and", "where");
		
		try {
			total = jdbc.selectResultSet(sql,selectedCourse.getStudentId(),selectedCourse.getCourseId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	/**
	 * 检查学生是否已经选择该门课程
	 * @param studentId
	 * @param courseId
	 * @return
	 */
	public boolean isSelected(int studentId,int courseId) throws SQLException {
		boolean ret = false;
		String sql = "select * from s_selected_course where student_id = ? and course_id = ? ";
		ResultSet query = jdbc.selectClazzAll(sql,studentId,courseId);
		try {
			if(query.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 添加选课信息
	 * @param selectedCourse
	 * @return
	 */
	public boolean addSelectedCourse(SelectedCourse selectedCourse) throws SQLException {
		String sql = "insert into s_selected_course values(null,?,?)";
		return jdbc.prepareUpdate(sql,selectedCourse.getStudentId(),selectedCourse.getCourseId()) > 0;
	}

	/**
	 * 删除所选课程
	 * @param id
	 * @return
	 */
	public boolean deleteSelectedCourse(int id) throws SQLException {
		String sql = "delete from s_selected_course where id = ?";
		return jdbc.prepareUpdate(sql,id) > 0;
	}
	/**
	 * 获取一条选课数据
	 * @param id
	 * @return
	 */
	public SelectedCourse getSelectedCourse(int id){
		SelectedCourse ret = new SelectedCourse();
		String sql = "select * from s_selected_course where id = ?";

		try {
			ret = jdbc.selectSelectedCourse(sql,id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
