package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.TeacherDao;
import mthree.com.fullstackschool.dao.TeacherDaoImpl;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherServiceInterface {

    //YOUR CODE STARTS HERE
    private final TeacherDao teacherDao;

    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    //YOUR CODE ENDS HERE

    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE
        return teacherDao.getAllTeachers();
        //YOUR CODE ENDS HERE
    }

    public Teacher getTeacherById(int id) {
        //YOUR CODE STARTS HERE
        return teacherDao.findTeacherById(id);
        //YOUR CODE ENDS HERE
    }

    public Teacher addNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE
        boolean valid = true;
        if(teacher.getTeacherFName().isEmpty() || teacher.getTeacherFName().trim().isEmpty()) {
            teacher.setTeacherFName("First Name blank, teacher NOT added");
            valid = false;
        } if(teacher.getTeacherLName().isEmpty() || teacher.getTeacherLName().trim().isEmpty()) {
            teacher.setTeacherLName("Last Name blank, teacher NOT added");
            valid = false;
        }
        if(!valid) return teacher;
        return teacherDao.createNewTeacher(teacher);
        //YOUR CODE ENDS HERE
    }

    public Teacher updateTeacherData(int id, Teacher teacher) {
        //YOUR CODE STARTS HERE
        if(id != teacher.getTeacherId()) {
            teacher.setTeacherFName("IDs do not match, teacher not updated");
            teacher.setTeacherLName("IDs do not match, teacher not updated");
            return teacher;
        }

        teacher.setTeacherId(id);
        teacherDao.updateTeacher(teacher);
        return teacher;
        //YOUR CODE ENDS HERE
    }

    public void deleteTeacherById(int id) {
        //YOUR CODE STARTS HERE
        teacherDao.deleteTeacher(id);
        //YOUR CODE ENDS HERE
    }
}
