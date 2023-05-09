package com.ischoolbar.programmer.model;
//¿¼ÇÚ±í
public class Attendance {
	
	private int id;
	private int courseId;
	private int studentId;
	private String type;
	private String date;

	public Attendance() {
	}

	public Attendance(int id, int courseId, int studentId, String type, String date) {
		this.id = id;
		this.courseId = courseId;
		this.studentId = studentId;
		this.type = type;
		this.date = date;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
