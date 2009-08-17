/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.data;

import chox.model.WebUser;

/**
 *
 * @author Emmanuel
 * for testing purpose only
 */
public class FakeSecurityInfoProvider implements SecurityInfoProvider {

    public WebUser getCurrentUser() {
        WebUser fakeUser = new WebUser();
        fakeUser.setId(999);
        return fakeUser;
    }
      public boolean getIsCHO(){return false;}
  public boolean getIsINS(){return false;}
  public boolean getIsCHOXAdmin(){return false;}
}
