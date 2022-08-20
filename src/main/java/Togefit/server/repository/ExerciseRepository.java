package Togefit.server.repository;

import Togefit.server.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, String> {
    Optional<Exercise> findByName(String exercise);
}
