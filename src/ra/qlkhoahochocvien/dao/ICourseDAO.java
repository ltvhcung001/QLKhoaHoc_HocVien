package ra.qlkhoahochocvien.dao;

import ra.qlkhoahochocvien.model.Course;

import java.util.List;

public interface ICourseDAO {
    public List<Course> listCoursesWithPagination(int page, int pageSize);
    public Course getCourse(int id);
    public boolean addCourse(Course c);
    public boolean updateCourse(Course c);
    public boolean deleteCourse(int id);
    public List<Course> listCoursesOrderByWithPagination(String orderBy, int page, int pageSize);
    public int countCourses();
    List<Course> listFullCoursesOrderBy(String orderBy);
    int countCoursesByName(String name);
    List<Course> getCourseByNameWithPagination(String name, int currentPage, int pageSize);
    int countStudents();
}
