package org.seefin.plan.tests.topup;

import org.seefin.plan.process.definition.ProcessNode;
import org.springframework.stereotype.Component;

@Component
public class Payer
	implements ProcessNode<TopupRequest>
{
	@Override
	public void execute ( TopupRequest context)
	{
		context.setState (  TopupState.PAID);
	}
}