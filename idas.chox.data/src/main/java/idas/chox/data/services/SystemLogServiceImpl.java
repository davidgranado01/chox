package idas.chox.data.services;

import idas.chox.core.model.SystemLog;
import idas.chox.core.services.SystemLogService;

public class SystemLogServiceImpl extends SecureDataService implements SystemLogService {

    public static Integer iLogLevel = 2;

    public SystemLogServiceImpl() {
    }

    public String getListingLogMsg(Integer iCount, String sFilter) {
        String sOutput = iCount + " of record(s) found.";

        if (sFilter.length() > 0) {
            sOutput = sOutput + " " + sFilter;
        }

        return sOutput;
    }

    public String getObjectActionLogMsg(String sAction, String sFilter) {

        String sOutput = "Record has been modified (" + sAction + ").";

        if (sFilter.length() > 0) {
            sOutput = sOutput + " " + sFilter;
        }

        return sOutput;
    }

    public void logSystemLog(String actionId, String msg, boolean status, int iLevel) {

        if (iLevel >= iLogLevel) {

            try {

                SystemLog systemLog = new SystemLog();
                systemLog.setActionId(actionId);
                systemLog.setMessage(msg);

                if (status) {
                    systemLog.setStatus("S");
                } else {
                    systemLog.setStatus("F");
                }

                save(systemLog);

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public SystemLog getObject(int id) {
        return (SystemLog) get(SystemLog.class, id);
    }
}
