package org.seefin.plan.tests.booking;

public enum SelectionState {
    NOT_FOUND, FOUND, NO_SELECTION;

    /**
     * answer true if this state represents a failure
     */
    public boolean isFailedState() {
        return this == NO_SELECTION;
    }
}