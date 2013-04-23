package idas.chox.web.scheduler;

import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;

public class ServerConfig implements ServletContextAware {

    private ServletContext servletContext;

    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}