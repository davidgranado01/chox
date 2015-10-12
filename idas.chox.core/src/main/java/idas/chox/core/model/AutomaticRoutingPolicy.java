package idas.chox.core.model;

import java.io.Serializable;

public class AutomaticRoutingPolicy extends Entity implements Serializable{

    private String expression;
    private Insurer insurer;
    private Workgroup workgroup;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
    }


}
