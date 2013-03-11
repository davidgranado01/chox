package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.ReasonOfDelay;

public interface ReasonOfDelayService {

    ReasonOfDelay getReasonOfDelay(int reasonOfDelayId);

    List<ReasonOfDelay> getReasonOfDelay();

    List<ReasonOfDelay> getAllReasonOfDelay();
}
