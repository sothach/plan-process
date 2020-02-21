package org.seefin.plan.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefin.plan.process.definition.ProcessGroup;
import org.seefin.plan.tests.booking.BookingState;
import org.seefin.plan.tests.booking.FlightBooking;
import org.seefin.plan.tests.booking.IATAAirportCode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BookingTest {
    @Resource
    private ProcessGroup<FlightBooking> flightBooker;

    @Test
    public void
    testContextConfig()
            throws ParseException {
        System.out.println("Process : " + flightBooker);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        FlightBooking request = new FlightBooking(
                sdf.parse("01/01/2012"), IATAAirportCode.DUB, IATAAirportCode.WAT);
        request.setState(BookingState.OPEN);
        flightBooker.execute(request);

        System.out.println(request);
        Assert.assertNotNull(request.getFlightNumber());
        Assert.assertSame(request.getState(), BookingState.COMPLETE);
    }

    @Test
    public void
    badBookingTest()
            throws ParseException {
        System.out.println("Process : " + flightBooker);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        FlightBooking request = new FlightBooking(
                sdf.parse("01/01/2012"), IATAAirportCode.DUB, IATAAirportCode.DUB);
        request.setState(BookingState.OPEN);
        flightBooker.execute(request);

        System.out.println(request);
        Assert.assertSame(request.getState(), BookingState.FAILED);
    }
}
