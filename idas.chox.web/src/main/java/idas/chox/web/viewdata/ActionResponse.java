package idas.chox.web.viewdata;

import java.util.ArrayList;
import java.util.List;

public class ActionResponse {

    public static String RESULT_TYPE_NEW_ID = "New";
    public static String RESULT_TYPE_MESSAGE = "Message";
     public static String RESULT_TYPE_YESNO = "YesNo";

    private List<String> errors;
    private String resultType;
    private Object result;

    public ActionResponse()
    {
        errors = new ArrayList<String>() {};
    }

    public Boolean getIsValid()
    {
        return getErrors().isEmpty();
    }  

    /**
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }

    public void AddError(final String errorMessage)
    {
        if(errorMessage!=null){
            errors.add(errorMessage);
        }
    }

    public void AssignResult(final String type,final Object result)
    {
        setResultType(type);
        setResult(result);
    }

    public void AssignNewIdResult(int id)
    {
        setResultType(RESULT_TYPE_NEW_ID);
        setResult(id);
    }

    public void AssignMessageResult(String message)
    {
        setResultType(RESULT_TYPE_MESSAGE);
        setResult(message);
    }

    public void AssignYesNoResult(Boolean result)
    {
        setResultType(RESULT_TYPE_YESNO);
        setResult(result ? "yes" : "no");
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }



}
