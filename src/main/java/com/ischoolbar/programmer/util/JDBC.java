package com.ischoolbar.programmer.util;
import com.ischoolbar.programmer.model.*;


import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JDBC {
    private static Connection conn;
    private static PreparedStatement stmt;
    private static ResultSet rs;
    private static PreparedStatement pstmt;

    private DbUtil dbUtil = new DbUtil();
    /**
     * 连接数据库
     */
    public static Connection getConnection() {
        try {
            Class.forName(Contents.driver);        //加载mysql驱动
            System.out.println(Contents.driver + "加载成功！");
        } catch (ClassNotFoundException e) {
            System.out.println(Contents.driver + "加载失败(╯﹏╰)");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(Contents.url, Contents.username, Contents.password);        //连接数据库
            System.out.println(Contents.url + "连接成功！");
        } catch (SQLException e) {
            System.out.println(Contents.url + "连接失败(╯﹏╰)");
            e.printStackTrace();
        }
        return conn;
    }
    /**
     * 关闭数据库连接
     *
     */
    public static void closeConnection() {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }        //关闭数据库
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }        //关闭数据库
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 查询操作
     * @param sql 查询语句
     * @param params 参数列表
     * @return 查询结果
     * @throws SQLException SQL异常
     */
    public static List<Student> query(String sql, Object... params) throws SQLException {
        List<Student> stu = new ArrayList<Student>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String sn = rs.getString("sn");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int clazzId = rs.getInt("clazzId");
                String sex = rs.getString("sex");
                String mobile = rs.getString("mobile");
                String qq = rs.getString("qq");
                InputStream photo = rs.getBinaryStream("photo");
                Student st = new Student(id, sn, name, password, clazzId, sex, mobile, qq, photo);
                stu.add(st);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return stu;
    }
    public static List<Course> selectCourse(String sql, Object... params) throws SQLException {
        List<Course> coursed = new ArrayList<Course>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int teacherId = rs.getInt("teacher_id");
                String courseDate = rs.getString("course_date");
                int selectedNum = rs.getInt("selected_num");
                int maxNum = rs.getInt("max_num");
                String info = rs.getString("info");

                Course course = new Course(id, name, teacherId, courseDate, selectedNum, maxNum, info);
                coursed.add(course);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return coursed;
    }
    public static List<Teacher> selectTeacherAll(String sql, Object... params) throws SQLException {
        List<Teacher> teacher = new ArrayList<Teacher>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String sn = rs.getString("sn");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int clazzId = rs.getInt("clazzId");
                String sex = rs.getString("sex");
                String mobile = rs.getString("mobile");
                String qq = rs.getString("qq");
                InputStream photo = rs.getBinaryStream("photo");

                Teacher tea = new Teacher(id, sn, name, password, clazzId, sex, mobile, qq, photo);

                teacher.add(tea);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return teacher;
    }
    public static List<Map<String, Object>> selectScore(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> scores = new ArrayList<>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if((Integer) params[j] == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("studentId");
                int courseId = rs.getInt("courseId");
                double score = rs.getDouble("score");
                String remark = rs.getString("remark");
                String studentName = rs.getString("studentName");
                String courseName = rs.getString("courseName");

                Map<String, Object> s = new HashMap<String, Object>();
                s.put("id",id);
                s.put("courseId",courseId);
                s.put("studentId",studentId);
                s.put("score",score);
                s.put("remark",remark);
                s.put("studentName", studentName);
                s.put("courseName", courseName);

                scores.add(s);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return scores;
    }

    public static Student select(String sql, Object... params) throws SQLException {
        Student stu = null;
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
                rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String sn = rs.getString("sn");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int clazzId = rs.getInt("clazz_id");
                String sex = rs.getString("sex");
                String mobile = rs.getString("mobile");
                String qq = rs.getString("qq");
                InputStream photo = rs.getBinaryStream("photo");
                stu = new Student(id, sn, name, password, clazzId, sex, mobile, qq, photo);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return stu;
    }

    public static Admin selectAdmin(String sql, Object... params) throws SQLException {
        Admin stu = null;
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int status = rs.getInt("status");
                stu = new Admin(id, name, password, status);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return stu;
    }
    public static SelectedCourse selectSelectedCourse(String sql, Object... params) throws SQLException {
        SelectedCourse stu = null;
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("studentId");
                int courseId = rs.getInt("courseId");

                stu = new SelectedCourse(id, studentId, courseId);


            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return stu;
    }



    public static Attendance selectAttendance(String sql, Object... params) throws SQLException {
        Attendance att = null;
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int courseId = rs.getInt("courseId");
                int studentId = rs.getInt("studentId");
                String type = rs.getString("type");
                String date = rs.getString("date");
                att = new Attendance(id, courseId, studentId, type, date);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return att;
    }
    public static Teacher selectTeacher(String sql, Object... params) throws SQLException {
        Teacher att = null;
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String sn = rs.getString("sn");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int clazzId = rs.getInt("clazz_id");
                String sex = rs.getString("sex");
                String mobile = rs.getString("mobile");
                String qq = rs.getString("qq");
                InputStream photo = rs.getBinaryStream("photo");

                att = new Teacher(id, sn, name, password, clazzId, sex, mobile, qq, photo);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return att;
    }


    public static ResultSet selectClazzAll(String sql, Object... params) throws SQLException {
        conn = null;
        pstmt = null;
        rs = null;
        conn = DBConn.getConnection();
        pstmt = conn.prepareStatement(sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }

        return pstmt.executeQuery();

    }

    public static int selectResultSet(String sql, Object... params) throws SQLException {
        int total = 0;
        conn = null;
        pstmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值

                        if((Integer) params[j] == 0 || StringUtil.isEmpty((String)params[j])){
                            i--;
                            continue;
                        }
                pstmt.setObject(i + 1, params[i]);
                }
            }

            rs = pstmt.executeQuery();
            while(rs.next()){
                // 检查rs是否为null
                if (rs == null) {
                    System.out.println("ResultSet对象为空");
                    return total;
                }

                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return total;
    }
    public static Map<String,Object> selectResultSetAvg(String sql, Object... params) throws SQLException {
        Map<String,Object> ret = new HashMap<String, Object>();
        conn = null;
        pstmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }

            rs = pstmt.executeQuery();
            while(rs.next()){
                ret.put("max_score",rs.getDouble("max_score"));
                ret.put("avg_score",rs.getDouble("avg_score"));
                ret.put("min_score",rs.getDouble("min_score"));
                ret.put("courseName",rs.getString("courseName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return ret;
    }

    public static Student selectBySno(String sql, String sno) throws SQLException {
        Student stu = null;
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setObject(1, sno);

            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String sn = rs.getString("sn");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int clazzId = rs.getInt("clazzId");
                String sex = rs.getString("sex");
                String mobile = rs.getString("mobile");
                String qq = rs.getString("qq");
                InputStream photo = rs.getBinaryStream("photo");
                stu = new Student(id, sn, name, password, clazzId, sex, mobile, qq, photo);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return stu;
    }
    public static Course selectByCno(String sql, int id) throws SQLException {
        Course course = null;
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setObject(1, id);

            rs = stmt.executeQuery();
            while(rs.next()) {
                // int id = rs.getInt("id");
                String name = rs.getString("name");
                int teacherId = rs.getInt("teacherId");
                String courseDate = rs.getString("courseDate");
                int selectedNum = rs.getInt("selectedNum");
                int maxNum = rs.getInt("maxNum");
                String info = rs.getString("info");
                course = new Course( id,  name,  teacherId,  courseDate,  selectedNum,  maxNum, info);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return course;
    }
    /**
     * 分页查询操作
     * @param sql 查询语句
     * @param startIndex 开始索引
     * @param pageSize 分页大小
     * @param params 参数列表
     * @return 查询结果
     * @throws SQLException SQL异常
     */
    public List<Student> queryPage(String sql, int startIndex, int pageSize, Object... params) throws SQLException {
        List<Student> stu = new ArrayList<Student>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql + " LIMIT ?, ?");
            int i = 0;
            if (params != null) {
                for (int j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            //assert params != null;
            stmt.setInt(i + 1, startIndex);
            stmt.setInt(i + 2, pageSize);
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String sn = rs.getString("sn");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int clazzId = rs.getInt("clazz_id");
                String sex = rs.getString("sex");
                String mobile = rs.getString("mobile");
                String qq = rs.getString("qq");
                InputStream photo = rs.getBinaryStream("photo");
                Student st = new Student(id, sn, name, password, clazzId, sex, mobile, qq, photo);
                stu.add(st);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return stu;
    }

    public static List<Course> queryCoursePage(String sql, int startIndex, int pageSize, Object... params) throws SQLException {
        List<Course> coursed = new ArrayList<Course>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql + " LIMIT ?, ?");
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    try {
                        if((Integer) params[j] == 0 || StringUtil.isEmpty((String)params[j])){
                            i--;
                            continue;
                        }
                    } catch (Exception e) {
                        if((Integer) params[j] == 0){
                            i--;
                            continue;
                        }
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            stmt.setInt(params.length + 1, startIndex);
            stmt.setInt(params.length + 2, pageSize);
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int teacherId = rs.getInt("teacher_id");
                String courseDate = rs.getString("course_date");
                int selectedNum = rs.getInt("selected_num");
                int maxNum = rs.getInt("max_num");
                String info = rs.getString("info");

                Course course = new Course(id, name, teacherId, courseDate, selectedNum, maxNum, info);
                coursed.add(course);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return coursed;
    }
    public List<Clazz> queryClazzPage(String sql, int startIndex, int pageSize, Object... params) throws SQLException {
        List<Clazz> clazzs = new ArrayList<Clazz>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql + " LIMIT ?, ?");
            int i = 0;
            if (params != null) {
                for (int j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            //assert params != null;
            stmt.setInt(i + 1, startIndex);
            stmt.setInt(i + 2, pageSize);
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String info = rs.getString("info");
                Clazz clazz = new Clazz(id, name, info);
                clazzs.add(clazz);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return clazzs;
    }
    public List<Leave> queryLeavePage(String sql, int startIndex, int pageSize, Object... params) throws SQLException {
        List<Leave> leaves = new ArrayList<Leave>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql + " LIMIT ?, ?");
            int i = 0;
            if (params != null) {
                for (int j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if((Integer) params[j]== 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            //assert params != null;
            stmt.setInt(i + 1, startIndex);
            stmt.setInt(i + 2, pageSize);
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                String info = rs.getString("info");
                String remark = rs.getString("remark");
                int status = rs.getInt("status");
                Leave leave = new Leave(id, studentId, info, status, remark);
                leaves.add(leave);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return leaves;
    }
    public List<Teacher> queryTeacherPage(String sql, int startIndex, int pageSize, Object... params) throws SQLException {
        List<Teacher> teacher = new ArrayList<Teacher>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql + " LIMIT ?, ?");
            int i = 0;
            if (params != null) {
                for (int j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if((Integer) params[j] ==0 ){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            //assert params != null;
            stmt.setInt(i + 1, startIndex);
            stmt.setInt(i + 2, pageSize);
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String sn = rs.getString("sn");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int clazzId = rs.getInt("clazz_id");
                String sex = rs.getString("sex");
                String mobile = rs.getString("mobile");
                String qq = rs.getString("qq");
                InputStream photo = rs.getBinaryStream("photo");

                Teacher tea = new Teacher(id, sn, name, password, clazzId, sex, mobile, qq, photo);
                teacher.add(tea);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return teacher;
    }
    public List<SelectedCourse> querySelectedCoursePage(String sql, int startIndex, int pageSize, Object... params) throws SQLException {
        List<SelectedCourse> scores = new ArrayList<SelectedCourse>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql + " LIMIT ?, ?");
            int i = 0;
            if (params != null) {
                for (int j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if((Integer) params[j] == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            //assert params != null;
            stmt.setInt(i + 1, startIndex);
            stmt.setInt(i + 2, pageSize);
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                int courseId = rs.getInt("course_id");

                SelectedCourse sc = new SelectedCourse(id, studentId, courseId);
                scores.add(sc);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return scores;
    }
    public List<Score> queryScorePage(String sql, int startIndex, int pageSize, Object... params) throws SQLException {
        List<Score> scores = new ArrayList<Score>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql + " LIMIT ?, ?");
            int i = 0;
            if (params != null) {
                for (int j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if((Integer) params[j] == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            //assert params != null;
            stmt.setInt(i + 1, startIndex);
            stmt.setInt(i + 2, pageSize);
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                int courseId = rs.getInt("course_id");
                double score = rs.getDouble("score");
                String remark = rs.getString("remark");

                Score sc = new Score(id, studentId, courseId, score, remark);
                scores.add(sc);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return scores;
    }


    public List<Attendance> queryAttendancePage(String sql) throws SQLException {
        List<Attendance> attendance = new ArrayList<Attendance>();
        conn = null;
        stmt = null;
        rs = null;
        try {
            conn = DBConn.getConnection();
            stmt = conn.prepareStatement(sql);
            //assert params != null;
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int courseId = rs.getInt("course_id");
                int studentId = rs.getInt("student_id");
                String type = rs.getString("type");
                String date = rs.getString("date");
                Attendance att = new Attendance(id, courseId, studentId, type, date);
                attendance.add(att);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return attendance;
    }
    /**
     * 预编译增删改操作
     * @param sql SQL语句
     * @param params 参数列表
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public int prepareUpdate(String sql, Object... params) throws SQLException {
        int affectedRows = 0;
        conn = null;
        stmt = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            affectedRows = stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            closeConnection();
        }
        return affectedRows;
    }
    /**
     * 带事务的预编译增删改操作
     * @param sql SQL语句
     * @param params 参数列表
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public int prepareUpdateWithTransaction(String sql, Object... params) throws SQLException {
        int affectedRows = 0;
        conn = null;
        stmt = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0, j = 0; j < params.length; i++,j++) { //i用来控制？ j控制传来的值
                    if(StringUtil.isEmpty((String)params[j]) ||  Integer.parseInt((String) params[j]) == 0){
                        i--;
                        continue;
                    }
                    stmt.setObject(i + 1, params[i]);
                }
            }
            affectedRows = stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            closeConnection();
        }
        return affectedRows;
    }


    //基础查询:多条件查询
    public ResultSet query(String sql){
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return null;
    }

}
