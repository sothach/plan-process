package org.seefin.plan.tests.booking;

import java.util.Calendar;

import org.seefin.plan.process.definition.ProcessNode;
import org.springframework.stereotype.Component;

@Component
public class Validator
	implements ProcessNode<FlightBooking>
{		
	@Override
	public void execute ( FlightBooking context)
	{
		System.out.println ( "Validating request => " + context);
		
		if ( context.getTravelDate () == null || 
				Calendar.getInstance ().after ( context.getTravelDate ()))
		{
			System.err.println ( "Invalid travel date: " + context.getTravelDate ());
			context.setState ( BookingState.FAILED);
			return;
		}
		if ( context.getOrigin () == null || 
				context.getDestination () == null ||
				context.getOrigin () == context.getDestination ())
		{
			System.err.println ( "Invalid origin/destination: " 
					+ context.getOrigin () + "/" + context.getDestination ());
			context.setState ( BookingState.FAILED);
			return;
		}
		context.setState ( BookingState.VALID);
		System.out.println ( "Request state => " + context.getState());
	}

}
