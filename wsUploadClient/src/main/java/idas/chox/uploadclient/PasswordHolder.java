/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.uploadclient;

/**
 *
 * @author seeni
 */
public class PasswordHolder {
    
    private static String password;
    private static String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        PasswordHolder.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        PasswordHolder.password = password;
    }
    
}
