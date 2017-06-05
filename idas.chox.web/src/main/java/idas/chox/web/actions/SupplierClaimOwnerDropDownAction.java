package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;

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

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(claimhandlers);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting claimhandlers to json string.");
        }
        LOG.trace("Returning json data: {}", jsonString);
        return "{totalCount:" + claimhandlers.size() + ",results:" + jsonString + "}";
    }

    @Override
    public String execute() throws Exception {
        LOG.debug("Executing in action {}", this);
        claimhandlers = new ArrayList<>();
        
        if (getIsCHO()) {
            LOG.debug("CHO - adding chorg id '{}'", getAuthenticatedUser().getChorganisation().getId());
            if (supplierId == null) {
                supplierId = new HashSet<>();
            } else {
                supplierId.clear();
            }
            supplierId.add(getAuthenticatedUser().getChorganisation().getId());
        }

        if (supplierId != null) {
            List<WebUser> users = new ArrayList<>();
            for (Integer suppId : supplierId) {
                users.addAll(userService.getOprUsersByChorganisation(suppId));
            }
            List items = new ArrayList<>();

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
