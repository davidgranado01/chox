package chox.services;

import chox.model.ReasonOfRejection;
import java.util.List;

public interface ReasonOfRejectionService {

    public ReasonOfRejection getObject(int id);

    public void updateObject(ReasonOfRejection reasonOfRejection);

    public List<ReasonOfRejection> getAllReasonOfRejection();
}
