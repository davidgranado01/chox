package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class SupplierClaimOwnerDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(SupplierClaimOwnerDropDownAction.class);

    private List<IdLookupItem> claimhandlers = null;
    private Integer supplierId;
    private UserService userService;


    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
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
        claimhandlers = new ArrayList<IdLookupItem>();

        if (getIsCHO()) {
            supplierId = getAuthenticatedUser().getChorganisation().getId();
        }

        if (supplierId > 0) {
            List<WebUser> users = userService.getOprUsersByChorganisation(supplierId);

            List items = new ArrayList<IdLookupItem>();

            for (WebUser user : users) {
                items.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }
            claimhandlers = items;
        }

        return SUCCESS;
    }
}
