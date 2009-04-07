package chox.services;

import chox.model.ReasonOfRejection;

public interface ReasonOfRejectionService {    
    public ReasonOfRejection getObject(int id);
    public void updateObject(ReasonOfRejection reasonOfRejection);
}
