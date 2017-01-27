package idas.chox.service.workflow.listeners;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.services.InvoiceService;
import idas.chox.data.services.SecureDataService;
import idas.chox.events.InvoiceCreatedEvent;

/**
 *
 * @author john
 */
@Listener
public class InvoiceCreatedListener extends SecureDataService {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCreatedListener.class);
    private InvoiceService invoiceService;

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    
    
    @Handler
    public void handle(InvoiceCreatedEvent event){
        LOG.info("InvoiceCreatedEvent Message received: {}", event);
        event.getClaim().getInvoice().setInvoiceOriginal(invoiceService.saveOriginalInvoice(event.getClaim().getInvoice()));
    } 

}
