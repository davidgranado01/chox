package idas.chox.service.workflow.event;

import java.lang.reflect.Method;

import com.google.common.eventbus.Subscribe;

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
    private boolean useGuava;
    private EventBusWrapper eventBus;
        
    public void setEventBus(EventBusWrapper eventBus) {
        this.eventBus = eventBus;
    }

    public void setUseGuava(boolean useGuava) {
        this.useGuava = useGuava;
    }


    
    public static boolean containsListener(Object bean) {
        Listener listener = bean.getClass().getAnnotation(Listener.class);
        return listener != null;
    }
    
    public static boolean containsHandler(Object bean) {
        Method[] methods = bean.getClass().getMethods();
        for(Method method : methods) {
            Handler handler = method.getAnnotation(Handler.class);
            if(handler != null) {
                return true;
            }
        }
        return false;
    }
    public static boolean containsSubscribe(Object bean) {
        Method[] methods = bean.getClass().getMethods();
        for(Method method : methods) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if(subscribe != null) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!useGuava && containsListener(bean)) {
            eventBus.registerListener(bean);
            log.info("Listener Bean registered to MBassador eventBus: {}", beanName);
        } else if(useGuava && containsSubscribe(bean)) {
            eventBus.registerListener(bean);
            log.info("Listener Bean registered to Guava eventBus: {}", beanName);
        } else if(!useGuava && containsHandler(bean)) {
            eventBus.registerListener(bean);
            log.info("Handler Bean registered to MBassador eventBus: {}", beanName);
        }
        return bean;
    }

}