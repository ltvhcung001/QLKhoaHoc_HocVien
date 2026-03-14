package ra.qlkhoahochocvien.presentation;

import ra.qlkhoahochocvien.bussiness.IEnrollService;
import ra.qlkhoahochocvien.bussiness.IStudentService;
import ra.qlkhoahochocvien.bussiness.impl.EnrollServiceImpl;
import ra.qlkhoahochocvien.bussiness.impl.StudentServiceImpl;
import ra.qlkhoahochocvien.model.Student;
import ra.qlkhoahochocvien.utils.Helper;

import java.time.LocalDate;
import java.util.Scanner;

public class StudentView {
    public static Student studentLogin = null;
    public static final IStudentService studentService = new StudentServiceImpl();
    public static final IEnrollService enrollService = new EnrollServiceImpl();
    public static void showMenuLogin(Scanner scanner) {
        while (true) {
            System.out.print("Nhập email: ");
            String email = scanner.nextLine();

            while(email.equals("")){
                System.out.println("Dữ liệu nhập vào không hợp lệ (không được rỗng), vui lòng nhập lại!");
                System.out.print("Nhập email: ");
                email = scanner.nextLine();
            }

            System.out.print("Nhập password: ");
            String password = scanner.nextLine();

            while(password.equals("")){
                System.out.println("Dữ liệu nhập vào không hợp lệ (không được rỗng), vui lòng nhập lại!");
                System.out.print("Nhập password: ");
                password = scanner.nextLine();
            }

            Student s = studentService.login(email, password);
            if (s != null) {
                System.out.println("Đăng nhập thành công!");
                studentLogin = s;
                showMainMenu(scanner);
                break;
            }
            else{
                System.out.println("Đăng nhập thất bại, email hoặc password không đúng, hãy thử lại!");
                break;
            }
        }
    }

    public static void showMainMenu(Scanner scanner) {
        while (true) {
            System.out.println("┌───────────────     MENU HỌC VIÊN      ───────────────┐");
            System.out.println("│ 1. Xem danh sách khoá học                            │");
            System.out.println("│ 2. Tìm kiếm khoá học theo tên                        │");
            System.out.println("│ 3. Đăng ký khoá học                                  │");
            System.out.println("│ 4. Xem khoá học đã đăng ký                           │");
            System.out.println("│ 5. Huỷ đăng ký (nếu chưa được xác nhận)              │");
            System.out.println("│ 6. Đổi mật khẩu                                      │");
            System.out.println("│ 7. Đăng xuất                                         │");
            System.out.println("└──────────────────────────────────────────────────────┘");
            int choice = Helper.getIntInput(scanner, "Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    CourseView.showCourseList(scanner);
                    break;
                case 2:
                    CourseView.findCourseByName(scanner);
                    break;
                case 3:
                    enrollService.addEnrollByStudent(scanner);
                    break;
                case 4:
                    enrollService.listEnrolledCoursesByStudent(StudentView.studentLogin.getId());
                    break;
                case 5:
                    enrollService.deleteEnrollByStudent(scanner);
                    break;
                case 6:
                    studentService.changePassword(scanner);
                    break;
                case 7:
                    System.out.println("Đăng xuất thành công!");
                    studentLogin = null;
                    return;
                default:
                    System.out.println("Lựa chọn sai, vui lòng chọn lại: ");
            }
        }

    }

}
