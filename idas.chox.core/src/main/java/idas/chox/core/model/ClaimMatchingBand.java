package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author john
 */
public class ClaimMatchingBand extends Entity implements Serializable, FullAudit {
    private BreBand breBand;
    private ClaimType claimType;
    private BigDecimal liabilityPercentage;
    private boolean autoAcknowledge;
    private boolean bClass;
    private boolean cmClass;
    private boolean cpClass;
    private boolean csClass;
    private boolean cvClass;
    private boolean fClass;
    private boolean mClass;
    private boolean ntClass;
    private boolean pClass;
    private boolean ptClass;
    private boolean pvClass;
    private boolean rvClass;
    private boolean sClass;
    private boolean spClass;
    private boolean tClass;
    private boolean uClass;

    public BreBand getBreBand() {
        return breBand;
    }

    public void setBreBand(BreBand breBand) {
        this.breBand = breBand;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public BigDecimal getLiabilityPercentage() {
        return liabilityPercentage;
    }

    public void setLiabilityPercentage(BigDecimal liabilityPercentage) {
        this.liabilityPercentage = liabilityPercentage;
    }

    public boolean isAutoAcknowledge() {
        return autoAcknowledge;
    }

    public void setAutoAcknowledge(boolean autoAcknowledge) {
        this.autoAcknowledge = autoAcknowledge;
    }

    public boolean isbClass() {
        return bClass;
    }

    public void setbClass(boolean bClass) {
        this.bClass = bClass;
    }

    public boolean isCmClass() {
        return cmClass;
    }

    public void setCmClass(boolean cmClass) {
        this.cmClass = cmClass;
    }

    public boolean isCpClass() {
        return cpClass;
    }

    public void setCpClass(boolean cpClass) {
        this.cpClass = cpClass;
    }

    public boolean isCsClass() {
        return csClass;
    }

    public void setCsClass(boolean csClass) {
        this.csClass = csClass;
    }

    public boolean isCvClass() {
        return cvClass;
    }

    public void setCvClass(boolean cvClass) {
        this.cvClass = cvClass;
    }

    public boolean isfClass() {
        return fClass;
    }

    public void setfClass(boolean fClass) {
        this.fClass = fClass;
    }

    public boolean ismClass() {
        return mClass;
    }

    public void setmClass(boolean mClass) {
        this.mClass = mClass;
    }

    public boolean isNtClass() {
        return ntClass;
    }

    public void setNtClass(boolean ntClass) {
        this.ntClass = ntClass;
    }

    public boolean ispClass() {
        return pClass;
    }

    public void setpClass(boolean pClass) {
        this.pClass = pClass;
    }

    public boolean isPtClass() {
        return ptClass;
    }

    public void setPtClass(boolean ptClass) {
        this.ptClass = ptClass;
    }

    public boolean isPvClass() {
        return pvClass;
    }

    public void setPvClass(boolean pvClass) {
        this.pvClass = pvClass;
    }

    public boolean isRvClass() {
        return rvClass;
    }

    public void setRvClass(boolean rvClass) {
        this.rvClass = rvClass;
    }

    public boolean issClass() {
        return sClass;
    }

    public void setsClass(boolean sClass) {
        this.sClass = sClass;
    }

    public boolean isSpClass() {
        return spClass;
    }

    public void setSpClass(boolean spClass) {
        this.spClass = spClass;
    }

    public boolean istClass() {
        return tClass;
    }

    public void settClass(boolean tClass) {
        this.tClass = tClass;
    }

    public boolean isuClass() {
        return uClass;
    }

    public void setuClass(boolean uClass) {
        this.uClass = uClass;
    }
    
    
}
