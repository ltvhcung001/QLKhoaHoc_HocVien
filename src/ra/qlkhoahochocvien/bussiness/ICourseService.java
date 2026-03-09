package ra.qlkhoahochocvien.bussiness;

import ra.qlkhoahochocvien.model.Course;

import java.util.List;

public interface ICourseService {
    public List<Course> listCourses();
    public Course getCourse(int id);
    public void addCouse(Course course);
    public void updateCourse(Course course);
    public void deleteCourse(int id);
    public List<Course> getCourseByName(String name);
    public List<Course> listCoursesOrderBy(String orderBy);
}
