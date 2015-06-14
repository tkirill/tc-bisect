package org.tkirill.teamcity.bisectPlugin;

public class BisectFinishedBuild {
    private BisectStep step;
    private boolean isSuccess;

    public BisectFinishedBuild(BisectStep step, boolean isSuccess) {
        this.step = step;
        this.isSuccess = isSuccess;
    }

    public BisectStep getStep() {
        return step;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getMid() {
        return step.getMid();
    }
}
