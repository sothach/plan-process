package org.seefin.plan.process.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * A process group represents a one or more steps (nodes) in a process,
 * defined as transitions: an input context state and an action to perform
 * on the context object when it is in that state
 * <p/>
 * A process group can be defined via the Spring custom schema provided, for example:
 * <pre>
 * 	&lt;plan:process id="flightBooker"
 * 			      state-class="org.seefin.plan.tests.booking.BookingState" &gt;
 * 		&lt;plan:node state="OPEN" action-ref="validator" /&gt;
 * 		&lt;plan:node state="PAID" action="org.seefin.plan.tests.booking.BookingAction" /&gt;
 * 		&lt;plan:node state="BOOKED" action-ref="notifier" /&gt;
 * 	&lt;/plan:process&gt;
 * </pre>
 * The actions associated with each processing node may be defined in a number of ways:
 * <ul>
 * <li>A qualified classname of a ProcessNode subclass</li>
 * <li>A bean reference to a ProcessNode typed bean, defined in the context or via annotations</li>
 * <li>A nest process definition under the <code>node</code> element</li>
 * <li>A bean reference to ProcessGroup typed bean</li>
 * </ul>
 *
 * @param <R> the concrete type of the processing context object
 * @author phillipsr
 */
public final class ProcessGroup<R extends ProcessContext>
        implements ProcessNode<R> {
    private String id; // name of the process, for identification purposes
    private Object initialState; // if set, coerce context to this state at start of execution
    private Map<Object, ProcessNode<R>> transitions
            = new HashMap<Object, ProcessNode<R>>(); // definition of the processing steps

    /**
     * Execute this process by iteratively looking-up the next transition action
     * for the <code>context</code> object's current state, and executing it
     * <p/>
     * Execution terminates when the context is in a state that is not
     * mapped in the <code>transitions</code> set for the process
     *
     * @throws IllegalArgumentException if the supplied context is null
     * @throws IllegalStateException    if the context's state is or becomes null
     */
    @Override
    public void
    execute(final R context) {
        if (context == null) {
            throw new IllegalArgumentException("Context may not be null");
        }
        if (initialState != null) {
            context.setState(initialState);
        }
        // select the appropriate transition from the current state:
        do {
            Object currentState = context.getState();
            if (currentState == null) {
                throw new IllegalStateException("Context state may not be null: " + context);
            }
            final ProcessNode<R> node = transitions.get(currentState);
            if (node == null) {
                break; // state not mapped: processing loop ends
            }
            node.execute(context);
            currentState = context.getState();
        }
        while (true);
    }

    /**
     * Answer with the name of the process
     */
    public String getId() {
        return id;
    }

    /**
     * Set the name of the process to <code>id</code>
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the initial state that a context object should be coerced into;
     * this would generally only be used when a sub-process uses a different
     * set of states than the parent, so the context state maps to a sub-process
     * transition
     *
     * @param initialState state to initialize context with
     */
    public void setInitialState(Object initialState) {
        this.initialState = initialState;
    }

    /**
     * Define the set of transitions that comprise this process's definition
     *
     * @param transitions state/action mappings representing the process transitions
     */
    public void
    setTransitions(Map<Object, ProcessNode<R>> transitions) {
        this.transitions = transitions;
    }

    /**
     * Add a transition node that specifies:<br/>
     * <i>When in</i> <code>state</code> <i>execute</i> <code>step</code></i>
     *
     * @param state state that triggers the transition
     * @param step  the action to be performed
     */
    public void
    addNode(Object state, ProcessNode<R> step) {
        transitions.put(state, step);
    }

    @Override
    public String
    toString() {
        return getId() + " transitions=" + (transitions == null ? "<not set>" : transitions)
                + " init-state=" + (initialState == null ? "<not set>" : initialState);
    }

}
