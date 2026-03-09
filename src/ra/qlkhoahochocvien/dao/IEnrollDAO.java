package ra.qlkhoahochocvien.dao;

import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.model.Enrollment;
import ra.qlkhoahochocvien.model.StatusType;
import ra.qlkhoahochocvien.model.Student;

import java.util.List;

public interface IEnrollDAO {
    void enrollCourse(int studentId, int courseId);
//    void cancelEnrollment(int studentId, int courseId);
    void cancelEnrollment(Student student, Course course);
//    void confirmEnrollment(int studentId, int courseId);
    void confirmEnrollment(Student student, Course course);
    void denyEnrollment(int studentId, int courseId);
//    void showEnrollments();

    List<Enrollment> getEnrollments();

    void deleteEnrollment(int studentId, int courseId);

    void updateEnrollmentStatus(int studentId, int courseId, StatusType status);
}
