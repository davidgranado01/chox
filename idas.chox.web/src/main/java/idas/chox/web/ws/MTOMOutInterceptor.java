package idas.chox.web.ws;

import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.MessageInfo;

/**
 *
 * @author John
 */
public class MTOMOutInterceptor extends AbstractPhaseInterceptor<Message> {
   public MTOMOutInterceptor() {
      super(Phase.PRE_STREAM);
      addBefore(AttachmentOutInterceptor.class.getName());
   }

   @Override
   public void handleMessage(Message message) {
      MessageInfo mi = (MessageInfo)message.get(MessageInfo.class);
//      if ("?".equals(mi.getName().getLocalPart()))
      message.put("mtom-enabled", false);
   }
  
}
