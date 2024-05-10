package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RaceRunnersController {

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private LapTimeRepository lapTimeRepository;

    @Autowired
    private RunnerService raceRunnersService;


    @PostMapping("/{raceId}/addRunner")
    public void addRunnerToRace(@PathVariable Long raceId, @RequestBody Long runnerId) {
        RaceEntity race = raceRepository.findById(raceId)
                .orElseThrow(() -> new RuntimeException("Race not found"));

        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        //raceRunnersService.addRunnerToRace(race.getRaceId(), runner.getRunnerId());

        // Generate lap time for the runner and associate it with the race
        //generateLapTimeForRunnerAndRace(runner, race);

        // Save the changes to the database
        runnerRepository.save(runner);
        raceRepository.save(race);
        System.out.println("RRC addRunner");

    }

    @PostMapping("/runner/{id}/addlaptime")
    public String addLaptime(@PathVariable Long id, @ModelAttribute LapTimeEntity lapTime) {
        RunnerEntity runner = runnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        // Set the lap time's runner
        lapTime.setRunner(runner);

        // Set the lap time's race
        RaceEntity race = lapTime.getRace();
        System.out.println("RRC addLapTime");
        if (race == null) {
            throw new RuntimeException("Race not specified for lap time");
        }

        // Add the lap time to the runner
        runner.getLapTimes().add(lapTime);

        // Save the changes to the database
        runnerRepository.save(runner);
        lapTimeRepository.save(lapTime);

        return "redirect:/runner/" + id;
    }
    @GetMapping("/addRaceToRunnerForm")
    public String showAddRaceForm(Model model) {
        List<RaceEntity> races = raceRepository.findAll();
        List<RunnerEntity> runners = runnerRepository.findAll();
        System.out.println("RRC show");

        model.addAttribute("races", races);
        model.addAttribute("runners", runners);

        return "/addRaceToRunnerForm";
    }
    @PostMapping("/addRaceRunner")
    public String addRunner(@RequestParam Long raceId, @RequestParam Long runnerId) {
        RaceEntity race = raceRepository.findById(raceId)
                .orElseThrow(() -> new RuntimeException("Race not found"));

        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));



        //raceRunnersService.getAverageLaptime(runnerId);
        //raceRunnersService.getAverageLaptime(runnerId);


        System.out.println("RRC addRunner");

        // Generate lap time for the runner and associate it with the race
       // generateLapTimeForRunnerAndRace(runner, race);

        // Save the changes to the database
//        runnerRepository.save(runner);
//        raceRepository.save(race);
//        raceRunnersService.generateLapTimeForRunnerAndRace(runner, race);

        runner.getRaces().add(race);
        race.getRunners().add(runner);
        //raceRunnersService.addRunnerToRace(runner1.getRunnerId(), race1.getRaceId());
//        raceRunnersService.addLapTimeToRunner(runner1.getRunnerId());
        raceRunnersService.generateLapTimeForRunnerAndRace(runner, race);

        System.out.println("Your assignment to a competition is successful.");
        return "redirect:/index.html";
    }


}