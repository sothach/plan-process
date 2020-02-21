package org.seefin.plan.tests.booking;

public enum BookingState {
    OPEN, VALID, SELECTED, PAID, BOOKED, FAILED, COMPLETE;

    /**
     * answer true if this state represents a failure
     */
    public boolean isFailedState() {
        return this == FAILED;
    }
}