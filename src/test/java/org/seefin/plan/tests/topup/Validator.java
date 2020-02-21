package org.seefin.plan.tests.topup;

import org.seefin.plan.process.definition.ProcessNode;
import org.springframework.stereotype.Component;

@Component
public class Validator
	implements ProcessNode<TopupRequest>
{
	@Override
	public void
	execute ( TopupRequest context)
	{
		if ( context.getMsisdn () == null)
		{
			context.setState ( TopupState.FAILED);
		}
		if ( context.getAmount () == null || context.getAmount().longValue () == 0)
		{
			context.setState ( TopupState.FAILED);
		}

		context.setState ( TopupState.VALIDATED);
	}
}