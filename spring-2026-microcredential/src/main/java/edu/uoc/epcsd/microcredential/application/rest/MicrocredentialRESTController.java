package edu.uoc.epcsd.microcredential.application.rest;


import edu.uoc.epcsd.microcredential.application.rest.request.MicrocredentialRequest;
import edu.uoc.epcsd.microcredential.domain.Microcredential;
import edu.uoc.epcsd.microcredential.domain.service.MicrocredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/microcredentials")
public class MicrocredentialRESTController {

    private final MicrocredentialService microcredentialService;

    @GetMapping("/{microcredentialId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Microcredential> getMicrocredentialById(@PathVariable @NotNull Long microcredentialId) {
        //TODO: Complete the implementation   ---- DONE
        log.trace("getMicrocredentialById: " + microcredentialId);


        return microcredentialService.getMicrocredentialById(microcredentialId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    //TODO: approvePendingMicrocredential()  ---- TODO
    @PutMapping("/{microcredentialId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> approvePendingMicrocredential(@PathVariable @NotNull Long microcredentialId){
        log.trace("approvePendingMicrocredential: " + microcredentialId);

        try{
            microcredentialService.approvePendingMicrocredential(microcredentialId);
            return ResponseEntity.ok().build();

        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    
    //TODO: rejectPendingMicrocredential()    ---- DONE
    @PutMapping("/{microcredentialId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <Void> rejectPendingMicrocredential(@PathVariable @NotNull Long microcredentialId){
        log.trace("rejectPendingMicrocredential: " + microcredentialId);

        try {
            microcredentialService.rejectPendingMicrocredential(microcredentialId);
            return ResponseEntity.ok().build();

        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    //TODO: requestCourseMicrocredentials()  ---- DONE
    @PostMapping("/request")
    public ResponseEntity<Long> requestCourseMicrocredentials(@Valid @RequestBody MicrocredentialRequest request) {

        log.trace("requestCourseMicrocredentials: " + request);

        try {

            Microcredential microcredential = Microcredential.builder()
                    .submitDate(request.getSubmitDate())
                    .assignmentDate(request.getAssignmentDate())
                    .content(request.getContent())
                    .enrollment(request.getEnrollment())
                    .build();

            Long id = microcredentialService.requestCourseMicrocredentials(microcredential);

            return ResponseEntity.status(HttpStatus.CREATED).body(id);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }



    //TODO: getPendingMicrocredentialRequests()    ---- DONE
    @GetMapping("/pending")
    public ResponseEntity<List<Microcredential>> getPendingMicrocredentialRequests(){

        log.trace("getPendingMicrocredentialRequests: ");

        return ResponseEntity.ok(microcredentialService.getPendingMicrocredentialRequests());

    }
    
}
