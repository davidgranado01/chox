package chox.services;

import chox.model.ReasonOfDelay;
import java.util.List;

public interface ReasonOfDelayService {

    public ReasonOfDelay getObject(int id);
    public List<ReasonOfDelay> getReasonOfDelay();
    public List<ReasonOfDelay> getAllReasonOfDelay();
    
}
