package ra.qlkhoahochocvien.bussiness.impl;

import org.mindrot.jbcrypt.BCrypt;
import ra.qlkhoahochocvien.bussiness.IStudentService;
import ra.qlkhoahochocvien.dao.IStudentDAO;
import ra.qlkhoahochocvien.dao.impl.StudentDAOImpl;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.model.Student;
import ra.qlkhoahochocvien.presentation.StudentView;
import ra.qlkhoahochocvien.utils.Helper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentServiceImpl implements IStudentService {
    private final IStudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public Student login(String email, String password) {
        Student student = studentDAO.findStudentByEmail(email);
        if (student != null)
            if (BCrypt.checkpw(password, student.getPassword()))
                return student;

        return null;
    }

    @Override
    public void showStudents(Scanner scanner) {
        final int PAGE_SIZE = 5;
        int totalStudents = studentDAO.countStudents();
        if (totalStudents == 0) {
            System.out.println("Không có sinh viên nào trong Database");
        }
        int currentPage = 1;
        int totalPages = (int) Math.ceil((double) totalStudents / PAGE_SIZE);

        while (true){
            List<Student> students = studentDAO.getStudentsListWithPaging(currentPage, PAGE_SIZE);
            System.out.println("============================= DANH SÁCH SINH VIÊN =============================");
            Helper.printStudents(students);
            System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalStudents);
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
    public Student getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    @Override
    public void addStudent(Scanner scanner) {
        Student student = new Student();
        System.out.println("Nhập thông tin sinh viên mới:");
        System.out.print("Họ tên: ");
        student.setName(scanner.nextLine());
        while (true) {
             System.out.print("Ngày sinh (yyyy-MM-dd): ");
             try {
                student.setDob(LocalDate.parse(scanner.nextLine()));
                break;
            } catch (Exception e) {
                System.out.println("Ngày sinh không hợp lệ, vui lòng nhập lại theo định dạng yyyy-MM-dd");
            }
        }
        System.out.print("Email: ");
        student.setEmail(scanner.nextLine());
        System.out.print("Nhập giới tính (Nam/Nữ): ");
        String genderInput = scanner.nextLine();
        while (!genderInput.equalsIgnoreCase("Nam") && !genderInput.equalsIgnoreCase("Nữ")) {
            System.out.print("Giới tính không hợp lệ. Vui lòng nhập lại (Nam/Nữ): ");
            genderInput = scanner.nextLine();
        }
        student.setSex(genderInput.equalsIgnoreCase("Nam"));
        System.out.print("Nhập số điện thoại: ");
        student.setPhone(scanner.nextLine());
        System.out.print("Password: ");
        String password = scanner.nextLine();
        while (password.isEmpty()) {
            System.out.print("Mật khẩu không được để trống, vui lòng nhập lại: ");
            password = scanner.nextLine();
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        student.setPassword(hashedPassword);
        student.setCreate_at(LocalDate.now());
        studentDAO.addStudent(student);
    }

    @Override
    public void updateStudent(Scanner scanner) throws Exception {
        System.out.println("=======================      CẬP NHẬT THÔNG TIN SINH VIÊN      =======================");
        int id = Helper.getIntInput(scanner, "Nhập ID sinh viên cần cập nhật: ");
        Student existingStudent = studentDAO.getStudentById(id);
        while (existingStudent == null) {
            System.out.println("Không tìm thấy sinh viên với ID: " + id);
            id = Helper.getIntInput(scanner, "Nhập ID sinh viên cần cập nhật: ");
            existingStudent = studentDAO.getStudentById(id);
        }
        System.out.println("Nhập thông tin mới (để trống nếu không muốn thay đổi):");
        System.out.print("Họ tên (" + existingStudent.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            existingStudent.setName(name);
        }
        System.out.print("Ngày sinh (" + existingStudent.getDob() + ") (yyyy-MM-dd): ");
        String dobInput = scanner.nextLine();
        try {
            if (!dobInput.isEmpty()) {
                existingStudent.setDob(LocalDate.parse(dobInput));
            }
        } catch (Exception e) {
            System.out.println("Ngày sinh không hợp lệ, bỏ qua cập nhật ngày sinh.");
        }
        System.out.print("Email (" + existingStudent.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            existingStudent.setEmail(email);
        }
        System.out.print("Giới tính (" + (existingStudent.getSex() ? "Nam" : "Nữ") + ") (Nam/Nữ): ");
        String genderInput = scanner.nextLine();
        if (!genderInput.isEmpty()) {
            while (!genderInput.equalsIgnoreCase("Nam") && !genderInput.equalsIgnoreCase("Nữ")) {
                System.out.println("Giới tính không hợp lệ. Vui lòng nhập lại (Nam/Nữ): ");
                genderInput = scanner.nextLine();
            }
            existingStudent.setSex(genderInput.equalsIgnoreCase("Nam"));
        }
        System.out.print("Password (để trống nếu không muốn thay đổi): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            existingStudent.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));
        }
        studentDAO.updateStudent(existingStudent);
    }

    @Override
    public void deleteStudent(Scanner scanner) {

        final int PAGE_SIZE = 5;
        int totalStudents = studentDAO.countStudents();
        if (totalStudents == 0) {
            System.out.println("Không có sinh viên nào trong Database");
        }
        int currentPage = 1;
        int totalPages = (int) Math.ceil((double) totalStudents / PAGE_SIZE);

        while (true){
            List<Student> students = studentDAO.getStudentsListWithPaging(currentPage, PAGE_SIZE);
            System.out.println("============================= DANH SÁCH SINH VIÊN =============================");
            Helper.printStudents(students);
            System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalStudents);
            System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [L] Xoá sinh viên  [Q] Thoát");
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
                    int id = Helper.getIntInput(scanner, "Nhập ID sinh viên cần xóa: ");
                    if (studentDAO.getStudentById(id) == null) {
                        System.out.println("Không tìm thấy sinh viên có ID = " + id + ".");
                        continue;
                    }
                    System.out.print("Bạn có chắc chắn muốn xóa sinh viên với ID " + id + "? (Y/N): ");
                    String confirm = scanner.nextLine();
                    while (!confirm.equalsIgnoreCase("Y") && !confirm.equalsIgnoreCase("N")) {
                        System.out.print("Lựa chọn không hợp lệ. Vui lòng nhập lại (Y/N): ");
                        confirm = scanner.nextLine();
                    }
                    if (confirm.equalsIgnoreCase("N")) {
                        System.out.println("Hủy xóa sinh viên.");
                        return;
                    }
                    else  {
                        studentDAO.deleteStudent(id);
                        System.out.println("Đã xóa sinh viên với ID: " + id);
                        return;
                    }
                case "Q":
                    return;
                default:
                    System.out.println("Lệnh không hợp lệ.");
            }
        }
    }

    @Override
    public void getStudents(Scanner scanner) throws Exception {
        System.out.println("Chọn tiêu chí tìm kiếm: ");
        System.out.println("1. Tìm kiếm theo tên");
        System.out.println("2. Tìm kiếm theo email");
        System.out.println("3. Tìm kiếm theo ID");
        int choice = Helper.getIntInput(scanner, "Nhập lựa chọn của bạn: ");
        List<Student> students = new ArrayList<>();
        switch (choice) {
            case 1:
                System.out.print("Nhập tên cần tìm: ");
                String name = scanner.nextLine();
                students = studentDAO.findStudentsByName(name);
                break;
            case 2:
                System.out.print("Nhập email cần tìm: ");
                String email = scanner.nextLine();
                students = studentDAO.findStudentsByEmail(email);
                break;
            case 3:
                int id = Helper.getIntInput(scanner, "Nhập ID cần tìm: ");
                Student student = studentDAO.getStudentById(id);
                students.add(student);
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại");
                break;
        }
        if (students.isEmpty()) {
            System.out.println("Không tìm thấy sinh viên nào.");
        } else {
            final int PAGE_SIZE = 5;
            int totalStudents = students.size();
            int currentPage = 1;
            int totalPages = (int) Math.ceil((double) totalStudents / PAGE_SIZE);

            while (true){
                int start = (currentPage - 1) * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, totalStudents);
                List<Student> pageStudents = students.subList(start, end);
                System.out.println("============================= KẾT QUẢ TÌM KIẾM =============================");
                Helper.printStudents(pageStudents);
                System.out.printf("Trang %d / %d  (Tổng: %d sinh viên)\n", currentPage, totalPages, totalStudents);
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
    }

    @Override
    public void showStudentInSorted(Scanner scanner) {
        while (true) {
            System.out.println("Chọn tiêu chí sắp xếp: ");
            System.out.println("1. Sắp xếp theo tên");
            System.out.println("2. Sắp xếp theo email");
            System.out.println("3. Sắp xếp theo ID");
            System.out.println("4. Quay lại menu chính");
            int choice = Helper.getIntInput(scanner, "Nhập lựa chọn: ");
            System.out.println("Chọn thứ tự sắp xếp");
            System.out.println("1. Tăng dần");
            System.out.println("2. Giảm dần");
            int orderChoice = Helper.getIntInput(scanner, "Nhập lựa chọn: ");
            String orderBy;
            switch (choice) {
                case 1:
                    if (orderChoice == 1) {
                        orderBy = "name asc";
                    } else {
                        orderBy = "name desc";
                    }
                    break;
                case 2:
                    if (orderChoice == 1) {
                        orderBy = "email asc";
                    } else {
                        orderBy = "email desc";
                    }
                    break;
                case 3:
                    if (orderChoice == 1) {
                        orderBy = "id asc";
                    } else {
                        orderBy = "id desc";
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
                    return;
            }
            List<Student> sortedStudents = studentDAO.listStudentsOrderBy(orderBy);
            if (sortedStudents.isEmpty()) {
                System.out.println("Không có sinh viên nào.");
                continue;
            }
            final int PAGE_SIZE = 5;
            int totalStudents = sortedStudents.size();
            int currentPage = 1;
            int totalPages = (int) Math.ceil((double) totalStudents / PAGE_SIZE);

            while (true){
                int start = (currentPage - 1) * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, totalStudents);
                List<Student> pageStudents = sortedStudents.subList(start, end);
                System.out.println("============================= DANH SÁCH SINH VIÊN SAU SẮP XẾP =============================");
                Helper.printStudents(pageStudents);
                System.out.printf("Trang %d / %d  (Tổng: %d sinh viên)\n", currentPage, totalPages, totalStudents);
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
                        break;
                    default:
                        System.out.println("Lệnh không hợp lệ.");
                }
                if (cmd.equals("Q")) break;
            }
        }

    }

    @Override
    public List<Student> getStudentsList() {
        return studentDAO.listStudentsOrderBy("id asc");
    }

    @Override
    public void changePassword(Scanner scanner) {
        System.out.print("Nhập mật khẩu cũ: ");
        String oldPassword = scanner.nextLine();
        while (!BCrypt.checkpw(oldPassword, StudentView.studentLogin.getPassword())) {
            System.out.println("Mật khẩu cũ vừa nhập không chính xác, vui lòng nhập lại!");
            System.out.print("Nhập mật khẩu cũ: ");
            oldPassword = scanner.nextLine();
        }
        System.out.print("Nhập mật khẩu mới: ");
        String newPassword = scanner.nextLine();
        while (BCrypt.checkpw(newPassword, StudentView.studentLogin.getPassword())) {
            System.out.println("Mật khẩu mới trùng với mật khẩu cũ, vui lòng nhập mật khẩu khác");
            System.out.print("Nhập mật khẩu mới: ");
            newPassword = scanner.nextLine();
        }
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        StudentView.studentLogin.setPassword(hashedPassword);
        studentDAO.updateStudent(StudentView.studentLogin);
        System.out.println("Đã đổi mật khẩu thành công!");
    }
}
