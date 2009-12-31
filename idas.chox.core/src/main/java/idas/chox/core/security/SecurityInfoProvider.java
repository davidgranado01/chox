/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.security;

import idas.chox.core.model.WebUser;

/**
 *
 * @author Emmanuel
 */
public interface SecurityInfoProvider {

    public WebUser getCurrentUser();

    public boolean getIsCHO();

    public boolean getIsINS();

    public boolean getIsCHOXAdmin();

    public boolean isInRoleOf(String role);
}
