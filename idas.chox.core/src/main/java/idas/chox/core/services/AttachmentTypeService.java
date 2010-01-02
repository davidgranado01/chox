package idas.chox.core.services;

import idas.chox.core.model.AttachmentType;
import java.util.List;

public interface AttachmentTypeService {

    public List<AttachmentType> getAllAttachmentType();

    public List<String> getAttachmentTypeCode();

    public AttachmentType getAttachmentType(String code);
}
