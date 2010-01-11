package idas.chox.core.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.Workgroup;
import java.util.List;

public interface WorkgroupService {

    public Workgroup getWorkgroup(int workgroupId);

    public void deleteWorkgroup(Workgroup workgroup);

    public void saveWorkgroup(Workgroup workgroup);

    public List<Workgroup> getActiveWorkgroupsByInsurer(int insurerId);

    public List<Workgroup> getWorkgroupsByInsurer(int insurerId);

    public boolean isWorkgroupNameExistByInsurer(int insurerId, String workgroupName);

    public void createDefaultWorkgroup(Insurer insurer);

    public List<Workgroup> getAvailableUserWorkgroupsByInsurer(int InsurerId, int webUserId);

    public List<Workgroup> getAvailableAutoRoutingWorkgroupsByInsurer(int insurerId);

    // TO BE REVIEWED
    public boolean isWorkgroupDeletable(int workgroupId);

    public boolean isInsurerWithWorkgroup(int insurerId);

    public boolean isWorkgroupAllowToInactive(int insurerId, int workgroup);
}
