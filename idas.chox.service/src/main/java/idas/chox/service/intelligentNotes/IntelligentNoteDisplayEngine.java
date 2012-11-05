package idas.chox.service.intelligentNotes;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.IntelligentNote;

public class IntelligentNoteDisplayEngine {
    private static final Logger LOG = LoggerFactory.getLogger(IntelligentNoteDisplayEngine.class);

    private List<IntelligentNote> availableIntelligentNotes;

    public List<String> getIntelligentNotes(Claim c) {
        LOG.debug("Getting intelligent notes for claim {} with status {}", c.getChoReference(), c.getStatus());
        List<String> intelligentNotes = new ArrayList<String>();

        if (checkClaimStatus(c)) {
            LOG.debug("Checking notes.");
            for (IntelligentNote intelligentNote : availableIntelligentNotes) {
                LOG.debug("Checking note: '{}'", intelligentNote.getNote());
                if (intelligentNote.isShowingFor(c)) {
                    LOG.debug("Note added: ", intelligentNote.getNote());
                    intelligentNotes.add(intelligentNote.getNote());
                }
            }
        }
        else {
            LOG.debug("No initelligent notes for claim due to status.");
        }

        LOG.debug("Returning {} notes", intelligentNotes.size());

        return intelligentNotes;
    }

    public List<String> getAllIntelligentNotes(Claim c) {
        LOG.debug("Getting all intelligent notes for claim {} with status {}", c.getChoReference(), c.getStatus());
        List<String> intelligentNotes = new ArrayList<String>();

        LOG.debug("Checking notes.");
        for (IntelligentNote intelligentNote : availableIntelligentNotes) {
            LOG.debug("Checking note: '{}'", intelligentNote.getNote());
            if (intelligentNote.isShowingFor(c)) {
                LOG.debug("Note added: ", intelligentNote.getNote());
                intelligentNotes.add(intelligentNote.getNote());
            }
        }

        LOG.debug("Returning {} notes", intelligentNotes.size());
        return intelligentNotes;
    }

    //moved the claim status check from individual intelligent note object to display engine
    //due to all of the intelligent notes
    private Boolean checkClaimStatus(Claim c) {
        String[] statuses = {ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
            ClaimStatus.CLAIM_PENDING, ClaimStatus.CLAIM_REJECTION_CONTESTED,
            ClaimStatus.CLAIM_UPDATE_BY_ENG, ClaimStatus.CLAIM_REF_TO_ENG};

        List<String> statusList = Arrays.asList(statuses);

        return statusList.contains(c.getStatus());
    }

    /**
     * @return the availableIntelligentNotes
     */
    public List<IntelligentNote> getAvailableIntelligentNotes() {
        return availableIntelligentNotes;
    }

    /**
     * @param availableIntelligentNotes the availableIntelligentNotes to set
     */
    public void setAvailableIntelligentNotes(List<IntelligentNote> availableIntelligentNotes) {
        this.availableIntelligentNotes = availableIntelligentNotes;
    }

}
