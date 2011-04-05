/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.web.viewdata;

import idas.chox.core.model.Bordereau;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author seeni
 */
public class BordereauViewData {

    private int id;
    private String status;
    private String fileName;
    private String createdDate;



    public BordereauViewData(Bordereau bordereau){
        this.id=bordereau.getId();
        this.status=bordereau.getStatus();
        this.fileName=bordereau.getFileName();
        this.createdDate=DateHelper.LocalDateTimeFormat.format(bordereau.getCreatedDate());
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
     public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


}
