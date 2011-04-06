package idas.chox.web.security;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class ChoxExceptionInterceptor extends ExceptionMappingInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ChoxExceptionInterceptor.class);

    @Override
    protected void publishException(ActionInvocation invocation, ExceptionHolder exceptionHolder) {
        try {
            if (exceptionHolder.getException() instanceof javax.net.ssl.SSLException
                    || (exceptionHolder.getException() != null && exceptionHolder.getException().getCause() instanceof javax.net.ssl.SSLException))
                LOG.warn("Exception intercepted: {}", exceptionHolder.getExceptionStack());
            else
                LOG.error("Exception intercepted: {}", exceptionHolder.getExceptionStack());
        } catch (Exception e) {
            LOG.error("Exception logging exception: {}", e.getMessage());
        }

//        HibUtil.rollback();

        super.publishException(invocation, exceptionHolder);
    }
}

