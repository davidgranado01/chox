package idas.chox.web.ws;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author John
 */
public class StringBuilderOutputStream extends OutputStream {

    private StringBuilder textBuffer = new StringBuilder();

    /**
     * 
     */
    public StringBuilderOutputStream() {
        super();
    }

    /*
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        char a = (char) b;
        textBuffer.append(a);
    }

    @Override
    public String toString() {
        return textBuffer.toString();
    }

    public void clear() {
        textBuffer.delete(0, textBuffer.length());
    }
}
