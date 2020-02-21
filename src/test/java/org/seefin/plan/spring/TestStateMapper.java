package org.seefin.plan.spring;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class TestStateMapper {
    private static final Class<?> target = TestSet.class;
    private static final Class<?> notAnEnum = EmptyState.class;

    @Test
    public void
    testMapper() {
        Set<Object> states = StateMapper.labelsToStateSet(target, "STATE1,STATE2, STATE3");

        Assert.assertTrue(states.contains(TestSet.STATE1));
        Assert.assertTrue(states.contains(TestSet.STATE2));
        Assert.assertTrue(states.contains(TestSet.STATE3));
    }

    @Test
    public void
    testMapState() {
        Object state = StateMapper.mapState(target, "STATE2");

        Assert.assertSame(state, TestSet.STATE2);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void
    testMapMissingLabel() {
        StateMapper.mapState(target, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void
    testMapNonEnum() {
        StateMapper.mapState(notAnEnum, "STATE2");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void
    testEmptyList() {
        StateMapper.labelsToStateSet(target, ",");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void
    testNullList() {
        StateMapper.labelsToStateSet(target, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void
    testNullStateClass() {
        StateMapper.labelsToStateSet(null, "STATE1,STATE2, STATE3");
    }

    @Test
    public void
    testConvertToSet() {
        final Map<String, Object> stateMap = StateMapper.getStates(target);
        Set<Object> states = StateMapper.convertToStateSet(stateMap, "STATE1,STATE2, STATE3");

        Assert.assertTrue(states.contains(TestSet.STATE1));
        Assert.assertTrue(states.contains(TestSet.STATE2));
        Assert.assertTrue(states.contains(TestSet.STATE3));
    }

    enum TestSet {
        STATE1, STATE2, STATE3;

        public boolean isFailedState() {
            return false;
        }
    }

    static class EmptyState {
        public boolean isFailedState() {
            return false;
        }
        // state left intentionally empty
    }

}
