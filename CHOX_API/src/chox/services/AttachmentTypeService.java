/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.AttachmentType;
import java.util.List;

public interface AttachmentTypeService {
    public List<AttachmentType> getAttachmentType();
    public List<AttachmentType> getAllAttachmentType();
    public List<String> getAttachmentTypeCode();
    public AttachmentType getAttachmentType(String code);
}
