package org.tkirill.teamcity.bisectPlugin;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class BisectHelperTest {
    @Test
    public void GetNextStep_EmptyHistoryAndSizeOne_ReturnsSolved() throws Exception {
        BisectStep currentStep = new BisectStep(0, 1);

        BisectDecision actual = BisectHelper.getNextStep(new ArrayList<BisectFinishedBuild>(), currentStep);

        assertTrue(actual.isSolved());
        assertEquals(actual.getAnswer(), 0);
    }

    @Test
    public void GetNextStep_EmptyHistoryAndMultipleChanges_ReturnsStep() throws Exception {
        BisectStep currentStep = new BisectStep(0, 2);

        BisectDecision actual = BisectHelper.getNextStep(new ArrayList<BisectFinishedBuild>(), currentStep);

        assertFalse(actual.isSolved());
        assertNextStep(actual, 0, 2);
    }

    @Test
    public void GetNextStep_SuccessAndStepSizeOneAndNoFailHistory_ReturnsNull() throws Exception {
        BisectStep currentStep = new BisectStep(0, 1);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 1), true));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertNull(actual);
    }

    @Test
    public void GetNextStep_SuccessAndStepSizeTwoAndNoFailHistory_ReturnsNull() throws Exception {
        BisectStep currentStep = new BisectStep(0, 2);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 2), true));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertNull(actual);
    }

    @Test
    public void GetNextStep_SuccessAndStepSizeOneAndHasFail_ReturnsLastFailed() throws Exception {
        BisectStep currentStep = new BisectStep(0, 1);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 8), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 4), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 1), true));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertTrue(actual.isSolved());
        assertEquals(actual.getAnswer(), 2);
    }

    @Test
    public void GetNextStep_SuccessAndStepSizeTwoAndHasFail_ReturnsLastFailed() throws Exception {
        BisectStep currentStep = new BisectStep(0, 2);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 8), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 4), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 2), true));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertTrue(actual.isSolved());
        assertEquals(actual.getAnswer(), 2);
    }

    @Test
    public void GetNextStep_SuccessAndStepSizeThree_ReturnsRight() throws Exception {
        BisectStep currentStep = new BisectStep(0, 3);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 12), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 6), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 3), true));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertTrue(actual.isSolved());
        assertEquals(actual.getAnswer(), 2);
    }

    @Test
    public void GetNextStep_SuccessAndStepSizeFour_ReturnsRight() throws Exception {
        BisectStep currentStep = new BisectStep(0, 4);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 16), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 8), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 4), true));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertTrue(actual.isSolved());
        assertEquals(actual.getAnswer(), 3);
    }

    @Test
    public void GetNextStep_SuccessAndBigStep_ReturnsLater() throws Exception {
        BisectStep currentStep = new BisectStep(0, 5);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 20), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 10), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 5), true));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertFalse(actual.isSolved());
        assertNextStep(actual, 3, 5);
    }

    @Test
    public void GetNextStep_FailAndStepSizeOne_ReturnsAnswer() throws Exception {
        BisectStep currentStep = new BisectStep(0, 1);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 4), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 2), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 1), false));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertTrue(actual.isSolved());
        assertEquals(actual.getAnswer(), 0);
    }

    @Test
    public void GetNextStep_FailAndStepSizeTwo_ReturnsOlder() throws Exception {
        BisectStep currentStep = new BisectStep(0, 2);
        List<BisectFinishedBuild> history = new ArrayList<BisectFinishedBuild>();
        history.add(new BisectFinishedBuild(new BisectStep(0, 8), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 4), false));
        history.add(new BisectFinishedBuild(new BisectStep(0, 2), false));

        BisectDecision actual = BisectHelper.getNextStep(history, currentStep);

        assertFalse(actual.isSolved());
        assertNextStep(actual, 0, 1);
    }

    private void assertNextStep(BisectDecision actual, int left, int right) {
        assertBuildStep(actual.getNextStep(), left, right);
    }

    private void assertBuildStep(BisectStep actual, int left, int right) {
        assertNotNull(actual);
        assertEquals(actual.getLeft(), left);
        assertEquals(actual.getRight(), right);
    }
}