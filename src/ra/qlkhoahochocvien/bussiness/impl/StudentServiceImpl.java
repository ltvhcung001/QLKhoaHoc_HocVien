package ra.qlkhoahochocvien.bussiness.impl;

import org.mindrot.jbcrypt.BCrypt;
import ra.qlkhoahochocvien.bussiness.IStudentService;
import ra.qlkhoahochocvien.dao.IStudentDAO;
import ra.qlkhoahochocvien.dao.impl.StudentDAOImpl;
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
    public void showStudents() {
        List<Student> students = studentDAO.getStudents();
        if (students == null || students.isEmpty()) {
            System.out.println("Không có sinh viên nào.");
            return;
        }
        System.out.println("Danh sách sinh viên:");
        Helper.printStudents(students);
    }

    @Override
    public Student getStudentById(Scanner scanner) {
        System.out.print("Nhập ID sinh viên: ");
        int id = Integer.parseInt(scanner.nextLine());
        return studentDAO.getStudentById(id);
    }

    @Override
    public void addStudent(Scanner scanner) {
        Student student = new Student();
        System.out.println("Nhập thông tin sinh viên mới:");
        System.out.print("Họ tên: ");
        student.setName(scanner.nextLine());
        System.out.print("Ngày sinh (yyyy-MM-dd): ");
        student.setDob(LocalDate.parse(scanner.nextLine()));
        System.out.print("Email: ");
        student.setEmail(scanner.nextLine());
        System.out.print("Nhập giới tính (Nam/Nữ): ");
        String genderInput = scanner.nextLine();
        while (!genderInput.equalsIgnoreCase("Nam") && !genderInput.equalsIgnoreCase("Nữ")) {
            System.out.println("Giới tính không hợp lệ. Vui lòng nhập lại (Nam/Nữ): ");
            genderInput = scanner.nextLine();
        }
        student.setSex(genderInput.equalsIgnoreCase("Nam"));
        System.out.print("Nhập số điện thoại: ");
        student.setPhone(scanner.nextLine());
        System.out.print("Password: ");
        student.setPassword(scanner.nextLine());
        student.setCreate_at(LocalDate.now());
        studentDAO.addStudent(student);
    }

    @Override
    public void updateStudent(Scanner scanner) throws Exception {
        System.out.print("Nhập ID sinh viên cần cập nhật: ");
        int id = Integer.parseInt(scanner.nextLine());
        Student existingStudent = studentDAO.getStudentById(id);
        if (existingStudent == null) {
            System.out.println("Không tìm thấy sinh viên với ID: " + id);
        }
        System.out.println("Nhập thông tin mới (để trống nếu không muốn thay đổi):");
        System.out.print("Họ tên (" + existingStudent.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            existingStudent.setName(name);
        }
        System.out.print("Ngày sinh (" + existingStudent.getDob() + ") (yyyy-MM-dd): ");
        String dobInput = scanner.nextLine();
        if (!dobInput.isEmpty()) {
            existingStudent.setDob(LocalDate.parse(dobInput));
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
            existingStudent.setPassword(password);
        }
        studentDAO.updateStudent(existingStudent);
    }

    @Override
    public void deleteStudent(Scanner scanner) {
        System.out.print("Nhập ID sinh viên cần xóa: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Bạn có chắc chắn muốn xóa sinh viên với ID " + id + "? (Y/N)");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Hủy xóa sinh viên.");
            return;
        }
        studentDAO.deleteStudent(id);
        System.out.println("Đã xóa sinh viên với ID: " + id);
    }

    @Override
    public void getStudents(Scanner scanner) throws Exception {
        System.out.println("Chọn tiêu chí tìm kiếm: ");
        System.out.println("1. Tìm kiếm theo tên");
        System.out.println("2. Tìm kiếm theo email");
        System.out.println("3. Tìm kiếm theo ID");
        System.out.print("Nhập lựa chọn: ");
        int choice = Integer.parseInt(scanner.nextLine());
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
                System.out.print("Nhập ID cần tìm: ");
                int id = Integer.parseInt(scanner.nextLine());
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
            System.out.println("Kết quả tìm kiếm:");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    @Override
    public void showStudentInSorted(Scanner scanner) {
        System.out.println("Chọn tiêu chí sắp xếp: ");
        System.out.println("1. Sắp xếp theo tên");
        System.out.println("2. Sắp xếp theo email");
        System.out.println("3. Sắp xếp theo ID");
        System.out.print("Nhập lựa chọn: ");
        int choice = Integer.parseInt(scanner.nextLine());
        System.out.println("Chọn thứ tự sắp xếp");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");
        System.out.print("Nhập lựa chọn: ");
        int orderChoice = Integer.parseInt(scanner.nextLine());
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
            default:
                System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
                return;
        }
        List<Student> sortedStudents = studentDAO.listStudentsOrderBy(orderBy);
        System.out.println("Danh sách sinh viên sau khi sắp xếp: ");
        for (Student student : sortedStudents) {
            System.out.println(student);
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
