package idas.chox.service.reports.viewdata;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.util.DateHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class ClaimFileEcdData {
        private int number;
        private String date;
        private String reason;
        private String note;

        static public List<ClaimFileEcdData> getClaimFileEcdData(Claim claim) {
            List<ClaimFileEcdData> claimFileEcdDataList = null;

            List<HireMonitoringEcd> hireMonitoringEcds = claim.getHireMonitoringEcds();
            if (hireMonitoringEcds != null) {
                int ecdNumber = 1;
                claimFileEcdDataList = new ArrayList<ClaimFileEcdData>();
                for (HireMonitoringEcd hireMonitoringEcd : hireMonitoringEcds) {
                    ClaimFileEcdData claimFileEcdData= new ClaimFileEcdData();
                    claimFileEcdData.date = DateHelper.LocalDateTimeFormat.format(hireMonitoringEcd.getEcdDate());
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
