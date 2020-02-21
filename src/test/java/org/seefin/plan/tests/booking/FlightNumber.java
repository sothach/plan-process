package org.seefin.plan.tests.booking;

public final class FlightNumber {
    private final String airline;
    private final int number;

    public FlightNumber(String airline, int number) {
        this.airline = airline;
        this.number = number;
    }

    @Override
    public String
    toString() {
        return airline + number;
    }
}
