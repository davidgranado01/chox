package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.Entity;

/**
 *
 * @author emmanuel
 */
public interface DataService {

    void save(final Object object);
    void saveFlushCommit(final Object object);
    void delete(final Object object);
    void evict(Object object);
    void flush();
    Object get(final Class c, final int id);
    void saveCollections(List<? extends Object> objects);
    void deleteAll(List<? extends Entity> objects);
}
