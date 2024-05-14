package hu.gde.aycbph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class DataLoader implements CommandLineRunner {

    private final RaceRepository raceRepository;
    private final RaceRunnersService raceRunnersService;
    private final RunnerRepository runnerRepository;
    private final LapTimeRepository lapTimeRepository;

    @Autowired
    public DataLoader(RaceRepository raceRepository, RunnerRepository runnerRepository, LapTimeRepository lapTimeRepository, RaceRunnersService raceRunnersService) {
        this.raceRepository = raceRepository;
        this.runnerRepository = runnerRepository;
        this.lapTimeRepository = lapTimeRepository;
        this.raceRunnersService = raceRunnersService;
    }


    @Override
    public void run(String... args) throws Exception {
        // Létrehozunk két versenyt
        RaceEntity race1 = new RaceEntity();
        race1.setRaceName("Kápolnai");
        race1.setRaceDistance(20.0);
        race1.setRaceDate("2024-04-23");
        race1.setRaceOrganizer("Kisfalvi Béla");
        raceRepository.save(race1);
        System.out.println("Race1" + race1);

        RaceEntity race2 = new RaceEntity();
        race2.setRaceName("Maraton");
        race2.setRaceDistance(26.0);
        race2.setRaceDate("2024-04-28");
        race2.setRaceOrganizer("Kelemen Andrea");
        raceRepository.save(race2);
        System.out.println("Race2" + race2);

        // Létrehozunk három futót
        RunnerEntity runner1 = new RunnerEntity();
        runner1.setRunnerName("Mészáros Zsanett");
        runner1.setAveragePace(290);
        runner1.setRunnerAge(24);
        runner1.setRunnerGender("female");
        runnerRepository.save(runner1);
        System.out.println("R1" + runner1);

        RunnerEntity runner2 = new RunnerEntity();
        runner2.setRunnerName("Hidvégi Zsuzsi");
        runner2.setAveragePace(240);
        runner2.setRunnerAge(24);
        runner2.setRunnerGender("female");
        runnerRepository.save(runner2);
        System.out.println("R2" + runner2);

        RunnerEntity runner3 = new RunnerEntity();
        runner3.setRunnerName("Herczeg Zoltán");
        runner3.setAveragePace(190);
        runner3.setRunnerAge(22);
        runner3.setRunnerGender("male");
        runnerRepository.save(runner3);
        System.out.println("R3" + runner3);

        RunnerEntity runner4 = new RunnerEntity();
        runner4.setRunnerName("Halmos Zoltán");
        runner4.setAveragePace(190);
        runner4.setRunnerAge(22);
        runner4.setRunnerGender("male");
        runnerRepository.save(runner4);
        System.out.println("R4" + runner4);

        RunnerEntity runner5 = new RunnerEntity();
        runner5.setRunnerName("Halmos Péter");
        runner5.setAveragePace(195);
        runner5.setRunnerAge(28);
        runner5.setRunnerGender("male");
        runnerRepository.save(runner5);
        System.out.println("R5" + runner5);

        RunnerEntity runner6 = new RunnerEntity();
        runner6.setRunnerName("Hidvégi Ármin");
        runner6.setAveragePace(198);
        runner6.setRunnerAge(27);
        runner6.setRunnerGender("male");
        runnerRepository.save(runner6);
        System.out.println("R6" + runner6);


        List<RunnerEntity> runners = runnerRepository.findAll();
        List<RaceEntity> races = raceRepository.findAll();



        Map<Long, List<LapTimeEntity>> lapTimesByRace2 = new HashMap<>();


        if (race2 != null) {

            List<LapTimeEntity> lapTimesForRace2 = new ArrayList<>();
            for (RunnerEntity runner : runners) {
                LapTimeEntity lapTime = raceRunnersService.generateLapTimeForRunnerAndRace(runner, race2);

                if (lapTime != null && race2.getRunners().contains(runner)) {
                    lapTimesForRace2.add(lapTime);
                }
            }
            lapTimesByRace2.put(race2.getRaceId(), lapTimesForRace2);


            List<LapTimeEntity> lapTimes = lapTimesByRace2.get(race2.getRaceId());
            if (lapTimes != null) {
                for (RunnerEntity runner : race2.getRunners()) {
                    if (!lapTimes.isEmpty()) {
                        LapTimeEntity lapTime = lapTimes.remove(0);
                        lapTime.setRace(race2);
                        lapTime.setRunner(runner);
                        runner.getLapTimes().add(lapTime);
                    }
                }
            }

            raceRunnersService.generateLapTimeForRunnerAndRace(runner1, race1);
            raceRunnersService.generateLapTimeForRunnerAndRace(runner2, race1);

        }
    }
}

