package idas.chox.core.services;

import idas.chox.core.model.Claim;
import org.w3c.dom.Element;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClassCeiling;
import java.util.List;

public interface InsurerService {

    public boolean isInsurerNameExist(String insurerName);

    Insurer getInsurerByName(String insurerName);

    Insurer getInsurerByNodeName(Element thisElement, String nodeName);

    public Insurer getInsurer(int insurerId);

    public List<Insurer> getInsurers();

    public Insurer updateInsurer(Insurer insurer);

    public VehicleClassCeiling getVechileClassCeilingForClaim(Claim claim);
}
