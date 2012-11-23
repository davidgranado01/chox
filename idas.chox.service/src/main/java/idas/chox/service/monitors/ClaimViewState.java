package idas.chox.service.monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel
 */
public class ClaimViewState extends ViewState {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimViewState.class);

    private ConcurrentHashMap<Integer, ViewState> users = new ConcurrentHashMap<Integer, ViewState>();

    public ClaimViewState(Integer userId, long interval) {
        super(interval);
        users.put(userId, new ViewState(interval));
        LOG.trace("Created for user: {}", userId);
    }

    public void ping(Integer userId, long interval) {
        users.putIfAbsent(userId, new ViewState(interval));
        users.get(userId).refresh();
        this.refresh();
        LOG.trace("User {} refreshed.", userId);
    }

    public List<Integer> getUserIds() {
        List<Integer> result = null;
        for (Integer i : users.keySet()) {
            if (result == null) {
                result = new ArrayList<Integer>();
            }
            ViewState v = users.get(i);
            if (!v.isExpired()) {
                LOG.trace("adding user to result: {}", i);
                result.add(i);
            } else {
                LOG.trace("removing user from result: {} (should probably be from users)", i);
                result.remove(i);
            }
        }
        LOG.trace("Returning {} user ids (viewing claim)", result.size());
        return result;
    }
}
