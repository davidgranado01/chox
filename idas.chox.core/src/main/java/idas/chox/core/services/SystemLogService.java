/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.SystemLog;

public interface SystemLogService {

    SystemLog getObject(int id);

    public void logSystemLog(String actionId, String msg, boolean status, int iLevel);

    public String getListingLogMsg(Integer iCount, String sFilter);

    public String getObjectActionLogMsg(String sAction, String sFilter);
}
