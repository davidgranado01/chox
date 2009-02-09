/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.WebUser;

/**
 *
 * @author Emmanuel
 */
public class UserAccountAction extends BaseAction {
    
    private WebUser webUser;
    
    @Override
    public String execute()
    {
        webUser = this.getAuthenticatedUser().getUser();
        return SUCCESS;
    }

    public

    WebUser getWebUser() {
        return webUser;
    }
    
    

}
