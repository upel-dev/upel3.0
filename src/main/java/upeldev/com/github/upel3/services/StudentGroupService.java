package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.StudentGroup;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.StudentGroupRepository;
import upeldev.com.github.upel3.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentGroupService {
    private final StudentGroupRepository studentGroupRepository;
    private final UserRepository userRepository;

    @Autowired
    public StudentGroupService(StudentGroupRepository studentGroupRepository, UserRepository userRepository) {
        this.studentGroupRepository = studentGroupRepository;
        this.userRepository = userRepository;
    }

    public StudentGroup save(StudentGroup group){
        return studentGroupRepository.save(group);
    }

    public List<StudentGroup> findAll(){
        return StreamSupport.stream(studentGroupRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    StudentGroup findById(Long id){
        return studentGroupRepository.findById(id);
    }

    public List<StudentGroup> findByCourse(Course course){
        return studentGroupRepository.findByCourse(course);
    }

    List<StudentGroup> findByName(String name){
        return studentGroupRepository.findByName(name);
    }

    List<StudentGroup> findByStudent(User student){
        return studentGroupRepository.findByStudentsContains(student);
    }

    public void changeName(StudentGroup group, String newName){
        if (newName == null || newName.isEmpty()){
            throw new IllegalArgumentException("New group name cannot be empty");
        }

        group.setName(newName);
        studentGroupRepository.save(group);
    }

    public void addStudentToGroup(StudentGroup group, User student){
        group.getStudents().add(student);
        studentGroupRepository.save(group);

        student.getGroups().add(group);
        userRepository.save(student);
    }

    public void removeStudentFromGroup(StudentGroup group, User student){
        group.getStudents().remove(student);
        studentGroupRepository.save(group);

        student.getGroups().remove(group);
        userRepository.save(student);
    }

}
