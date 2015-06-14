package org.tkirill.teamcity.bisectPlugin;

public class BisectDecision {
    private BisectStep nextStep;
    private boolean isSolved;
    private int answer;

    public BisectStep getNextStep() {
        return nextStep;
    }

    public void setNextStep(BisectStep nextStep) {
        this.nextStep = nextStep;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        this.isSolved = solved;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public static BisectDecision solved(int answer) {
        BisectDecision result = new BisectDecision();
        result.setSolved(true);
        result.setAnswer(answer);
        return result;
    }

    public static BisectDecision nextStep(BisectStep nextStep) {
        BisectDecision result = new BisectDecision();
        result.setSolved(false);
        result.setNextStep(nextStep);
        return result;
    }
}
