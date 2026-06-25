package edu.uoc.epcsd.course.application.rest;

import edu.uoc.epcsd.course.application.rest.request.CourseRequest;
import edu.uoc.epcsd.course.application.rest.response.GetUserResponse;
import edu.uoc.epcsd.course.domain.Course;
import edu.uoc.epcsd.course.domain.Enrollment;
import edu.uoc.epcsd.course.domain.service.CourseService;
import edu.uoc.epcsd.course.domain.service.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ParameterNameProvider;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/courses")
public class CourseRESTController {

    private final CourseService courseService;
    private final CourseServiceImpl courseServiceImpl;

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Course> getCourseById(@PathVariable @NotNull Long courseId) {
        log.trace("getCourseById");


        //TODO----DONE
        return courseService.getCourseById(courseId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // TODO findCourses()
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Course>> findCourses() {
        log.trace("findCourses");

        return ResponseEntity.ok(courseService.findCourses());
    }

    // TODO createCourse() -----DONE
    @PostMapping
    public ResponseEntity<Long> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        log.trace("createCourse");

        log.trace("Creating course " + courseRequest);

        try{

            Long courseId = courseService.createCourse(Course.builder()
                    .instructor(courseRequest.getInstructor())
                    .title(courseRequest.getTitle())
                    .description(courseRequest.getDescription())
                    .enrollmentStartDate(courseRequest.getEnrollmentStartDate())
                    .enrollmentEndDate(courseRequest.getEnrollmentEndDate())
                    .mode(courseRequest.getMode())
                    .price(courseRequest.getPrice())
                    .objectives(courseRequest.getObjectives())
                    .methology(courseRequest.getMethology())
                    .duration(courseRequest.getDuration())
                    .language(courseRequest.getLanguage())
                    .location(courseRequest.getLocation())
                    .build());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{courseId}")
                    .buildAndExpand(courseId)
                    .toUri();

            return ResponseEntity.created(uri).body(courseId);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }



    // TODO modifyCourseDetails() ---- DONE

    @PutMapping("/{courseId}")
    public ResponseEntity<Void> modifyCourseDetails(@PathVariable Long courseId, @RequestBody CourseRequest courseRequest) {

        log.trace("modifyCourseDetails");

        try {

            Course course = Course.builder()
                    .instructor(courseRequest.getInstructor())
                    .title(courseRequest.getTitle())
                    .description(courseRequest.getDescription())
                    .enrollmentStartDate(courseRequest.getEnrollmentStartDate())
                    .enrollmentEndDate(courseRequest.getEnrollmentEndDate())
                    .mode(courseRequest.getMode())
                    .price(courseRequest.getPrice())
                    .objectives(courseRequest.getObjectives())
                    .methology(courseRequest.getMethology())
                    .duration(courseRequest.getDuration())
                    .language(courseRequest.getLanguage())
                    .location(courseRequest.getLocation())
                    .build();

            courseService.modifyCourseDetails(courseId, course);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    // TODO getEnrollmentsByCourse() ---- DONE


    @GetMapping("/{courseId}/enrollments")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseId(@PathVariable Long courseId ) {

        log.trace("getEnrollmentsByCourseId");
        try{
            return ResponseEntity.ok(courseService.getEnrollmentsByCourse(courseId));


        }catch (IllegalArgumentException e){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }


    // TODO getEnrolledStudents()   ---- DONE
    @GetMapping("/{courseId}/students")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<String>> getEnrolledStudents(@PathVariable Long courseId) {

        log.trace("getEnrolledStudents");
        try{
            return ResponseEntity.ok(courseService.getEnrolledStudents(courseId));
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    // TODO openEnrollment()   --- DONE
    @PutMapping("{courseId}/open-enrollment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> openEnrollments(@PathVariable Long courseId) {
        log.trace("openEnrollments");

        try{
            courseService.openEnrollment(courseId);
            return ResponseEntity.ok().build();

        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // TODO closeEnrollment() ---- DONE
    @PutMapping("{courseId}/close-enrollment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> closeEnrollment(@PathVariable Long courseId) {
        log.trace("closeEnrollment");

        try{
            courseService.closeEnrollment(courseId);
            return ResponseEntity.ok().build();

        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // TODO enrollInCourse()        ---- DONE
    @PostMapping("/{courseId}/enroll")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> enrollInCourse( @PathVariable Long courseId, @RequestParam String student) {

        log.trace("enrollInCourse");
        try{
            Long enrollmentId = courseService.enrollInCourse(courseId, student);
            return ResponseEntity.ok(enrollmentId);


        }catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // TODO closeGradeReports() ---- DONE

    @PutMapping("/{courseId}/close-grade-reports")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <Void> closeGradeReports(@PathVariable Long courseId) {
        log.trace("closeGradeReports");

        try {
            courseService.closeGradeReports(courseId);
            return ResponseEntity.ok().build();


        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // TODO closeCourse()  ---- DONE

    @PutMapping("/{courseId}/close-Course")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> closeCourse(@PathVariable Long courseId) {
        log.trace("closeCourse");
        try{
            courseService.closeCourse(courseId);
            return ResponseEntity.ok().build();

        }catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
 
}
