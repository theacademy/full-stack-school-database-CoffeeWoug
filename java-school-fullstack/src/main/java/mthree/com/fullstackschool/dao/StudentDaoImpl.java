package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.model.Course;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentDaoImpl implements StudentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Student createNewStudent(Student student) {
        //YOUR CODE STARTS HERE
        final String INSERT_STUDENT = "INSERT INTO student(fName, lName) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_STUDENT,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, student.getStudentFirstName());
            statement.setString(2, student.getStudentLastName());
            return statement;

        }, keyHolder);

        if(keyHolder.getKey() != null) {
            student.setStudentId(keyHolder.getKey().intValue());
        }

        return student;
        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE
        List<Student> getCourses = jdbcTemplate.query("SELECT * FROM student", new StudentMapper());
        return getCourses;
        //YOUR CODE ENDS HERE
    }

    @Override
    public Student findStudentById(int id) throws DataAccessException {
        //YOUR CODE STARTS HERE
        final String SELECT_STUDENT_BY_ID = "SELECT * FROM student WHERE sid = ?";
        return jdbcTemplate.queryForObject(SELECT_STUDENT_BY_ID, new StudentMapper(), id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateStudent(Student student) {
        //YOUR CODE STARTS HERE
        final String UPDATE_STUDENT = "UPDATE student SET fName = ?, lName = ? WHERE sid = ?";
        jdbcTemplate.update(UPDATE_STUDENT, student.getStudentFirstName(), student.getStudentLastName(), student.getStudentId());
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudent(int id) {
        //YOUR CODE STARTS HERE
        final String DELETE_COURSE_STUDENT = "DELETE FROM course_student WHERE student_id = ?";
        jdbcTemplate.update(DELETE_COURSE_STUDENT, id);

        final String DELETE_STUDENT = "DELETE FROM student WHERE sid = ?";
        jdbcTemplate.update(DELETE_STUDENT, id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        final String INSERT_STUDENT_TO_COURSE = "INSERT INTO course_student (student_id, course_id) VALUES(?,?)";
        jdbcTemplate.update(INSERT_STUDENT_TO_COURSE, studentId, courseId);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        String testSQl1 = "SELECT COUNT(student_id) FROM course_student WHERE student_id = ?";
        int count1 = jdbcTemplate.queryForObject(testSQl1, Integer.class, studentId);
        System.out.println(count1 + " Current rows left");

        final String DELETE_STUDENT_FROM_COURSE = "DELETE FROM course_student WHERE student_id = ? AND course_id = ?;";
        jdbcTemplate.update(DELETE_STUDENT_FROM_COURSE, studentId, courseId);

        String testSQl2 = "SELECT COUNT(student_id) FROM course_student WHERE student_id = ?";
        int count2 = jdbcTemplate.queryForObject(testSQl2, Integer.class, studentId);
        System.out.println(count2 + " rows left");
        //YOUR CODE ENDS HERE
    }
}
