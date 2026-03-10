package ra.qlkhoahochocvien.bussiness.impl;

import ra.qlkhoahochocvien.bussiness.ICourseService;
import ra.qlkhoahochocvien.dao.ICourseDAO;
import ra.qlkhoahochocvien.dao.impl.CourseDAOImpl;
import ra.qlkhoahochocvien.model.Course;
import ra.qlkhoahochocvien.utils.Helper;

import java.util.List;

public class CourseServiceImpl implements ICourseService {
    public final ICourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void listCourses() {
        List<Course> courses = courseDAO.listCourses();
        if (courses.isEmpty()) {
            System.out.println("Chưa có khoá học nào.");
            return;
        }
        System.out.println("Danh sách khoá học:");
        Helper.printCourses(courses);
    }


    @Override
    public Course getCourse(int id) {
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
    public List<Course> getCourseByName(String name) {
        return courseDAO.getCourseByName(name);
    }

    @Override
    public List<Course> listCoursesOrderBy(String orderBy) {
        return courseDAO.listCoursesOrderBy(orderBy);
    }
}
