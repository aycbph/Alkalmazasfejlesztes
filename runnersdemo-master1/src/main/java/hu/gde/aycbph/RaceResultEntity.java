package hu.gde.aycbph;

import jakarta.persistence.*;

@Entity
@Table(name = "race_results")
public class RaceResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "race_result_id")
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "race_id", nullable = false)
    private RaceEntity race;

    @ManyToOne
    @JoinColumn(name = "runner_id", nullable = false)
    private RunnerEntity runner;

    @Column(name = "position")
    private int position;

    // Konstruktorok, getterek és setterek

    public RaceResultEntity() {
    }

    public RaceResultEntity(RaceEntity race, RunnerEntity runner, int position) {
        this.race = race;
        this.runner = runner;
        this.position = position;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public RaceEntity getRace() {
        return race;
    }

    public void setRace(RaceEntity race) {
        this.race = race;
    }

    public RunnerEntity getRunner() {
        return runner;
    }

    public void setRunner(RunnerEntity runner) {
        this.runner = runner;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
