package ra.qlkhoahochocvien.presentation;

import ra.qlkhoahochocvien.bussiness.ICourseService;
import ra.qlkhoahochocvien.bussiness.impl.CourseServiceImpl;
import ra.qlkhoahochocvien.model.Course;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CourseView {
    private static final ICourseService courseService = new CourseServiceImpl();

    public static void showCourseList() {
        List<Course> courses = courseService.listCourses();
        if (courses.isEmpty()) {
            System.out.println("Chưa có khoá học nào.");
            return;
        }
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    public static void addNewCourse(Scanner scanner) {
        System.out.println("===== THÊM KHOÁ HỌC MỚI =====");

        String name;
        do {
            System.out.print("Nhập tên khoá học: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Tên khoá học không đuược để trống.");
            }
        } while (name.isEmpty());

        int duration;
        while (true) {
            System.out.print("Nhập thời lượng (số giờ): ");
            try {
                duration = Integer.parseInt(scanner.nextLine().trim());
                if (duration <= 0) {
                    System.out.println("Thời lượng của khoá học phải lớn hơn 0.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên.");
            }
        }

        String instructor;
        do {
            System.out.print("Nhập tên giảng viên phụ trách: ");
            instructor = scanner.nextLine().trim();
            if (instructor.isEmpty()) {
                System.out.println("Tên giảng viên không được để trống.");
            }
        } while (instructor.isEmpty());

        Course newCourse = new Course(0, name, duration, instructor, LocalDate.now());
        try {
            courseService.addCouse(newCourse);
            System.out.println("Thêm khoá học mới thành công!");
        } catch (Exception e) {
            System.out.println("Thêm khoá học thất bại, lý do: " + e.getMessage());
        }
    }

    public static void editCourse(Scanner scanner) {
        System.out.print("Nhập ID khoá học cần chỉnh sửa: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Course course = courseService.getCourse(id);
        if (course == null) {
            System.out.println("Không tìm thấy khoá học với ID: " + id);
            return;
        }
        System.out.println("Thông tin khoá học hiện tại: ");
        System.out.println(course);
        System.out.println("Chọn thuộc tính cần chỉnh sửa: ");
        System.out.println("1. Tên khoá học");
        System.out.println("2. Thời lượng");
        System.out.println("3. Giảng viên phụ trách");
        System.out.println("4. Quay lại");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        switch (choice) {
            case 1:
                System.out.print("Nhập tên khoá học mới: ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty()) {
                    course.setName(newName);
                } else {
                    System.out.println("Tên khoá học không được để trống, giữ nguyên giá trị cũ.");
                }
                break;
            case 2:
                System.out.print("Nhập thời lượng mới (số giờ): ");
                try {
                    int newDuration = Integer.parseInt(scanner.nextLine().trim());
                    if (newDuration > 0) {
                        course.setDuration(newDuration);
                    } else {
                        System.out.println("Thời lượng phải lớn hơn 0, giữ nguyên giá trị cũ.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Vui lòng nhập một số nguyên, giữ nguyên giá trị cũ.");
                }
                break;
            case 3:
                System.out.print("Nhập tên giảng viên mới: ");
                String newInstructor = scanner.nextLine().trim();
                if (!newInstructor.isEmpty()) {
                    course.setInstructor(newInstructor);
                } else {
                    System.out.println("Tên giảng viên không được để trống, giữ nguyên giá trị cũ.");
                }
                break;
            case 4:
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
        }
        try {
            courseService.updateCourse(course);
            System.out.println("Cập nhật khoá học thành công!");
        } catch (Exception e) {
            System.out.println("Cập nhật khoá học thất bại, lý do: " + e.getMessage());
        }
    }

    public static void deleteCourse(Scanner scanner) {
        System.out.print("Nhập ID khoá học cần xoá: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Course course = courseService.getCourse(id);
        if (course == null) {
            System.out.println("Không tìm thấy khoá học với ID: " + id);
            return;
        }
        System.out.println("Thông tin khoá học cần xoá: ");
        System.out.println(course);
        System.out.print("Bạn có chắc chắn muốn xoá khoá học này? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y")) {
            try {
                courseService.deleteCourse(id);
                System.out.println("Xoá khoá học thành công!");
            } catch (Exception e) {
                System.out.println("Xoá khoá học thất bại, lý do: " + e.getMessage());
            }
        } else {
            System.out.println("Đã huỷ xoá khoá học.");
        }
    }

    public static void getCourseByName(Scanner scanner) {
        System.out.print("Nhập tên khoá học cần tìm kiếm: ");
        String name = scanner.nextLine().trim();
        List<Course> courses = courseService.getCourseByName(name);
        if (courses == null || courses.isEmpty()) {
            System.out.println("Không tìm thấy khoá học nào với tên: " + name);
            return;
        }
        System.out.println("Kết quả tìm kiếm: ");
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    public static void showCourseInSorted(Scanner scanner) {
        System.out.println("Chọn thuộc tính để sắp xếp: ");
        System.out.println("1. Tên khoá học");
        System.out.println("2. ID khoá học");
        System.out.print("Lựa chọn của bạn: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Chọn thứ tự sắp xếp: ");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");
        System.out.print("Lựa chọn của bạn: ");
        int orderChoice = Integer.parseInt(scanner.nextLine().trim());
        List<Course> sortedCourses;
        switch (choice) {
            case 1:

                if (orderChoice == 1) {
                    sortedCourses = courseService.listCoursesOrderBy("name asc");
                } else {
                    sortedCourses = courseService.listCoursesOrderBy("name desc");
                }
                break;
            case 2:
                if (orderChoice == 1) {
                    sortedCourses = courseService.listCoursesOrderBy("id asc");
                } else {
                    sortedCourses = courseService.listCoursesOrderBy("id desc");
                }
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
                return;
        }
        System.out.println("Danh sách khoá học trước khi sắp xếp: ");
        showCourseList();
        System.out.println("===============================================");
        System.out.println("Danh sách khoá học sau khi sắp xếp: ");
        for (Course course : sortedCourses) {
            System.out.println(course);
        }

    }
}