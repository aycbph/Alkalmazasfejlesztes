package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RunnerController {

    @Autowired
    private RunnerRepository runnerRepository;
    @Autowired
    private RaceRunnersService runnerService;
    @Autowired
    private RaceRepository raceRepository;


    @Autowired
    private LapTimeRepository lapTimeRepository;
    @GetMapping("/runners")
    public String getAllRunners(Model model) {
        //public String getAllRunners(Model model) {
        List<RunnerEntity> runners = runnerRepository.findAll();
        System.out.println("RC runnerse");
        model.addAttribute("runners", runners);
        return "runners";
    }

    @GetMapping("/runner/{id}")
    public String getRunnerById(@PathVariable Long id, Model model) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        System.out.println("RC getRunBYD");
        if (runner != null) {
            model.addAttribute("runner", runner);
            double averageLaptime = runnerService.getAverageLaptime(runner.getRunnerId());
            model.addAttribute("averageLaptime", averageLaptime);
            return "/runner";
        } else {
            // handle error when runner is not found
            return "error";
        }
    }




    @GetMapping("/addRunnerForm")
    public String showAddRunnerForm(Model model) {
        System.out.println("RC addRunForm");
        model.addAttribute("runner", new RunnerEntity());
        return "addRunnerForm";
    }

    @PostMapping("/addRunner")
    public String addRunner(@ModelAttribute RunnerEntity runner) {
        System.out.println("RC runne");
        // Add the new runner to the database using the service
        runnerService.addRunner(runner);
        return "redirect:/runners";
    }



}






