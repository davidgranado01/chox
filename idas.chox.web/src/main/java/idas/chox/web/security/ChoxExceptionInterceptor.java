package idas.chox.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;

/**
 *
 * @author John
 */
public class ChoxExceptionInterceptor extends ExceptionMappingInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ChoxExceptionInterceptor.class);

    @Override
    protected void publishException(ActionInvocation invocation, ExceptionHolder exceptionHolder) {
        try {
            // We'll log some exceptions as warnings as they do not relate to an underlying problem
            // but are caused by general usage
            if (exceptionHolder.getException() instanceof javax.net.ssl.SSLException
                    || (exceptionHolder.getException() instanceof java.io.IOException)
                    || (exceptionHolder.getException() != null
                        && (exceptionHolder.getException().getCause() instanceof javax.net.ssl.SSLException)
                            || exceptionHolder.getExceptionStack().contains("getOutputStream() has already been called for this response")
                            || exceptionHolder.getExceptionStack().contains("getAttribute: Session already invalidated"))) {
                LOG.warn("Exception intercepted from action '{}': ", invocation.getAction().toString(), exceptionHolder.getException());
            }
            else {
                LOG.error("Exception intercepted from action '{}': ", invocation.getAction().toString(), exceptionHolder.getException());
            }
        } catch (Exception e) {
            LOG.error("Exception logging exception: {}", e.getMessage(), e);
        }

        super.publishException(invocation, exceptionHolder);
    }
}

