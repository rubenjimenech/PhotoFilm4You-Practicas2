package edu.uoc.epcsd.course.application.rest;

import edu.uoc.epcsd.course.application.rest.request.CreateEnrollmentRequest;
import edu.uoc.epcsd.course.domain.Enrollment;
import edu.uoc.epcsd.course.domain.EnrollmentStatus;
import edu.uoc.epcsd.course.domain.service.EnrollmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/enrollments")
public class EnrollmentRESTController {
    private final EnrollmentService enrollmentService;

    @GetMapping("/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable @NotNull Long courseId) {
        log.trace("getCourseEnrollments");
        log.trace("courseId: " + courseId);

        try{
            return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));

        }catch (RestClientException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Enrollment>> getCourseEnrollmentsByStudent(@PathVariable @NotNull String email) {
        log.trace("getCourseEnrollmentsByStudent");
        log.trace("email: " + email);

        try{

            return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(email));

        }catch (RestClientException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @GetMapping("/All")
    @ResponseStatus(HttpStatus.OK)
    public  List<Enrollment> findAllEnrollment(){

        log.trace("findAllEnrollment");
        return enrollmentService.findAllEnrollment();
    }

    @PostMapping()
    public ResponseEntity<Long> createEnrollment(@Valid @RequestBody @NotNull CreateEnrollmentRequest createEnrollmentRequest) {
        log.trace("createEnrollment");
        log.trace("Creating enrollment " + createEnrollmentRequest);
        try{
            Long enrollmentId = enrollmentService.createEnrollment(Enrollment.builder()
                    .student(createEnrollmentRequest.getStudent())
                    .status(EnrollmentStatus.ACTIVE)
                    .enrollmentDate(createEnrollmentRequest.getEnrollmentDate())
                    .qualification(createEnrollmentRequest.getQualification())
                    .courseId(createEnrollmentRequest.getCourseId())
                    .build());

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(enrollmentId)
                    .toUri();

            return ResponseEntity.created(uri).body(enrollmentId);
        }catch(IllegalArgumentException e){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }
    @PutMapping("/{enrollmentId}")
    public ResponseEntity<Long> modifyEnrollment(@PathVariable Long enrollmentId,@Valid @RequestBody Enrollment enrollment) {

        try{
            enrollment.setId(enrollmentId);
            Long updatedEnrollmentId = enrollmentService.modifyEnrollment(enrollment);
            return ResponseEntity.ok(updatedEnrollmentId);


        }catch(RestClientException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
