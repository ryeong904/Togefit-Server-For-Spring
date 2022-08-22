package Togefit.server.repository.Routine;

import Togefit.server.domain.Routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findByUserId(String userId);
}
