/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.notifications;

import chox.model.Claim;

/**
 *
 * @author emmanuel
 */
    public interface AnomalousCheck {

        boolean check(Claim c);

}
