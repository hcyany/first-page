package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Course;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.util.JDBC;
import com.ischoolbar.programmer.util.StringUtil;

//�γ����ݿ������
public class CourseDao extends BaseDao {
	JDBC jdbc = new JDBC();
	
   //���ӿγ�
	public boolean addCourse(Course course) throws SQLException {
		String sql = "insert into s_course values(null,?,?,?,?,?) ";
		return jdbc.prepareUpdate(sql,course.getName(),course.getTeacherId(),course.getCourseDate(),course.getMaxNum(),course.getInfo()) > 0;
	}
	// ɾ���γ�
	public boolean deleteCourse(String ids) throws SQLException {
		String sql = "delete from s_course where id in(?)";
		return jdbc.prepareUpdate(sql,ids) > 0;
	}
	//�޸Ŀγ�
	public boolean editCourse(Course course) throws SQLException {
		String sql = "update s_course set name = ?,teacher_id = ?,course_date = ?,max_num = ? ,info = ? where id = ? ";
		return jdbc.prepareUpdate(sql,course.getName(),course.getTeacherId(),course.getCourseDate(),course.getMaxNum(),course.getInfo(),course.getId()) > 0;
	}

	//��ѯ�γ�
	public List<Course> getCourseList(Course course,Page page) throws SQLException {
		List<Course> ret = new ArrayList<Course>();
		String sql = "select * from s_course ";
		if(!StringUtil.isEmpty(course.getName())){
			sql += "and name like '%" + course.getName() + "%' ";
		}
		if(course.getTeacherId() != 0){
			sql += "and teacher_id = "+ course.getTeacherId();
		}
		// sql += " limit " + page.getStart() + "," + page.getPageSize();
		sql = sql.replaceFirst("and", "where");

		try {
			ret = jdbc.queryCoursePage(sql, page.getStart(), page.getPageSize());
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	//ģ����ѯ����
	public int getCourseListTotal(Course course) throws SQLException {
		int total = 0;
		String sql = "select count(*) as total from s_course ";
		if(!StringUtil.isEmpty(course.getName())){
			sql += "and name like '%" + course.getName() + "%' ";
		}
		if(course.getTeacherId() != 0){
			sql += " and teacher_id = ?";
		}
		sql = sql.replaceFirst("and", "where");


		try {
			total = jdbc.selectResultSet(sql,  course.getTeacherId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	// ���ÿγ��Ƿ���ѡ��
	public boolean isFull(int courseId) throws SQLException {
		boolean ret = false;
		String sql = "select * from s_course where selected_num >= max_num and id = ?";
		ResultSet query = jdbc.selectClazzAll(sql,courseId);

		try {
			if(query.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}


	//���¿γ���ѡ����
	public void updateCourseSelectedNum(int courseId ,int num) throws SQLException {
		String sql = "";
		if(num > 0){
			sql = "update s_course set selected_num = selected_num + ? where id = ?" ;
		}else{
			sql = "update s_course set selected_num = selected_num - ? where id = ?" ;
		}
		jdbc.prepareUpdate(sql, num, courseId, Math.abs(num), courseId);
	}
	
	//��ȡ�ƶ�id��Χ�ڵĿγ��б�
	public List<Course> getCourse(String ids) throws SQLException {
		List<Course> ret = new ArrayList<Course>();
		String sql = "select * from s_course where id in("+ids+")";

		try {
			ret = jdbc.selectCourse(sql);
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}


	//��ȡָ���Ŀγ�
	public Course getCourse(int id){
		Course course = new Course();
		String sql = "select * from s_course where id = ?";

		try {
				course = jdbc.selectByCno(sql,id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return course;
	}
}
