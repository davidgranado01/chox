/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.data;

import chox.exception.InvalidaUserDataException;
import org.hibernate.Session;

/**
 *
 * @author Emmanuel
 */
public interface FilterProvider {
    
    public void setFilter(Session s) throws InvalidaUserDataException;

}
