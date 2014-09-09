package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author John
 */
public class SupplierClaimOwnerDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(SupplierClaimOwnerDropDownAction.class);

    private List<IdLookupItem> claimhandlers = null;
    private Set<Integer> supplierId;
    private UserService userService;

    public Set<Integer> getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Set<Integer> supplierId) {
        if (supplierId.contains(null)) 
            this.supplierId = null;
        else
            this.supplierId = supplierId;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List getClaimhandlers() {
        return claimhandlers;
    }

    public void setClaimhandlers(List claimhandlers) {
        this.claimhandlers = claimhandlers;
    }

    public String getJsonData() {
        LOG.debug("Returning json data from claimhandlers: {}", claimhandlers);

        JSONArray jsonArray;
        try {
            jsonArray = JSONArray.fromObject(claimhandlers);
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return null;
        }
        LOG.debug("Returning json data: {}", jsonArray.toString());
        return "{totalCount:" + claimhandlers.size() + ",results:" + jsonArray.toString() + "}";
    }

    @Override
    public String execute() throws Exception {
        LOG.debug("Executing in action {}", this);
        claimhandlers = new ArrayList<IdLookupItem>();

        if (getIsCHO()) {
            LOG.debug("CHO - adding chorg id '{}'", getAuthenticatedUser().getChorganisation().getId());
            supplierId.clear();
            supplierId.add(getAuthenticatedUser().getChorganisation().getId());
        }

        if (supplierId != null) {
            List<WebUser> users = new ArrayList<WebUser>();
            for (Integer suppId : supplierId) {
                users.addAll(userService.getOprUsersByChorganisation(suppId));
            }
            List items = new ArrayList<IdLookupItem>();

            for (WebUser user : users) {
                items.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }
            claimhandlers = items;
        } else {
            LOG.debug("No supplierID");
        }

        return SUCCESS;
    }
}
