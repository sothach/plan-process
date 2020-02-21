package org.seefin.plan.tests.booking;

import org.seefin.plan.process.definition.ProcessNode;
import org.springframework.stereotype.Component;

@Component
public class Selector
	implements ProcessNode<FlightBooking>
{		
	@Override
	public void execute ( FlightBooking context)
	{
		System.out.println ( "Selector state => " + context.getState());
		
		context.setFlightNumber ( new FlightNumber ( "EI", 232));
		// final state belongs to parent process
		context.setState ( BookingState.SELECTED);
		System.out.println ( "Selector state => " + context.getState());
	}

}
