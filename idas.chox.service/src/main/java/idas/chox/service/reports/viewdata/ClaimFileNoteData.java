package idas.chox.service.reports.viewdata;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.util.DateHelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author John
 */
public class ClaimFileNoteData {

    private int number;
    private String createdOn;
    private String createdBy;
    private String note;

    static public List<ClaimFileNoteData> getClaimFileNoteData(Claim claim, boolean showInsurer, boolean showCHO) {
        List<ClaimFileNoteData> claimFileNoteDataList = null;

        List<Comment> comments = claim.getComments();
        if (comments != null) {
            claimFileNoteDataList = new ArrayList<ClaimFileNoteData>();
            int noteNumber = 1;
            for (Comment comment : comments) {
                if (!comment.isReverted() && (comment.getVisibilityType() == 0
                        || (comment.getVisibilityType() == 1 && showInsurer)
                        || (comment.getVisibilityType() == 2 && showCHO))) {
                    ClaimFileNoteData claimFileNoteData = new ClaimFileNoteData();
                    if (comment.getRaisedBy() != null) {
                        claimFileNoteData.createdBy = comment.getRaisedBy().getFullName();
                    } else {
                        claimFileNoteData.createdBy = comment.getCreatedBy().getFullName();
                    }
                    claimFileNoteData.createdOn = DateHelper.getLocalDateTimeFormat().format(comment.getCreatedDate());
                    claimFileNoteData.note = StringEscapeUtils.unescapeHtml4(comment.getComment());
                    claimFileNoteData.number = noteNumber++;
                    claimFileNoteDataList.add(claimFileNoteData);
                }
            }
        }

        return claimFileNoteDataList;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.createdBy = CreatedBy;
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
