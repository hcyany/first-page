package com.ischoolbar.programmer.model;
//成绩实体表
public class Score {

	private int id;
	private int studentId;
	private int courseId;
	private double score;
	private String remark;

	public Score() {
	}

	public Score(int id, int studentId, int courseId, double score, String remark) {
		this.id = id;
		this.studentId = studentId;
		this.courseId = courseId;
		this.score = score;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
