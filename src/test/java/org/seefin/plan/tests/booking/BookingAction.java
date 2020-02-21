package org.seefin.plan.tests.booking;

import org.seefin.plan.process.definition.ProcessNode;

public class BookingAction
        implements ProcessNode<FlightBooking> {
    @Override
    public void execute(FlightBooking context) {
        BookingState state = (BookingState) context.getState();

        switch (state) {
            case SELECTED:
                context.setState(BookingState.PAID);
                break;
            case PAID:
                context.setState(BookingState.BOOKED);
                break;
            case BOOKED:
                context.setState(BookingState.COMPLETE);
                break;
            default:
                // do nothing
        }
        System.out.println("Booking " + state + " => " + context.getState());
    }

}
