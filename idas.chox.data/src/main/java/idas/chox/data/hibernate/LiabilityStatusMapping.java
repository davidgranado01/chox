package idas.chox.data.hibernate;

import idas.chox.core.model.LiabilityStatus;

/**
 *
 * @author abrar
 */
public class LiabilityStatusMapping extends IntEnumCustomType<LiabilityStatus>{
    public LiabilityStatusMapping(){
        super(LiabilityStatus.class,LiabilityStatus.values());
    }
}
