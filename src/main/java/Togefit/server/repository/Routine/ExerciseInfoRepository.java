package Togefit.server.repository.Routine;

import Togefit.server.domain.Routine.ExerciseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseInfoRepository extends JpaRepository<ExerciseInfo, Long> {
    List<ExerciseInfo> findByRoutineListId(Long id);
    void deleteByRoutineListId(Long id);
}
