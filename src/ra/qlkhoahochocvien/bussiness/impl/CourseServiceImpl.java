package ra.qlkhoahochocvien.bussiness.impl;

import ra.qlkhoahochocvien.bussiness.ICourseService;
import ra.qlkhoahochocvien.dao.ICourseDAO;
import ra.qlkhoahochocvien.dao.IEnrollDAO;
import ra.qlkhoahochocvien.dao.impl.CourseDAOImpl;
import ra.qlkhoahochocvien.dao.impl.EnrollDAOImpl;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.presentation.StudentView;
import ra.qlkhoahochocvien.utils.Helper;

import java.util.List;
import java.util.Scanner;

public class CourseServiceImpl implements ICourseService {
    public final ICourseDAO courseDAO = new CourseDAOImpl();
    public final IEnrollDAO enrollDAO = new EnrollDAOImpl();
    @Override
    public void listCourses(Scanner scanner) {
        final int PAGE_SIZE = 5;
        int totalCourses = courseDAO.countCourses();
        if (totalCourses == 0) {
            System.out.println("Chưa có khoá học nào.");
            return;
        }
        int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
        int currentPage = 1;

        while (true) {
            List<Course> courses = courseDAO.listCoursesWithPagination(currentPage, PAGE_SIZE);
            System.out.println("============================= DANH SÁCH KHOÁ HỌC =============================");
            Helper.printCourses(courses);
            System.out.printf("Trang %d / %d  (Tổng: %d khoá học)\n", currentPage, totalPages, totalCourses);
            System.out.println("[N] Trang sau  [P] Trang trước  [G] Đến trang  [L] Đề xuất 5 khoá học có nhiều lượt đăng ký nhất  [Q] Thoát");
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
                    List<Course> topCourses = enrollDAO.getTop5CoursesByEnrollment();
                    System.out.println("============================= TOP 5 KHOÁ HỌC ĐƯỢC ĐĂNG KÝ NHIỀU NHẤT =============================");
                    Helper.printCourses(topCourses);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Lệnh không hợp lệ.");
            }
        }
    }

    @Override
    public Course getCourseByID(int id) {
        return courseDAO.getCourse(id);
    }

    @Override
    public void addCouse(Course course) {
        courseDAO.addCourse(course);
    }

    @Override
    public void updateCourse(Course course) {
        courseDAO.updateCourse(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseDAO.deleteCourse(id);
    }

    @Override
    public void getCourseByName(String name) {
        final int PAGE_SIZE = 5;
        int totalCourses = courseDAO.countCoursesByName(name);
        if (totalCourses == 0) {
            System.out.println("Không tìm thấy khoá học nào với tên: " + name);
            return;
        }
        int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
        int currentPage = 1;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            List<Course> courses = courseDAO.getCourseByNameWithPagination(name, currentPage, PAGE_SIZE);
            System.out.println("============================= KẾT QUẢ TÌM KIẾM =============================");
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
    public void listCoursesOrderBy(String orderBy) {
        final int PAGE_SIZE = 5;
        int totalCourses = courseDAO.countCourses();
        if (totalCourses == 0) {
            System.out.println("Chưa có khoá học nào.");
            return;
        }
        int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);
        int currentPage = 1;
        Scanner scanner = new Scanner(System.in);
        while (true){
            List<Course> courses = courseDAO.listCoursesOrderByWithPagination(orderBy, currentPage, PAGE_SIZE);

            System.out.println("============================= DANH SÁCH KHOÁ HỌC =============================");
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
    public int countCourse() {
        return courseDAO.countStudents();
    }

    @Override
    public List<Course> listFullCoursesOrderBy(String orderBy) {
        return courseDAO.listFullCoursesOrderBy(orderBy);
    }
}
