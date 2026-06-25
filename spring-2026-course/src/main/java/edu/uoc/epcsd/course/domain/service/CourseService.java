package edu.uoc.epcsd.course.domain.service;

import edu.uoc.epcsd.course.application.rest.response.GetUserResponse;
import edu.uoc.epcsd.course.domain.Course;
import edu.uoc.epcsd.course.domain.Enrollment;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    
	Optional<Course> getCourseById(Long courseId);

	List<Course> findCourses();

	Long createCourse(Course course);

	Long modifyCourseDetails(Long courseId, Course course);

	List<Enrollment> getEnrollmentsByCourse(Long courseId);

	List<String> getEnrolledStudents(Long courseId);

	void openEnrollment(Long courseId);

	void closeEnrollment(Long courseId);

	Long enrollInCourse(Long courseId, String student);

	void closeGradeReports (Long courseId);

	void closeCourse(long courseId);
}

