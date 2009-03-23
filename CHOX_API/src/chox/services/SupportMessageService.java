/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.SupportMessage;

public interface SupportMessageService {
   public SupportMessage getObject(int id);
   public void updateObject(SupportMessage object);
}
