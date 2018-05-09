package idas.chox.core.model;

/**
 *
 * @author John
 */
public class EmailAttachment {
    private final String name;
    private final long size;
    private final byte[] content;
    
    public EmailAttachment(String name, byte[] content) {
        this.name = name;
        this.content = content;
        this.size = content.length;
    }


    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public byte[] getContent() {
        return content;
    }
    
}
