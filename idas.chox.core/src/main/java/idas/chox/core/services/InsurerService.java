package idas.chox.core.services;

import java.util.List;

import org.w3c.dom.Element;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClassCeiling;

public interface InsurerService {

    boolean isInsurerNameExist(String insurerName);

    Insurer getInsurerByName(String insurerName);

    Insurer getInsurerByNodeName(Element thisElement, String nodeName);

    Insurer getInsurer(int insurerId);

    List<Insurer> getInsurers();

    void saveInsurer(Insurer insurer);

    VehicleClassCeiling getVechileClassCeilingForClaim(Claim claim);
}
