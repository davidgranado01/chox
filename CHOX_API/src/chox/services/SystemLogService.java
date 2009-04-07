/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.SystemLog;

public interface SystemLogService {
    SystemLog getObject(int id);
    public void logSystemLog(String actionId, String msg, boolean status);
}
