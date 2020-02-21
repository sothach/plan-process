package org.seefin.plan.process.definition;

/**
 * Specification of the required behavior for all process steps,
 * i.e., that they must all implement an <code>execute</code>
 * method that accepts a <i>ProcessContext</i> object
 * 
 * @author phillipsr
 *
 * @param <R> concrete type of context object
 */
public interface ProcessNode<R extends ProcessContext>
{
	/** Carry out an action on the supplied context */
	public void execute ( R context);
}
