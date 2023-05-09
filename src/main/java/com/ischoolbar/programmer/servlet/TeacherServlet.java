package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.dao.TeacherDao;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.Teacher;
import com.ischoolbar.programmer.util.SnGenerateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TeacherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TeacherServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String method = request.getParameter("method");
		if("toTeacherListView".equals(method)){
			teacherList(request,response);
		}else if("AddTeacher".equals(method)){
			try {
				addTeacher(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if("TeacherList".equals(method)){
			try {
				getTeacherList(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if("EditTeacher".equals(method)){
			try {
				editTeacher(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if("DeleteTeacher".equals(method)){
			try {
				deleteTeacher(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}
	
	private void addTeacher(HttpServletRequest request,HttpServletResponse response) throws SQLException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String mobile = request.getParameter("mobile");
		String qq = request.getParameter("qq");
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));
		Teacher teacher = new Teacher();
		teacher.setClazzId(clazzId);
		teacher.setMobile(mobile);
		teacher.setName(name);
		teacher.setPassword(password);
		teacher.setSex(sex);
		teacher.setQq(qq);
		teacher.setSn(SnGenerateUtil.generateTeacherSn(clazzId));
		TeacherDao teacherDao = new TeacherDao();
		if(teacherDao.addTeacher(teacher)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				teacherDao.closeCon();
			}
		}
		
	}
	
	private void deleteTeacher(HttpServletRequest request,HttpServletResponse response) throws SQLException {
		
		String[] ids = request.getParameterValues("ids[]");
		String idStr = "";
		for(String id:ids){
			idStr += id+",";
		}
		idStr = idStr.substring(0,idStr.length()-1);
		TeacherDao teacherDao = new TeacherDao();
		if(teacherDao.deleteTeacher(idStr)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				teacherDao.closeCon();
			}
		}
	}
	
	private void editTeacher(HttpServletRequest request,HttpServletResponse response) throws SQLException {
		
		String name = request.getParameter("name");
		int id = Integer.parseInt(request.getParameter("id"));
		String sex = request.getParameter("sex");
		String mobile = request.getParameter("mobile");
		String qq = request.getParameter("qq");
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));
		
		Teacher teacher = new Teacher();
		teacher.setClazzId(clazzId);
		teacher.setMobile(mobile);
		teacher.setName(name);
		teacher.setId(id);
		teacher.setQq(qq);
		teacher.setSex(sex);
		
		TeacherDao teacherDao = new TeacherDao();		
		if(teacherDao.editTeacher(teacher)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				teacherDao.closeCon();
			}
		}
	}
	
	private void getTeacherList(HttpServletRequest request,HttpServletResponse response) throws SQLException {
	
		String name = request.getParameter("teacherName");
		Integer currentPage = request.getParameter("page") ==null ? 1 :Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Integer clazz = request.getParameter("clazzid") ==null ? 0 : Integer.parseInt(request.getParameter("clazzid"));
		//获取当前登录状态用户类型
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		Teacher teacher = new Teacher();
		teacher.setName(name);
		teacher.setClazzId(clazz);
		if(userType==3){
			Teacher currentUser = (Teacher)request.getSession().getAttribute("user");
			teacher.setId(currentUser.getId());
		}
		TeacherDao teacherDao = new TeacherDao();
		List<Teacher> teacherList = teacherDao.getTeacherList(teacher, new Page(currentPage, pageSize));
		int total = teacherDao.getTeacherListTotal(teacher);
		teacherDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", teacherList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)){
				response.getWriter().write(JSONArray.fromObject(teacherList).toString());
			}else{
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void teacherList(HttpServletRequest request,HttpServletResponse response){
		
		try {
			request.getRequestDispatcher("view/teacherList.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
