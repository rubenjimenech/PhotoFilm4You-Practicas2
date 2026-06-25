package edu.uoc.epcsd.microcredential.infrastructure.repository.jpa;

import edu.uoc.epcsd.microcredential.domain.Microcredential;
import edu.uoc.epcsd.microcredential.domain.MicrocredentialStatus;
import edu.uoc.epcsd.microcredential.domain.repository.MicrocredentialRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.kafka.common.metrics.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicrocredentialRepositoryImpl implements MicrocredentialRepository {

    private final SpringDataMicrocredentialRepository jpaMicrocredentialRepository;
    
    @Override
    public Optional<Microcredential> getMicrocredentialById(Long microcredentialId) {
        //TODO: Complete the implementation  ---- DONE

        return jpaMicrocredentialRepository.getMicrocredentialById(microcredentialId).map(MicrocredentialEntity::toDomain);
    } 

    //TODO: createMicrocredential() ---- DONE
    @Override
    public Long createMicrocredential(Microcredential microcredential) {
        MicrocredentialEntity microcredentialEntity = MicrocredentialEntity.fromDomain(microcredential);

        return jpaMicrocredentialRepository.save(microcredentialEntity).getId();
    }

    
    //TODO: updateStatusPendingMicrocredential() ---- DONE
    @Override
    public void updateStatusPendingMicrocredential(Long id, MicrocredentialStatus status) {

        MicrocredentialEntity microcredentialEntity = jpaMicrocredentialRepository.getMicrocredentialById(id)
                .orElseThrow(() -> new IllegalArgumentException("Microcredential not found"));

        microcredentialEntity.setStatus(status);

        jpaMicrocredentialRepository.save(microcredentialEntity);
    }
    //TODO: getPendingMicrocredentialRequests()   ---- DONE

    @Override
    public List<Microcredential> getPendingMicrocredentialRequests(){

        return jpaMicrocredentialRepository.findByStatus(MicrocredentialStatus.REQUESTED).stream().map(MicrocredentialEntity::toDomain).collect(Collectors.toList());

    }


    
}
