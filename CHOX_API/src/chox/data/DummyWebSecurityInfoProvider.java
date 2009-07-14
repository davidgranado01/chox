/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.data;

import chox.model.WebUser;


public class DummyWebSecurityInfoProvider {


    
    public DummyWebSecurityInfoProvider() {
    }
    
    public WebUser getCurrentUser() {
        
        WebUser user = new WebUser();
        user.setEmail("admin@chox.com");
        user.setId(999);
        
        return user;
    }

    public boolean getIsCHO() {
        return true;
    }

    public boolean getIsINS() {
        return true;
    }

    public boolean getIsCHOXAdmin() {
        return true;
    }

}
