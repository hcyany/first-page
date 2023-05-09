package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Attendance;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.util.JDBC;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * ������Ϣ���ݿ����
 *
 */
public class AttendanceDao extends BaseDao {
	JDBC jdbc = new JDBC();
	
	/**
	 * ��ӿ�����Ϣ
	 * @param attendance
	 * @return
	 */
	public boolean addAttendance(Attendance attendance) throws SQLException {
		String sql = "insert into s_attendance values(null,?,?,?,?)";
		
		return jdbc.prepareUpdate(sql,attendance.getCourseId(),attendance.getStudentId(),attendance.getType(),attendance.getDate()) > 0;
		
	}
	
	/**
	 * �жϵ�ǰ�Ƿ���ǩ��
	 * @param studentId
	 * @param courseId
	 * @param type
	 * @return
	 */
	public boolean isAttendance(int studentId, int courseId, String type, String date) throws SQLException {
		boolean ret = false;
		String sql = "select * from s_attendance where student_id = ? and course_id = ? and type = ? and date = ?";


		Attendance attendance = JDBC.selectAttendance(sql, studentId, courseId, type, date);
		try {
			if(attendance != null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * ��ȡָ���Ŀ�����Ϣ�б�
	 * @param attendance
	 * @param page
	 * @return
	 */
	public List<Attendance> getSelectedCourseList(Attendance attendance,Page page) throws SQLException {
		List<Attendance> ret = new ArrayList<Attendance>();
		String sql = "select * from s_attendance";
		if(attendance.getStudentId() != 0){
			sql += " and student_id = "+attendance.getStudentId();
		}
		if(attendance.getCourseId() != 0){
			sql += " and course_id = "+attendance.getCourseId();
		}
		if(!StringUtil.isEmpty(attendance.getType())){
			sql += " and type = "+attendance.getType();
		}
		if(!StringUtil.isEmpty(attendance.getDate())){
			sql += " and date = "+attendance.getDate();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		sql = sql.replaceFirst("and", "where");


		try {
			ret = jdbc.queryAttendancePage(sql);
				return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * ��ȡ����������¼����
	 * @param attendance
	 * @return
	 */
	public int getAttendanceListTotal(Attendance attendance){
		int total = 0;
		String sql = "select count(*) as total from s_attendance ";
		if(attendance.getStudentId() != 0){
			sql += " and student_id = "+attendance.getStudentId();
		}
		if(attendance.getCourseId() != 0){
			sql += " and course_id = "+attendance.getCourseId();
		}
		sql = sql.replaceFirst("and", "where");

		try {
			total = jdbc.selectResultSet(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * ɾ��
	 * @param id
	 * @return
	 */
	public boolean deleteAttendance(int id) throws SQLException {
		String sql = "delete from s_attendance where id = "+id;
		return jdbc.prepareUpdate(sql) > 0;
	}
}
