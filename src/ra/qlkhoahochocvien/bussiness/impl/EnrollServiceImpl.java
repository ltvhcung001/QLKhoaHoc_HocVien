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

import java.util.List;
import java.util.Scanner;

public class EnrollServiceImpl implements IEnrollService {
    public final IEnrollDAO enrollDAO = new EnrollDAOImpl();
    public final ICourseService courseService = new CourseServiceImpl();
    public final IStudentService studentService = new StudentServiceImpl();

    @Override
    public void showEnrollByCourse(Scanner scanner) {
        int courseID = Helper.getIntInput(scanner, "Nhập ID khoá học: ");
        Course course = courseService.getCourseByID(courseID);

        final int PAGE_SIZE = 5;
        int totalEnrollments = enrollDAO.countNumberOfConfirmEnrollmentsByCourseId(courseID);
        if (totalEnrollments == 0) {
            System.out.println("Chưa có Đăng ký nào.");
            return;
        }
        int totalPages = (int) Math.ceil((double) totalEnrollments / PAGE_SIZE);
        int currentPage = 1;

        System.out.println("======================= DANH SÁCH HỌC VIÊN ĐĂNG KÝ KHOÁ HỌC =======================");
        while (true){
            List<Enrollment> enrollments = enrollDAO.getConfirmEnrollmentsByCourseIdWithPaging(courseID, currentPage, PAGE_SIZE);
            List<Student> students = enrollments.stream()
                    .map(e -> studentService.getStudentById(e.getStudent_id()))
                    .toList();
            Helper.printStudents(students);
            System.out.printf("Trang %d / %d  (Tổng: %d học viên)\n", currentPage, totalPages, totalEnrollments);
            System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [Q] Thoát");
            System.out.print("Nhập lệnh: ");
            String cmd = scanner.nextLine().trim().toUpperCase();
            switch (cmd) {
                case "N":
                    if (currentPage < totalPages) currentPage++;
                    else System.out.println("Đã ở trang cuối.");
                    break;
                case "P":
                    if (currentPage > 1) currentPage--;
                    else System.out.println("Đã ở trang đầu.");
                    break;
                case "G":
                    System.out.printf("Nhập số trang (1-%d): ", totalPages);
                    try {
                        int page = Integer.parseInt(scanner.nextLine().trim());
                        if (page >= 1 && page <= totalPages) currentPage = page;
                        else System.out.println("Số trang không hợp lệ.");
                    } catch (NumberFormatException e) {
                        System.out.println("Nhập không hợp lệ.");
                    }
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Lệnh không hợp lệ.");
            }
        }

    }

