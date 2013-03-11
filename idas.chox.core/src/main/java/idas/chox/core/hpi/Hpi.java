package idas.chox.core.hpi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author John
 */
public final class Hpi {

    private static final Logger LOG = LoggerFactory.getLogger(Hpi.class);
    private static String today;
//    private static final String hpiUrl = "http://www.q.hpixml.com/servlet/HpiGate1_0";
//    private static final String efxidParam = "0503522";
//    private static final String passwordParam = "t3sting";
//    private static final String initialsParam = "er";
    private static final String productCodeParam = "HPI11";
    private static final String functionParam = "SEARCH";
    private static final String deviceTypeParam = "XM";
    private static final Hpi INSTANCE = new Hpi();
    private Map<String, String> params;
    private String session;
    private HttpClient httpClient;
    private String hpiUrl;
    private String efxidParam;
    private String passwordParam;
    private String initialsParam;
    private boolean active = false;
    private static final Integer lock =  Integer.valueOf(0);
    
    private Hpi() {
        if (INSTANCE != null) {
            throw new IllegalStateException("HPI Already instantiated");
        }
        // Create and initialize HTTP parameters
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
//        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection to 20
        cm.setMaxTotal(20);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);

        
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        httpClient = new DefaultHttpClient(cm);

//        httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());
        LOG.info("HPI I/F (singleton) utility class has been created.");
    }

    public static Hpi getInstance() {
        return INSTANCE;
    }

    public static HpiResponse getHpiInfo(String vrn) throws HpiException {
        return getInstance().getHpi(vrn);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEfxidParam(String efxidParam) {
        this.efxidParam = efxidParam;
    }

    public void setHpiUrl(String hpiUrl) {
        this.hpiUrl = hpiUrl;
    }

    public void setInitialsParam(String initialsParam) {
        this.initialsParam = initialsParam;
    }

    public void setPasswordParam(String passwordParam) {
        this.passwordParam = passwordParam;
    }

    private String getURL(String vrn) {
        return getURL(vrn, null);
    }

    private String getURL(String vrn, String sessionId) {
        char joinChar = '?';
        StringBuilder sb = new StringBuilder(hpiUrl);
        for (Map.Entry<String, String> mapEntry : params.entrySet()) {
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            sb.append(joinChar).append(key).append("=").append(value);
            joinChar = '&';
        }
        sb.append(joinChar).append("vrm=").append(vrn.toLowerCase());
        if (sessionId != null) {
            sb.append(joinChar).append("SessionNo=").append(sessionId);
        }

        return sb.toString();

    }

    private HpiResponse getHpi(String vrn) throws HpiException {
        if (!active) {
            LOG.debug("HPI check functionality has been disabled.");
            throw new HpiException("HPI check functionality has been de-activated.");
        } else if (params == null) {
            params = new HashMap<String, String>();
            params.put("forward", "YES");
            params.put("XML", "YES");
            params.put("efxid", efxidParam);
            params.put("password", passwordParam);
            params.put("initials", initialsParam);
            params.put("function", functionParam);
            params.put("product", productCodeParam);
            params.put("deviceType", deviceTypeParam);
        }
        
        synchronized (lock) {
            if (today == null || !getDate().equals(today)) {
                // initialize sessions
                today = getDate();
                setSession(null);
                LOG.info("A new HPI session will be created for today: '{}'", today);
            } else {
                if (session != null) {
                    LOG.debug("Using todays session '{}'", session);
                } else {
                    LOG.debug("No sessions available - a new one will be created.");
                }
            }
        }

        String url = getURL(vrn, getSession());

        HttpGet httpget = new HttpGet(url);

        LOG.info("Executing HPI request with sessionId={} : {} ", getSession(), httpget.getURI());

        String responseBody = null;
        HpiResponse hpiResponse = null;
        try {
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // do something useful with the entity
                hpiResponse = HpiResponse.parseResponse(entity.getContent());
                setSession(hpiResponse.getSessionId());
            }
            // ensure the connection gets released to the manager
            EntityUtils.consume(entity);
        } catch (IOException ex) {
            LOG.warn("IOException thrown during HPI call: {}", ex.getMessage(), ex);
            httpget.abort();
            throw new HpiException("Error calling HPI: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            LOG.warn("Exception thrown during HPI call: {}", ex.getMessage(), ex);
            httpget.abort();
            throw new HpiException("Error calling HPI: " + ex.getMessage(), ex);
        } finally {
            LOG.info("HPI request completed");
            httpget.releaseConnection();
        }

        LOG.debug("----------------------------------------");
        LOG.debug(responseBody);
        LOG.debug("----------------------------------------");

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
//        httpclient.getConnectionManager().shutdown();

        return hpiResponse;
    }

    private synchronized String getSession() {
        return session;
    }

    private synchronized void setSession(String session) {
        this.session = session;
    }

    private static String getDate() {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        } catch (Exception ex) {
            LOG.error("Error getting todays date: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        httpClient.getConnectionManager().shutdown();
        super.finalize();
    }
}
