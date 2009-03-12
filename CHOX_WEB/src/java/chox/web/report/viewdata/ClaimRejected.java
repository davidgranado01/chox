/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report.viewdata;

import chox.Util.MathHelper;
import chox.model.ReasonOfRejection;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class ClaimRejected {
    
    private int id;
    private String name;    
    
    private Integer OrgCount0 = 0;
    private Integer OrgCount1 = 0;
    private Integer OrgCount2 = 0;
    private Integer OrgCount3 = 0;
    private Integer OrgCount4 = 0;
    private Integer OrgCount5 = 0;
    private Integer OrgCount6 = 0;
    private Integer OrgCount7 = 0;
    private Integer OrgCount8 = 0;
    private Integer OrgCount9 = 0;
    private Integer OrgCountAll = 0;
    
    private BigDecimal OrgPercentage0 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage1 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage2 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage3 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage4 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage5 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage6 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage7 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage8 = new BigDecimal(0.00);
    private BigDecimal OrgPercentage9 = new BigDecimal(0.00);
    private BigDecimal OrgPercentageAll = new BigDecimal(0.00);
    
    public Integer getOrgCount0() {
        return OrgCount0;
    }

    public void setOrgCount0(Integer OrgCount0) {
        this.OrgCount0 = OrgCount0;
    }

    public Integer getOrgCount1() {
        return OrgCount1;
    }

    public void setOrgCount1(Integer OrgCount1) {
        this.OrgCount1 = OrgCount1;
    }

    public Integer getOrgCount2() {
        return OrgCount2;
    }

    public void setOrgCount2(Integer OrgCount2) {
        this.OrgCount2 = OrgCount2;
    }

    public Integer getOrgCount3() {
        return OrgCount3;
    }

    public void setOrgCount3(Integer OrgCount3) {
        this.OrgCount3 = OrgCount3;
    }

    public Integer getOrgCount4() {
        return OrgCount4;
    }

    public void setOrgCount4(Integer OrgCount4) {
        this.OrgCount4 = OrgCount4;
    }

    public Integer getOrgCount5() {
        return OrgCount5;
    }

    public void setOrgCount5(Integer OrgCount5) {
        this.OrgCount5 = OrgCount5;
    }

    public Integer getOrgCount6() {
        return OrgCount6;
    }

    public void setOrgCount6(Integer OrgCount6) {
        this.OrgCount6 = OrgCount6;
    }

    public Integer getOrgCount7() {
        return OrgCount7;
    }

    public void setOrgCount7(Integer OrgCount7) {
        this.OrgCount7 = OrgCount7;
    }

    public Integer getOrgCount8() {
        return OrgCount8;
    }

    public void setOrgCount8(Integer OrgCount8) {
        this.OrgCount8 = OrgCount8;
    }

    public Integer getOrgCount9() {
        return OrgCount9;
    }

    public void setOrgCount9(Integer OrgCount9) {
        this.OrgCount9 = OrgCount9;
    }

    public BigDecimal getOrgPercentage0() {
        return OrgPercentage0;
    }

    public void setOrgPercentage0(BigDecimal OrgPercentage0) {
        this.OrgPercentage0 = OrgPercentage0;
    }

    public BigDecimal getOrgPercentage1() {
        return OrgPercentage1;
    }

    public void setOrgPercentage1(BigDecimal OrgPercentage1) {
        this.OrgPercentage1 = OrgPercentage1;
    }

    public BigDecimal getOrgPercentage2() {
        return OrgPercentage2;
    }

    public void setOrgPercentage2(BigDecimal OrgPercentage2) {
        this.OrgPercentage2 = OrgPercentage2;
    }

    public BigDecimal getOrgPercentage3() {
        return OrgPercentage3;
    }

    public void setOrgPercentage3(BigDecimal OrgPercentage3) {
        this.OrgPercentage3 = OrgPercentage3;
    }

    public BigDecimal getOrgPercentage4() {
        return OrgPercentage4;
    }

    public void setOrgPercentage4(BigDecimal OrgPercentage4) {
        this.OrgPercentage4 = OrgPercentage4;
    }

    public BigDecimal getOrgPercentage5() {
        return OrgPercentage5;
    }

    public void setOrgPercentage5(BigDecimal OrgPercentage5) {
        this.OrgPercentage5 = OrgPercentage5;
    }

    public BigDecimal getOrgPercentage6() {
        return OrgPercentage6;
    }

    public void setOrgPercentage6(BigDecimal OrgPercentage6) {
        this.OrgPercentage6 = OrgPercentage6;
    }

    public BigDecimal getOrgPercentage7() {
        return OrgPercentage7;
    }

    public void setOrgPercentage7(BigDecimal OrgPercentage7) {
        this.OrgPercentage7 = OrgPercentage7;
    }

    public BigDecimal getOrgPercentage8() {
        return OrgPercentage8;
    }

    public void setOrgPercentage8(BigDecimal OrgPercentage8) {
        this.OrgPercentage8 = OrgPercentage8;
    }

    public BigDecimal getOrgPercentage9() {
        return OrgPercentage9;
    }

    public void setOrgPercentage9(BigDecimal OrgPercentage9) {
        this.OrgPercentage9 = OrgPercentage9;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrgCountAll() {
        return OrgCountAll;
    }

    public void setOrgCountAll(Integer OrgCountAll) {
        this.OrgCountAll = OrgCountAll;
    }

    public BigDecimal getOrgPercentageAll() {
        return OrgPercentageAll;
    }

    public void setOrgPercentageAll(BigDecimal OrgPercentageAll) {
        this.OrgPercentageAll = OrgPercentageAll;
    }
    
}
