/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.WebUser;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Emmanuel
 */
public class UserCacheManager {

    private static long lifeTime = 1000 * 3600;
    private static UserCacheManager instance;
    private static int checkedOut = 0;
    private static ConcurrentHashMap<String,WebUser> userCache = new ConcurrentHashMap<String,WebUser>();
    private static ConcurrentHashMap<String,Long> userCacheTime = new ConcurrentHashMap<String,Long>();
    
   private UserCacheManager() {
      
    }

    /**
     * Singleton access point to the manager.
     */
    public static UserCacheManager getInstance() {
        synchronized (UserCacheManager.class) {
            if (instance == null) {
                instance = new UserCacheManager();
            }
        }

        synchronized (instance) {
            instance.checkedOut++;
        }

        return instance;
    }

    public WebUser getUserFromCache(int id) {
        WebUser result = null;
        String key = "user_" + id;
        if (!isExpired(key)) {
            result = userCache.get(key);
        }
        else
        {
            userCache.remove(key);
        }

        return result;
    }
    
    private boolean isExpired(String key) {
            Long cacheTime = userCacheTime.get(key);
            return  cacheTime == null ? true : System.currentTimeMillis() - cacheTime > lifeTime;
    }
    
    public void setCacheLifeTime(long time)
    {
        lifeTime = time;        
    }

    public void putUserToCache(WebUser user) {
        try {
            String key = "user_" + user.getId();
            userCache.put(key, user);
            userCacheTime.put(key, System.currentTimeMillis());
        } catch (Exception ex) {
        }
    }
}


