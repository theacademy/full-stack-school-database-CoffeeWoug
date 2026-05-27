package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.TeacherMapper;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher createNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE
        final String INSERT_TEACHER = "INSERT INTO teacher(tFName, tLName, dept) VALUES(?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_TEACHER,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, teacher.getTeacherFName());
            statement.setString(2, teacher.getTeacherLName());
            statement.setString(3, teacher.getDept());
            return statement;

        }, keyHolder);

        if(keyHolder.getKey() != null) {
            teacher.setTeacherId(keyHolder.getKey().intValue());
        }

        return teacher;
        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE
        List<Teacher> teachers = jdbcTemplate.query("SELECT * FROM teacher", new TeacherMapper());
        return teachers;

        //YOUR CODE ENDS HERE
    }

    @Override
    public Teacher findTeacherById(int id) {
        //YOUR CODE STARTS HERE
        final String SELECT_TEACHER_BY_ID = "SELECT * FROM teacher WHERE tid = ?";
        return jdbcTemplate.queryForObject(SELECT_TEACHER_BY_ID, new TeacherMapper(), id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateTeacher(Teacher t) {
        //YOUR CODE STARTS HERE
        final String UPDATE_TEACHER = "UPDATE teacher SET tFName = ? , tLName = ?, dept = ? WHERE tid = ?";
        jdbcTemplate.update(UPDATE_TEACHER, t.getTeacherFName(), t.getTeacherLName(), t.getDept(), t.getTeacherId());
        //YOUR CODE ENDS HERE
    }

    @Override
    @Transactional
    public void deleteTeacher(int id) {
        //YOUR CODE STARTS HERE
        // Had to look online for this statement due to H2 database error, lms code does not always work with H2 database for some reason, investigate when time
        final String DELETE_COURSE_STUDENT_BY_TEACHER = "DELETE FROM course_student WHERE course_id IN (SELECT cid FROM course WHERE teacherId = ?)";
        jdbcTemplate.update(DELETE_COURSE_STUDENT_BY_TEACHER, id);

        final String DELETE_COURSE_BY_TEACHER = "DELETE FROM course WHERE teacherId = ?";
        jdbcTemplate.update(DELETE_COURSE_BY_TEACHER, id);

        final String DELETE_TEACHER = "DELETE FROM teacher WHERE tid = ?";
        jdbcTemplate.update(DELETE_TEACHER, id);
        //YOUR CODE ENDS HERE
    }
}