    @Override
    public void addEnrollByAdmin(Scanner scanner) {
        System.out.println("===== ĐĂNG KÝ KHOÁ HỌC =====");
        int studentId = Helper.getIntInput(scanner, "Nhập ID sinh viên: ");
        int totalCourses = enrollDAO.countNumberOfCoursesCanEnrollByAdminUsingStudentID(studentId);
        int PAGE_SIZE = 5;
        int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
        int currentPage = 1;

        while (true){
            List<Course> courses = enrollDAO.getCoursesCanEnrollByAdminStudentIDWithPaging(studentId, currentPage, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Sinh viên đã đăng ký tất cả các khoá học hoặc không có khoá học nào để đăng ký.");
                return;
            }
            Helper.printCourses(courses);
            System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalCourses);
            System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [L] Chọn khoá học để đăng ký  [Q] Thoát");
            System.out.print("Nhập lệnh: ");
            String cmd = scanner.nextLine().trim().toUpperCase();
            switch (cmd) {
                case "N":
                    if (currentPage < totalPages) currentPage++;
                    else System.out.println("Đã ở trang cuối.");
                    break;
                case "P":
                    if (currentPage > 1) currentPage--;
                    else System.out.println("Đã ở trang đầu.");
                    break;
                case "G":
                    System.out.printf("Nhập số trang (1-%d): ", totalPages);
                    try {
                        int page = Integer.parseInt(scanner.nextLine().trim());
                        if (page >= 1 && page <= totalPages) currentPage = page;
                        else System.out.println("Số trang không hợp lệ.");
                    } catch (NumberFormatException e) {
                        System.out.println("Nhập không hợp lệ.");
                    }
                    break;
                case "L":
                        int courseId = Helper.getIntInput(scanner, "Nhập ID khoá học: ");
                        if (enrollDAO.enrollCourseByAdmin(studentId, courseId)){
                            System.out.println("Đăng ký khoá học thành công!");
                            return;
                        }
                        else {
                            break;
                        }
                case "Q":
                    return;
                default:
                    System.out.println("Lệnh không hợp lệ.");
            }
        }
    }

    @Override
    public void addEnrollByStudent(Scanner scanner) {
        System.out.println("===== ĐĂNG KÝ KHOÁ HỌC =====");
        int studentId = StudentView.studentLogin.getId();
        int totalCourses = enrollDAO.countNumberOfCoursesCanEnrollByStudentUsingStudentID(studentId);
        int PAGE_SIZE = 5;
        int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
        int currentPage = 1;

        while (true){
            List<Course> courses = enrollDAO.getCoursesCanEnrollByStudentUsingStudentIDWithPaging(studentId, currentPage, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Sinh viên không có khoá học được phép đăng ký.");
                return;
            }
            Helper.printCourses(courses);
            System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalCourses);
            System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [L] Chọn khoá học để đăng ký  [Q] Thoát");
            System.out.print("Nhập lệnh: ");
            String cmd = scanner.nextLine().trim().toUpperCase();
            switch (cmd) {
                case "N":
                    if (currentPage < totalPages) currentPage++;
                    else System.out.println("Đã ở trang cuối.");
                    break;
                case "P":
                    if (currentPage > 1) currentPage--;
                    else System.out.println("Đã ở trang đầu.");
                    break;
                case "G":
                    System.out.printf("Nhập số trang (1-%d): ", totalPages);
                    try {
                        int page = Integer.parseInt(scanner.nextLine().trim());
                        if (page >= 1 && page <= totalPages) currentPage = page;
                        else System.out.println("Số trang không hợp lệ.");
                    } catch (NumberFormatException e) {
                        System.out.println("Nhập không hợp lệ.");
                    }
                    break;
                case "L":
                    int courseId = Helper.getIntInput(scanner, "Nhập ID khoá học: ");
                    if (enrollDAO.enrollCourse(studentId, courseId)){
                        System.out.println("Đăng ký khoá học thành công!");
                        return;
                    }
                    else{
                        break;
                    }

                case "Q":
                    return;
                default:
                    System.out.println("Lệnh không hợp lệ.");
            }
        }
    }

    @Override
    public void deleteEnroll(Scanner scanner) {
        while (true){
            int studentId = Helper.getIntInput(scanner, "Nhập ID sinh viên cần xoá khỏi khoá học: ");
            Student student = studentService.getStudentById(studentId);
            if (student == null) {
                System.out.println("Không tìm thấy sinh viên với ID = " + studentId);
                continue;
            }
            final int PAGE_SIZE = 5;
            int totalCourses = enrollDAO.countNumberOfConfirmCoursesByStudentID(studentId);
            int currentPage = 1;
            int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
            while (true){
                List<Course> courses = enrollDAO.getConfirmCoursesByStudentIDWithPaging(studentId, currentPage, PAGE_SIZE);
                if (courses.isEmpty()) {
                    System.out.println("Sinh viên này không đăng ký bất kì khoá học nào.");
                    return;
                }
                Helper.printCourses(courses);
                System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalCourses);
                System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [L] Chọn khoá học để xoá  [Q] Thoát");
                System.out.print("Nhập lệnh: ");
                String cmd = scanner.nextLine().trim().toUpperCase();
                switch (cmd) {
                    case "N":
                        if (currentPage < totalPages) currentPage++;
                        else System.out.println("Đã ở trang cuối.");
                        break;
                    case "P":
                        if (currentPage > 1) currentPage--;
                        else System.out.println("Đã ở trang đầu.");
                        break;
                    case "G":
                        System.out.printf("Nhập số trang (1-%d): ", totalPages);
                        try {
                            int page = Integer.parseInt(scanner.nextLine().trim());
                            if (page >= 1 && page <= totalPages) currentPage = page;
                            else System.out.println("Số trang không hợp lệ.");
                        } catch (NumberFormatException e) {
                            System.out.println("Nhập không hợp lệ.");
                        }
                        break;
                    case "L":
                        int courseId = Helper.getIntInput(scanner, "Nhập ID khoá học: ");
                        if (!enrollDAO.checkConfirmEnrollmentExist(studentId, courseId)) {
                            System.out.println("Sinh viên không học khoá này, vui lòng chọn khoá học khác!");
                            break;
                        }
                        enrollDAO.deleteEnrollmentByAdmin(studentId, courseId);
                        System.out.println("Xoá đăng ký khoá học thành công!");
                        return;
                    case "Q":
                        return;
                    default:
                        System.out.println("Lệnh không hợp lệ.");
                }
            }
        }
    }

    @Override
    public void ChangeStatus(Scanner scanner) {
        while (true){
            int studentId = Helper.getIntInput(scanner, "Nhập ID sinh viên cần duyệt đăng ký: ");
            Student student = studentService.getStudentById(studentId);
            if (student == null) {
                System.out.println("Không tìm thấy sinh viên với ID = " + studentId);
                continue;
            }

            final int PAGE_SIZE = 5;
            int totalCourses = enrollDAO.countNumberOfWaitingCoursesByStudentID(studentId);
            if (totalCourses == 0) {
                System.out.println("Sinh viên này không đăng ký khoá học nào!");
                return;
            }
            int currentPage = 1;
            int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
            while (true){
                List<Course> courses = enrollDAO.getWaitingCoursesByAdminUsingStudentIDWithPaging(studentId, currentPage, PAGE_SIZE);
                Helper.printCourses(courses);
                System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalCourses);
                System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [L] Chọn khoá học để thực hiện thao tác  [Q] Thoát");
                System.out.print("Nhập lệnh: ");
                String cmd = scanner.nextLine().trim().toUpperCase();
                switch (cmd) {
                    case "N":
                        if (currentPage < totalPages) currentPage++;
                        else System.out.println("Đã ở trang cuối.");
                        break;
                    case "P":
                        if (currentPage > 1) currentPage--;
                        else System.out.println("Đã ở trang đầu.");
                        break;
                    case "G":
                        System.out.printf("Nhập số trang (1-%d): ", totalPages);
                        try {
                            int page = Integer.parseInt(scanner.nextLine().trim());
                            if (page >= 1 && page <= totalPages) currentPage = page;
                            else System.out.println("Số trang không hợp lệ.");
                        } catch (NumberFormatException e) {
                            System.out.println("Nhập không hợp lệ.");
                        }
                        break;
                    case "L":
                        int courseId = Helper.getIntInput(scanner, "Nhập ID khoá học: ");
                        System.out.println("Bạn muốn thực hiện hành động nào sau dây?");
                        System.out.println("1. Duyệt đăng ký khoá học này");
                        System.out.println("2. Từ chối đăng ký khoá học này");
                        int action = Helper.getIntInput(scanner, "Lựa chọn của bạn: ");
                        if (action == 2){
                             System.out.print("Bạn có chắc chắn muốn từ chối đăng ký khoá học này? (y/n): ");
                             String confirm = scanner.nextLine().trim().toLowerCase();
                             if (!confirm.equals("y")) {
                                 System.out.println("Đã huỷ bỏ việc từ chối đăng ký khoá học.");
                                 return;
                             }
                             enrollDAO.denyEnrollment(studentId, courseId);
                             System.out.println("Từ chối đăng ký khoá học thành công!");
                             return;
                        }
                        System.out.print("Bạn có chắc chắn muốn duyệt đăng ký khoá học này? (y/n): ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (!confirm.equals("y")) {
                            System.out.println("Đã huỷ bỏ việc duyệt đăng ký khoá học.");
                            return;
                        }
                        if (!enrollDAO.checkWaitingEnrollmentExist(studentId, courseId)) {
                            System.out.println("Sinh viên không đăng ký học khoá này, vui lòng chọn khoá học khác!");
                            break;
                        }
                        enrollDAO.approveStatusByAdmin(studentId, courseId);
                        System.out.println("Duyệt đăng ký khoá học thành công!");
                        return;
                    case "Q":
                        return;
                    default:
                        System.out.println("Lệnh không hợp lệ.");
                }

            }
        }
    }

    @Override
    public void showTotalCoursesAndStudents() {
        int totalCourses = courseService.countCourse();
        int totalStudents = studentService.countStudent();
        System.out.println("Tổng số lượng khoá học: " + totalCourses);
        System.out.println("Tổng số lượng học viên: " + totalStudents);
    }

    @Override
    public void showNumberOfStudentsByCourse() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listFullCoursesOrderBy("id");
        List<Long> counts = courses.stream()
                .map(course -> enrollments.stream()
                        .filter(e -> e.getCourse_id() == course.getId() && e.getStatus() == StatusType.CONFIRM)
                        .count())
                .toList();
        Helper.printNumberOfStudentInCourse(courses, counts);

    }

    @Override
    public void showTop5CoursesByEnrollment() {
        List<Course> courses = enrollDAO.getTop5CoursesByEnrollment();
        System.out.println("========= TOP 5 KHOÁ HỌC ĐÔNG HỌC VIÊN NHẤT =========");
        Helper.printNumberOfStudentInCourse(courses, courses.stream().map(
                course -> {
                    long count = enrollDAO.countNumberOfConfirmEnrollmentsByCourseId(course.getId());
                    return count;
                }
        ).toList());
    }

    @Override
    public void showCoursesWithMoreThan10Students() {
        List<Enrollment> enrollments = enrollDAO.getEnrollments();
        List<Course> courses = courseService.listFullCoursesOrderBy("id");
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
    public void listEnrolledCoursesByStudent(int studentId) {
        Scanner scanner = new Scanner(System.in);
        final int PAGE_SIZE = 5;
        int totalCourses = enrollDAO.countNumberOfConfirmCoursesByStudentID(studentId);
        if (totalCourses == 0) {
            System.out.println("Sinh viên chưa đăng ký khoá học nào được duyệt.");
            return;
        }
        int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
        int currentPage = 1;

        while (true){
            List<Course> courses = enrollDAO.getConfirmCoursesByStudentIDWithPaging(studentId, currentPage, PAGE_SIZE);
            System.out.println("============================= DANH SÁCH KHOÁ HỌC ĐÃ ĐƯỢC DUYỆT =============================");
            Helper.printCourses(courses);
            System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalCourses);
            System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [Q] Thoát");
            System.out.print("Nhập lệnh: ");
            String cmd = scanner.nextLine().trim().toUpperCase();
            switch (cmd) {
                case "N":
                    if (currentPage < totalPages) currentPage++;
                    else System.out.println("Đã ở trang cuối.");
                    break;
                case "P":
                    if (currentPage > 1) currentPage--;
                    else System.out.println("Đã ở trang đầu.");
                    break;
                case "G":
                    System.out.printf("Nhập số trang (1-%d): ", totalPages);
                    try {
                        int page = Integer.parseInt(scanner.nextLine().trim());
                        if (page >= 1 && page <= totalPages) currentPage = page;
                        else System.out.println("Số trang không hợp lệ.");
                    } catch (NumberFormatException e) {
                        System.out.println("Nhập không hợp lệ.");
                    }
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Lệnh không hợp lệ.");
            }
        }
    }

    @Override
    public void deleteEnrollByStudent(Scanner scanner) {
        int studentId = StudentView.studentLogin.getId();
        final int PAGE_SIZE = 5;
        int totalEnrollments = enrollDAO.countWaitingEnrollmentsByStudentId(studentId);
        if (totalEnrollments == 0) {
            System.out.println("Bạn không có khoá học nào đang chờ duyệt để huỷ đăng ký.");
            return;
        }
        int currentPage = 1;
        int totalPages = (int) Math.ceil((double) totalEnrollments / PAGE_SIZE);

        while (true){
            List<Course> courses = enrollDAO.getWaitingCourseByStudentIdWithPaging(studentId, currentPage, PAGE_SIZE);
            System.out.println("=== DANH SÁCH KHOÁ HỌC ĐÃ ĐĂNG KÝ NHƯNG CHƯA ĐƯỢC XÁC NHẬN ===");
            Helper.printCourses(courses);
            System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalEnrollments);
            System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [L] Chọn khoá học để xoá  [Q] Thoát");
            System.out.print("Nhập lệnh: ");
            String cmd = scanner.nextLine().trim().toUpperCase();
            switch (cmd) {
                case "N":
                    if (currentPage < totalPages) currentPage++;
                    else System.out.println("Đã ở trang cuối.");
                    break;
                case "P":
                    if (currentPage > 1) currentPage--;
                    else System.out.println("Đã ở trang đầu.");
                    break;
                case "G":
                    System.out.printf("Nhập số trang (1-%d): ", totalPages);
                    try {
                        int page = Integer.parseInt(scanner.nextLine().trim());
                        if (page >= 1 && page <= totalPages) currentPage = page;
                        else System.out.println("Số trang không hợp lệ.");
                    } catch (NumberFormatException e) {
                        System.out.println("Nhập không hợp lệ.");
                    }
                    break;
                case "L":
                    int courseId = Helper.getIntInput(scanner, "Nhập ID khoá học: ");
                    if (!enrollDAO.checkEnrollmentExist(studentId, courseId)) {
                        System.out.println("Sinh viên không học khoá này, vui lòng chọn khoá học khác!");
                        break;
                    }
                    System.out.print("Bạn có chắc chắn muốn huỷ đăng ký khoá học này? (y/n): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        System.out.println("Đã huỷ bỏ việc huỷ đăng ký khoá học.");
                        return;
                    }
                    enrollDAO.deleteEnrollmentByStudent(studentId, courseId);
                    System.out.println("Xoá đăng ký khoá học thành công!");
                    return;
                case "Q":
                    return;
                default:
                    System.out.println("Lệnh không hợp lệ.");
            }
        }
    }

}
