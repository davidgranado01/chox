package idas.chox.service;

import java.lang.reflect.Method;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * EventBusPostProcessor registers Spring beans with EventBus.
 */
public class EventBusPostProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(EventBusPostProcessor.class);

    
    public static boolean containsListener(Object bean)
    {
        Listener listener = bean.getClass().getAnnotation(Listener.class);
        return listener != null;
    }
    
    public static boolean containsHandler(Object bean)
    {
        Method[] methods = bean.getClass().getMethods();
        for(Method method : methods)
        {
            Handler handler = method.getAnnotation(Handler.class);
            if(handler != null)
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
                  throws BeansException
    {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
                  throws BeansException
    {
        if(containsListener(bean)) {
            mBassador.subscribe(bean);
            log.info("Listener Bean registered to MBassador eventBus: {}", beanName);
        }
        else if(containsHandler(bean)) {
            mBassador.subscribe(bean);
            log.info("Handler Bean registered to MBassador eventBus: {}", beanName);
        }
        return bean;
    }

    private MBassador mBassador;
        
    public void setMBassador(MBassador mBassador) {
        this.mBassador = mBassador;
    }
}