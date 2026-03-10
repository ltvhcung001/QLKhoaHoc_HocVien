package ra.qlkhoahochocvien.bussiness;

import ra.qlkhoahochocvien.model.Student;

import java.util.List;
import java.util.Scanner;

public interface IStudentService {
//    void register(Student student);
    Student login(String email, String password);
    void showStudents();
    Student getStudentById(Scanner scanner);
    void addStudent(Scanner scanner) throws  Exception;
    void updateStudent(Scanner scanner) throws  Exception;
    void deleteStudent(Scanner scanner);
    void getStudents(Scanner scanner) throws  Exception;
    void showStudentInSorted(Scanner scanner);
    List<Student> getStudentsList();

    void changePassword(Scanner scanner);
}
