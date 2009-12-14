/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.xmlValidation;

/**
 *
 * @author Carlson
 */
public class NodeRuleModel {
    
    protected String nodeName;
    protected String nodeDesc;
    protected String dataType;
    protected boolean dataMandatory;
    protected String regExp;

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public boolean isDataMandatory() {
        return dataMandatory;
    }

    public void setDataMandatory(String dataMandatory) {
        this.dataMandatory = false;
        if(dataMandatory.trim().toLowerCase().equalsIgnoreCase("t")){
            this.dataMandatory = true;
        }
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }
    
    
    

}
