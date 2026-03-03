package ra.qlkhoahochocvien.dao;

import ra.qlkhoahochocvien.model.Student;

public interface IStudentDAO {
    void saveStudent(Student student);
    Student findStudentByEmail(String email);
}
