package chox.services;

import chox.model.Insurer;
import chox.model.Workgroup;
import java.util.List;

public interface WorkgroupService {

    public List<Workgroup> getObjects(int insurerId);
    public List<Workgroup> getAllObjects(int insurerId);
    public Workgroup getObject(int id);
    public boolean DeleteObject(Workgroup object);
    public void updateObject(Workgroup object);
    public boolean isWorkgroupExist(int insurerId, String workgroupName);
    public boolean isWorkgroupDeletable(int workgroupId);
    public void createDefaultRecord(Insurer insurer);
    public boolean isInsurerAllowToEnableWorkgroup(Insurer insurer);
    public boolean isWorkgroupAllowToInactive(int insurerId, int workgroup);
}
