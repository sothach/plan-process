package org.seefin.plan.tests.booking;

import org.seefin.plan.process.definition.ProcessNode;
import org.springframework.stereotype.Component;

@Component
public class Notifier
        implements ProcessNode<FlightBooking> {
    @Override
    public void execute(FlightBooking request) {
        System.out.println("Notifying request => " + request);

        if (request.getState() != BookingState.BOOKED) {
            System.err.println("ERROR: state must be " + BookingState.BOOKED);
            request.setState(BookingState.FAILED);
            return;
        }

        System.out.println("NOTIFICATION: Booking Succeeded: " + request);
        request.setState(BookingState.COMPLETE);
        System.out.println("Request state => " + request.getState());
    }

}
