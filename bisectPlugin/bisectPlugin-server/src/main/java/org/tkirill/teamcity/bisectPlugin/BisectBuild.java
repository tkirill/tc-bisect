package org.tkirill.teamcity.bisectPlugin;

public class BisectBuild {
    private long buildId;
    private int left;
    private int right;

    public long getBuildId() {
        return buildId;
    }

    public void setBuildId(long buildId) {
        this.buildId = buildId;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }
}
