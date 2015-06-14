package org.tkirill.teamcity.bisectPlugin;

import org.jetbrains.annotations.Nullable;

public final class BisectBoundaryHelper {
    private BisectBoundaryHelper() {
    }

    public static BisectStep getNextStep(int left, int right, boolean isSuccess) {
        return isSuccess ? later(left, right) : earlier(left, right);
    }

    public static BisectStep firstStep(int number) {
        return checkBoundaries(0, number);
    }

    private static BisectStep later(int left, int right) {
        int nextLeft = getMid(left, right) + 1;
        return checkBoundaries(nextLeft, right);
    }

    private static BisectStep earlier(int left, int right) {
        int nextRight = getMid(left, right);
        return checkBoundaries(left, nextRight);
    }

    @Nullable
    private static BisectStep checkBoundaries(int nextLeft, int nextRight) {
        if (nextRight <= nextLeft) return null;
        return new BisectStep(nextLeft, nextRight);
    }

    public static int getMid(int left, int right) {
        return left + (right - left) / 2;
    }
}
