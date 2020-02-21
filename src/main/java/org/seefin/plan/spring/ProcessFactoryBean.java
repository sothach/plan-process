package org.seefin.plan.spring;

import org.seefin.plan.process.definition.ProcessGroup;
import org.seefin.plan.process.definition.ProcessNode;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * Factory that is capable to creating a <code>ProcessGroup</code> object from
 * properties set in a Spring context
 *
 * @author phillipsr
 */
@SuppressWarnings("rawtypes")
public class ProcessFactoryBean
        implements FactoryBean {
    private ProcessGroup<?> process;
    private List<Transition> transitions;

    /**
     * Set the process object that will be decorated and
     * returned by this bean factory
     *
     * @param process the top-level process object defined
     */
    public void
    setProcess(ProcessGroup<?> process) {
        assert getObjectType().isInstance(process) : "argument conforms to factory's type";
        this.process = process;
    }

    /**
     * Set the list of intermediate transition definitions that
     * will be used to populate the <code>transitions</code> map
     * of the process object
     *
     * @param transitions
     */
    public void
    setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * Answer with the process object, with its state fields
     * assigned from the values previously set in the factory,
     * providing the required type conversion
     */
    @Override
    public ProcessGroup<?>
    getObject()
            throws Exception {
        if (this.transitions != null && this.transitions.size() > 0) {
            for (Transition t : transitions) {
                addTransitionNode(process, t);
            }
        }
        return this.process;
    }

    /**
     * Add the transition specified by <code>node</code> to the supplied
     * <code>process</code>, setting the firing state and the action
     *
     * @param process to which transition to be added to
     * @param node    definition of the transition (state/action)
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private void
    addTransitionNode(ProcessGroup process, Transition node)
            throws InstantiationException, IllegalAccessException {
        Object action = node.getAction();
        if (action instanceof Class) { // action specifies a class to be instantiated
            Class<?> actionClass = (Class<?>) action;
            ProcessNode<?> processAction = (ProcessNode<?>) actionClass.newInstance();
            process.addNode(node.getState(), processAction);
        } else { // action is either a bean reference, or a sub-process definition
            process.addNode(node.getState(), (ProcessNode<?>) action);
        }
    }

    /**
     * Answer with the target object type that this factory will create
     */
    @Override
    public Class<?> getObjectType() {
        return ProcessGroup.class;
    }

    /**
     * Should Spring treat the constructed object as a singleton?
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}