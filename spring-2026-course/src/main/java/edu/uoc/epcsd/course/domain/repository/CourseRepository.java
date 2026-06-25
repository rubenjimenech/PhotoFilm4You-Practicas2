package edu.uoc.epcsd.course.domain.repository;

import edu.uoc.epcsd.course.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

	Optional<Course> getCourseById(Long courseId);

	List<Course> findCourses();

	Long createCourse(Course course);

	Long modifyCourseDetails(Course course);

	void openEnrollmentCourse(Long courseId);

	void closeEnrollmentCourse(Long courseId);

	void closeGradeReports(Long courseId);

	void closeCourse(Long courseId);
}
