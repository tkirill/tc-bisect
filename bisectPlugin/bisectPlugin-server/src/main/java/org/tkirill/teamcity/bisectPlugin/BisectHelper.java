package org.tkirill.teamcity.bisectPlugin;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BisectHelper {
    private BisectHelper() {
    }

    public static BisectDecision getNextStep(List<BisectFinishedBuild> history, BisectStep currentStep) {
        if (history.isEmpty()) {
            if (currentStep.size() == 1) {
                return BisectDecision.solved(currentStep.getMid());
            }
            return BisectDecision.nextStep(currentStep);
        }

        BisectFinishedBuild lastBuild = history.get(history.size() - 1);
        if (lastBuild.isSuccess()) {
            BisectStep nextStep = BisectBoundaryHelper.getNextStep(lastBuild.getStep(), true);
            if (nextStep == null) {
                BisectFinishedBuild lastFailed = getLastFailed(history);
                if (lastFailed == null) {
                    return null;
                } else {
                    return BisectDecision.solved(lastFailed.getMid());
                }
            }

            if (nextStep.size() == 1) {
                return BisectDecision.solved(nextStep.getMid());
            }

            BisectDecision result = new BisectDecision();
            result.setSolved(false);
            result.setNextStep(nextStep);
            return result;
        }

        if (currentStep.size() == 1) {
            return BisectDecision.solved(currentStep.getMid());
        }
        BisectStep nextStep = BisectBoundaryHelper.getNextStep(lastBuild.getStep(), false);
        if (nextStep == null) {
            return BisectDecision.solved(currentStep.getMid());
        }
        BisectDecision result = new BisectDecision();
        result.setSolved(false);
        result.setNextStep(nextStep);
        return result;
    }

    @Nullable
    private static BisectFinishedBuild getLastFailed(List<BisectFinishedBuild> history) {
        BisectFinishedBuild lastFailed = null;
        for (int i = history.size() - 1; i >= 0 ; i--) {
            if (!history.get(i).isSuccess()) {
                lastFailed = history.get(i);
                break;
            }
        }
        return lastFailed;
    }
}
