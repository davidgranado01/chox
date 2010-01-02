package idas.chox.core.services;

import idas.chox.core.model.ReasonOfDelay;
import java.util.List;

public interface ReasonOfDelayService {

    public ReasonOfDelay getReasonOfDelay(int reasonOfDelayId);

    public List<ReasonOfDelay> getReasonOfDelay();

    public List<ReasonOfDelay> getAllReasonOfDelay();
}
