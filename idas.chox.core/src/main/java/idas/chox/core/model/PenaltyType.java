package idas.chox.core.model;

public enum PenaltyType {

    DEFAULT     (0),
    SUBSCRIBER  (1);
    
    private final int type;

    PenaltyType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
