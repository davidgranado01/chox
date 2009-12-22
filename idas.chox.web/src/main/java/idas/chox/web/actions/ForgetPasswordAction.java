package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.struts2.interceptor.SessionAware;

public class ForgetPasswordAction extends BaseAction{

    private String userName;
    private String email;
    private String confirmEmail;

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

    public String requestToResetPassword(){

        System.out.println(" >>>>>>>>>>>>> userName:"+this.userName + ":"+getUserName());
        System.out.println(" >>>>>>>>>>>>> email:"+this.email);

        return SUCCESS;
    }

}
