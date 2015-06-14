package org.tkirill.teamcity.bisectPlugin;

import java.util.ArrayList;
import java.util.List;

public class Bisect {
    private long buildId;
    private List<BisectBuild> builds;
    private boolean isFinished;

    public boolean isSolved() {
        return solved;
    }

    public int getAnswer() {
        return answer;
    }

    private boolean solved;
    private int answer;

    public Bisect(long buildId) {
        this.buildId = buildId;
        builds = new ArrayList<>();
    }

    public List<BisectBuild> getBuilds() {
        return builds;
    }

    @SuppressWarnings("unused")
    public void setBuilds(List<BisectBuild> builds) {
        this.builds = new ArrayList<>(builds);
    }

    public long getBuildId() {
        return buildId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void addBuild(BisectBuild build) {
        builds.add(build);
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
