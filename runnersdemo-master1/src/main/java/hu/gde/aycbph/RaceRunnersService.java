package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RaceRunnersService {

    private final RunnerRepository runnerRepository;
    private final RunnerService runnerService;
    private final RaceRepository raceRepository;


    @Autowired
    public RaceRunnersService(RunnerRepository runnerRepository, RaceRepository raceRepository, RunnerService runnerService) {
        this.runnerRepository = runnerRepository;
        this.raceRepository = raceRepository;
        this.runnerService = runnerService;
    }

    int minLapTimeValue = 180;
    int maxLapTimeValue = 800;


    @Transactional
    public double getAverageLaptime(Long runnerId) {
        RunnerEntity runner;
        runner = runnerRepository.findById(runnerId).orElse(null);
        System.out.println("RRS getAve");
        if (runner != null) {
            List<LapTimeEntity> laptimes;
            laptimes = runner.getLapTimes();
            int totalTime = 0;
            for (LapTimeEntity laptime : laptimes) {
                totalTime += laptime.getTimeSecond();
                System.out.println("RRS getAve");
            }
            if (!laptimes.isEmpty()) {
                return (double) totalTime / laptimes.size();
            } else {

                return 0.0;
            }
        } else {

            return -1.0;
        }
    }

    @Transactional
    public void addRunner(RunnerEntity runner) {
        System.out.println("RRS addR");
        RaceEntity race = runner.getRaces().get(0);
        if (!race.getRunners().contains(runner)) {
            race.getRunners().add(runner);
            raceRepository.save(race);
        }
    }


    @Transactional
    public LapTimeEntity generateLapTimeForRunnerAndRace(RunnerEntity runner, RaceEntity race) throws ChangeSetPersister.NotFoundException {

        double result = ThreadLocalRandom.current().nextInt(minLapTimeValue, maxLapTimeValue + 1);
        int timeSeconds = ThreadLocalRandom.current().nextInt(minLapTimeValue, maxLapTimeValue + 1);
        int lapTimeValue = ThreadLocalRandom.current().nextInt(minLapTimeValue, maxLapTimeValue + 1);


        int lastLapNumber;
        lastLapNumber = runner.getLapTimes().isEmpty() ? 0 : runner.getLapTimes().get(runner.getLapTimes().size() - 1).getLapNumber();

        LapTimeEntity lapTime = new LapTimeEntity(runner, race, lastLapNumber + 1, timeSeconds, lapTimeValue);
        lapTime.setLapNumber(lastLapNumber + 1);
        lapTime.setTimeSecond(timeSeconds);
        lapTime.setRunner(runner);
        lapTime.setRace(race);


        if (runner.getLapTimes().isEmpty()) {
            int averagePace = ThreadLocalRandom.current().nextInt(minLapTimeValue, maxLapTimeValue + 1);
            runner.setAveragePace(averagePace);
        }

        double averageLaptime = calculateAverageRunningTime(runner.getRunnerId());
        double averageTime = calculateAverageRunningTime(runner.getRunnerId());
      System.out.println("Átlagos idő: " + averageTime + " másodperc");




        race.setAverageLaptime(averageLaptime);
        System.out.println("Átlagos idő: " + averageLaptime + " másodperc");


        runner.getLapTimes().add(lapTime);
        lapTime.setRunner(runner);
        lapTime.setRace(race);
        runnerRepository.save(runner);

        raceRepository.save(race);

        runner.getRaces().forEach(r -> r.getLapTimes().add(lapTime));
        try {
            calculateAverageRunningTime(runner.getRunnerId());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("average: " + runner.getAveragePace());
        System.out.println("LapTime: " + lapTime.getRunner());
        System.out.println("timesecond  laptimeValue: " + lapTime.lapTimeValue);
        System.out.println("timesecond  laptimegetTimeSec: " + lapTime.getTimeSecond());
        return lapTime;
    }




    @Transactional
    public double calculateAverageRunningTime(Long runnerId) throws ChangeSetPersister.NotFoundException {
        double result = 0.0;

        RunnerEntity runner;
        runner = runnerRepository.findById(runnerId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        List<LapTimeEntity> lapTimes = runner.getLapTimes();
        System.out.println("RRS calc");

        if (!lapTimes.isEmpty()) {
            double totalTime = lapTimes.stream().mapToDouble(LapTimeEntity::getTimeSecond).sum();
            result = totalTime / lapTimes.size();
        }


        return result;
    }


}





