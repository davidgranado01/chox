package scsbre.engine;

public class RuleEvaluationResult {

    private String message;
    private Boolean wasPassed;
    private Boolean isVisibleToCHO;

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public boolean getWasPassed() {
        return wasPassed;
    }

    public void setWasPassed(boolean passed) {
        wasPassed = passed;
    }

    public boolean getIsVisibleToCHO() {
        return isVisibleToCHO;
    }

    public void setIsVisibleToCHO(boolean vtcho) {
        isVisibleToCHO = vtcho;
    }
}
