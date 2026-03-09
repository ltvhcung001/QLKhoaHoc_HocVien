package ra.qlkhoahochocvien.dao;

import ra.qlkhoahochocvien.model.Course;

import java.util.List;

public interface ICourseDAO {
    public List<Course> listCourses();
    public Course getCourse(int id);
    public boolean addCourse(Course c);
    public boolean updateCourse(Course c);
    public boolean deleteCourse(int id);
    public List<Course> getCourseByName(String name);
    public List<Course> listCoursesOrderBy(String orderBy);
}
