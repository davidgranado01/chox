package chox.web.data;

import chox.data.FilterProvider;
import chox.data.HibernateUtil;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * @author Emmanuel Kong
 * @version $Id$
 */
public class HibernateUserDataFilterInterceptor extends AbstractInterceptor {

    FilterProvider filterProvider;

    public String intercept(ActionInvocation invocation) throws Exception {

        if(!HibernateUtil.getIsFilterProviderSet())
        {
            HibernateUtil.setFilterProvider(this.filterProvider);
        }

        return invocation.invoke();
    }

    public void setFilterProvider(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
    }
}
