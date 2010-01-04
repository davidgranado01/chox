package idas.chox.web.actions;

import idas.chox.core.model.WebUser;
import idas.chox.web.security.WebUserService;

public class ForgetPasswordAction extends BaseAction {

    private String userName;
    private String email;
    private String confirmEmail;
    private WebUserService userDetailsService;

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String requestToResetPassword() {
        System.out.println(" >>>>>>>>>>>>> userName:" + this.userName + ":" + getUserName());
        System.out.println(" >>>>>>>>>>>>> email:" + this.email);


        //this.userName = "admin@ins.com";
        //this.email = "admin@ins.com";

        // VALIDATE IS VALID MATCH USER NAME OR EMAIL?
        System.out.println(" >>>>>>>>>>>>> webUserService:" + userDetailsService);
        WebUser user = userDetailsService.findByUserName(this.userName);
        System.out.println(" >>>>>>>>>>>>> user:" + user);

        return SUCCESS;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    /**
     * @return the userDetailsService
     */
    public WebUserService getUserDetailsService() {
        return userDetailsService;
    }

    /**
     * @param userDetailsService the userDetailsService to set
     */
    public void setUserDetailsService(WebUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
