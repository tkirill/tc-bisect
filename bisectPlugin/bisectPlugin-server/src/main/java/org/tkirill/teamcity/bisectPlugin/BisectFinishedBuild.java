package org.tkirill.teamcity.bisectPlugin;

public class BisectFinishedBuild {
    private int left;
    private int right;
    private boolean isSuccess;

    public BisectFinishedBuild(int left, int right, boolean isSuccess) {
        this.left = left;
        this.right = right;
        this.isSuccess = isSuccess;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getMid() {
        return new BisectStep(left, right).getMid();
    }
}
