package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.CourseDao;
import mthree.com.fullstackschool.dao.CourseDaoImpl;
import mthree.com.fullstackschool.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseServiceInterface {

    //YOUR CODE STARTS HERE
    private final CourseDao courseDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    //YOUR CODE ENDS HERE

    public List<Course> getAllCourses() {
        //YOUR CODE STARTS HERE
        return courseDao.getAllCourses();
        //YOUR CODE ENDS HERE
    }

    public Course getCourseById(int id) {
        //YOUR CODE STARTS HERE
        try {
            return courseDao.findCourseById(id);
        } catch(EmptyResultDataAccessException e) {
            Course nonExsistantCourse = new Course();
            nonExsistantCourse.setCourseName("Course Not Found");
            nonExsistantCourse.setCourseDesc("Course Not Found");
            return nonExsistantCourse;
        }

        //YOUR CODE ENDS HERE
    }

    public Course addNewCourse(Course course) {
        //YOUR CODE STARTS HERE
        boolean valid = true;
        if(course.getCourseName().isEmpty() || course.getCourseName().trim().isEmpty()) {
            course.setCourseName("Name blank, course NOT added");
            valid = false;
        } if(course.getCourseDesc().isEmpty() || course.getCourseDesc().trim().isEmpty()) {
            course.setCourseDesc("Description blank, course NOT added");
            valid = false;
        }
        if(!valid) return course;
        return courseDao.createNewCourse(course);
        //YOUR CODE ENDS HERE
    }

    public Course updateCourseData(int id, Course course) {
        //YOUR CODE STARTS HERE
        try {
            course.setCourseId(id);
            courseDao.findCourseById(id);
            courseDao.updateCourse(course);
            return course;
        } catch(EmptyResultDataAccessException e) {
            Course deadCourse = new Course();
            deadCourse.setCourseName("Id's do not match, course not updated");
            deadCourse.setCourseDesc("Id's do not match, course not updated");
            return deadCourse;
        }


        //YOUR CODE ENDS HERE
    }

    public void deleteCourseById(int id) {
        //YOUR CODE STARTS HERE
        courseDao.deleteCourse(id);
        //YOUR CODE ENDS HERE
    }
}
