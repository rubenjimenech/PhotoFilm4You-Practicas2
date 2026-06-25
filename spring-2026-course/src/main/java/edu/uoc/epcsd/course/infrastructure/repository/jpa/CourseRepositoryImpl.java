package edu.uoc.epcsd.course.infrastructure.repository.jpa;

import edu.uoc.epcsd.course.domain.Course;
import edu.uoc.epcsd.course.domain.CourseStatus;
import edu.uoc.epcsd.course.domain.exception.UserNotFoundException;
import edu.uoc.epcsd.course.domain.repository.CourseRepository;
import edu.uoc.epcsd.course.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseRepositoryImpl implements CourseRepository {

    private final SpringDataCourseRepository jpaCourseRepository;
 
    
    private final UserRepository userRepository;
    
    @Override
    public Optional<Course> getCourseById(Long courseId) {
        return jpaCourseRepository.getCourseById(courseId).map(CourseEntity::toDomain);
    }
    //TODO findCourses() --- DONE
    @Override
    public List<Course> findCourses() {

        return jpaCourseRepository.findAll().stream().map(CourseEntity::toDomain).collect(Collectors.toList());


    }


    //TODO createCourse() ---- DONE


    @Override
    public Long createCourse(Course course) {

        CourseEntity courseEntity = CourseEntity.fromDomain(course);
        return jpaCourseRepository.save(courseEntity).getId();
    }


    //TODO modifyCourseDetails() ---- DONE

    @Override
    public Long modifyCourseDetails(Course course) {

        CourseEntity courseEntity = jpaCourseRepository.findById(course.getId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        courseEntity.setTitle(course.getTitle());
        courseEntity.setInstructor(course.getInstructor());
        courseEntity.setDescription(course.getDescription());
        courseEntity.setEnrollmentStartDate(course.getEnrollmentStartDate());
        courseEntity.setEnrollmentEndDate(course.getEnrollmentEndDate());
        courseEntity.setMode(course.getMode());
        courseEntity.setPrice(course.getPrice());
        courseEntity.setObjectives(course.getObjectives());
        courseEntity.setMethology(course.getMethology());
        courseEntity.setDuration(course.getDuration());
        courseEntity.setLanguage(course.getLanguage());
        courseEntity.setLocation(course.getLocation());
        courseEntity.setStatus(course.getStatus());

        return jpaCourseRepository.save(courseEntity).getId();
    }


    //TODO openEnrollmentCourse()       ---- DONE
    @Override
    public void openEnrollmentCourse(Long courseId) {

        if (!jpaCourseRepository.existsById(courseId)) {
            throw new UserNotFoundException("Course not found");
        }
        CourseEntity courseEntity = jpaCourseRepository.getById(courseId);
        courseEntity.setStatus(CourseStatus.ENROLLMENT_OPEN);
        jpaCourseRepository.save(courseEntity);

    }

    //TODO closeEnrollmentCourse()   ---- DONE
    @Override
    public void closeEnrollmentCourse(Long courseId) {

        if (!jpaCourseRepository.existsById(courseId)) {
            throw new UserNotFoundException("Course not found");
        }
        CourseEntity courseEntity = jpaCourseRepository.getById(courseId);
        courseEntity.setStatus(CourseStatus.CLOSED);
        jpaCourseRepository.save(courseEntity);

    }

    //TODO closeGradeReports()   ---- DONE
    @Override
    public void closeGradeReports(Long courseId) {
        if (!jpaCourseRepository.existsById(courseId)) {
            throw new UserNotFoundException("Course not found");
        }
        CourseEntity courseEntity = jpaCourseRepository.getById(courseId);
        courseEntity.setStatus(CourseStatus.PENDING_CLOSE);
        jpaCourseRepository.save(courseEntity);
    }

    //TODO closeCourse()  ---- DONE

    public void closeCourse(Long courseId) {
        if (!jpaCourseRepository.existsById(courseId)) {
            throw new UserNotFoundException("Course not found");
        }
        CourseEntity courseEntity = jpaCourseRepository.getById(courseId);
        courseEntity.setStatus(CourseStatus.CLOSED);
        jpaCourseRepository.save(courseEntity);
    }

	    
}
