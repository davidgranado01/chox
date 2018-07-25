package idas.chox.uploadclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.ws.security.WSPasswordCallback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author John
 */
public class ClientPasswordCallback implements CallbackHandler {

    static final Logger LOG = LoggerFactory.getLogger(ClientPasswordCallback.class);
    private Map<String, String> passwords = new HashMap<String, String>();
    private static final String[] LOCATIONS = {"client.xml"};
    
    

    public ClientPasswordCallback() {
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext(LOCATIONS);
        PasswordHolder passwordHolder = (PasswordHolder) ctx.getBean("PasswordHolder");
        
        passwords.put(passwordHolder.getUserName(), passwordHolder.getPassword());
      
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
            LOG.debug("PasswordCallback: identifier: " + pc.getIdentifier());
            String pass = passwords.get(pc.getIdentifier());
            if (pass != null) {
                LOG.debug("PasswordCallback: setting password: " + pass);
                pc.setPassword(pass);
                return;
            }
        }
    }
}
