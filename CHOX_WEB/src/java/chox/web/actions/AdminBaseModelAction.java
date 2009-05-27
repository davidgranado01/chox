package chox.web.actions;

import chox.services.SystemLogService;

public abstract class AdminBaseModelAction extends BaseAction {
    
    private SystemLogService systemLogService;
    private String actionResult;

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public SystemLogService getSystemLogService() {
        return systemLogService;
    }

    public void setSystemLogService(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }    
    
}
