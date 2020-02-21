package org.seefin.plan.tests.topup;

import org.seefin.plan.process.definition.ProcessContext;

import java.math.BigDecimal;

public class TopupRequest
        implements ProcessContext {
    private Object state;
    private String msisdn;
    private BigDecimal amount;

    public TopupRequest(String msisdn, BigDecimal amount) {
        this.msisdn = msisdn;
        this.amount = amount;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void setState(Object state) {
        this.state = state;
    }

}