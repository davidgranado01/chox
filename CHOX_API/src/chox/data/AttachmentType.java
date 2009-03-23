package chox.data;

import java.util.ArrayList;
import java.util.List;

public class AttachmentType {
    
    public static final String ATTTYPE_TEXT = "txt";
    public static final String ATTTYPE_EXCEL = "excel";
    public static final String ATTTYPE_WORD = "doc";
    public static final String ATTTYPE_BMP = "bmp";
    public static final String ATTTYPE_JPG = "jpg";
    
    public static List<String> getAttachmentType() {
        List<String> status = new ArrayList<String>();
        status.add(ATTTYPE_TEXT);
        status.add(ATTTYPE_EXCEL);
        status.add(ATTTYPE_WORD);
        status.add(ATTTYPE_BMP);
        status.add(ATTTYPE_JPG);
        return status;
    }
}
