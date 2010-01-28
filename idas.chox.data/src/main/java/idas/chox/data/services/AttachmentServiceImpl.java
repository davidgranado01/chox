package idas.chox.data.services;

import idas.chox.core.model.Attachment;
import idas.chox.core.services.AttachmentService;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AttachmentServiceImpl extends SecureDataService implements AttachmentService {

    public List getAttachmentsByClaim(int claimId) {

        StringBuffer sb = new StringBuffer();
        sb.append("select id, version, file_name, remarks, category, file_type, last_modified_date, claim_id, created_date from attachment ");
        sb.append("where claim_id=:pClaimId");
        Map extParameters = new HashMap();
        extParameters.put("pClaimId", claimId);
        return externalQuery(sb.toString(), extParameters);
    }

    public Attachment getAttachment(int AttachmentId) {
        return (Attachment) get(Attachment.class, AttachmentId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAtatchment(int AttachmentId){
        Attachment attachment = getAttachment(AttachmentId);
        delete(attachment);
    }
}
