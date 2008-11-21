package scsbre.engine.util;

import scsbre.model.ICHOBandInfo;

public class CHOBandCalcHelper {

    private ICHOBandInfo band;

    private CHOBandCalcHelper(ICHOBandInfo b) {
        band = b;
    }

    public static CHOBandCalcHelper getInstance(ICHOBandInfo b) {
        return new CHOBandCalcHelper(b);
    }

    public int getTotalLossInspectionDays() {
        return band.getOfferMadeDays() 
                + band.getReceiptOfFinalStatementChequeDays() 
                + band.getInspectionDelayDays();
    }
}
