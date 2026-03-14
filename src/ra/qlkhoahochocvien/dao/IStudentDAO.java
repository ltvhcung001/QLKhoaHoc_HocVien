package ra.qlkhoahochocvien.dao;

import ra.qlkhoahochocvien.model.Student;

import java.util.List;

public interface IStudentDAO {
    Student findStudentByEmail(String email);
    List<Student> getStudents();
    Student getStudentById(int id);
    void addStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(int id);
    List<Student> findStudentsByName(String name);
    List<Student> findStudentsByEmail(String email);
    List<Student> listStudentsOrderBy(String orderBy);
    int countStudents();

    List<Student> getStudentsListWithPaging(int currentPage, int pageSize);
}

