package ra.qlkhoahochocvien.presentation;

import ra.qlkhoahochocvien.bussiness.IStudentService;
import ra.qlkhoahochocvien.bussiness.impl.AdminServiceImpl;
import ra.qlkhoahochocvien.bussiness.IAdminService;
import ra.qlkhoahochocvien.bussiness.impl.StudentServiceImpl;
import ra.qlkhoahochocvien.model.Admin;

import java.util.Scanner;

public class AdminView {
    public static Admin userAdmin = null;
    public static final IAdminService adminService = new AdminServiceImpl();
    public static final IStudentService studentService = new StudentServiceImpl();
    public static void showMenuLogin(Scanner scanner) {
        while (true) {
            System.out.print("Nhập username: ");
            String email = scanner.nextLine();
            while(email == ""){
                System.out.println("Dữ liệu nhập vào không hợp lệ (không được rỗng), vui lòng nhập lại!");
                System.out.print("Nhập username: ");
                email = scanner.nextLine();
            }

            System.out.print("Nhập password: ");
            String password = scanner.nextLine();

            while (password == ""){
                System.out.println("Dữ liệu nhập vào không hợp lệ (không được rỗng), vui lòng nhập lại!");
                System.out.print("Nhập password: ");
                password = scanner.nextLine();
            }

            Admin ad = adminService.login(email, password);
            if (ad != null) {
                System.out.println("Đăng nhập thành công!");
                userAdmin = ad;
                break;
            }
            else{
                System.out.println("Đăng nhập thất bại, email hoặc password không đúng, hãy thử lại!");
            }
        }

    }

    public static void showMainMenu(Scanner scanner) throws Exception {
        while (true) {
            System.out.println("============ MENU ADMIN ============");
            System.out.println("1. Quản lý khoá học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký học");
            System.out.println("4. Thống kê học viên theo khoá học");
            System.out.println("5. Đăng xuất");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    showCourseManaer(scanner);
                    break;
                case 2:
                    showStudentManager(scanner);
                    break;
                case 3:
                    showEnrollManager();
                    break;
                case 4:
                    showStatistic();
                    break;
                case 5:
                    System.out.println("Đăng xuất thành công!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại");
                    break;
            }
        }
    }

    public static void showCourseManaer(Scanner scanner) {
        while (true) {
            System.out.println("============ QUẢN LÝ KHOÁ HỌC ============");
            System.out.println("1. Hiển thị danh sách khoá học");
            System.out.println("2. Thêm khoá học mới");
            System.out.println("3. Chỉnh sửa thông tin khoá học (hiển thị menu chọn thuộc tính cần sửa)");
            System.out.println("4. Xoá khoá học (xác nhận trước khi xoá)");
            System.out.println("5. Tìm kiếm theo tên (tương đối)");
            System.out.println("6. Sắp xếp theo tên hoặc id (tăng/giảm dần)");
            System.out.println("7. Quay về menu chính");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    CourseView.showCourseList();
                    break;
                case 2:
                    CourseView.addNewCourse(scanner);
                    break;
                case 3:
                    CourseView.editCourse(scanner);
                    break;
                case 4:
                    CourseView.deleteCourse(scanner);
                    break;
                case 5:
                    CourseView.getCourseByName(scanner);
                    break;
                case 6:
                    CourseView.showCourseInSorted(scanner);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Lua chon khong hop le, vui long chon lai.");
                    break;
            }
        }
    }

    public static void showStudentManager(Scanner scanner) throws Exception {
        while(true) {
            System.out.println("============ QUẢN LÝ HỌC VIÊN ============");
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa thông tin học viên (hiển thị menu chọn thuộc tính cần sửa)");
            System.out.println("4. Xoá học viên");
            System.out.println("5. Tìm kiếm theo tên, email hoặc id (tương đối)");
            System.out.println("6. Sắp xếp theo tên hoặc id (tăng/giảm dần)");
            System.out.println("7. Quay về menu chính");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    studentService.showStudents();
                    break;
                case 2:
                    studentService.addStudent(scanner);
                    break;
                case 3:
                    studentService.updateStudent(scanner);
                    break;
                case 4:
                    studentService.deleteStudent(scanner);
                    break;
                case 5:
                    studentService.getStudents(scanner);
                    break;
                case 6:
                    studentService.showStudentInSorted(scanner);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại");
                    break;
            }
        }
    }

    public static void showEnrollManager() {
        while (true) {
            System.out.println("1. Hiển thị học viên theo từng khoá học");
            System.out.println("2. Thêm học viên vào khoá học");
            System.out.println("3. Xoá học viên khỏi khoá học");
            System.out.println("4. Quay về menu chính");
        }
    }

    public static void showStatistic() {
        System.out.println("1. Thống kê tổng số lượng khoá học và học viên");
        System.out.println("2. Thống kê học viên theo từng khoá học");
        System.out.println("3. Top 5 khoá học đông học viên nhất");
        System.out.println("4. Liệt kê khoá học có trên 10 học viên");
        System.out.println("5. Quay về menu chính");
    }

}
