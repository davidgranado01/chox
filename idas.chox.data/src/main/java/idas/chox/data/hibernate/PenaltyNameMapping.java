package idas.chox.data.hibernate;

import idas.chox.core.model.PenaltyName;

public class PenaltyNameMapping extends IntEnumCustomType<PenaltyName> {

    public PenaltyNameMapping() {
        super(PenaltyName.class, PenaltyName.values());
    }
}