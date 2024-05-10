package hu.gde.aycbph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LapTimeRepository extends JpaRepository<LapTimeEntity, Long> {
}