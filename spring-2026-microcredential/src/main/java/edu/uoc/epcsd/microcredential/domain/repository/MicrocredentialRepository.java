package edu.uoc.epcsd.microcredential.domain.repository;

import edu.uoc.epcsd.microcredential.domain.Microcredential;
import edu.uoc.epcsd.microcredential.domain.MicrocredentialStatus;

import java.util.List;
import java.util.Optional;

public interface MicrocredentialRepository {
	
	Optional<Microcredential> getMicrocredentialById(Long microcredentialId);

	Long createMicrocredential(Microcredential microcredential);

	void updateStatusPendingMicrocredential(Long id , MicrocredentialStatus status);

	List<Microcredential> getPendingMicrocredentialRequests();
	
}
