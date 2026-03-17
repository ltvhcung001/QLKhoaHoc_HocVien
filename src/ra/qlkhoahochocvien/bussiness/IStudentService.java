package ra.qlkhoahochocvien.bussiness;

import ra.qlkhoahochocvien.model.Student;

import java.util.List;
import java.util.Scanner;

public interface IStudentService {
    Student login(String email, String password);
    void showStudents(Scanner scanner);
    Student getStudentById(int id);
    void addStudent(Scanner scanner);
    void updateStudent(Scanner scanner);
    void deleteStudent(Scanner scanner);
    void getStudents(Scanner scanner);
    void showStudentInSorted(Scanner scanner);
    void changePassword(Scanner scanner);
    int countStudent();
}
