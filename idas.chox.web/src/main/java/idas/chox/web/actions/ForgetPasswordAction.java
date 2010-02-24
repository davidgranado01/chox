package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;
import idas.chox.web.security.WebUserService;

public class ForgetPasswordAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(ForgetPasswordAction.class);

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
        LOG.debug("userName: {} : {}", this.userName, getUserName());
//        System.out.println(" >>>>>>>>>>>>> userName:" + this.userName + ":" + getUserName());
//        System.out.println(" >>>>>>>>>>>>> email:" + this.email);
        LOG.debug("email: {}", this.email);

        // VALIDATE IS VALID MATCH USER NAME OR EMAIL?
//        System.out.println(" >>>>>>>>>>>>> webUserService:" + userDetailsService);
        LOG.debug("webUserService: {}", userDetailsService);
        WebUser user = userDetailsService.findByUserName(this.userName);
//        System.out.println(" >>>>>>>>>>>>> user:" + user);
        LOG.debug("user: {}", user);

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
