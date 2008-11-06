package chox.daemon;

import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.Logger;
import idas.alert.Alert;
import idas.configuration.DatabaseConnectionFactory;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class SessionTimeoutWatch {

    private String configFile;
    public static final int MAX_RUNTIME_SECONDS = 240;
    public static final int MAX_PER_BATCH=5000;
    public static final long startTime = System.currentTimeMillis();
    
    public static final long SESSION_TIMEOUT_SECONDS=60*60;

    public static void main(String[] args) {

        //args = new String[]{"/Users/stu/Projects/CHOX/config.xml"};

        if (args.length != 1) {
            System.err.println("Usage: chox.daemon.SessionTimeoutWatch <config.xml>");
            System.exit(1);
        }

        try {
            SessionTimeoutWatch a = new SessionTimeoutWatch(args[0]);
            a.process();

        } catch (Exception e) {
            Alert.message("chox.daemon.SessionTimeoutWatch Exception: "+e.getMessage());
            e.printStackTrace(Logger.err);
        }
    }

    private SessionTimeoutWatch(String configFile) {
        this.configFile = configFile;
        Alert.init(configFile);
    }

    private void process() throws Exception {
        DBConnectionWrapper connection = DatabaseConnectionFactory.newConnection(configFile);

        PreparedStatement s=connection.prepareStatement("update web_user_session set status_id=2 where last_used<?");
        s.setTimestamp(1,new Timestamp(System.currentTimeMillis()-(SESSION_TIMEOUT_SECONDS*1000)));
        s.executeUpdate();
        s.close();

        connection.commit();
        connection.close();
    }


}
