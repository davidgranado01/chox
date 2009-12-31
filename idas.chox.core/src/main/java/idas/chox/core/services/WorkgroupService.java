package idas.chox.core.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.Workgroup;
import java.util.List;

public interface WorkgroupService {

    public Workgroup getWorkgroup(int id);

    public void delete(Workgroup object);

    public void save(Workgroup object);

    public List<Workgroup> getWorkgroupsByInsurer(int insurerId);

    public List<Workgroup> getAllWorkgroupsByInsurer(int insurerId, String strOrder);

    public boolean isWorkgroupNameExistByInsurer(int insurerId, String workgroupName);

    public void defaultWorkgroup(Insurer insurer);

    public List<Workgroup> getAvailableWorkgroupsByInsurer(int InsurerId, int webUserId);

    // TO BE REVIEWED
    public boolean isWorkgroupDeletable(int workgroupId);

    public boolean isInsurerAllowToEnableWorkgroup(Insurer insurer);

    public boolean isWorkgroupAllowToInactive(int insurerId, int workgroup);
}
