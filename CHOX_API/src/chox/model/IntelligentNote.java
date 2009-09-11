/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import chox.data.SecurityInfoProvider;

/**
 *
 * @author emmanuel
 */
public interface IntelligentNote {
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider);
    public String getNote();
}
