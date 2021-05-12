package upeldev.com.github.upel3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.repositories.GradeRepository;
import upeldev.com.github.upel3.repositories.GroupGradeRepository;
import upeldev.com.github.upel3.repositories.StudentGradeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentGradeRepository studentGradeRepository;
    private final GroupGradeRepository groupGradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository,
                        StudentGradeRepository studentGradeRepository,
                        GroupGradeRepository groupGradeRepository) {
        this.gradeRepository = gradeRepository;
        this.studentGradeRepository = studentGradeRepository;
        this.groupGradeRepository = groupGradeRepository;
    }

    public boolean canUserAddGrade(Grade gradeDTO, User user){

        if(user.getRoles().contains(Role.ADMIN)) return true;
        if(!user.getRoles().contains(Role.LECTURER)) return false;

        return user.getCoursesLectured().contains(gradeDTO.getActivity().getCourse());

    }

    public List<Grade> findGradeByUser(User user){
        return studentGradeRepository.findGradeByUser(user);
    }

    public List<Grade> findGradeByActivity(Activity activity){
        return gradeRepository.findGradeByActivity(activity);
    }

    public List<Grade> findGradeByCourseAndUserAndActivity(Course course, User user, Activity activity){
        return studentGradeRepository.findGradeByCourseAndUserAndActivity(course.getId(), user.getId(), activity.getId());
    }

    public Grade findGradeById(Long id){
        return gradeRepository.findGradeById(id);
    }

    public List<Grade> findAll(){
        return StreamSupport.stream(gradeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Grade> findGradeByCourse(Course course){
        return gradeRepository.findGradeByCourse(course.getId());
    }

    public List<Grade> findGradeByCourseAndUser(Course course, User user){
        return studentGradeRepository.findGradeByCourseAndUser(course.getId(), user.getId());
    }

    public List<Grade> findGradeByCourseAndGroupAndActivity(Course course, StudentGroup group, Activity activity){
        return groupGradeRepository.findGradeByCourseAndGroupAndActivity(course.getId(), group.getId(), activity.getId());
    }

    public List<Grade> findGradeByCourseAndGroup(Course course, StudentGroup group){
        return groupGradeRepository.findGradeByCourseAndGroup(course.getId(), group.getId());
    }

    public List<Grade> findGradeByGroup(StudentGroup group){
        return groupGradeRepository.findGradeByGroup(group);
    }

    public long deleteById(Long id){return gradeRepository.deleteById(id);}

    public Grade save(Grade gradeDTO){
        return gradeRepository.save(gradeDTO);
    }

    public void changeDescription(Grade grade, String newDescription){
        if (newDescription == null || newDescription.isEmpty()){
            throw new IllegalArgumentException("New grade description cannot be empty");
        }

        grade.setDescription(newDescription);
        gradeRepository.save(grade);
    }

    public void removeSubGrade(Grade grade, SubGrade subGrade){
        grade.getSubGrades().remove(subGrade);
        gradeRepository.save(grade);
    }
}
