package idas.chox.web.scheduler;

import java.io.InputStream;

/**
 *
 * @author John
 */
public class EmailAttachment {
    private final InputStream is;
    private final String name;
    private final long size;
    
    public EmailAttachment(String name, InputStream is, long size) {
        this.name = name;
        this.is = is;
        this.size = size;
    }

    public InputStream getIs() {
        return is;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
    
}
