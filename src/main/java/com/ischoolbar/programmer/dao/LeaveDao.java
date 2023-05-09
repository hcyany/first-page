package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Leave;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.util.JDBC;

//����б����ݲ���
public class LeaveDao extends BaseDao {
	JDBC jdbc = new JDBC();

	//��������Ϣ
	public boolean addLeave(Leave leave) throws SQLException {
		String sql = "insert into s_leave values(null,?,?,?,?)";
		return jdbc.prepareUpdate(sql,leave.getStudentId(),leave.getInfo(),Leave.LEAVE_STATUS_WAIT,leave.getRemark()) > 0;
	}

	//�༭��ٲ˵�
	public boolean editLeave(Leave leave) throws SQLException {
		String sql = "update s_leave set student_id = ?, info = ?,status = ?,remark = ? where id = ?" ;
		return jdbc.prepareUpdate(sql,leave.getStudentId(),leave.getInfo(),leave.getStatus(),leave.getRemark(),leave.getId()) > 0;
	}

	//ɾ�������Ϣ
	public boolean deleteLeave(int id) throws SQLException {
		String sql ="delete from s_leave where id = ?";
		return jdbc.prepareUpdate(sql,id) > 0;
	}

	//��ȡ��ҳ��ٵ���Ϣ�б�
	public List<Leave> getLeaveList(Leave leave,Page page){
		List<Leave> ret = new ArrayList<Leave>();
		String sql = "select * from s_leave ";
		if(leave.getStudentId() != 0){
			sql += " and student_id = "+leave.getStudentId();
		}
		//sql += " limit " + page.getStart() + "," + page.getPageSize();

		sql = sql.replaceFirst("and", "where");

		try {
				ret = jdbc.queryLeavePage(sql,page.getStart(),page.getPageSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	//��ȡ�ܼ�¼��
	public int getLeaveListTotal(Leave leave) throws SQLException {
		int total = 0;
		String sql = "select count(*) as total from s_leave ";
		if(leave.getStudentId() != 0){
			sql += " and student_id = ? ";
		}

		try {
			total = jdbc.selectResultSet(sql.replaceFirst("and", "where"), leave.getStudentId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	//�鿴�Ƿ����ͨ��
	public  boolean getStatus(int id) throws SQLException {
		String sql = "select status as total from s_leave where id = ? ";
		int status = 0;

		try {
			status = jdbc.selectResultSet(sql,id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(status >0){
			return true;
		}
		return false;
		
	}
}
