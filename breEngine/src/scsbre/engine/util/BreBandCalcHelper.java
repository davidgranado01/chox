package scsbre.engine.util;

import scsbre.model.IBREBandInfo;

public class BreBandCalcHelper {

    private IBREBandInfo band;

    private BreBandCalcHelper(IBREBandInfo b) {
        band = b;
    }

    public static BreBandCalcHelper getInstance(IBREBandInfo b) {
        return new BreBandCalcHelper(b);
    }

    public int getTotalLossInspectionDays() {
        return band.getOfferMadeDays() 
                + band.getReceiptOfFinalStatementChequeDays() 
                + band.getInspectionDelayDays();
    }
}
