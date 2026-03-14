package ra.qlkhoahochocvien.bussiness;

import ra.qlkhoahochocvien.model.Course;

import java.util.List;
import java.util.Scanner;

public interface ICourseService {
    public void listCourses(Scanner scanner);
    public void listCoursesByUser(Scanner scanner);
    public Course getCourseByID(int id);
    public void addCouse(Course course);
    public void updateCourse(Course course);
    public void deleteCourse(int id);
    public void getCourseByName(String name);
    public List<Course> listFullCoursesOrderBy(String orderBy);
    public void listCoursesOrderBy(String orderBy);
    List<Course> getListCourses();
}
