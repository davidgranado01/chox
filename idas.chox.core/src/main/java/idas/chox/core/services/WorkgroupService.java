package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.Workgroup;

public interface WorkgroupService {

    Workgroup getWorkgroup(int workgroupId);

    void deleteWorkgroup(Workgroup workgroup);

    void saveWorkgroup(Workgroup workgroup);

    List<Workgroup> getActiveWorkgroupsByInsurer(int insurerId);

    List<Workgroup> getWorkgroupsByInsurer(int insurerId);

    boolean isWorkgroupNameExistByInsurer(int insurerId, String workgroupName);

    void createDefaultWorkgroup(Insurer insurer);

    List<Workgroup> getAvailableUserWorkgroupsByInsurer(int InsurerId, int webUserId);

    List<Workgroup> getAvailableAutoRoutingWorkgroupsByInsurer(int insurerId, boolean isActiveOnly);

    // TO BE REVIEWED
    boolean isWorkgroupDeletable(int workgroupId);

    boolean isInsurerWithWorkgroup(int insurerId);

    boolean isWorkgroupAllowToInactive(int insurerId, int workgroup);
}
