package ra.qlkhoahochocvien.dao.impl;

import org.postgresql.core.ConnectionFactory;
import ra.qlkhoahochocvien.dao.IStudentDAO;
import ra.qlkhoahochocvien.model.Student;
import ra.qlkhoahochocvien.utils.DBUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentDAOImpl implements IStudentDAO {
    @Override
    public void saveStudent(Student student) {
        // (name, dob, email, sex, phone, password)
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
            ps.setString(2, student.getPhone());
            ps.setString(3, student.getPassword());
            ps.execute();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
}
