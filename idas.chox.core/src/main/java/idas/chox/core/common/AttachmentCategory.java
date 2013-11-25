package idas.chox.core.common;

import java.util.ArrayList;
import java.util.List;

public class AttachmentCategory {
    
    public static final String ATTCAT_PAYMENTPACK = "Payment Pack";
    public static final String ATTCAT_TOTALLOSS = "Total Loss Inspection Check";
    public static final String ATTCAT_CHO_ALLEGATIONS = "CHO's Client Allegations";
    public static final String ATTCAT_INS_ALLEGATIONS = "Insurer's Client Allegations";
    public static final String ATTCAT_ENG_REPORTS = "Engineer's Reports";
    public static final String ATTCAT_INV_REPORTS = "Investigator Reports";
    public static final String ATTCAT_REPAIR_DOCS = "Repair Documents";
    public static final String ATTCAT_REPAIR_STATEMENT = "Repairer Statement";
    public static final String ATTCAT_TOTALLOSS_PACK = "Total Loss Pack";
    public static final String ATTCAT_WITNESS_STATEMENT = "Witness Statement";
    public static final String ATTCAT_OTHER = "Other";
    public static final String ATTCAT_MITIGATION_STATEMENT = "Mitigation Statement";
    public static final String ATTCAT_INTERVENTION_LETTER = "Intervention Letter";
    public static final String ATTCAT_VIDEO_FOOTAGE = "Video Footage";

    public static List<String> getAttachmentCategory() {
        List<String> status = new ArrayList<String>();
        status.add(ATTCAT_CHO_ALLEGATIONS);
        status.add(ATTCAT_INS_ALLEGATIONS);
        status.add(ATTCAT_ENG_REPORTS);
        status.add(ATTCAT_INTERVENTION_LETTER);
        status.add(ATTCAT_INV_REPORTS);
        status.add(ATTCAT_MITIGATION_STATEMENT);
        status.add(ATTCAT_PAYMENTPACK);
        status.add(ATTCAT_REPAIR_DOCS);
        status.add(ATTCAT_REPAIR_STATEMENT);
        status.add(ATTCAT_TOTALLOSS);
        status.add(ATTCAT_TOTALLOSS_PACK);
        status.add(ATTCAT_VIDEO_FOOTAGE);
        status.add(ATTCAT_WITNESS_STATEMENT);
        status.add(ATTCAT_OTHER);
        return status;
    }
}
