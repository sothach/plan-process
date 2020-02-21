package org.seefin.plan.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefin.plan.process.definition.ProcessGroup;
import org.seefin.plan.tests.topup.TopupRequest;
import org.seefin.plan.tests.topup.TopupState;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Configure a process in this class's default context, and execute it
 *
 * @author royp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestTopupProcess {
    @Resource
    private ProcessGroup<TopupRequest> topupProcessor;

    @Test
    public void
    testContextually() {
        TopupRequest context = new TopupRequest("+353861018765", BigDecimal.TEN);
        context.setState(TopupState.CREATED);
        topupProcessor.execute(context);

        Assert.assertSame(context.getState(), TopupState.NOTIFIED);
    }


    @Test(expected = java.lang.IllegalStateException.class)
    public void
    testNullState() {
        topupProcessor.execute(new TopupRequest("+353861018765", BigDecimal.TEN));
    }

}
