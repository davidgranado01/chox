package idas.chox.core.workflow;

import java.util.List;

import idas.chox.core.model.EmailAttachment;

public interface ScheduleActivity {

    public boolean process(String body, List<EmailAttachment> attachments, String from, String subject) throws Exception;
    
    public String getResponse(String subject, String from);
    
}
