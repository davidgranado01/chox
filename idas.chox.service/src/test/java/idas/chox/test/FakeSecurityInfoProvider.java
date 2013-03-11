package idas.chox.test;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author emmanuel
 */
public class FakeSecurityInfoProvider implements SecurityInfoProvider  {
    private boolean isCHO;
    private boolean isINS;
    private boolean isCHOXAdmin;
    private WebUser currentUser;

    public FakeSecurityInfoProvider()
    {
        WebUserRole webUserRole = new WebUserRole();
        webUserRole.setName(WebUserRole.ROLE_CH_OPR);
        Set roles = new HashSet();
        roles.add(webUserRole);
        
        currentUser = new WebUser();
        currentUser.setId(999);
        currentUser.setFirstName("UnitTest");
        currentUser.setLastName("User");
        currentUser.setVersion(1);
        currentUser.setRoles(roles);
        
        isCHOXAdmin = true;

        // SET CHORGANISATION
        Chorganisation chorganisation = new Chorganisation();
        chorganisation.setId(1006);
        chorganisation.setVersion(1);
        currentUser.setChorganisation(chorganisation);
    }

    /**
     * @return the isCHO
     */
    @Override
    public boolean getIsCHO() {
        return isCHO;
    }

    /**
     * @param isCHO the isCHO to set
     */
    public void setIsCHO(boolean isCHO) {
        this.isCHO = isCHO;
    }

    /**
     * @return the isINS
     */
    @Override
    public boolean getIsINS() {
        return isINS;
    }

    /**
     * @param isINS the isINS to set
     */
    public void setIsINS(boolean isINS) {
        this.isINS = isINS;
    }

    /**
     * @return the isCHOXAdmin
     */
    @Override
    public boolean getIsCHOXAdmin() {
        return isCHOXAdmin;
    }

    /**
     * @param isCHOXAdmin the isCHOXAdmin to set
     */
    public void setIsCHOXAdmin(boolean isCHOXAdmin) {
        this.isCHOXAdmin = isCHOXAdmin;
    }

    /**
     * @return the currentUser
     */
    @Override
    public WebUser getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(WebUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean isInRoleOf(String role) {
        return true;
    }





}
