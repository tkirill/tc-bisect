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
        builds = new ArrayList<BisectBuild>();
    }

    public List<BisectBuild> getBuilds() {
        return builds;
    }

    @SuppressWarnings("unused")
    public void setBuilds(List<BisectBuild> builds) {
        this.builds = new ArrayList<BisectBuild>(builds);
    }

    public long getBuildId() {
        return buildId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
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

    public List<Long> getBuildIds() {
        List<Long> result = new ArrayList<Long>();
        for (BisectBuild build : getBuilds()) result.add(build.getBuildId());
        return result;
    }
}
