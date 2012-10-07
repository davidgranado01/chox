/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.scheduler;

import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author seeni
 */
public class ServerConfig implements ServletContextAware {

    private ServletContext servletContext;
//    private String serverRootUrl2;

//    public String getServerRootUrl2() {
//        return serverRootUrl2;
//    }
//
//    public void setServerRootUrl2(String serverRootUrl2) {
//        this.serverRootUrl2 = serverRootUrl2;
//    }

    public ServletContext getServletContext() {
        return servletContext;
    }
    


    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
//        this.serverRootUrl2 = servletContext.getResource("/").getPath();
    }
}