package ra.qlkhoahochocvien.dao.impl;

import ra.qlkhoahochocvien.dao.IStudentDAO;
import ra.qlkhoahochocvien.model.Student;
import ra.qlkhoahochocvien.utils.DBUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements IStudentDAO {

    @Override
    public Student findStudentByEmail(String StudentEmail) {
        String sql = "Select * from student where email = ?";
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, StudentEmail);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                LocalDate dob = rs.getDate("dob").toLocalDate();
                String email = rs.getString("email");
                boolean sex = rs.getBoolean("sex");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                LocalDate create_at =  rs.getDate("create_at").toLocalDate();
                return new Student(id, name, dob, email, sex, phone, password, create_at);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Student> getStudents() {
        String sql = "SELECT * FROM student ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("email"),
                        rs.getBoolean("sex"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return students;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("email"),
                        rs.getBoolean("sex"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getDate("create_at").toLocalDate()
                );
            } else {
                return null;
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addStudent(Student student) {
        String sql = """
                        INSERT INTO student(name, dob, email, sex, phone, password) 
                        VALUES (?, ?, ?, ?::bit, ?, ?)
                        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, student.getName());
            ps.setDate(2, Date.valueOf(student.getDob()));
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getSex()?"1":"0");
            ps.setString(5, student.getPhone());
            ps.setString(6, student.getPassword());
            ps.execute();

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStudent(Student student) {
        String sql = """
                        UPDATE student 
                        SET name = ?, dob = ?, email = ?, sex = ?::bit, phone = ?, password = ? 
                        WHERE id = ?
                        """;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, student.getName());
            ps.setDate(2, Date.valueOf(student.getDob()));
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getSex()?"1":"0");
            ps.setString(5, student.getPhone());
            ps.setString(6, student.getPassword());
            ps.setInt(7, student.getId());
            ps.execute();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteStudent(int id) {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            ps.execute();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        String sql = "SELECT * FROM student WHERE name ILIKE ? ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            List<Student> students = new java.util.ArrayList<>();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("email"),
                        rs.getBoolean("sex"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return students;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> findStudentsByEmail(String email) {
        String sql = "SELECT * FROM student WHERE email ILIKE ? ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, "%" + email + "%");
            ResultSet rs = ps.executeQuery();
            List<Student> students = new java.util.ArrayList<>();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("email"),
                        rs.getBoolean("sex"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return students;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> listStudentsOrderBy(String orderBy) {
        String sql = "SELECT * FROM student ORDER BY " + orderBy;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ResultSet rs = ps.executeQuery();
            List<Student> students = new java.util.ArrayList<>();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("email"),
                        rs.getBoolean("sex"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getDate("create_at").toLocalDate()
                ));
            }
            return students;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countStudents() {
        String sql = "SELECT COUNT(*) FROM student";
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
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
    public List<Student> getStudentsListWithPaging(int currentPage, int pageSize) {
        String sql = "SELECT * FROM student ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, pageSize);
            ps.setInt(2, (currentPage - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDate("dob").toLocalDate(),
                    rs.getString("email"),
                    rs.getBoolean("sex"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    rs.getDate("create_at").toLocalDate()
                ));
            }
            return students;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean findStudentByPhone(String phone) {
        String sql = "SELECT * FROM student WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
