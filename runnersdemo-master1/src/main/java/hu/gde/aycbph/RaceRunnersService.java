package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

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
                // handle error when lap times are not found
                return 0.0;
            }
        } else {
            // handle error when runner is not found
            return -1.0;
        }
    }

    @Transactional
    public void addRunner(RunnerEntity runner) {
        // Itt lehet végezni a szükséges ellenőrzéseket vagy validációkat,
        // majd hozzáadni a futót az adatbázishoz a RunnerRepository segítségével
        System.out.println("RRS addR");
        runnerRepository.save(runner);
    }


    public void addRunner(Long raceId, Long runnerId) throws ChangeSetPersister.NotFoundException {
        RaceEntity race = raceRepository.findById(raceId)
                .orElseThrow(() -> new RuntimeException("Race not found"));

        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        getAverageLaptime(runnerId);
        getAverageLaptime(raceId);
        addLapTimeToRunner(runner.getRunnerId());

        race.getRunners().add(runner);
        raceRepository.save(race);

        runner.getRaces().add(race);
        runnerRepository.save(runner);
        //getAverageLaptime(runnerId);
        //generateLapTimeForRunnerAndRace(runner, race);


        System.out.println("RRS addRnunner " + runner + " race: " + race + " average: " + getAverageLaptime(runnerId));


    }
    @Transactional
    public void addRunnerToRace(Long raceId, Long runnerId) throws ChangeSetPersister.NotFoundException {
        RaceEntity race = raceRepository.findById(raceId)
                .orElseThrow(() -> new RuntimeException("Race not found"));

        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        System.out.println("RRS addRunnerToRace");



        race.getRunners().add(runner);
        raceRepository.save(race);

        runner.getRaces().add(race);
        runnerRepository.save(runner);

        generateLapTimeForRunnerAndRace(runner, race);

        System.out.println("RRS addRunnerToRace");

    }

    @Transactional
    public void generateLapTimeForRunnerAndRace(RunnerEntity runner, RaceEntity race) throws ChangeSetPersister.NotFoundException {
        Random random = new Random();
        double result = random.nextInt(800) + 350;
        int timeSeconds = random.nextInt(800) + 350;

        // Get the last lapNumber of the runner
        int lastLapNumber = runner.getLapTimes().isEmpty() ? 0 : runner.getLapTimes().get(runner.getLapTimes().size() - 1).getLapNumber();

        LapTimeEntity lapTime = new LapTimeEntity();
        lapTime.setLapNumber(lastLapNumber + 1);
        lapTime.setTimeSecond(timeSeconds);
        lapTime.setRunner(runner);
        lapTime.setRace(race);

        // Set average pace for the runner
        int averagePace = random.nextInt(800) + 190; // generált avaragPace
        runner.setAveragePace(averagePace);

        // Add lap time to the runner
        runner.getLapTimes().add(lapTime);
        lapTime.setRunner(runner);
        lapTime.setRace(race);
        runnerRepository.save(runner);

        System.out.println("average: " + runner.getAveragePace());
        System.out.println("LapTime: " + lapTime.getRunner());
        System.out.println("timesecond  laptimeValue: " + lapTime.lapTimeValue);
        System.out.println("timesecond  laptimegetTimeSec: " + lapTime.getTimeSecond());
    }



    @Transactional
    public void addLapTime(LapTimeEntity lapTime, int averagePace, RunnerEntity runner, RaceEntity race) throws ChangeSetPersister.NotFoundException {
        runner.getLapTimes().add(lapTime);
        lapTime.setRunner(runner);
        lapTime.setRace(race);
        lapTime.setTimeSecond(averagePace);
        System.out.println("RRS addLapTime");

        runner.setAveragePace(averagePace);
        //calculateAverageRunningTime(runner.getRunnerId());
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


    @Transactional
    public void addLapTimeToRunner(Long runnerId) throws ChangeSetPersister.NotFoundException {
        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        System.out.println("RaceRunnerS addLapTimeToRunner");
        List<RaceEntity> races = runner.getRaces();
        for (RaceEntity race : races) {
            generateLapTimeForRunnerAndRace(runner, race);
        }

        // Save changes to the database
        runnerRepository.save(runner);
    }
}





