package idas.chox.service.workflow.event;

import net.engio.mbassy.bus.MBassador;
import com.google.common.eventbus.EventBus;
import idas.chox.events.BaseActivityEvent;

/**
 *
 * @author john
 */
public class EventBusWrapper {
    private boolean useGuava;
    private EventBus guavaEventBus; 
    private MBassador mBassadorEventBus; 

    public void setUseGuava(boolean useGuava) {
        this.useGuava = useGuava;
    }

    public void setGuavaEventBus(EventBus guavaEventBus) {
        this.guavaEventBus = guavaEventBus;
    }

    public void setMBassadorEventBus(MBassador mBassadorEventBus) {
        this.mBassadorEventBus = mBassadorEventBus;
    }

    public void post(BaseActivityEvent event) {
        if (useGuava) {
            guavaEventBus.post(event);
        } else {
            mBassadorEventBus.post(event).now();
        }
    }

    public void registerListener(Object listener) {
        if (useGuava) {
            guavaEventBus.register(listener);
        } else {
            mBassadorEventBus.subscribe(listener);
        }
    }
}
