package chox.services;

import chox.model.ReasonOfRejection;

public class ReasonOfRejectionServiceImpl  extends DataService implements ReasonOfRejectionService{

    public ReasonOfRejection getObject(int id) {
        return (ReasonOfRejection) get(ReasonOfRejection.class, id);
    }

    public void updateObject(ReasonOfRejection reasonOfRejection) {
        save(reasonOfRejection);
    }
    
}
