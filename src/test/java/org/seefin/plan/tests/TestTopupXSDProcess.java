package org.seefin.plan.tests;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefin.plan.process.definition.ProcessGroup;
import org.seefin.plan.tests.topup.TopupRequest;
import org.seefin.plan.tests.topup.TopupState;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Configure a process in this class's default context
 *  (specified as customer xsd), and execute it
 * 
 * @author royp
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:org/seefin/plan/tests/TopupProcess-context.xml"})
public class TestTopupXSDProcess
{
	@Resource
	private ProcessGroup<TopupRequest> topupWorkflow;
	
	@Test
	public void
	testContextually()
	{
		TopupRequest context = new TopupRequest("+353861018765", BigDecimal.TEN);
		topupWorkflow.execute ( context);

		Assert.assertSame ( context.getState(), TopupState.NOTIFIED);
	}
	
}
