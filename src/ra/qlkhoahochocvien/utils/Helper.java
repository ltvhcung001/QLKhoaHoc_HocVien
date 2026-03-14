package ra.qlkhoahochocvien.utils;

import org.mindrot.jbcrypt.BCrypt;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.model.Enrollment;
import ra.qlkhoahochocvien.model.StatusType;
import ra.qlkhoahochocvien.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Helper {
    public static void printCourses(List<Course> course) {
        System.out.println("┌──────┬──────────────────────────────────────────────────┬──────────┬──────────────────────────────┬───────────────┐");
        System.out.printf("│%-6s│%-50s│%-10s│%-30s│%-15s│\n",
                "ID",
                "Name",
                "Duration",
                "Instructor",
                "Create At");
        System.out.println("├──────┼──────────────────────────────────────────────────┼──────────┼──────────────────────────────┼───────────────┤");
        for (Course c : course) {
            System.out.printf("│%-6d│%-50s│%-10d│%-30s│%-15s│\n",
                    c.getId(),
                    c.getName().length() > 50 ? c.getName().substring(0, 47) + "..." : c.getName(),
                    c.getDuration(),
                    c.getInstructor().length() > 30 ? c.getInstructor().substring(0, 27) + "..." : c.getInstructor(),
                    c.getCreate_at());
        }
        System.out.println("└──────┴──────────────────────────────────────────────────┴──────────┴──────────────────────────────┴───────────────┘");
    }

    public static void printStudents(List<Student> students) {
        System.out.println("┌──────┬────────────────────┬────────────────────┬────────────────────┬──────────┬───────────────┬────────────────────┬────────────────────┐");
        System.out.printf("│%-6s│%-20s│%-20s│%-20s│%-10s│%-15s│%-20s│%-20s│\n",
                "ID",
                "Name",
                "Date of Birth",
                "Email",
                "Sex",
                "Phone",
                "Password",
                "Create At");
        System.out.println("├──────┼────────────────────┼────────────────────┼────────────────────┼──────────┼───────────────┼────────────────────┼────────────────────┤");
        for (Student s : students) {
            System.out.printf("│%-6d│%-20s│%-20s│%-20s│%-10s│%-15s│%-20s│%-20s│\n",
                    s.getId(),
                    s.getName(),
                    s.getDob(),
                    s.getEmail(),
                    s.getSex()? "Male":"Female",
                    s.getPhone(),
                    s.getPassword().length() > 10 ? s.getPassword().substring(0, 10) + "..." : s.getPassword(),
                    s.getCreate_at());
        }
        System.out.println("└──────┴────────────────────┴────────────────────┴────────────────────┴──────────┴───────────────┴────────────────────┴────────────────────┘");
    }

    public static void printEnrollments(List<Enrollment> enrollments, List<Student> students, List<Course> courses) {
        System.out.println("┌──────┬──────────┬──────────────────────────────┬──────────┬──────────────────────────────┬──────────────────────────────┬───────────────┐");
        System.out.printf("│%-6s│%-10s│%-30s│%-10s│%-30s│%-30s│%-15s│\n",
                "ID",
                "Student ID",
                "Student Name",
                "Course ID",
                "Course Name",
                "Register At",
                "Status");
        System.out.println("├──────┼──────────┼──────────────────────────────┼──────────┼──────────────────────────────┼──────────────────────────────┼───────────────┤");
//        System.out.println("├──────┼──────────┼───────────────┼────────────────────┤");
        for (Enrollment e : enrollments) {
            System.out.printf("│%-6d│%-10s│%-30s│%-10s│%-30s│%-30s│%-15s│\n",
                    e.getId(),
                    e.getStudent_id(),
                    students.stream().filter(s -> s.getId() == e.getStudent_id()).findFirst().map(Student::getName).orElse("Unknown"),
                    e.getCourse_id(),
                    courses.stream().filter(c -> c.getId() == e.getCourse_id()).findFirst().map(Course::getName).orElse("Unknown"),
                    e.getRegistered_at(),
                    e.getStatus().name());
        }
        System.out.println("└──────┴──────────┴──────────────────────────────┴──────────┴──────────────────────────────┴──────────────────────────────┴───────────────┘");
    }

    public static void printEnrollmentsByCourses(List<Enrollment> enrollments, List<Student> students, List<Course> courses) {
        for (Course c : courses) {
            System.out.println("Course: " + c.getName());
            List<Enrollment> enrollmentsByCourse = enrollments.stream().filter(e -> e.getCourse_id() == c.getId()).toList();
            printEnrollments(enrollmentsByCourse, students, courses);
        }
    }

    public static void printEnrollmentsByStudents(List<Enrollment> enrollments, List<Course> courses, int studentId) {
        System.out.println("┌──────┬────────────────────┬──────────┬────────────────────┬──────────────────────────────┬───────────────┐");
        System.out.printf("│%-6s│%-20s│%-10s│%-20s│%-30s│%-15s│\n",
                "ID",
                "Name",
                "Duration",
                "Instructor",
                "Register At",
                "Status");
        System.out.println("├──────┼────────────────────┼──────────┼────────────────────┼──────────────────────────────┼───────────────┤");
        for (Course c : courses) {
            System.out.printf("│%-6d│%-20s│%-10d│%-20s│%-30s│%-15s│\n",
                c.getId(),
                c.getName(),
                c.getDuration(),
                c.getInstructor(),
                enrollments.stream().filter(e -> e.getCourse_id() == c.getId()
                                                    && e.getStudent_id() == studentId)
                                    .findFirst()
                                    .map(e -> e.getRegistered_at().toString())
                                    .orElse("Not Enrolled"),
                enrollments.stream().filter(e -> e.getCourse_id() == c.getId()
                                                    && e.getStudent_id() == studentId)
                                    .findFirst()
                                    .map(e -> e.getStatus().name())
                                    .orElse("Not Enrolled"));
        }
        System.out.println("└──────┴────────────────────┴──────────┴────────────────────┴──────────────────────────────┴───────────────┘");
    }

    public static void main(String[] args) {
//        List<Course> courses = List.of(
//                new Course(1, "Java", 30, "Nguyen Van A", java.time.LocalDate.now()),
//                new Course(2, "Python", 25, "Le Thi B", java.time.LocalDate.now()),
//                new Course(3, "C++", 20, "Tran Van C", java.time.LocalDate.now())
//        );
//        printCourses(courses);
//        List<Student> students = List.of(
//                new Student(1, "Nguyen Van A", java.time.LocalDate.of(2000, 1, 1), "nva@gmail.com", true, "0123456789", "password", LocalDate.now()),
//                new Student(2, "Le Thi B", java.time.LocalDate.of(2001, 2, 2), "ltb@gmail.com", false, "0987654321", "password", LocalDate.now()),
//                new Student(3, "Tran Van C", java.time.LocalDate.of(2002, 3, 3), "tvanc@gmail.com", true, "0123456789", "password", LocalDate.now()));
//        printStudents(students);
//        List<Enrollment> enrollments = List.of(
//                new Enrollment(1, 1, 1, java.time.LocalDateTime.now(), StatusType.CONFIRM),
//                new Enrollment(2, 1, 3, java.time.LocalDateTime.now(), StatusType.CANCEL),
//                new Enrollment(3, 3, 2, java.time.LocalDateTime.now(), StatusType.WAITING),
//                new Enrollment(4, 1, 2, java.time.LocalDateTime.now(), StatusType.CONFIRM),
//                new Enrollment(5, 1, 3, java.time.LocalDateTime.now(), StatusType.CANCEL),
//                new Enrollment(6, 3, 1, java.time.LocalDateTime.now(), StatusType.WAITING)
//        );
//        printEnrollmentsByStudents(enrollments, courses, 3);
        String password = "123";
        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt(12)));
//        printMainMenu();
    }

    public static void printMainMenu(){
        System.out.println("┌──────────     HỆ THỐNG QUẢN LÝ ĐÀO TẠO     ──────────┐");
        System.out.println("│ 1. Đăng nhập với tư cách Quản trị viên               │");
        System.out.println("│ 2. Đăng nhập với tư cách Học viên                    │");
        System.out.println("│ 3. Cài đặt thông số hiển thị                         │");
        System.out.println("│ 4. Thoát                                             │");
        System.out.println("└──────────────────────────────────────────────────────┘");
    }

    public static int getIntInput(Scanner scanner, String prompt) {
        int choice;
        while (true) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 0){
                    System.out.println("Nhập số lớn hơn 0");
                    continue;
                }
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ, vui lòng nhập lại!");
            }
        }
    }

}
