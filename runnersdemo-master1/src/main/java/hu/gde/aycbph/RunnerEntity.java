package hu.gde.aycbph;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "runners")
public class RunnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "runner_id")
    private Long runnerId;

    @Column(name = "runner_name", nullable = false)
    private String runnerName;

    @Column(name = "runner_gender", nullable = false)
    private String runnerGender;

    @Column(name = "runner_age", nullable = false)
    private int runnerAge;

    @Column(name = "average_pace", nullable = false)
    private long averagePace;

    @Column(name = "timeseconds", nullable = false)
    private int timeSeconds;

    @ManyToMany
    @JoinTable(
            name = "race_runner",
            joinColumns = @JoinColumn(name = "runner_id"),
            inverseJoinColumns = @JoinColumn(name = "race_id")
    )
    private List<RaceEntity> races = new ArrayList<>();

    @OneToMany(mappedBy = "runner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LapTimeEntity> lapTimes = new ArrayList<>();


    public void addLapTime(LapTimeEntity lapTime, int averagePace, RunnerEntity runner, RaceEntity race) {
        // Assuming lapTimes is a list in RunnerEntity
        lapTimes.add(lapTime);
        lapTime.setRunner(runner);
        lapTime.setRace(race);
        lapTime.setTimeSecond(averagePace);
        this.timeSeconds = lapTime.lapTimeValue;
    }




    public List<LapTimeEntity> getLapTimes() {
        return lapTimes;
    }




    // Getterek és setterek
    public RunnerEntity() {}

    public List<RaceEntity> getRaces() {
        return races;
    }


    public Long getRunnerId() {
        return runnerId;
    }

    public String getRunnerName() {
        return runnerName;
    }

    public void setRunnerName(String runnerName) {
        this.runnerName = runnerName;
    }

    public String getRunnerGender() {
        return runnerGender;
    }

    public void setRunnerGender(String runnerGender) {
        this.runnerGender = runnerGender;
    }

    public int getRunnerAge() {
        return runnerAge;
    }

    public void setRunnerAge(int runnerAge) {
        this.runnerAge = runnerAge;
    }

    public long getAveragePace() {
        return averagePace;
    }


    public void setAveragePace(long averagePace) {
        this.averagePace = averagePace;
    }


    public int getTimeSeconds() {
        return timeSeconds;
    }



}
