package ra.qlkhoahochocvien.dao;

import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.model.Enrollment;
import ra.qlkhoahochocvien.model.StatusType;
import ra.qlkhoahochocvien.model.Student;

import java.util.List;

public interface IEnrollDAO {
    boolean enrollCourseByAdmin(int studentId, int courseId);
    boolean enrollCourse(int studentId, int courseId);
    void denyEnrollment(int studentId, int courseId);
    List<Enrollment> getEnrollments();
    void deleteEnrollmentByAdmin(int studentId, int courseId);
    void deleteEnrollmentByStudent(int studentId, int courseId);
    void approveStatusByAdmin(int studentId, int courseId);
    List<Enrollment> getConfirmEnrollmentsByCourseIdWithPaging(int courseId, int page, int pageSize);
    int countNumberOfConfirmEnrollmentsByCourseId(int courseId);
    int countNumberOfCoursesCanEnrollByAdminUsingStudentID(int studentId);
    List<Course> getCoursesCanEnrollByAdminStudentIDWithPaging(int studentId, int page, int pageSize);
    List<Course> getCoursesCanEnrollByStudentUsingStudentIDWithPaging(int studentId, int page, int pageSize);
    boolean checkWaitingEnrollmentExist(int studentId, int courseId);
    boolean checkEnrollmentExist(int studentId, int courseId);
    List<Course> getConfirmCoursesByStudentIDWithPaging(int studentId, int page, int pageSize);
    boolean checkConfirmEnrollmentExist(int studentId, int courseId);
    List<Course> getWaitingCoursesByAdminUsingStudentIDWithPaging(int studentId, int page, int pageSize);
    int countNumberOfWaitingCoursesByStudentID(int studentId);
    int countNumberOfConfirmCoursesByStudentID(int studentId);
    List<Course> getWaitingCourseByStudentIdWithPaging(int id, int currentPage, int pageSize);
    int countWaitingEnrollmentsByStudentId(int studentId);
    int countNumberOfCoursesCanEnrollByStudentUsingStudentID(int studentId);
    List<Course> getTop5CoursesByEnrollment();
}
