package idas.chox.core.model;

public enum PenaltyName {

    HIRE    (0),
    REPAIR  (1);
    
    private int penaltyNameType;

    private PenaltyName(int type) {
        this.penaltyNameType = type;
    }

    public int getPenaltyNameType() {
        return penaltyNameType;
    }
}
