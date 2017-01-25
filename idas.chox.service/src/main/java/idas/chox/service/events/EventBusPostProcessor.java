package idas.chox.service.events;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * EventBusPostProcessor registers Spring beans with EventBus.
 * @author pmeade
 */
public class EventBusPostProcessor implements BeanPostProcessor, ApplicationContextAware
{
    private static final Logger log = LoggerFactory.getLogger(EventBusPostProcessor.class);

    public static boolean containsSubscribe(Object bean)
    {
        Method[] methods = bean.getClass().getMethods();
        for(Method method : methods)
        {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if(subscribe != null)
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
        if(containsSubscribe(bean) && applicationContext.isSingleton(beanName)) {
            eventBus.register(bean);
            log.info("Bean registered to eventBus: {}", beanName);
        } else {
            if(containsSubscribe(bean)) {
                eventBus.register(bean);
                log.warn("Non-singleton Bean {} containing @Subscribe annotation(s) was "
                    + "registered with EventBus. EventBus registration of "
                    + "prototype beans (isSingleton() == false) can cause "
                    + "memory leaks.", beanName);
            }
        }
        return bean;
    }

    private ApplicationContext applicationContext;
    private EventBus eventBus;
    
    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.applicationContext = ac;
    }
    
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}