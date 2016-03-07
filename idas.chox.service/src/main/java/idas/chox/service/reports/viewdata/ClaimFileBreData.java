package idas.chox.service.reports.viewdata;

import idas.chox.core.model.Claim;
import idas.chox.core.model.History;
import idas.chox.core.util.DateHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class ClaimFileBreData {
        private int number;
        private String createdOn;
        private String createdBy;
        private String note;

        static public List<ClaimFileBreData> getClaimFileBreData(Claim claim, boolean showPrivate) {
            List<ClaimFileBreData> claimFileBreDataList = null;

            List<History> histories = claim.getHistories();
            if (histories != null) {
                claimFileBreDataList = new ArrayList<ClaimFileBreData>();
                int noteNumber = 1;
                for (History history : histories) {
                    if (history.getType().equals("ERROR") && (history.getIsPublic() || (!history.getIsPublic() && showPrivate))) {
                        ClaimFileBreData claimFileBreData= new ClaimFileBreData();
                        claimFileBreData.createdBy = history.getCreatedBy().getFullName() + " (" +  history.getCreatedBy().getOrganisationName() + ")";
                        claimFileBreData.createdOn = DateHelper.getLocalDateTimeFormat().format(history.getCreatedDate());
                        claimFileBreData.note = history.getNarrative();
                        claimFileBreData.number = noteNumber++;
                        claimFileBreDataList.add(claimFileBreData);
                    }
                }
            }

            return claimFileBreDataList;
        }
        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
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

}
