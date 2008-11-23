/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.services.ClaimService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 *
 * @author Emmanuel
 */
public class OpenClaimDetailAction extends BaseAction implements ModelDriven<Claim>, Preparable {

    private Claim claim;
    private int id = -1;
    private ClaimService service;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public Claim getModel() {
        return claim;
    }

    public void prepare() throws Exception {
        if (id == -1) {
            claim = new Claim();
        } else {
            claim = service.getClaim(id);
        }
    }
}
