package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.InsurerHireMonitoringEcd;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author John
 */
public class ClaimFileInsurerEcdData {
        private int number;
        private String date;
        private String reason;
        private String note;

        static public List<ClaimFileInsurerEcdData> getClaimFileEcdData(Claim claim) {
            List<ClaimFileInsurerEcdData> claimFileEcdDataList = null;

            List<InsurerHireMonitoringEcd> hireMonitoringEcds = claim.getInsurerHireMonitoringEcds();
            if (hireMonitoringEcds != null) {
                int ecdNumber = 1;
                claimFileEcdDataList = new ArrayList<>();
                for (InsurerHireMonitoringEcd hireMonitoringEcd : hireMonitoringEcds) {
                    ClaimFileInsurerEcdData claimFileEcdData= new ClaimFileInsurerEcdData();
                    claimFileEcdData.date = DateHelper.getLocalDateTimeFormat().format(hireMonitoringEcd.getEcdDate());
                    claimFileEcdData.note = hireMonitoringEcd.getSupportingNote();
                    claimFileEcdData.number = ecdNumber++;
                    claimFileEcdData.reason = hireMonitoringEcd.getReason();
                    claimFileEcdDataList.add(claimFileEcdData);
                }
            }
            return claimFileEcdDataList;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

}
