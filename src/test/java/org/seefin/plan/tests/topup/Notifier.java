package org.seefin.plan.tests.topup;

import org.seefin.plan.process.definition.ProcessNode;
import org.springframework.stereotype.Component;

@Component
public class Notifier
	implements ProcessNode<TopupRequest>
{
	@Override
	public void execute ( TopupRequest context)
	{
		context.setState ( TopupState.NOTIFIED);
	}
}