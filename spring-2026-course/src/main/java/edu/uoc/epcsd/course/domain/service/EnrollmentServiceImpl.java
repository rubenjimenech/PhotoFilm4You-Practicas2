package edu.uoc.epcsd.course.domain.service;

import edu.uoc.epcsd.course.domain.Course;
import edu.uoc.epcsd.course.domain.CourseStatus;
import edu.uoc.epcsd.course.domain.Enrollment;
import edu.uoc.epcsd.course.domain.EnrollmentStatus;
import edu.uoc.epcsd.course.domain.repository.CourseRepository;
import edu.uoc.epcsd.course.domain.repository.EnrollmentRepository;
import edu.uoc.epcsd.course.domain.repository.UserRepository;
import edu.uoc.epcsd.course.infrastructure.repository.jpa.EnrollmentEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    
    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
    	
            return enrollmentRepository.findEnrollmentByCourse(courseId);
    }


    // TODO getEnrollmentByStudent()  ---- DONE
    @Override
    public List<Enrollment> getEnrollmentsByStudent(String email) {
        return enrollmentRepository.findEnrollmentByStudent(email);
    }

    // TODO findAllEnrollment()    ---- DONE
    @Override
    public List<Enrollment> findAllEnrollment(){

        return enrollmentRepository.findAllEnrollment();
    }

    // TODO createEnrollment()  ---- DONE
    @Override
    public Long createEnrollment(Enrollment enrollment) {

        if (enrollment== null){
            throw new IllegalArgumentException("Enrollment cannot be null");
        }
        courseRepository.getCourseById(enrollment.getCourseId()).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!userRepository.findUserByEmail(enrollment.getStudent())){
            throw new IllegalArgumentException("Student not found");
        }
        return enrollmentRepository.createEnrollment(enrollment);
    }



    // TODO modifyEnrollment() ---- DONE
    @Override
    public Long modifyEnrollment(Enrollment enrollment) {
        if (enrollment== null){
            throw new IllegalArgumentException("Enrollment cannot be null");
        }

        enrollmentRepository.getEnrollmentById(enrollment.getId()).orElseThrow(IllegalArgumentException::new);

        if (!userRepository.findUserByEmail(enrollment.getStudent())){
            throw new IllegalArgumentException("Student not found");

        }
        return enrollmentRepository.updateEnrollment(enrollment);

    }


    // TODO enrollInCourse()   ---- DONE

    @Override
    public Long enrollInCourse(Long courseId, String student) {

        Course course = courseRepository.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

       if (!userRepository.findUserByEmail(student)){
           throw new IllegalArgumentException("Student not found");
       }

       if (course.getStatus() != CourseStatus.ENROLLMENT_OPEN){
            throw new IllegalArgumentException("Course is not enrolment open");
       }

       List<Enrollment> enrollments = enrollmentRepository.findEnrollmentByStudent(student);


       for (Enrollment enrollment : enrollments){
           if (enrollment.getCourseId().equals(courseId)){
               throw new IllegalArgumentException("Enrollment is already enrolled");
           }
       }

       Enrollment enrollment = Enrollment.builder()
               .student(student)
               .courseId(courseId)
               .enrollmentDate(Date.valueOf(LocalDate.now()))
               .status(EnrollmentStatus.ACTIVE)
               .qualification(0L)
               .build();
       return enrollmentRepository.createEnrollment(enrollment);


    }
   
}
