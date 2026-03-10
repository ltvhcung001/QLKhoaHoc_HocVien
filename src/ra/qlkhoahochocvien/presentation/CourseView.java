package ra.qlkhoahochocvien.presentation;

import ra.qlkhoahochocvien.bussiness.ICourseService;
import ra.qlkhoahochocvien.bussiness.impl.CourseServiceImpl;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.utils.Helper;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CourseView {
    private static final ICourseService courseService = new CourseServiceImpl();

    public static void showCourseList() {
        courseService.listCourses();
    }

    public static void addNewCourse(Scanner scanner) {
        System.out.println("=======================      THÊM KHOÁ HỌC MỚI      =======================");
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
        System.out.println("=======================      CHỈNH SỬA THÔNG TIN KHOÁ HỌC      =======================");
        Course course = null;
        while (true) {
            System.out.print("Nhập ID khoá học cần chỉnh sửa: ");
            int id;
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ID phải là một số nguyên, vui lòng nhập lại!");
                continue;
            }
            course = courseService.getCourse(id);
            if (course == null) {
                System.out.println("Không tìm thấy khoá học với ID: " + id);
            }
            else {
                break;
            }
        }

        OUTER:
        while (true){
            System.out.println("Thông tin khoá học hiện tại: ");
            Helper.printCourses(List.of(course));
            System.out.println("Chọn thuộc tính cần chỉnh sửa: ");
            System.out.println("1. Tên khoá học");
            System.out.println("2. Thời lượng");
            System.out.println("3. Giảng viên phụ trách");
            System.out.println("4. Quay lại");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
                continue;
            }
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
                    break OUTER;
                default:
                    System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
                    break;
            }

        }
        try {
            courseService.updateCourse(course);
            System.out.println("Cập nhật khoá học thành công!");
        } catch (Exception e) {
            System.out.println("Cập nhật khoá học thất bại, lý do: " + e.getMessage());
        }

    }

    public static void deleteCourse(Scanner scanner) {
        Course course = null;
        while (true) {
            System.out.print("Nhập ID khoá học cần xoá: ");
            int id = -1;
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ID phải là một số nguyên, vui lòng nhập lại!");
                continue;
            }
            course = courseService.getCourse(id);
            if (course == null) {
                System.out.println("Không tìm thấy khoá học với ID: " + id);
                continue;
            }
            System.out.println("Thông tin khoá học cần xoá: ");
            Helper.printCourses(List.of(course));
            System.out.print("Bạn có chắc chắn muốn xoá khoá học này? (y/n): ");
            String confirm = scanner.nextLine().trim();
            while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n")) {
                System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
                System.out.print("Bạn có chắc chắn muốn xoá khoá học này? (y/n): ");
                confirm = scanner.nextLine().trim();
            }
            if (confirm.equalsIgnoreCase("y")) {
                try {
                    courseService.deleteCourse(id);
                    System.out.println("Xoá khoá học thành công!");
                    return;
                } catch (Exception e) {
                    System.out.println("Xoá khoá học thất bại, lý do: " + e.getMessage());
                }
            } else {
                System.out.println("Đã huỷ xoá khoá học.");
                break;
            }
        }
    }

    public static void findCourseByName(Scanner scanner) {
        System.out.print("Nhập tên khoá học cần tìm kiếm: ");
        String name = scanner.nextLine().trim();
        courseService.getCourseByName(name);
    }

    public static void showCourseInSorted(Scanner scanner) {
        System.out.println("Chọn thuộc tính để sắp xếp: ");
        System.out.println("1. Tên khoá học");
        System.out.println("2. ID khoá học");
        System.out.print("Lựa chọn của bạn: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
            return;
        }
        System.out.println("Chọn thứ tự sắp xếp: ");
        System.out.println("1. Tăng dần");
        System.out.println("2. Giảm dần");
        System.out.print("Lựa chọn của bạn: ");
        int orderChoice;
        try {
            orderChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
            return;
        }

        List<Course> sortedCourses;
        String orderBy;
        switch (choice) {
            case 1:

                if (orderChoice == 1)
                    orderBy = "name asc";
                else
                    orderBy = "name desc";

                break;
            case 2:
                if (orderChoice == 1)
                    orderBy = "id asc";
                else
                    orderBy = "id desc";
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ, quay lại menu chính.");
                return;
        }
        courseService.listCoursesOrderBy(orderBy);

    }
}