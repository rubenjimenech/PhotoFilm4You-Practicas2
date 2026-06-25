package edu.uoc.epcsd.microcredential.domain.service;

import edu.uoc.epcsd.microcredential.domain.Microcredential;
import edu.uoc.epcsd.microcredential.domain.MicrocredentialStatus;
import edu.uoc.epcsd.microcredential.domain.repository.MicrocredentialRepository;
import edu.uoc.epcsd.microcredential.infrastructure.kafka.KafkaConstants;
import edu.uoc.epcsd.microcredential.infrastructure.kafka.MicrocredentialMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Validated
public class MicrocredentialServiceImpl implements MicrocredentialService {

    private final MicrocredentialRepository microcredentialRepository;
	private final KafkaTemplate<String, MicrocredentialMessage> microcredentialKafkaTemplate;

    @Value("${courseService.url}")
    private String courseServiceUrl;
    
	@Override public Optional<Microcredential> getMicrocredentialById(Long microcredentialId) {
		// TODO: Complete the implementation ---- DONE
		return microcredentialRepository.getMicrocredentialById(microcredentialId);
	}
    //TODO: approvePendingMicrocredential   ----DONE

	@Override
	public void approvePendingMicrocredential(Long microcredentialId) {
		Microcredential microcredential = getMicrocredentialById(microcredentialId).orElseThrow(() -> new IllegalArgumentException("Microcredential not found"));

		if(microcredential.getStatus() != MicrocredentialStatus.REQUESTED) {
			throw new IllegalArgumentException("Microcredential is not requested");
		}

		microcredentialRepository.updateStatusPendingMicrocredential(microcredentialId, MicrocredentialStatus.GRANTED);

		microcredentialKafkaTemplate.send(KafkaConstants.MICROCREDENTIAL_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.APPROVED,
				MicrocredentialMessage.builder().microcredentialId(microcredentialId).enrollment(microcredential.getEnrollment()).build());

	}


	//TODO: rejectPendingMicrocredential()    ---- DONE

	@Override
	public void rejectPendingMicrocredential(Long microcredentialId) {
		Microcredential microcredential = getMicrocredentialById(microcredentialId).orElseThrow(() -> new IllegalArgumentException("Microcredential not found"));

		if(microcredential.getStatus() != MicrocredentialStatus.REQUESTED) {
			throw new IllegalArgumentException("Microcredential is not requested");
		}
		microcredentialRepository.updateStatusPendingMicrocredential(microcredentialId, MicrocredentialStatus.REJECTED);

		microcredentialKafkaTemplate.send(KafkaConstants.MICROCREDENTIAL_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.REJECTED,
				MicrocredentialMessage.builder().microcredentialId(microcredentialId).enrollment(microcredential.getEnrollment()).build());
	}


	//TODO: requestCourseMicrocredentials()  ---- DONE
	@Override
	public Long requestCourseMicrocredentials(Microcredential microcredential){
		if (microcredential == null){
			throw new IllegalArgumentException("Microcredential cannot be null");
		}
		microcredential.setStatus(MicrocredentialStatus.REQUESTED);
		Long microcredentialId = microcredentialRepository.createMicrocredential(microcredential);

		microcredentialKafkaTemplate.send (KafkaConstants.MICROCREDENTIAL_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.PENDING,
				MicrocredentialMessage.builder().microcredentialId(microcredentialId).enrollment(microcredential.getEnrollment()).build());
	return microcredentialId;
	}
	

	
	//TODO: getPendingMicrocredentialRequests()   ---- DONE
	@Override
	public List<Microcredential> getPendingMicrocredentialRequests(){

		return microcredentialRepository.getPendingMicrocredentialRequests();

	}
    
}
