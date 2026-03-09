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
    public void enrollCourse(int studentId, int courseId) {
        String checkSql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, studentId);
            checkPs.setInt(2, courseId);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Student is already enrolled in this course.");
                return;
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        String sql = "INSERT INTO enrollment (student_id, course_id, status) VALUES (?, ?, ?::status_values)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setString(3, "WAITING");
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelEnrollment(Student student, Course course) {
        String sql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, student.getId());
            ps.setInt(2, course.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void confirmEnrollment(Student student, Course course) {
        String sql = "UPDATE enrollment SET status = 'CONFIRMED' WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, student.getId());
            ps.setInt(2, course.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public void deleteEnrollment(int studentId, int courseId) {
        String sql = "DELETE FROM enrollment WHERE student WHERE student_id = ? AND course_id = ?";
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
    public void updateEnrollmentStatus(int studentId, int courseId, StatusType status) {
        String sql = "UPDATE enrollment SET status = ?::status_values WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
