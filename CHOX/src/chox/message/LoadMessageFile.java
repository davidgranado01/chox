package chox.message;

import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.Logger;
import com.filesystemsoftware.utils.XMLUtils;
import idas.configuration.DatabaseConnectionFactory;
import chox.data.Message;
import java.io.File;
import java.util.HashMap;
import org.w3c.dom.Document;

public class LoadMessageFile {

    private HashMap<String,Document> messages=new HashMap<String,Document>();
    private String configFile;
    
    public static void main(String [] args) {

        args = new String[]{"/Users/stu/Projects/CHOX/config.xml","/Users/stu/Projects/CHOX/test/test-message.xml"};
        
        if (args.length < 2) {
            System.err.println("Usage: chox.message.LoadMessageFile <config.xml> <message-file>.... ");
            System.exit(1);
        }        

        try {
            LoadMessageFile a = new LoadMessageFile(args[0]);
            for (int i = 1; i < args.length; i++) {
                Logger.out.println("Loading: " + args[i]);
                Document d = XMLUtils.toDocument(new File(args[i]));
                a.addDocument(args[i],d);
            }
            a.process();

        } catch (Exception e) {
            Logger.err.println(e.getMessage());
            e.printStackTrace(Logger.err);
        }
    }
    
    private LoadMessageFile(String configFile) {
        this.configFile=configFile;
    }
    
    private void addDocument(String name,Document doc) {
        messages.put(name,doc);
    }
    
    private void process() throws Exception
    {
        DBConnectionWrapper connection=DatabaseConnectionFactory.newConnection(configFile);
        
        for(String name:messages.keySet()) {
            Document doc=messages.get(name);
            Message msg=new Message();
            msg.setStatusID(3);
            msg.setMessage(XMLUtils.toString(doc));
            msg.save(connection);
        }
        
        connection.commit();
        connection.close();
    }
           
    
}
