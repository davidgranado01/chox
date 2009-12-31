/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.services;

/**
 *
 * @author emmanuel
 */
public interface DataService {

    public void save(final Object object);
    public void delete(final Object object);
    public Object get(final Class c, final int id);
}
