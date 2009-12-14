/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.SupportMessage;

public interface SupportMessageService {

    public SupportMessage getObject(int id);

    public void updateObject(SupportMessage object);
}
