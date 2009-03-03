/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

class SpringContextTestFactory {

    private static ApplicationContext serviceContext;

    public static ApplicationContext getServiceContext() {
        if (serviceContext == null) {
            serviceContext = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        }
        return serviceContext;
    }
}
