package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.Score;
import com.ischoolbar.programmer.util.JDBC;

//成绩表数据库基础操作
public class ScoreDao extends BaseDao {
	
	JDBC jdbc = new JDBC();

	//成绩录入
	public boolean addScore(Score score ) throws SQLException {
		String sql = "insert into s_score values(null,?,?,?,?)";
	    return jdbc.prepareUpdate(sql,score.getStudentId(),score.getCourseId(),score.getScore(),score.getRemark()) > 0;
	}
	//删除成绩
	public boolean deleteScore(int id) throws SQLException {
		String sql ="delete from s_score where id = ?";
		return jdbc.prepareUpdate(sql,id) > 0;
	}
	//修改成绩
	public boolean editScore(Score score) throws SQLException {
		String sql = "update s_score set student_id = ? ,course_id = ? ,score = ?, remark = ? where id = ? ";
		return jdbc.prepareUpdate(sql,score.getStudentId(),score.getCourseId(),score.getScore(),score.getRemark(),score.getId()) > 0;
	}
	//判断成绩是否录入
	public boolean isAdd(int studentId,int courseId) throws SQLException {
		String sql = "select * from s_score where student_id = ? and course_id = ?";
		ResultSet query = jdbc.selectClazzAll(sql,studentId,courseId);
		try {
			if(query.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//分页获取成绩列表
	public List<Score> getScoreList(Score score,Page page){
		List<Score> ret = new ArrayList<Score>();
		String sql = "select * from s_score ";
		if(score.getStudentId() != 0){
			sql += "and student_id =  "+score.getStudentId();
		}
		if(score.getCourseId() != 0){
			sql += "and course_id =  "+score.getCourseId();
		}
		// sql += " limit ? page.getStart() + ",? page.getPageSize();
		sql = sql.replaceFirst("and", "where");

		try {
				ret = jdbc.queryScorePage(sql,page.getStart(), page.getPageSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	//获取符合某一条件的所有成绩列表
	public List<Map<String, Object>> getScoreList(Score score){
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		String sql = "select s_score.*,s_student.name as studentName,s_course.name as courseName from " +
				"s_score,s_student,s_course where s_score.student_id=s_student.id and " +
				"s_score.course_id=s_course.id ";
		if(score.getStudentId()!=0){
			sql += " and student_id = ?";
		}
		if(score.getCourseId()!=0){
			sql += " and course_id = ?";
		}

		try {
			ret = jdbc.selectScore(sql, score.getStudentId(), score.getCourseId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}


	//获取成绩总记录数
	public int getScoreListTotal(Score score) throws SQLException {
		int total = 0;
		String sql = "select count(*) as total from s_score ";
		if(score.getStudentId() != 0){
			sql += " and student_id = ? ";
		}
		if(score.getCourseId() != 0){
			sql += " and course_id = ? ";
		}
		sql = sql.replaceFirst("and", "where");

		try {
			total = jdbc.selectResultSet(sql, score.getStudentId(), score.getCourseId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}


	//计算学生平均成绩
	public Map<String, Object> getAvgStats(Score score) throws SQLException {
		Map<String,Object> ret = new HashMap<String, Object>();
		String sql = "select max(s_score.score) as max_score," +
				"avg(s_score.score) as avg_score," +
				"min(s_score.score) as min_score," +
				"s_course.name as courseName from " +
				"s_score,s_course where " +
				"s_score.course_id=s_course.id and s_score.course_id =  "+score.getCourseId();

		try {
			ret = jdbc.selectResultSetAvg(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
