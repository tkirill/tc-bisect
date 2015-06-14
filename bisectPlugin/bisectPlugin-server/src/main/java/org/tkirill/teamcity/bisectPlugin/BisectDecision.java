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
}
