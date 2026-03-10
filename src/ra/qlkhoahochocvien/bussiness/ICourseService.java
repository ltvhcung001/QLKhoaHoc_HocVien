package ra.qlkhoahochocvien.bussiness;

import ra.qlkhoahochocvien.model.Course;

import java.util.List;

public interface ICourseService {
    public void listCourses();
    public Course getCourse(int id);
    public void addCouse(Course course);
    public void updateCourse(Course course);
    public void deleteCourse(int id);
    public void getCourseByName(String name);
    public List<Course> listFullCoursesOrderBy(String orderBy);
    public void listCoursesOrderBy(String orderBy);
    List<Course> getListCourses();
}
