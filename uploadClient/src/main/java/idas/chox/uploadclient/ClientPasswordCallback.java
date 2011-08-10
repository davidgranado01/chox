package idas.chox.uploadclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ws.security.WSPasswordCallback;

/**
 *
 * @author John
 */
public class ClientPasswordCallback implements CallbackHandler {

    static final Logger LOG = LoggerFactory.getLogger(ClientPasswordCallback.class);
    private Map<String, String> passwords = new HashMap<String, String>();

    public ClientPasswordCallback() {
        passwords.put("wsClient", "wsClientPass");
        passwords.put("dummyUser", "dummyPassword");
        passwords.put("admin@cho.com", "C0mpliance");
        passwords.put("op@cho.com", "C0mpliance");
        passwords.put("admin@ins.com", "C0mpliance");
        passwords.put("op@abc.com", "C0mpliance");
        passwords.put("admin@abc.com", "C0mpliance");
        passwords.put("bob", "password");
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
