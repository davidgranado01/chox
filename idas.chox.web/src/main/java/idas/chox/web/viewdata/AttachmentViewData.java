package idas.chox.web.viewdata;

import idas.chox.core.util.DateHelper;
import java.util.Map;

public class AttachmentViewData {

    private int id;
    private String fileName;
    private String category;
    private String remarks;
    private String modifiedDate;
    private String delete = "Delete";

    public AttachmentViewData(Map data) {
        int dId = (Integer) data.get("id".toLowerCase());
        String dModifiedDate = DateHelper.LocalDateTimeFormat.format(data.get("last_modified_date".toLowerCase()));
        this.id = dId;
        this.fileName = (String) data.get("file_name".toLowerCase());
        this.category = (String) data.get("category".toLowerCase());
        this.remarks = (String) data.get("remarks".toLowerCase());
        this.modifiedDate = dModifiedDate;
    }

    public String getCategory() {
        return category;
    }

    public String getDelete() {
        return delete;
    }

    public String getFileName() {
        return fileName;
    }

    public int getId() {
        return id;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getRemarks() {
        return remarks;
    }
}
