package idas.chox.service.reports.viewdata;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.util.DateHelper;


/**
 *
 * @author John
 */
public class ClaimFileNoteData {

    private final int number;
    private final String createdOn;
    private final String createdBy;
    private final String note;

    private ClaimFileNoteData(int number, String createdOn, String createdBy, String note) {
        this.number = number;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.note = note;
    }
    
    static public List<ClaimFileNoteData> getClaimFileNoteData(Claim claim, boolean showInsurer, boolean showCHO) {
        List<ClaimFileNoteData> claimFileNoteDataList = null;

        List<Comment> comments = claim.getComments();
        if (comments != null) {
            claimFileNoteDataList = new ArrayList<>();
            int noteNumber = 1;
            for (Comment comment : comments) {
                if (!comment.isReverted() && (comment.getVisibilityType() == 0
                        || (comment.getVisibilityType() == 1 && showInsurer)
                        || (comment.getVisibilityType() == 2 && showCHO))) {
                    ClaimFileNoteData claimFileNoteData = new ClaimFileNoteData(
                                noteNumber++,
                                DateHelper.getLocalDateTimeFormat().format(comment.getCreatedDate()),
                                comment.getRaisedBy() != null ? comment.getRaisedBy().getFullName()
                                                              : comment.getCreatedBy().getFullName(),
                                StringEscapeUtils.unescapeHtml4(comment.getComment()));
                                
                    claimFileNoteDataList.add(claimFileNoteData);
                }
            }
        }

        return claimFileNoteDataList;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getNote() {
        return note;
    }

    public int getNumber() {
        return number;
    }

}
