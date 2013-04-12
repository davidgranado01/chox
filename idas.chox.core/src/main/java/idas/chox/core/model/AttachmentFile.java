package idas.chox.core.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author John
 */
public class AttachmentFile extends Entity implements Serializable {
    private Attachment attachment;
    private byte[] fileBuffer;

    public byte[] getFileBuffer() {
        if (fileBuffer == null) {
            return null;
        }

        return Arrays.copyOf(fileBuffer, fileBuffer.length);
    }

    public void setFileBuffer(byte[] fb) {
        if (fb == null) {
            fileBuffer = null;
        } else {
            fileBuffer = Arrays.copyOf(fb, fb.length);
        }
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
