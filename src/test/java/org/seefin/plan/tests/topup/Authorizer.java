package org.seefin.plan.tests.topup;

import org.seefin.plan.process.definition.ProcessNode;

public class Authorizer
        implements ProcessNode<TopupRequest> {
    @Override
    public void execute(TopupRequest context) {
        context.setState(TopupState.AUTHORIZED);
    }
}