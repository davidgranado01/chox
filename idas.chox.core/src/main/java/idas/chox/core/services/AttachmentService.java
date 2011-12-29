package idas.chox.core.services;

import idas.chox.core.model.Attachment;
import java.util.List;

public interface AttachmentService {

    public List<Attachment> getAttachmentsByClaim(int claimId);

    Attachment getAttachment(int attachmentId);

    public boolean deleteAtatchment(int webUserId, int AttachmentId);
}
