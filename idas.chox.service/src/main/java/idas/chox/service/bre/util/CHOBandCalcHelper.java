package idas.chox.service.bre.util;

import idas.chox.core.model.BreBand;


public final class CHOBandCalcHelper {

    private BreBand band;

    private CHOBandCalcHelper(BreBand b) {
        band = b;
    }

    public static CHOBandCalcHelper getInstance(BreBand b) {
        return new CHOBandCalcHelper(b);
    }

    public int getTotalLossInspectionDays() {
        return band.getOfferMadeDays() 
                + band.getReceiptOfFinalStatementChequeDays() 
                + band.getInspectionDelayDays();
    }
}
