package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.CourseMapper;
import mthree.com.fullstackschool.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Course createNewCourse(Course course) {
        //YOUR CODE STARTS HERE
        final String INSERT_COURSE = "INSERT INTO course(courseCode, courseDesc, teacherId) VALUES(?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder(); // Helps to get id of last inserted row

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_COURSE,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getCourseDesc());
            statement.setInt(3, course.getTeacherId());
            return statement;

        }, keyHolder);

        if(keyHolder.getKey() != null) {
            course.setCourseId(keyHolder.getKey().intValue());
        }

        return course;
        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Course> getAllCourses() {
        //YOUR CODE STARTS HERE
        List<Course> courses = jdbcTemplate.query("SELECT * FROM course", new CourseMapper());
        return courses;
        //YOUR CODE ENDS HERE
    }

    @Override
    public Course findCourseById(int id) throws EmptyResultDataAccessException {
        //YOUR CODE STARTS HERE
        final String SELECT_COURSE_BY_ID = "SELECT * FROM course WHERE cid = ?";
        return jdbcTemplate.queryForObject(SELECT_COURSE_BY_ID, new CourseMapper(), id);
        //YOUR CODE ENDS HERE
    }

    @Override
    @Transactional
    public void updateCourse(Course course) {
        //YOUR CODE STARTS HERE
        final String UPDATE_COURSE = "UPDATE course SET courseCode = ?, courseDesc = ?, teacherId = ? WHERE cid = ?";
        jdbcTemplate.update(UPDATE_COURSE, course.getCourseName(), course.getCourseDesc(), course.getTeacherId(), course.getCourseId());
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteCourse(int id) {
        //YOUR CODE STARTS HERE
        deleteAllStudentsFromCourse(id);

        final String DELETE_COURSE = "DELETE FROM course WHERE cid = ?";
        jdbcTemplate.update(DELETE_COURSE, id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteAllStudentsFromCourse(int courseId) {
        //YOUR CODE STARTS HERE

        String testSQl1 = "SELECT COUNT(course_id) FROM course_student WHERE course_id = ?";
        int count1 = jdbcTemplate.queryForObject(testSQl1, Integer.class, courseId);
        System.out.println(count1 + " Current rows left");

        final String DELETE_ALL_STUDENTS_FROM_COURSE = "DELETE FROM course_student WHERE course_id = ?";
        jdbcTemplate.update(DELETE_ALL_STUDENTS_FROM_COURSE, courseId);

        String testSQl2 = "SELECT COUNT(course_id) FROM course_student WHERE course_id = ?";
        int count2 = jdbcTemplate.queryForObject(testSQl2, Integer.class, courseId);
        System.out.println(count2 + " rows left");
        //YOUR CODE ENDS HERE
    }

}

/*
TEST CODE FOR DELETE
String testSQl2 = "SELECT COUNT(course_id) FROM course_student WHERE course_id = ?";
int count2 = jdbcTemplate.queryForObject(testSQl2, Integer.class, id);
        System.out.println(count2 + " rows left");

String testSQl1 = "SELECT COUNT(course_id) FROM course_student WHERE course_id = ?";
int count1 = jdbcTemplate.queryForObject(testSQl1, Integer.class, id);
        System.out.println(count1 + " Current rows left");
*/