package idas.chox.web.actions;

import com.opensymphony.xwork2.ActionSupport;
import idas.chox.core.model.WebUser;
import idas.chox.web.security.WebUserService;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

public class ForgetPasswordAction extends BaseAction{

    private String userName;
    private String email;
    private String confirmEmail;
    private WebUserService webUserService;

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

    public void setWebUserService(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    public WebUserService getWebUserService() {
        return webUserService;
    }

    public String requestToResetPassword(){
        
        // System.out.println(" >>>>>>>>>>>>> userName:"+this.userName + ":"+getUserName());
        // System.out.println(" >>>>>>>>>>>>> email:"+this.email);

        this.userName = "admin@ins.com";
        this.email = "admin@ins.com";

        // VALIDATE IS VALID MATCH USER NAME OR EMAIL?
        System.out.println(" >>>>>>>>>>>>> webUserService:"+webUserService);
        WebUser user = webUserService.findByUserName(this.userName);
        System.out.println(" >>>>>>>>>>>>> user:"+user);
        
        return SUCCESS;
    }

    @Override
    public String execute() {

        try {

        System.out.println(" >>>>>>>>>>>>> userName:"+this.userName + ":"+getUserName());
        System.out.println(" >>>>>>>>>>>>> email:"+this.email);
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }
}
