package ra.qlkhoahochocvien.bussiness;

import java.util.Scanner;

public interface IEnrollService {
    void showEnrollByCourse(Scanner scanner);
    void addEnrollByAdmin(Scanner scanner);
    void addEnrollByStudent(Scanner scanner);
    void deleteEnroll(Scanner scanner);
    void ChangeStatus(Scanner scanner);
    void showTotalCoursesAndStudents();
    void showNumberOfStudentsByCourse();
    void showTop5CoursesByEnrollment();
    void showCoursesWithMoreThan10Students();
    void listEnrolledCoursesByStudent(int studentId);
    void deleteEnrollByStudent(Scanner scanner);
}
