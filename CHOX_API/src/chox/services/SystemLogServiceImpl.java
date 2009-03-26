package chox.services;

import chox.model.SystemLog;

public class SystemLogServiceImpl extends SecureDataService implements SystemLogService{
    
    public SystemLogServiceImpl() {
    }
    
    public void logSystemLog(String actionId, String msg, boolean status){
        
        SystemLog systemLog = new SystemLog();
        systemLog.setActionId(actionId);
        systemLog.setMessage(msg);
        
        if(status){
            systemLog.setStatus("S");
        }else{
            systemLog.setStatus("F");
        }
        save(systemLog);
    } 

    public SystemLog getObject(int id) {
        return (SystemLog) get(SystemLog.class, id);
    }  
    
}
