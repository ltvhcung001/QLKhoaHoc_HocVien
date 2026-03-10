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
import ra.qlkhoahochocvien.presentation.StudentView;
import ra.qlkhoahochocvien.utils.Helper;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class EnrollServiceImpl implements IEnrollService {
    public final IEnrollDAO enrollDAO = new EnrollDAOImpl();
    public final ICourseService courseService = new CourseServiceImpl();
    public final IStudentService studentService = new StudentServiceImpl();
    @Override
    public void showEnrollByCourse() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        List<Student> students = studentService.getStudentsList();
//        for (Course course : courses) {
//            AtomicInteger index = new AtomicInteger(1);
//            if (enrollments.stream().anyMatch(e -> e.getCourse_id() == course.getId())) {
//                System.out.println("Khoá học: " + course.getName() + "(ID: " + course.getId() + ")");
//                enrollments.stream()
//                        .filter(e -> e.getCourse_id() == course.getId())
//                        .forEach(e -> {
//                            Student student = students.stream()
//                                    .filter(s -> s.getId() == e.getStudent_id())
//                                    .findFirst()
//                                    .orElse(null);
//                            if (student != null) {
//                                System.out.println(index.getAndIncrement() + ". " + student.getName() + " (ID: " + student.getId() + ")" + " - Trạng thái: " + e.getStatus());
//                            }
//                        });
//            }
//        }
        System.out.println("========= DANH SÁCH ĐĂNG KÝ KHOÁ HỌC =========");
        Helper.printEnrollments(enrollments, students, courses);
        System.out.println("========= DANH SÁCH ĐĂNG KÝ KHOÁ HỌC THEO MÔN HỌC =========");
        Helper.printEnrollmentsByCourses(enrollments, students, courses);
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
    public List<Course> getCoursesCanEnroll(int studentId) {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        List<Course> enrolledCourses = enrollments.stream()
                .filter(e -> e.getStudent_id() == studentId)
                .map(e -> courses.stream().filter(c -> c.getId() == e.getCourse_id()).findFirst().orElse(null))
                .toList();
        return courses.stream()
                .filter(c -> !enrolledCourses.contains(c))
                .toList();
    }

    @Override
    public void addEnrollByStudent(Scanner scanner) {
        System.out.println("===== ĐĂNG KÝ KHOÁ HỌC =====");
        List<Course> courses = getCoursesCanEnroll(StudentView.studentLogin.getId());
        Helper.printCourses(courses);
        System.out.print("Nhập ID khoá học: ");
        var ref = new Object() {
            int courseId = Integer.parseInt(scanner.nextLine());
        };
        while (courses.stream().noneMatch(c -> c.getId() == ref.courseId)) {
            System.out.println("ID khoá học không hợp lệ hoặc bạn đã đăng ký khoá học này, vui lòng chọn lại!");
            System.out.print("Nhập ID khoá học: ");
            ref.courseId = Integer.parseInt(scanner.nextLine());
        }
        enrollDAO.enrollCourse(StudentView.studentLogin.getId(), ref.courseId);
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

    @Override
    public void showTotalCoursesAndStudents() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        long totalCourses = enrollments.stream().map(Enrollment::getCourse_id).distinct().count();
        long totalStudents = enrollments.stream().map(Enrollment::getStudent_id).distinct().count();
        System.out.println("Tổng số lượng khoá học: " + totalCourses);
        System.out.println("Tổng số lượng học viên: " + totalStudents);
    }

    @Override
    public void showNumberOfStudentsByCourse() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        List<Student> students = studentService.getStudentsList();
        for (Course course : courses) {
            long count = enrollments.stream()
                    .filter(e -> e.getCourse_id() == course.getId() && e.getStatus() == StatusType.CONFIRM)
                    .count();
            System.out.println("Khoá học: " + course.getName() + " (ID: " + course.getId() + ") - Số lượng học viên đã đăng ký: " + count);
        }
    }

    @Override
    public void showTop5CoursesByEnrollment() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        System.out.println("Top 5 khoá học đông học viên nhất:");
        courses.stream()
                .sorted((c1, c2) -> Long.compare(
                        enrollments.stream().filter(e -> e.getCourse_id() == c2.getId() && e.getStatus() == StatusType.CONFIRM).count(),
                        enrollments.stream().filter(e -> e.getCourse_id() == c1.getId() && e.getStatus() == StatusType.CONFIRM).count()
                ))
                .limit(5)
                .forEach(course -> {
                    long count = enrollments.stream()
                            .filter(e -> e.getCourse_id() == course.getId() && e.getStatus() == StatusType.CONFIRM)
                            .count();
                    System.out.println("Khoá học: " + course.getName() + " (ID: " + course.getId() + ") - Số lượng học viên đã đăng ký: " + count);
                });
    }

    @Override
    public void showCoursesWithMoreThan10Students() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        System.out.println("Khoá học có trên 10 học viên đã đăng ký:");
        courses.stream()
                .filter(course -> enrollments.stream()
                        .filter(e -> e.getCourse_id() == course.getId() && e.getStatus() == StatusType.CONFIRM)
                        .count() > 10)
                .forEach(course -> {
                    long count = enrollments.stream()
                            .filter(e -> e.getCourse_id() == course.getId() && e.getStatus() == StatusType.CONFIRM)
                            .count();
                    System.out.println("Khoá học: " + course.getName() + " (ID: " + course.getId() + ") - Số lượng học viên đã đăng ký: " + count);
                });
    }

    @Override
    public List<Course> listEnrolledCoursesByStudentID(int studentId){
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        List<Enrollment> studentEnrollments = enrollments.stream()
                .filter(e -> e.getStudent_id() == studentId)
                .toList();
        if (studentEnrollments.isEmpty()) {
            System.out.println("Bạn chưa đăng ký khoá học nào.");
            return new ArrayList<>();
        }
        List<Course> result = new ArrayList<>();
        studentEnrollments.forEach(e -> {
            Course course = courses.stream().filter(c -> c.getId() == e.getCourse_id()).findFirst().orElse(null);
            if (course != null) {
                result.add(course);
            }
        }
        );
        return result;
    }

    @Override
    public void listEnrolledCoursesByStudent(int id) {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listCoursesOrderBy("id");
        List<Enrollment> studentEnrollments = enrollments.stream()
                .filter(e -> e.getStudent_id() == id)
                .toList();
        if (studentEnrollments.isEmpty()) {
            System.out.println("Bạn chưa đăng ký khoá học nào.");
        }
        System.out.println("Chọn tiêu chí để sắp xếp danh sách khoá học đã đăng ký: ");
        System.out.println("1. Sắp xếp theo tên khoá học");
        System.out.println("2. Sắp xếp theo ngày đăng ký");
        System.out.print("Lựa chọn của bạn: ");
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(scanner.nextLine());
        System.out.println("Chọn thứ tự sắp xếp: ");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");
        System.out.print("Lựa chọn của bạn: ");
        int orderChoice = Integer.parseInt(scanner.nextLine());
        System.out.println("Danh sách khoá học bạn đã đăng ký:");
        List<Course> result = new ArrayList<>();
        switch (choice) {
            case 1:
                Collator viCollator = Collator.getInstance(new Locale("vi", "VN"));
                if (orderChoice == 1) {
                    studentEnrollments.stream()
                            .sorted((e1, e2) -> {
                                Course c1 = courses.stream().filter(c -> c.getId() == e1.getCourse_id()).findFirst().orElse(null);
                                Course c2 = courses.stream().filter(c -> c.getId() == e2.getCourse_id()).findFirst().orElse(null);
                                return viCollator.compare(c2.getName(), c1.getName()) * -1;
                            })
                            .forEach(e -> {
                                result.add(courses.stream().filter(c -> c.getId() == e.getCourse_id()).findFirst().orElse(null));
                            });
                } else {
                    studentEnrollments.stream()
                            .sorted((e1, e2) -> {
                                Course c1 = courses.stream().filter(c -> c.getId() == e1.getCourse_id()).findFirst().orElse(null);
                                Course c2 = courses.stream().filter(c -> c.getId() == e2.getCourse_id()).findFirst().orElse(null);
                                return viCollator.compare(c2.getName(), c1.getName());
                            })
                            .forEach(e -> {
                                result.add(courses.stream().filter(c -> c.getId() == e.getCourse_id()).findFirst().orElse(null));
                            });
                }
                break;
            case 2:
                if (orderChoice == 1) {
                    studentEnrollments.stream().sorted((e1, e2) -> e1.getRegistered_at().compareTo(e2.getRegistered_at()))
                            .forEach(e -> {
                                 result.add(courses.stream().filter(c -> c.getId() == e.getCourse_id()).findFirst().orElse(null));
                            });
                }
                else {
                    studentEnrollments.stream().sorted((e1, e2) -> e2.getRegistered_at().compareTo(e1.getRegistered_at()))
                            .forEach(e -> {
                                result.add(courses.stream().filter(c -> c.getId() == e.getCourse_id()).findFirst().orElse(null));
                            });
                }
        }
        Helper.printCourses(result);

    }

    @Override
    public void deleteEnrollByStudent(Scanner scanner) {
        List<Course> courses = listEnrolledCoursesByStudentID(StudentView.studentLogin.getId());
        System.out.println("=== DANH SÁCH KHOÁ HỌC ĐÃ ĐĂNG KÝ ===");
        Helper.printCourses(courses);
        System.out.println("Nhập ID khoá học bạn muốn huỷ đăng ký: ");
        var ref = new Object() {
            int courseId = Integer.parseInt(scanner.nextLine());
        };
        while(courses.stream().noneMatch(c -> c.getId() == ref.courseId)) {;
            System.out.println("ID khoá học không hợp lệ hoặc bạn chưa đăng ký khoá học này, vui lòng chọn lại!");
            System.out.println("Nhập ID khoá học bạn muốn huỷ đăng ký: ");
            ref.courseId = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Bạn có chắc chắn muốn huỷ đăng ký khoá học này? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Đã huỷ bỏ việc huỷ đăng ký khoá học.");
            return;
        }
        enrollDAO.deleteEnrollment(StudentView.studentLogin.getId(), ref.courseId);
        System.out.println("Huỷ đăng ký khoá học thành công!");
    }

}
