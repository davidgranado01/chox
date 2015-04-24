package idas.chox.core.model;

import java.io.Serializable;

/**
 *
 * @author John
 */
public class BreRules  implements Serializable {
    private Integer id;
    private String ruleName;
    private String ruleDescription;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
