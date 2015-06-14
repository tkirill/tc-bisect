package org.tkirill.teamcity.bisectPlugin;

public class BisectBuild {
    private long buildId;
    private BisectStep step;

    public BisectBuild(long buildId, BisectStep step) {
        this.buildId = buildId;
        this.step = new BisectStep(step);
    }

    public long getBuildId() {
        return buildId;
    }

    public BisectStep getStep() {
        return step;
    }
}
