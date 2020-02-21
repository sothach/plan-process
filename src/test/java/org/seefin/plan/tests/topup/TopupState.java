package org.seefin.plan.tests.topup;

public enum TopupState
{
	CREATED, VALIDATED, AUTHORIZED, PAID, ELIGIBLE, CREDIT_APPLIED, NOTIFIED, FAILED;
	public boolean isFailedState () { return this == FAILED; }
}