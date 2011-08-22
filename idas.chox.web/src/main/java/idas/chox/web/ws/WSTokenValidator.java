package idas.chox.web.ws;

import idas.chox.service.admin.AdminUserService;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
//import org.apache.ws.security.WSConstants;
//import org.apache.ws.security.WSSConfig;
//import org.apache.ws.security.validate.Credential;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.util.Base64;
import org.apache.ws.security.validate.UsernameTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class WSTokenValidator extends UsernameTokenValidator {
    static final Logger LOG = LoggerFactory.getLogger(WSTokenValidator.class);
    
    private AdminUserService adminUserService;

    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }
/***
    @Override
    public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
        LOG.debug("Validating request: username='{}', type='{}' ", data.getUsername(), data.getPwType());
        LOG.debug("Algorithm='{}', ", data.getSigAlgorithm());

        if (credential == null || credential.getUsernametoken() == null) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "noCredential");
        }

        boolean handleCustomPasswordTypes = false;
        boolean passwordsAreEncoded = false;
        String requiredPasswordType = null;
        WSSConfig wssConfig = data.getWssConfig();
        if (wssConfig != null) {
            handleCustomPasswordTypes = wssConfig.getHandleCustomPasswordTypes();
            passwordsAreEncoded = wssConfig.getPasswordsAreEncoded();
            requiredPasswordType = wssConfig.getRequiredPasswordType();
        }
      
        UsernameToken usernameToken = credential.getUsernametoken();
        usernameToken.setPasswordsAreEncoded(passwordsAreEncoded);

        String pwType = usernameToken.getPasswordType();
        if (LOG.isDebugEnabled()) {
            LOG.debug("UsernameToken user " + usernameToken.getName());
            LOG.debug("UsernameToken password type " + pwType);
        }

        if (requiredPasswordType != null && !requiredPasswordType.equals(pwType)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Authentication failed as the received password type does not " 
                             + "match the required password type of: " + requiredPasswordType);
            }
            throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
        }
        
        String password = usernameToken.getPassword();
        LOG.debug("Password is: '{}'", password);

        if (usernameToken.isHashed()) {
            LOG.debug("**** Digest password ****");
//            verifyDigestPassword(usernameToken, data);
        } else if (WSConstants.PASSWORD_TEXT.equals(pwType)
            || (password != null && (pwType == null || "".equals(pwType.trim())))) {
            LOG.debug("**** Plain text password ****");
//            verifyPlaintextPassword(usernameToken, data);
        } else if (password != null) {
            if (!handleCustomPasswordTypes) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Authentication failed as handleCustomUsernameTokenTypes is false");
                }
                throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
            }
            LOG.debug("**** Custom password ****");
//            verifyCustomPassword(usernameToken, data);
        } else {
            LOG.debug("**** Unknown password ****");
//            verifyUnknownPassword(usernameToken, data);
        }
        
        Credential returnedCredential = super.validate(credential, data);

        return returnedCredential;
    }
***/
    @Override
    protected void verifyPlaintextPassword(UsernameToken usernameToken,
                                    RequestData data) throws WSSecurityException {
            LOG.debug("Verifying plain text password...... *************");
            verifyDigestPassword(usernameToken, data);
    }

    @Override
    protected void verifyDigestPassword(UsernameToken usernameToken,
                    RequestData data) throws WSSecurityException {
            LOG.debug("Verifying digest password...... *************");
            if (data.getCallbackHandler() == null) {
                LOG.error("No callback supplied");
                throw new WSSecurityException(WSSecurityException.FAILURE, "noCallback");
            }
   
            String user = usernameToken.getName();
            String password = usernameToken.getPassword();
            String nonce = usernameToken.getNonce();
            String createdTime = usernameToken.getCreated();
            String pwType = usernameToken.getPasswordType();
            boolean passwordsAreEncoded = usernameToken.getPasswordsAreEncoded();

            LOG.debug("Creating callback...");
            WSPasswordCallback pwCb = 
                new WSPasswordCallback(user, null, pwType, WSPasswordCallback.USERNAME_TOKEN, data);
            try {
                LOG.debug("Calling callback...");
                data.getCallbackHandler().handle(new Callback[]{pwCb});
            } catch (IOException e) {
                LOG.error(e.getMessage());
                throw new WSSecurityException(
                    WSSecurityException.FAILED_AUTHENTICATION, null, null, e
                );
            } catch (UnsupportedCallbackException e) {
                LOG.error(e.getMessage());
                throw new WSSecurityException(
                    WSSecurityException.FAILED_AUTHENTICATION, null, null, e
                );
            }
            String userPassword = pwCb.getPassword();
            if (userPassword == null) {
                LOG.error("Callback supplied no password for user: '{}'", user);
                throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
            }
            LOG.debug("userPassword='{}'", userPassword);
            
            /*
             * Need to encode the provided password
             */
            
            if (usernameToken.isHashed()) {
                LOG.debug("Usernametoken is hashed");
                String passDigest;
                if (passwordsAreEncoded) {
                    LOG.debug("Passwords are encoded: {}", userPassword);
                    passDigest = UsernameToken.doPasswordDigest(nonce, createdTime, Base64.decode(password));
                } else {
                    passDigest = UsernameToken.doPasswordDigest(nonce, createdTime, password);
                }
                LOG.debug("Retrieved password ('{}') from digest ('{}')", passDigest, password);
                String encodedPassword = adminUserService.encodePassword(passDigest);
                LOG.debug("Comparing password provided ('{}'[encoded]) to the one retrieved ('{}')", encodedPassword, userPassword);
                if (!passDigest.equals(encodedPassword)) {
                    throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
                }
            } else {
                String encodedPassword = adminUserService.encodePassword(password);
                LOG.debug("Comparing password provided ('{}'[encoded]) to the one retrieved ('{}')", encodedPassword, userPassword);
                if (!userPassword.equals(encodedPassword)) {
                    throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
                }
            }
        }
}
