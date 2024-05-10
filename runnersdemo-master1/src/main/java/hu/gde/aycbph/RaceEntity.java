package hu.gde.aycbph;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// RaceEntity osztály
@Entity
@Table(name = "races")
public class RaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "race_id")
    private Long raceId;

    @Column(name = "race_name")
    private String raceName;

    @Column(name = "race_distance")
    private double raceDistance;

    @Column(name = "race_organizer")
    private String raceOrganizer;

    @Column(name = "race_date")
    private String raceDate;

    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LapTimeEntity> lapTimes = new ArrayList<>();

    @ManyToMany(mappedBy = "races", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RunnerEntity> runners = new ArrayList<>();

    public void addRunner(RunnerEntity runner) {
        runners.add(runner);
        runner.getRaces().add(this); // Beállítjuk a versenyt a futóhoz
    }

    public void addLapTime(LapTimeEntity lapTime) {
        lapTimes.add(lapTime);
        lapTime.setRace(this); // Beállítjuk a laptidőhöz a versenyt
    }

    // Getterek és setterek

    public RaceEntity() {}

    public Long getRaceId() {
        return raceId;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public double getRaceDistance() {
        return raceDistance;
    }

    public void setRaceDistance(double raceDistance) {
        this.raceDistance = raceDistance;
    }

    public String getRaceOrganizer() {
        return raceOrganizer;
    }

    public void setRaceOrganizer(String raceOrganizer) {
        this.raceOrganizer = raceOrganizer;
    }

    public List<LapTimeEntity> getLapTimes() {
        return lapTimes;
    }

    public void setLapTimes(List<LapTimeEntity> lapTimes) {
        this.lapTimes = lapTimes;
    }

    public List<RunnerEntity> getRunners() {
        return runners;
    }

    public void setRunners(List<RunnerEntity> runners) {
        this.runners = runners;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }

    public String getRaceDate() {
        return raceDate;
    }
}

