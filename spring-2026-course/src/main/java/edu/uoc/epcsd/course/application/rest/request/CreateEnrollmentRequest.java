package edu.uoc.epcsd.course.application.rest.request;

import edu.uoc.epcsd.course.domain.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@AllArgsConstructor
public final class CreateEnrollmentRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String student;

    @NotNull
    private Date enrollmentDate;

    @NotNull
    private Long qualification;

    @NotNull
    private final EnrollmentStatus status = EnrollmentStatus.ACTIVE;


    @NotNull
    private Long courseId;


}




