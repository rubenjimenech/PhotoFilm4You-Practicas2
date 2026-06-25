package edu.uoc.epcsd.course.domain.service;

import edu.uoc.epcsd.course.application.rest.request.MicrocredentialRequest;
import edu.uoc.epcsd.course.application.rest.response.GetUserResponse;
import edu.uoc.epcsd.course.domain.*;
import edu.uoc.epcsd.course.domain.repository.CourseRepository;
import edu.uoc.epcsd.course.domain.repository.EnrollmentRepository;
import edu.uoc.epcsd.course.domain.repository.UserRepository;
import edu.uoc.epcsd.course.infrastructure.repository.jpa.EnrollmentEntity;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;
    private final RestTemplate restTemplate;

    @Value("${userService.getUserByEmail.url}")
    private String usersServiceUrl;

    @Value("${credentialService.create.url}")
    private String microcredentialServiceUrl;
    
    
    @Override
    public Optional<Course> getCourseById(Long courseId) {
 
    	return Optional.of(courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("The "+courseId+"th course does not exist!")));
    
    }


    // TODO findCourses() --- DONE
    @Override
    public List<Course> findCourses() {
        return courseRepository.findCourses();
    }


    // TODO createCourse() --- DONE
    @Override
    public Long createCourse(Course course) {
        if(course == null) {
            throw new IllegalArgumentException("The course must not be null");
        }
        return courseRepository.createCourse(course);
    }


    // TODO modifyCourseDetails() ---- DONE
    @Override
    public Long modifyCourseDetails(Long courseId, Course updatedCourse) {

        if (updatedCourse == null) {
            throw new IllegalArgumentException("The course must not be null");
        }
        Course existing = courseRepository.getCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Course modified = Course.builder()
                .id(existing.getId())
                .instructor(updatedCourse.getInstructor())
                .title(updatedCourse.getTitle())
                .description(updatedCourse.getDescription())
                .enrollmentStartDate(updatedCourse.getEnrollmentStartDate())
                .enrollmentEndDate(updatedCourse.getEnrollmentEndDate())
                .mode(updatedCourse.getMode())
                .price(updatedCourse.getPrice())
                .objectives(updatedCourse.getObjectives())
                .methology(updatedCourse.getMethology())
                .duration(updatedCourse.getDuration())
                .language(updatedCourse.getLanguage())
                .location(updatedCourse.getLocation())
                .status(existing.getStatus())
                .build();

        return courseRepository.modifyCourseDetails(modified);
    }


    // TODO getEnrollmentsByCourse() ---- DONE

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return enrollmentService.getEnrollmentsByCourse(courseId);
    }


    // TODO getEnrolledStudents() ---- DONE

    @Override
    public List<String> getEnrolledStudents(Long courseId) {


        courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);

        return enrollments.stream().map(Enrollment::getStudent).collect(Collectors.toList());
    }



    // TODO openEnrollment()   ---- DONE

    @Override
    public void openEnrollment(Long courseId) {
        Course course = courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if(course == null) {
            throw new IllegalArgumentException("The course must not be null");
        }

        courseRepository.openEnrollmentCourse(courseId);
    }

    // TODO closeEnrollment()     ---- DONE
    @Override
    public void closeEnrollment(Long courseId) {
        Course course = courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if(course == null) {
            throw new IllegalArgumentException("The course must not be null");
        }

        courseRepository.closeEnrollmentCourse(courseId);
    }


    // TODO enrollInCourse()   ---- DONE
    @Override
    public Long enrollInCourse(Long courseId, String student){

        return enrollmentService.enrollInCourse(courseId, student);
    }


    // TODO closeGradeReports()      ---- DONE
    @Override
    public void closeGradeReports(Long courseId) {

        Course course = courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));


        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new IllegalArgumentException("The course is not in active status");
        }

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getQualification() == 0L) {
                enrollment.setQualification(10L);
            }
            enrollment.setStatus(EnrollmentStatus.GRADED);
            enrollmentRepository.updateEnrollment(enrollment);
        }

        courseRepository.closeGradeReports(courseId);

    }

    // TODO closeCourse()   ---- DONE
    @Override
    public void closeCourse(long courseId) {
        Course course = courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.getStatus() != CourseStatus.PENDING_CLOSE) {
            throw new IllegalArgumentException("The course is not in Pending_Close status");
        }
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStatus() != EnrollmentStatus.GRADED) {
                throw new IllegalArgumentException("enrollments must be in grade status");
            }

            MicrocredentialRequest microcredentialRequest = new MicrocredentialRequest(new Date(),enrollment.getEnrollmentDate(),"MICROCREDENCIAL CREADA",enrollment.getId());

            restTemplate.postForEntity(microcredentialServiceUrl, microcredentialRequest, Void.class, courseId);

            enrollment.setStatus(EnrollmentStatus.CLOSED);
            enrollmentRepository.updateEnrollment(enrollment);
        }
        courseRepository.closeCourse(courseId);

    }

    
}
