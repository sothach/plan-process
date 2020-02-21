package org.seefin.plan.tests.booking;

import org.seefin.plan.process.definition.ProcessContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightBooking
        implements ProcessContext {
    private Object state;
    private int attempts = 3;
    private IATAAirportCode origin, destination;
    private Date travelDate;
    private FlightNumber flightNumber;

    public FlightBooking(Date data, IATAAirportCode from, IATAAirportCode to) {
        this.travelDate = data;
        this.origin = from;
        this.destination = to;
    }

    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void setState(Object state) {
        this.state = state;
    }

    public boolean isSelected() {
        return attempts <= 0;
    }

    public void attemptSelection() {
        attempts--;
    }

    public IATAAirportCode getOrigin() {
        return origin;
    }

    public void setOrigin(IATAAirportCode origin) {
        this.origin = origin;
    }

    public IATAAirportCode getDestination() {
        return destination;
    }

    public void setDestination(IATAAirportCode destination) {
        this.destination = destination;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public FlightNumber getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(FlightNumber flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public String
    toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return "Booking flight from: " + origin + " to " + destination
                + " on " + sdf.format(travelDate) + ", state=" + state
                + (flightNumber != null ? " flight=" + flightNumber : "");
    }
}