/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Attachment;
import java.util.List;

public interface AttachmentService {
    public Boolean saveObj(Attachment attachment);
    public List<Attachment> getAttachmentByClaimId(int claimId);
    Attachment getObject(int id);
    public Boolean deleteAttachment(Attachment att);
}
