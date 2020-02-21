package org.seefin.plan.process.definition;

import java.io.Serializable;

/**
 * The process context is typically the object being processed, such as
 * a request of some sort; this interface requires that any context must
 * be able to respond with its current state and have its state set
 * <p/>
 * The State class should be implemented by an enum: initially, state was
 * represented by a <i>State</i> interface (defined in this package), but
 * in practice this created undesirable dependencies between models and
 * this project
 *
 * @author phillipsr
 */
public interface ProcessContext extends Serializable {
    /**
     * answer with the current summary state of the processing context
     */
    Object getState();

    /**
     * Set the summary state of the processing context
     *
     * @param state to be set
     */
    void setState(Object state);
}
