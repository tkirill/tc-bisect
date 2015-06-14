package org.tkirill.teamcity.bisectPlugin;

public class BisectStep {
    private int left;
    private int right;

    public BisectStep(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getMid() {
        return BisectBoundaryHelper.getMid(left, right);
    }
}
