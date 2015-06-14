package org.tkirill.teamcity.bisectPlugin;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BisectNextBuildHelperTest {
    @Test
    public void GetNextStep_EmptyIntervalStartsFromZero_ReturnsNull() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 0), true);
        assertNull(actual);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 0), false);
        assertNull(actual);
    }

    @Test
    public void GetNextStep_EmptyIntervalStartsFromNonZero_ReturnsNull() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(1, 1), true);
        assertNull(actual);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(1, 1), false);
        assertNull(actual);
    }

    @Test
    public void GetNextStep_IntervalLengthOneStartsFromZero_ReturnsNull() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 1), true);
        assertNull(actual);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 1), false);
        assertNull(actual);
    }

    @Test
    public void GetNextStep_IntervalLengthOneStartsFromNonZero_ReturnsNull() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(1, 2), true);
        assertNull(actual);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(1, 2), false);
        assertNull(actual);
    }

    @Test
    public void GetNextStep_IntervalLengthTwoStartsFromZeroAndSuccess_ReturnsNull() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 2), true);
        assertNull(actual);
    }

    @Test
    public void GetNextStep_IntervalLengthTwoStartsFromNonZeroAndSuccess_ReturnsNull() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(14, 16), true);
        assertNull(actual);
    }

    @Test
    public void GetNextStep_OddIntervalStartsFromZeroAndSuccess_ReturnsLater() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 3), true);
        assertBuildStep(actual, 2, 3);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 11), true);
        assertBuildStep(actual, 6, 11);
    }

    @Test
    public void GetNextStep_OddIntervalStartsFromNonZeroAndSuccess_ReturnsLater() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(5, 8), true);
        assertBuildStep(actual, 7, 8);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(14, 27), true);
        assertBuildStep(actual, 21, 27);
    }

    @Test
    public void GetNextStep_OddIntervalStartsFromZeroAndFail_ReturnsOlder() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 3), false);
        assertBuildStep(actual, 0, 1);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 17), false);
        assertBuildStep(actual, 0, 8);
    }

    @Test
    public void GetNextStep_OddIntervalStartsFromNonZeroAndFail_ReturnsOlder() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(6, 9), false);
        assertBuildStep(actual, 6, 7);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(21, 46), false);
        assertBuildStep(actual, 21, 33);
    }

    @Test
    public void GetNextStep_EvenIntervalStartsFromZeroAndSuccess_ReturnsLater() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 4), true);
        assertBuildStep(actual, 3, 4);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 36), true);
        assertBuildStep(actual, 19, 36);
    }

    @Test
    public void GetNextStep_EvenIntervalStartsFromNonZeroAndSuccess_ReturnsLater() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(6, 10), true);
        assertBuildStep(actual, 9, 10);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(12, 38), true);
        assertBuildStep(actual, 26, 38);
    }

    @Test
    public void GetNextStep_EvenIntervalStartsFromZeroAndFail_ReturnsOlder() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 4), false);
        assertBuildStep(actual, 0, 2);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(0, 42), false);
        assertBuildStep(actual, 0, 21);
    }

    @Test
    public void GetNextStep_EvenIntervalStartsFromNonZeroAndFail_ReturnsOlder() throws Exception {
        BisectStep actual = BisectBoundaryHelper.getNextStep(new BisectStep(5, 9), false);
        assertBuildStep(actual, 5, 7);

        actual = BisectBoundaryHelper.getNextStep(new BisectStep(16, 44), false);
        assertBuildStep(actual, 16, 30);
    }

    @Test
    public void FirstStep_Zero_ReturnsNull() throws Exception {
        BisectStep actual = BisectBoundaryHelper.firstStep(0);

        assertNull(actual);
    }

    @Test
    public void FirstStep_One_ReturnsFromZeroToOne() throws Exception {
        BisectStep actual = BisectBoundaryHelper.firstStep(1);

        assertBuildStep(actual, 0, 1);
    }

    @Test
    public void FirstStep_SomeNumber_ReturnsFromZeroToNumber() throws Exception {
        BisectStep actual = BisectBoundaryHelper.firstStep(21);

        assertBuildStep(actual, 0, 21);
    }

    @Test
    public void GetMid_EqualBoundaries_ReturnsTheBoundary() throws Exception {
        int actual = BisectBoundaryHelper.getMid(0, 0);
        assertEquals(actual, 0);

        actual = BisectBoundaryHelper.getMid(5, 5);
        assertEquals(actual, 5);
    }

    @Test
    public void GetMid_IntervalLengthOneStartsFromZero_ReturnsLeft() throws Exception {
        int actual = BisectBoundaryHelper.getMid(0, 1);
        assertEquals(actual, 0);
    }

    @Test
    public void GetMid_IntervalLengthOneStartsFromOdd_ReturnsLeft() throws Exception {
        int actual = BisectBoundaryHelper.getMid(13, 14);
        assertEquals(actual, 13);
    }

    @Test
    public void GetMid_IntervalLengthOneStartsFromEvenReturnsLeft() throws Exception {
        int actual = BisectBoundaryHelper.getMid(34, 35);
        assertEquals(actual, 34);
    }

    @Test
    public void GetMid_OddIntervalStartsFromZero_ReturnsMid() throws Exception {
        int actual = BisectBoundaryHelper.getMid(0, 3);
        assertEquals(actual, 1);

        actual = BisectBoundaryHelper.getMid(0, 11);
        assertEquals(actual, 5);
    }

    @Test
    public void GetMid_OddIntervalStartsFromOdd_ReturnsMid() throws Exception {
        int actual = BisectBoundaryHelper.getMid(11, 14);
        assertEquals(actual, 12);

        actual = BisectBoundaryHelper.getMid(31, 38);
        assertEquals(actual, 34);

        actual = BisectBoundaryHelper.getMid(7, 28);
        assertEquals(actual, 17);
    }

    @Test
    public void GetMid_OddIntervalStartsFromEven_ReturnsMid() throws Exception {
        int actual = BisectBoundaryHelper.getMid(14, 17);
        assertEquals(actual, 15);

        actual = BisectBoundaryHelper.getMid(6, 13);
        assertEquals(actual, 9);

        actual = BisectBoundaryHelper.getMid(12, 43);
        assertEquals(actual, 27);
    }

    @Test
    public void GetMid_EvenIntervalStartsFromZero_ReturnsUpperMid() throws Exception {
        int actual = BisectBoundaryHelper.getMid(0, 2);
        assertEquals(actual, 1);

        actual = BisectBoundaryHelper.getMid(0, 26);
        assertEquals(actual, 13);
    }

    @Test
    public void GetMid_EvenIntervalStartsFromOdd_ReturnsUpperMid() throws Exception {
        int actual = BisectBoundaryHelper.getMid(3, 5);
        assertEquals(actual, 4);

        actual = BisectBoundaryHelper.getMid(7, 15);
        assertEquals(actual, 11);

        actual = BisectBoundaryHelper.getMid(13, 35);
        assertEquals(actual, 24);
    }

    @Test
    public void GetMid_EvenIntervalStartsFromEven_ReturnsUpperMid() throws Exception {
        int actual = BisectBoundaryHelper.getMid(6, 8);
        assertEquals(actual, 7);

        actual = BisectBoundaryHelper.getMid(14, 16);
        assertEquals(actual, 15);

        actual = BisectBoundaryHelper.getMid(8, 32);
        assertEquals(actual, 20);
    }

    private void assertBuildStep(BisectStep actual, int left, int right) {
        assertNotNull(actual);
        assertEquals(actual.getLeft(), left);
        assertEquals(actual.getRight(), right);
    }
}