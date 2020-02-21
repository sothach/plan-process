package org.seefin.plan.process.definition;

/**
 * Specification of the required behavior for all process steps,
 * i.e., that they must all implement an <code>execute</code>
 * method that accepts a <i>ProcessContext</i> object
 *
 * @param <R> concrete type of context object
 * @author phillipsr
 */
public interface ProcessNode<R extends ProcessContext> {
    /**
     * Carry out an action on the supplied context
     */
	void execute(R context);
}
