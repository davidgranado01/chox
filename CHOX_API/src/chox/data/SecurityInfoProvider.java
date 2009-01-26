/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.data;

import chox.model.WebUser;

/**
 *
 * @author Emmanuel
 */
public interface SecurityInfoProvider {
    
  public WebUser getCurrentUSer();
  public boolean getIsCHO();
  public boolean getIsINS();
  public boolean getIsCHOXAdmin();

}
