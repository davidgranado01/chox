package idas.chox.core.services;

import idas.chox.core.model.AttachmentType;
import java.util.List;

public interface AttachmentTypeService {

    List<AttachmentType> getAllAttachmentType();

    List<String> getAttachmentTypeCode();

    AttachmentType getAttachmentType(String code);
}
