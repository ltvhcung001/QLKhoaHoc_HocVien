package ra.qlkhoahochocvien.dao.impl;

import ra.qlkhoahochocvien.dao.ICourseDAO;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements ICourseDAO {

    @Override
    public List<Course> listCourses() {
        String sql = "SELECT * FROM course ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);){
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
    public List<Course> listCoursesWithPagination(int page, int pageSize) {
        String sql = "SELECT * FROM course ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
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
    public Course getCourse(int id) {
        String sql = "SELECT * FROM course WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Course(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("instructor"),
                        rs.getDate("create_at").toLocalDate());
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addCourse(Course c) {
        String sql = "INSERT INTO Course(name, duration, instructor) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getDuration());
            ps.setString(3, c.getInstructor());
            ps.executeUpdate();
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCourse(Course c) {
        String sql = "UPDATE course SET name = ?, duration = ?, instructor = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getDuration());
            ps.setString(3, c.getInstructor());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getCourseByName(String name) {
        String sql = "SELECT * FROM course WHERE name ILIKE ? ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("instructor"),
                        rs.getDate("create_at").toLocalDate()));
            }
            return courses;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Course> listCoursesOrderBy(String orderBy) {
        String sql = "SELECT * FROM course ORDER BY " + orderBy;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
    public List<Course> listCoursesOrderByWithPagination(String orderBy, int page, int pageSize) {
        String sql = "SELECT * FROM course ORDER BY " + orderBy + " LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
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
    public int countCourses() {
        String sql = "SELECT COUNT(*) FROM course";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> listFullCoursesOrderBy(String orderBy) {
        String sql = "SELECT * FROM course ORDER BY " + orderBy;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
    public int countCoursesByName(String name) {
        String sql = "SELECT COUNT(*) FROM course WHERE name ILIKE ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getCourseByNameWithPagination(String name, int currentPage, int pageSize) {
        String sql = "SELECT * FROM course WHERE name ILIKE ? ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
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
}
