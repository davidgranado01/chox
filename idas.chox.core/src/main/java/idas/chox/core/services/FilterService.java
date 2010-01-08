/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.Filter;
import idas.chox.core.model.WebUser;

/**
 *
 * @author emmanuel
 */
public interface FilterService {

    void setAvailableFilters(List<Filter> availableFilters);
    List<Filter> getAvailableFilters(WebUser webUser);
    Filter getFilter(String filterName) throws Exception;

}
