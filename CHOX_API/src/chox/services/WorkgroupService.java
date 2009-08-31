package chox.services;

import chox.model.Workgroup;
import java.util.List;

public interface WorkgroupService {

    public List<Workgroup> getObjects(int insurerId);
    public Workgroup getObject(int id);
    public boolean DeleteObject(Workgroup object);
    public void updateObject(Workgroup object);
    public boolean isWorkgroupExist(int insurerId, String workgroupName);
    public boolean isWorkgroupDeletable(int workgroupId);
}
