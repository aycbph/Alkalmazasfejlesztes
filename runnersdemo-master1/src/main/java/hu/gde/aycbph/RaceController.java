package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private RaceService raceService;

    @Autowired
    private RunnerRepository runnerRepository; // Itt a RunnerRepository-t kell injektálni, nem a raceRepository-t

    @Autowired
    private LapTimeRepository lapTimeRepository;

    @GetMapping("/races")
    public String getAllRaces(Model model) {
        System.out.println("RaceC races");
        List<RaceEntity> races = raceRepository.findAll();
        model.addAttribute("races", races);
        return "races";
    }

    @GetMapping("/addRaceForm")
    public String showAddRaceForm(Model model) {
        System.out.println("RaceC addRForm");
        model.addAttribute("race", new RaceEntity());
        return "addRaceForm";
    }

    @PostMapping("/addRace")
    public String addRace(@ModelAttribute RaceEntity race) {
        System.out.println("RaceC addRace");

        raceService.addRace(race);
        return "redirect:/races";
    }

    @GetMapping("/{id}/averagelaptime")
    public double getAverageLaptime(@PathVariable Long id) {
        System.out.println("RaceC GetAver");
        RunnerEntity runner = runnerRepository.findById(id).orElse(null); // A runnerRepository-t kell használni
        if (runner != null) {
            List<LapTimeEntity> lapTimes = runner.getLapTimes();
            int totalTime = 0;
            for (LapTimeEntity lapTime : lapTimes) {
                totalTime += lapTime.getTimeSecond();
                System.out.println("RaceC get");
            }
            double averageLaptime = (double) totalTime / lapTimes.size();
            return averageLaptime;
        } else {
            return -1.0;
        }
    }

    @GetMapping("")
    public List<RaceEntity> getAllRaces() { // Vissza kell adni a RaceEntity listát, nem a RunnerEntity-t
        return raceRepository.findAll();
    }

    @PostMapping("/{id}/addlaptime")
    public ResponseEntity addLaptime(@PathVariable Long id, @RequestBody LapTimeEntity.LapTimeRequest lapTimeRequest) {
        System.out.println("RaceC addLapTim");
        RunnerEntity runner = runnerRepository.findById(id).orElse(null); // A runnerRepository-t kell használni
        if (runner != null) {
            LapTimeEntity lapTime = new LapTimeEntity();
            lapTime.setTimeSecond(lapTimeRequest.getLapTimeSeconds());
            lapTime.setLapNumber(runner.getLapTimes().size() + 1);
            lapTime.setRunner(runner);
            lapTimeRepository.save(lapTime);
            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Runner with ID " + id + " not found");
        }
    }

    @GetMapping("/race/{id}")
    public String getRaceById(@PathVariable Long id, Model model) {
        System.out.println("RaceC getBYD");
        RaceEntity race = raceRepository.findById(id).orElse(null);
        if (race != null) {
            model.addAttribute("race", race);
            double averageLaptime = raceService.getAverageLaptime(id);
            model.addAttribute("averageLaptime", averageLaptime);
            return "race";
        } else {
            // handle error when race is not found
            return "error";
        }
    }
    @PostMapping("/race/{id}/addRunner") //futó hozzárendelése a versenyhez
    public ResponseEntity addRunnerToRace(@PathVariable Long id, @RequestBody RunnerEntity runner) {
        System.out.println("RaceC addR");
        RaceEntity race = raceRepository.findById(id).orElse(null);
        if (race != null) {
            // Add logic to add the runner to the race
            race.addRunner(runner);
            raceRepository.save(race);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Race with ID " + id + " not found");
        }
    }

    public void addRunnerToRace(RaceEntity race, RunnerEntity runner) {
        race.addRunner(runner);
    }



}
