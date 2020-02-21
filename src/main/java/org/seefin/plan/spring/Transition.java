package org.seefin.plan.spring;

/**
 * Intermediate value holder used to construction transitions
 * from a Spring bean definition
 *
 * @author phillipsr
 */
public class Transition {
    private Object state;
    private Object action;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

}
