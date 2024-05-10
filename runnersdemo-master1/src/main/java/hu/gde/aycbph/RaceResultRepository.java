package hu.gde.aycbph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RaceResultRepository extends JpaRepository<RaceResultEntity, Long> {

}
