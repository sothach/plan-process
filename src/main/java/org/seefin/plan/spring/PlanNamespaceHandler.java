package org.seefin.plan.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class PlanNamespaceHandler
        extends NamespaceHandlerSupport {
    @Override
    public void
    init() {
        registerBeanDefinitionParser("process", new ProcessBeanDefinitionParser());
    }
}