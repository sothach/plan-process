package org.seefin.plan.tests;

import org.junit.Assert;
import org.junit.Test;
import org.seefin.plan.process.definition.ProcessGroup;
import org.seefin.plan.process.definition.ProcessNode;
import org.seefin.plan.tests.topup.*;

import java.math.BigDecimal;

/**
 * Create a process programmatically and execute it
 *
 * @author royp
 */
public class TestProcessGraph {
    @Test
    public void
    testProgrammaticProcess() {
        ProcessGroup<TopupRequest> topupProcess = new ProcessGroup<TopupRequest>();
        ProcessNode<TopupRequest> validation = new Validator();
        ProcessNode<TopupRequest> authorization = new Authorizer();
        ProcessNode<TopupRequest> payer = new Payer();
        ProcessNode<TopupRequest> applyCredit = new Recharger();
        ProcessNode<TopupRequest> notifier =
                new ProcessNode<TopupRequest>() {
                    @Override
                    public void execute(TopupRequest context) {
                        context.setState(TopupState.NOTIFIED);
                    }
                };

        topupProcess.addNode(TopupState.CREATED, validation);
        topupProcess.addNode(TopupState.VALIDATED, authorization);
        topupProcess.addNode(TopupState.AUTHORIZED, payer);
        topupProcess.addNode(TopupState.PAID, applyCredit);
        topupProcess.addNode(TopupState.CREDIT_APPLIED, notifier);

        TopupRequest context = new TopupRequest("+353861018765", BigDecimal.TEN);
        context.setState(TopupState.CREATED);
        topupProcess.execute(context);

        Assert.assertSame(context.getState(), TopupState.NOTIFIED);
    }

}
