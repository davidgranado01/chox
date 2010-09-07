/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.viewdata;

import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.util.DateHelper;

public class UserroleViewData {

    private int id;
    private int webUserId;
    private String webUserName;
    private int webUserroleId;
    private String webUserroleRole;
    private String webUserroleName;
    private String createdBy;
    private String createdDate;

    public UserroleViewData(WebUserUserRole object) {

        this.id = object.getId();
        this.webUserId = object.getWebUser().getId();
        this.webUserName = object.getWebUser().getDisplayName();
        this.webUserroleId = object.getWebUserRole().getId();
        this.webUserroleRole = object.getWebUserRole().getName();
        this.webUserroleName = object.getWebUserRole().getDescription();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.LocalDateTimeFormat.format(object.getCreatedDate());

    }

    public UserroleViewData(WebUserRole object) {

        this.id = object.getId();
        this.webUserroleId = object.getId();
        this.webUserroleRole = object.getName();
        this.webUserroleName = object.getDescription();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.LocalDateTimeFormat.format(object.getCreatedDate());

    }

    public int getId() {
        return id;
    }

    public int getWebUserId() {
        return webUserId;
    }

    public String getWebUserName() {
        return webUserName;
    }

    public int getWebUserroleId() {
        return webUserroleId;
    }

    public String getWebUserroleName() {
        return webUserroleName;
    }

    public String getWebUserroleRole() {
        return webUserroleRole;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
