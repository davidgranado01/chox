package idas.chox.web.viewdata;

import idas.chox.core.model.ChorganisationAlias;
import idas.chox.core.util.DateHelper;

public class ChoAliasViewData {

    private int id;
    private String name;
    private int choId;
    private String choName;
    private String createdBy;
    private String createdDate;

    public ChoAliasViewData(ChorganisationAlias object) {

        this.id = object.getId();
        this.choId = object.getChorganisation().getId();
        this.choName = object.getChorganisation().getName();
        this.name = object.getAliasName();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getChoId() {
        return choId;
    }

    public void setChoId(int choId) {
        this.choId = choId;
    }

    public String getChoName() {
        return choName;
    }

    public void setChoName(String choName) {
        this.choName = choName;
    }
}
