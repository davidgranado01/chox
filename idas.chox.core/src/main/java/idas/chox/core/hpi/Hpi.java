package idas.chox.core.hpi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class Hpi {
    private static final Logger LOG = LoggerFactory.getLogger(Hpi.class);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static String today;

//    private static final String hpiUrl = "http://www.q.hpixml.com/servlet/HpiGate1_0";
//    private static final String efxidParam = "0503522";
//    private static final String passwordParam = "t3sting";
//    private static final String initialsParam = "er";
    private static final String productCodeParam = "HPI11";
    private static final String functionParam = "SEARCH";
    private static final String deviceTypeParam = "XM";
    private static Hpi instance = new Hpi();

    private Map<String, String> params;
    private List<String> session;
    private HttpClient httpClient;
    private String hpiUrl;
    private String efxidParam;
    private String passwordParam;
    private String initialsParam;
    private boolean active = false;

    private Hpi() {
       // Create and initialize HTTP parameters
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        // Create and initialize scheme registry
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, httpParams);

//        httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());
        LOG.debug("HPI I/F class has been created (url={})", hpiUrl);
    }

    public static Hpi getInstance() {
        return instance;
    }

    public static HpiResponse getHpiInfo(String vrn) throws HpiException {
        return instance.getHpi(vrn);
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
        StringBuffer sb = new StringBuffer(hpiUrl);
        for(Map.Entry<String, String> mapEntry : params.entrySet()) {
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            sb.append(joinChar + key + "=" + value);
            joinChar = '&';
        }
        sb.append(joinChar + "vrm=" + vrn.toLowerCase());
        if (sessionId != null)
            sb.append(joinChar + "SessionNo=" + sessionId);

        return sb.toString();

    }

    private HpiResponse getHpi(String vrn) throws HpiException {
        String sessionId = null;

        if (!active) {
            LOG.debug("HPI check functionality has been disabled.");
            throw new HpiException("HPI check functionality has been de-activated.");
        }
        else if (params == null) {
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
        if (!getDate().equals(today)) {
            // initialize sessions
            today = getDate();
            session = new ArrayList<String>();
            LOG.info("New session list created for today={}", today);
        }
        else {
            sessionId = getSession();
            if (sessionId != null)
                LOG.info("Using todays session '{}'", sessionId);
            else
                LOG.info("No sessions available - a new one will be created.");
        }

        String url = getURL(vrn, sessionId);

        HttpGet httpget = new HttpGet(url);

        LOG.debug("executing HPI request with sessionId={} : {} ", sessionId, httpget.getURI());

        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = null;
        try {
            responseBody = httpClient.execute(httpget, responseHandler);
        } catch (IOException ex) {
            LOG.error("IOException thrown : {}", ex.getMessage());
        }

        LOG.debug("----------------------------------------");
        LOG.debug(responseBody);
        LOG.debug("----------------------------------------");

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
//        httpclient.getConnectionManager().shutdown();

        // Parse response
        HpiResponse response = HpiResponse.parseResponse(responseBody);

        saveSession(response.getSessionId());

        return response;
    }

    private synchronized String getSession() {
        if (session.size() > 0) {
            return session.remove(0);
        }

        return null;
    }
    private synchronized void saveSession(String sessionId) {
            session.add(sessionId);
            LOG.debug("Session saved: {}", sessionId);
    }

    private static String getDate() {
        try {
            return dateFormat.format(Calendar.getInstance().getTime());
        } catch (Exception ex) {
            LOG.error("Error getting todays date: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        httpClient.getConnectionManager().shutdown();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }

}
