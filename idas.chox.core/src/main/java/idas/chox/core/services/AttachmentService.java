package idas.chox.core.services;

import java.io.InputStream;
import java.util.List;

import idas.chox.core.model.Attachment;
import idas.chox.core.model.Claim;

public interface AttachmentService {

    List<Attachment> getAttachmentsByClaim(int claimId);

    Attachment getAttachment(int attachmentId);

    boolean deleteAtatchment(int webUserId, int AttachmentId);

    boolean addAttachment(Claim claim, InputStream streamIn, String filename, long length, String category,
                          String remark, boolean notify, boolean isInsurer, String whoCreated);

    boolean addAttachment(Claim claim, byte[] fileContent, String filename, long length, String category,
                          String remark, boolean notify, boolean isInsurer, String whoCreated);
}
