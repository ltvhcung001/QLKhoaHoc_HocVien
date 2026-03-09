package ra.qlkhoahochocvien.bussiness.impl;

import ra.qlkhoahochocvien.bussiness.ICourseService;
import ra.qlkhoahochocvien.bussiness.IEnrollService;
import ra.qlkhoahochocvien.bussiness.IStudentService;
import ra.qlkhoahochocvien.dao.IEnrollDAO;
import ra.qlkhoahochocvien.dao.impl.EnrollDAOImpl;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.model.Enrollment;
import ra.qlkhoahochocvien.model.StatusType;
import ra.qlkhoahochocvien.model.Student;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class EnrollServiceImpl implements IEnrollService {
    public final IEnrollDAO enrollDAO = new EnrollDAOImpl();
    public final ICourseService courseService = new CourseServiceImpl();
    public final IStudentService studentService = new StudentServiceImpl();
    @Override
    public void showEnrollByCourse() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        List<Student> students = studentService.getStudentsList();
        for (Course course : courses) {
            AtomicInteger index = new AtomicInteger(1);
            if (enrollments.stream().anyMatch(e -> e.getCourse_id() == course.getId())) {
                System.out.println("Khoá học: " + course.getName() + "(ID: " + course.getId() + ")");
                enrollments.stream()
                        .filter(e -> e.getCourse_id() == course.getId())
                        .forEach(e -> {
                            Student student = students.stream()
                                    .filter(s -> s.getId() == e.getStudent_id())
                                    .findFirst()
                                    .orElse(null);
                            if (student != null) {
                                System.out.println(index.getAndIncrement() + ". " + student.getName() + " (ID: " + student.getId() + ")" + " - Trạng thái: " + e.getStatus());
                            }
                        });
            }
        }
    }

    @Override
    public void addEnroll(Scanner scanner) {
        System.out.println("===== ĐĂNG KÝ KHOÁ HỌC =====");
        courseService.listCourses();
        studentService.showStudents();
        System.out.print("Nhập ID sinh viên: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        System.out.print("Nhập ID khoá học: ");
        int courseId = Integer.parseInt(scanner.nextLine());
        enrollDAO.enrollCourse(studentId, courseId);
        System.out.println("Đăng ký khoá học thành công!");
    }

    @Override
    public void cancelEnrollment(int studentId, int courseId) {

    }

    @Override
    public void confirmEnrollment(int studentId, int courseId) {

    }

    @Override
    public void denyEnrollment(int studentId, int courseId) {

    }

    @Override
    public void deleteEnroll(Scanner scanner) {
        System.out.println("Nhập ID sinh viên cần xoá khỏi khoá học: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập ID khoá học cần xoá sinh viên khỏi: ");
        int courseId = Integer.parseInt(scanner.nextLine());
        enrollDAO.deleteEnrollment(studentId, courseId);
        System.out.println("Xoá đăng ký khoá học thành công!");
    }

    @Override
    public void ChangeStatus(Scanner scanner) {
        System.out.print("Nhập ID sinh viên cần duyệt đăng ký: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        System.out.print("Nhập ID khoá học cần duyệt đăng ký: ");
        int courseId = Integer.parseInt(scanner.nextLine());
        System.out.println("Chọn trạng thái mới: ");
        System.out.println("1. WAITING (Đang chờ duyệt)");
        System.out.println("2. CONFIRM (Chấp nhận)");
        System.out.println("3. DENIED (Đã từ chối)");
        System.out.println("4. CANCEL (Huỷ bỏ đăng ký)");
        System.out.print("Nhập lựa chọn: ");
        int statusChoice = Integer.parseInt(scanner.nextLine());
        StatusType status = null;
        switch (statusChoice) {
            case 1:
                status = StatusType.WAITING;
                break;
            case 2:
                status = StatusType.CONFIRM;
                break;
            case 3:
                status = StatusType.DENIED;
                break;
            case 4:
                status = StatusType.CANCEL;
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
        }
        if (status != null) {
            enrollDAO.updateEnrollmentStatus(studentId, courseId, status);
            System.out.println("Cập nhật trạng thái đăng ký khoá học thành công!");
        }

    }
}
