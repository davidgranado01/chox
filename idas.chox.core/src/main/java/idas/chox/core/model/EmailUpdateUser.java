package idas.chox.core.model;

import java.io.Serializable;

public class EmailUpdateUser extends Entity implements Serializable {

    private String email;
    private String userName;
    private String password;
    private boolean ecdUpdate;
    private boolean refUpdate;
    private boolean penaltyUpdate;

    public boolean isEcdUpdate() {
        return ecdUpdate;
    }

    public void setEcdUpdate(boolean ecdUpdate) {
        this.ecdUpdate = ecdUpdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isPenaltyUpdate() {
        return penaltyUpdate;
    }

    public void setPenaltyUpdate(boolean penaltyUpdate) {
        this.penaltyUpdate = penaltyUpdate;
    }

    public boolean isRefUpdate() {
        return refUpdate;
    }

    public void setRefUpdate(boolean refUpdate) {
        this.refUpdate = refUpdate;
    }

}
