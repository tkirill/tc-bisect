package org.tkirill.teamcity.bisectPlugin;

import java.util.List;

public class BisectHelper {
    private BisectHelper() {
    }

    public static BisectDecision getNextStep(List<BisectFinishedBuild> history, BisectStep currentStep) {
        if (history.isEmpty()) {
            if (currentStep.size() == 1) {
                BisectDecision bisectDecision = new BisectDecision();
                bisectDecision.setSolved(true);
                bisectDecision.setAnswer(currentStep.getMid());
                return bisectDecision;
            }
            BisectDecision bisectDecision = new BisectDecision();
            bisectDecision.setSolved(false);
            bisectDecision.setNextStep(currentStep);
            return bisectDecision;
        }

        BisectFinishedBuild lastBuild = history.get(history.size() - 1);
        if (lastBuild.isSuccess()) {
            BisectStep nextStep = BisectBoundaryHelper.getNextStep(lastBuild.getLeft(), lastBuild.getRight(), true);
            if (nextStep == null) {
                BisectFinishedBuild lastFailed = null;
                for (int i = history.size() - 1; i >= 0 ; i--) {
                    if (!history.get(i).isSuccess()) {
                        lastFailed = history.get(i);
                        break;
                    }
                }
                if (lastFailed == null) {
                    return null;
                } else {
                    BisectDecision result = new BisectDecision();
                    result.setSolved(true);
                    result.setAnswer(lastFailed.getMid());
                    return result;
                }
            }

            if (nextStep.size() == 1) {
                BisectDecision result = new BisectDecision();
                result.setSolved(true);
                result.setAnswer(nextStep.getMid());
                return result;
            }

            BisectDecision result = new BisectDecision();
            result.setSolved(false);
            result.setNextStep(nextStep);
            return result;
        }

        if (currentStep.size() == 1) {
            BisectDecision result = new BisectDecision();
            result.setSolved(true);
            result.setAnswer(currentStep.getMid());
            return result;
        }
        BisectStep nextStep = BisectBoundaryHelper.getNextStep(lastBuild.getLeft(), lastBuild.getRight(), false);
        BisectDecision result = new BisectDecision();
        result.setSolved(false);
        result.setNextStep(nextStep);
        return result;
    }
}
