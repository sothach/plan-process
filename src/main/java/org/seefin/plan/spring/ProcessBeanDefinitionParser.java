package org.seefin.plan.spring;

import org.seefin.plan.process.definition.ProcessGroup;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parse a <code>&lt;plan:process&gt;</code> definition from a Spring context file,
 * creating a bean definition that can be used by Spring to instantiate a process bean
 *
 * @author phillipsr
 */
public class ProcessBeanDefinitionParser
        extends AbstractBeanDefinitionParser {
    private static final String STATE_ATTR = "state";
    private static final String STATE_CLASS_ATTR = "state-class";
    @SuppressWarnings("unused")
    private static final String STATE_CLASS_REF_ATTR = "state-class-ref";
    private static final String INITIAL_STATE_ATTR = "initial-state";
    private static final String ACTION_ATTR = "action";
    private static final String ACTION_REF_ATTR = "action-ref";
    private static final String TRANSITIONS = "transitions";
    private static final String NODE = "node";
    private static final String PROCESS = "process";

    /**
     * Template method called when plan:process bean definition needs to be parsed
     */
    @Override
    protected AbstractBeanDefinition
    parseInternal(Element element, ParserContext parserContext) {
        AbstractBeanDefinition processBean = parseProcess(element);
        parserContext.getRegistry().registerBeanDefinition(element.getAttribute(ID_ATTRIBUTE), processBean);
        return processBean;
    }

    /**
     * Parse the process definition in <code>element</code>
     *
     * @param element the &lt;process&gt; defining a Plan process group
     * @return bean definition of the process group
     */
    private AbstractBeanDefinition
    parseProcess(Element element) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ProcessFactoryBean.class);
        BeanDefinitionBuilder processBuilder = this.parseProcessDefinition(element, factory);
        factory.addPropertyValue(PROCESS, processBuilder.getBeanDefinition());
        return factory.getBeanDefinition();
    }

    /**
     * Parse a <code>&lt;plan:process&gt;</code> definition element, returning a builder
     * to be used when instantiating the bean for the application context
     *
     * @param element
     * @param factory
     * @return
     */
    private BeanDefinitionBuilder
    parseProcessDefinition(Element element, BeanDefinitionBuilder factory) {
        BeanDefinitionBuilder result
                = BeanDefinitionBuilder.rootBeanDefinition(ProcessGroup.class);
        result.addPropertyValue("id", element.getAttribute(ID_ATTRIBUTE));

        List<Element> childElements = DomUtils.getChildElementsByTagName(element, NODE);
        if (childElements == null || childElements.size() == 0) {
            return result;
        }

        Class<?> stateClass = getStateClass(element);

        final Map<String, Object> stateMap = StateMapper.getStates(stateClass);
        String initStateName = element.getAttribute(INITIAL_STATE_ATTR);
        if (initStateName != null && initStateName.length() > 0) {
            result.addPropertyValue("initialState", stateMap.get(initStateName));
        }

        List<AbstractBeanDefinition> transitions = parseProcessNodes(childElements, stateClass, stateMap);
        factory.addPropertyValue(TRANSITIONS, transitions);
        return result;
    }

    /**
     * get state-class attribute, and valid that the class exists and implements 'State'<p/>
     * Note: class-loading classes from arbitrary packages can be problematic when a process
     * is instantiated in an OSGi bundle (class not in Named Class Space): solution is to
     * create a state class instance in the spring context and inject it via 'state-class-ref'
     * property instead
     *
     * @param element
     * @return
     */
    private Class<?>
    getStateClass(Element element) {
        String stateClassname = element.getAttribute(STATE_CLASS_ATTR);
        try {
            //stateClass = (Class<State>)Class.forName ( stateClassname);
            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            return Class.forName(stateClassname, true, tccl);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("could not find state class specified: "
                    + STATE_CLASS_ATTR + "=\"" + stateClassname + "\"", e);
        }
    }

    /**
     * Answer with a list of transitions from the <code>&lt;plan:node&gt;</code> elements that
     * form the process definition
     *
     * @param nodeElements the xml node definition list
     * @param stateClass   the parent process's state class, default for sub-processes
     * @param stateMap     a mapping of the parent process's state values
     * @return a list of bean definitions representing the process steps
     */
    private List<AbstractBeanDefinition>
    parseProcessNodes(List<Element> nodeElements, Class<?> stateClass, Map<String, Object> stateMap) {
        ManagedList<AbstractBeanDefinition> result
                = new ManagedList<AbstractBeanDefinition>(nodeElements.size());
        Set<Object> mappedStates = new HashSet<Object>();
        for (Element node : nodeElements) {
            Object state = stateMap.get(node.getAttribute(STATE_ATTR));
            if (state == null) {
                throw new IllegalStateException("State [" + node.getAttribute(STATE_ATTR)
                        + "] not defined in process's state-class: " + stateMap.keySet());
            }
            if (mappedStates.contains(state)) {
                throw new IllegalStateException("State [" + state + "] has already been mapped to an action");
            }
            mappedStates.add(state);

            // build an intermediate transition bean, for the factory bean to use in defining the transition
            BeanDefinitionBuilder child = BeanDefinitionBuilder.rootBeanDefinition(Transition.class);
            child.addPropertyValue(STATE_ATTR, state);

            boolean actionSet = setActionObject(node, child);
            if (actionSet == false) { // no action attribute: assume sub-process definition present
                child.addPropertyValue(ACTION_ATTR, getSubProcess(node, stateClass));
            }

            result.add(child.getBeanDefinition());
        }
        return result;
    }


    /**
     * Look for a <code>&lt;plan:process&gt;</code> element under the supplied
     * <code>&lt;plan:node&gt;</code> element, and return its bean definition
     * to be set as the ProcessNode action
     *
     * @param nodeElement <code>&lt;plan:node&gt;</code>
     * @param stateClass  parent process state class, to use as default
     * @return the sub-process bean definition
     */
    private AbstractBeanDefinition
    getSubProcess(Element nodeElement, Class<?> stateClass) {
        List<Element> nodes = DomUtils.getChildElementsByTagName(nodeElement, "process");
        if (nodes.size() != 1) {
            throw new IllegalStateException("expected node action to be defined by a (single) process element");
        }
        Element processNode = nodes.get(0);
        // if the sub-process does not define a state class, inherit the class from the parent process
        String stateClassName = processNode.getAttribute(STATE_CLASS_ATTR);
        if (stateClassName == null || stateClassName.isEmpty()) {
            processNode.setAttribute(STATE_CLASS_ATTR, stateClass.getCanonicalName());
        }
        return parseProcess(processNode);
    }

    /**
     * Attempt to read the action definition from the supplied <code>&lt;plan:node&gt;</code>
     * element, either  from a 'action' attribute that specifies a ProcessNode sub-class to be
     * instantiated, or a 'action-ref' attribute, that refers to a bean by its ID, that should
     * also be a ProcessNode sub-class;</br>
     * Sets the action in the supplied <code>node</code>
     *
     * @param nodeElement XML element representing the node
     * @param node        builder object that will define the action node
     * @return true if the action was set, false if no appropriate attribute present,
     * assume that action will be specified as a sub-process
     */
    private boolean
    setActionObject(Element nodeElement, BeanDefinitionBuilder node) {
        String actionName = nodeElement.getAttribute(ACTION_ATTR);
        // check is 'action=class-name' is specified
        if (actionName != null && actionName.isEmpty() == false) { // action class specified, check that it is a valid class name
            try {
                Class<?> processAction = ProcessBeanDefinitionParser.class.getClassLoader().loadClass(actionName);
                node.addPropertyValue(ACTION_ATTR, processAction);
            } catch (Exception e) {
                throw new IllegalStateException("could load action class for action: " + actionName, e);
            }
            return true;
        }
        actionName = nodeElement.getAttribute(ACTION_REF_ATTR);
        if (actionName != null && actionName.isEmpty() == false) { // set the action as a property reference
            node.addPropertyReference(ACTION_ATTR, nodeElement.getAttribute(ACTION_REF_ATTR));
            return true;
        }
        return false; // no action attribute set
    }

}