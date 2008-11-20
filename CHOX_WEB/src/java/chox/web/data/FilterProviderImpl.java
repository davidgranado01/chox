/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.data;

import chox.exception.InvalidaUserDataException;
import chox.model.WebUser;
import chox.web.security.PermissionedUser;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.hibernate.Session;

/**
 *
 * @author Emmanuel
 */
public class FilterProviderImpl implements chox.data.FilterProvider {

    public void setFilter(Session s) throws InvalidaUserDataException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        Object principal = currentUser.getPrincipal();

        if (principal instanceof PermissionedUser) {
            PermissionedUser user = (PermissionedUser) currentUser.getPrincipal();
            WebUser webUser = user.getUser();
            if (user.getIsCHO() && webUser.getChorganisation() != null) {

                s.enableFilter("CHOFilter").setParameter("chorganisationId", webUser.getChorganisation().getId());
            } else if (user.getIsINS() && webUser.getInsurer() != null) {
                s.enableFilter("InsurerFilter").setParameter("insurerId", webUser.getInsurer().getId());
            } else if (user.getIsCHOXAdmin()) {
            } else {
                throw new InvalidaUserDataException("Invalida user data");
            }
        }
        else
        {
            s.enableFilter("CHOFilter").setParameter("chorganisationId", 999999999);
        }
    }
}
