package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final RunnerRepository runnerRepository;

    @Autowired
    private LapTimeRepository lapTimeRepository;
    @Autowired
    public RaceService(RaceRepository raceRepository, RunnerRepository runnerRepository) {
        this.raceRepository = raceRepository;
        this.runnerRepository = runnerRepository;
    }



    public void addRace(RaceEntity race) {
        // Itt lehet végezni a szükséges ellenőrzéseket vagy validációkat,
        // majd hozzáadni a futót az adatbázishoz a RunnerRepository segítségével
        raceRepository.save(race);
    }
    public double getAverageLaptime(Long raceId) {
        RaceEntity race = raceRepository.findById(raceId).orElse(null);
        if (race != null) {
            List<RunnerEntity> runners = race.getRunners();
            int totalLapTimes = 0;
            int totalRunners = 0;
            for (RunnerEntity runner : runners) {
                List<LapTimeEntity> lapTimes = runner.getLapTimes();
                for (LapTimeEntity lapTime : lapTimes) {
                    totalLapTimes += lapTime.getTimeSecond();
                    System.out.println("RaceS get");
                }
                totalRunners++;
            }
            return totalRunners > 0 ? (double) totalLapTimes / totalRunners : 0.0;
        } else {
            return -1.0;
        }
    }



    }


