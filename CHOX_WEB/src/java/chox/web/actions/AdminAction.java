/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

public class AdminAction extends BaseAction{
    
    private String adminPanelName;
    private String gridViewType;
    
    
    public String adminPanel() {
        return SUCCESS;
    }
    
    public String loadAdminPanel() {
        return this.adminPanelName;
    }

    public String getAdminPanelName() {
        return adminPanelName;
    }

    public void setAdminPanelName(String adminPanelName) {
        this.adminPanelName = adminPanelName;
    }

    public void getGridviewList(){
    }
    
    public String getGridViewType() {
        return gridViewType;
    }

    public void setGridViewType(String gridViewType) {
        this.gridViewType = gridViewType;
    }
    
    
}
