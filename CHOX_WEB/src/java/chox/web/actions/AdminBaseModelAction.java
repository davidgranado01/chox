package chox.web.actions;

import chox.services.SystemLogService;

public abstract class AdminBaseModelAction extends BaseAction {
    
    private SystemLogService systemLogService;


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
