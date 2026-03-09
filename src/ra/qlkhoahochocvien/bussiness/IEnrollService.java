package ra.qlkhoahochocvien.bussiness;

import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.model.Student;

import java.sql.SQLException;
import java.util.Scanner;

public interface IEnrollService {
    void showEnrollByCourse();
    void addEnroll(Scanner scanner);
    void cancelEnrollment(int studentId, int courseId);
    void confirmEnrollment(int studentId, int courseId);
    void denyEnrollment(int studentId, int courseId);
    void deleteEnroll(Scanner scanner);
    void ChangeStatus(Scanner scanner);

    void showTotalCoursesAndStudents();

    void showNumberOfStudentsByCourse();

    void showTop5CoursesByEnrollment();

    void showCoursesWithMoreThan10Students();
}
