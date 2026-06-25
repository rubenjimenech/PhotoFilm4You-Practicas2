package edu.uoc.epcsd.microcredential.domain.service;

import edu.uoc.epcsd.microcredential.domain.Microcredential;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;

public interface MicrocredentialService {

	Optional<Microcredential> getMicrocredentialById(@NotNull Long microcredentialId);

	void approvePendingMicrocredential(Long microcredentialId);

	void rejectPendingMicrocredential(Long microcredentialId);

	Long requestCourseMicrocredentials(Microcredential microcredential);

	List<Microcredential> getPendingMicrocredentialRequests();
}
