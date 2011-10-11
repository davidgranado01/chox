package idas.chox.test;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.WebUser;

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
        currentUser = new WebUser();
        //currentUser.setId(999);
        //currentUser.setFirstName("UnitTest");
        //currentUser.setLastName("User");

        currentUser.setId(5);
        currentUser.setFirstName("Admin");
        currentUser.setLastName("Chox");


        // SET CHORGANISATION
        Chorganisation chorganisation = new Chorganisation();
        chorganisation.setId(1006);
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
