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

    void save(final Object object);
    void delete(final Object object);
    void evict(Object object);
    Object get(final Class c, final int id);
}
