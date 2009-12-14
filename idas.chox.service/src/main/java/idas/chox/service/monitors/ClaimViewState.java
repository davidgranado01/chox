/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Emmanuel
 */
public class ClaimViewState extends ViewState {

    private ConcurrentHashMap<Integer, ViewState> users = new ConcurrentHashMap<Integer, ViewState>();

    public ClaimViewState(Integer userId) {
        super();
        users.put(userId, new ViewState());
    }

    public void ping(Integer userId) {
        users.putIfAbsent(userId, new ViewState());
        users.get(userId).refresh();
        this.refresh();
    }

    public List<Integer> getUserIds() {
        List<Integer> result = new ArrayList<Integer>();
        for (Integer i : users.keySet()) {
            ViewState v = (ViewState) users.get(i);
            if (!v.isExpired()) {
                result.add(i);
            } else {
                result.remove(i);
            }
        }
        return result;
    }
}
