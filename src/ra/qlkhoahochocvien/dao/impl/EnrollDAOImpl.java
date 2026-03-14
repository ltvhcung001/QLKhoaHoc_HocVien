package ra.qlkhoahochocvien.dao.impl;

import ra.qlkhoahochocvien.dao.IEnrollDAO;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.model.Enrollment;
import ra.qlkhoahochocvien.model.StatusType;
import ra.qlkhoahochocvien.model.Student;
import ra.qlkhoahochocvien.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EnrollDAOImpl implements IEnrollDAO {


    @Override
    public boolean enrollCourseByAdmin(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("status").equals("DENIED")) {
                    String updateSql = "UPDATE enrollment SET status = 'WAITING'::status_values WHERE student_id = ? AND course_id = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, studentId);
                        updatePs.setInt(2, courseId);
                        updatePs.executeUpdate();
                        return true;
                    }
                }
            }
            String insertSql = "INSERT INTO enrollment (student_id, course_id, status) VALUES (?, ?, ?::status_values)";
            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setInt(1, studentId);
                insertPs.setInt(2, courseId);
                insertPs.setString(3, "WAITING");
                insertPs.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean enrollCourse(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("status").equals("CANCEL")) {
                    String updateSql = "UPDATE enrollment SET status = 'WAITING'::status_values WHERE student_id = ? AND course_id = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, studentId);
                        updatePs.setInt(2, courseId);
                        updatePs.executeUpdate();
                        return true;
                    }
                } else {
                    System.out.println("Thao tác không hợp lệ! Vui lòng kiểm tra lại");
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String insertSql = "INSERT INTO enrollment (student_id, course_id, status) VALUES (?, ?, ?::status_values)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setString(3, "WAITING");
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Thao tác không hợp lệ! Vui lòng kiểm tra lại");
            return false;
        }
    }

    @Override
    public void denyEnrollment(int studentId, int courseId) {
        String sql = "UPDATE enrollment SET status = 'DENIED' WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Enrollment> getEnrollments() {
        String sql = "SELECT * FROM enrollment ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Enrollment> enrollments = new ArrayList<>();
            while (rs.next()) {
                enrollments.add(new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getTimestamp("registered_at").toLocalDateTime(),
                        StatusType.valueOf(rs.getString("status"))
                ));
            }
            return enrollments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEnrollmentByAdmin(int studentId, int courseId) {
        String sql = "UPDATE enrollment SET status = 'DELETED'::status_values WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEnrollmentByStudent(int studentId, int courseId) {
        String sql = "UPDATE enrollment SET status = 'CANCEL'::status_values WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void approveStatusByAdmin(int studentId, int courseId) {
        String sql = "UPDATE enrollment SET status = 'CONFIRM'::status_values WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Enrollment> getConfirmEnrollmentsByCourseIdWithPaging(int courseId, int page, int pageSize) {
        String sql = """
                    SELECT * FROM enrollment 
                    WHERE course_id = ? 
                    AND status = 'CONFIRM'::status_values
                    ORDER BY id 
                    LIMIT ? OFFSET ?""";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Enrollment> enrollments = new ArrayList<>();
            while (rs.next()) {
                enrollments.add(new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getTimestamp("registered_at").toLocalDateTime(),
                        StatusType.valueOf(rs.getString("status"))
                ));
            }
            return enrollments;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countNumberOfConfirmEnrollmentsByCourseId(int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE course_id = ? AND status = 'CONFIRM'::status_values";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countNumberOfCoursesCanEnrollByAdminUsingStudentID(int studentId) {
        String sql = """
                     SELECT COUNT(*) FROM course c
                     WHERE c.id NOT IN (SELECT e.course_id FROM enrollment e WHERE e.student_id = ?
                     AND e.status != 'DENIED'::status_values)""";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getCoursesCanEnrollByAdminStudentIDWithPaging(int studentId, int page, int pageSize) {
        String sql = """
                     SELECT c.* FROM course c
                     WHERE c.id NOT IN (SELECT e.course_id 
                                        FROM enrollment e 
                                        WHERE e.student_id = ?
                                        AND e.status != 'DENIED'::status_values)
                     ORDER BY c.id
                     LIMIT ? OFFSET ?""";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("instructor"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return courses;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getCoursesCanEnrollByStudentUsingStudentIDWithPaging(int studentId, int page, int pageSize) {
        String sql = """
                     SELECT c.* FROM course c
                     WHERE c.id NOT IN (SELECT e.course_id 
                                        FROM enrollment e 
                                        WHERE e.student_id = ?
                                        AND e.status != 'CANCEL'::status_values)
                     ORDER BY c.id
                     LIMIT ? OFFSET ?""";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("instructor"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return courses;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkWaitingEnrollmentExist(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollment WHERE student_id = ? AND course_id = ? AND status = 'WAITING'::status_values";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkEnrollmentExist(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("status").equals("WAITING") || rs.getString("status").equals("CONFIRM"))
                    return true;
            }
            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Course> getConfirmCoursesByStudentIDWithPaging(int studentId, int page, int pageSize) {
        String sql = """
                    SELECT c.*
                    FROM enrollment e JOIN course c ON e.course_id = c.id
                    WHERE e.student_id = ?
                    AND e.status = 'CONFIRM'::status_values
                    ORDER BY c.id
                    LIMIT ? OFFSET ?""";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("instructor"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return courses;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkConfirmEnrollmentExist(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollment WHERE student_id = ? AND course_id = ? AND status = 'CONFIRM'::status_values";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getWaitingCoursesByAdminUsingStudentIDWithPaging(int studentId, int page, int pageSize) {
        String sql = """
                    SELECT * FROM course c
                    where c.id in (SELECT course_id FROM enrollment WHERE student_id = ? AND status = 'WAITING'::status_values)
                    ORDER BY c.id 
                    LIMIT ? OFFSET ?
                   """;
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, studentId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("instructor"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return courses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countNumberOfWaitingCoursesByStudentID(int studentId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND status = 'WAITING'::status_values";
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countNumberOfConfirmCoursesByStudentID(int studentId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND status = 'CONFIRM'::status_values";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countCoursesByStudentId(int userId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ?";
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getWaitingCourseByStudentIdWithPaging(int studentId, int currentPage, int pageSize) {
        String sql = """
                        SELECT * FROM course 
                        WHERE id in (SELECT course_id FROM enrollment 
                                     WHERE student_id = ? AND status = 'WAITING'::status_values) 
                                     ORDER BY id 
                                     LIMIT ? OFFSET ?""";
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, studentId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (currentPage - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("duration"),
                    rs.getString("instructor"),
                    rs.getDate("create_at").toLocalDate()
                ));
            }
            return courses;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int countWaitingEnrollmentsByStudentId(int studentId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND status = 'WAITING'::status_values";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public int countNumberOfCoursesCanEnrollByStudentUsingStudentID(int studentId) {
        String sql = """
                     SELECT COUNT(*) FROM course c
                     WHERE c.id NOT IN (SELECT e.course_id FROM enrollment e WHERE e.student_id = ?
                     AND e.status != 'CANCEL'::status_values)""";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
