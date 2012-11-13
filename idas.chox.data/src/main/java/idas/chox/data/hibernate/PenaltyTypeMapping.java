package idas.chox.data.hibernate;

import idas.chox.core.model.PenaltyType;

public class PenaltyTypeMapping extends IntEnumCustomType<PenaltyType> {

    public PenaltyTypeMapping() {
        super(PenaltyType.class, PenaltyType.values());
    }
}
