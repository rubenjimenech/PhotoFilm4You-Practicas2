package edu.uoc.epcsd.course.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MicrocredentialRequest {

    @NotNull
    private Date submitDate;

    @NotNull
    private Date assignmentDate;

    @NotNull
    private String content;

    @NotNull
    private Long enrollment;
}