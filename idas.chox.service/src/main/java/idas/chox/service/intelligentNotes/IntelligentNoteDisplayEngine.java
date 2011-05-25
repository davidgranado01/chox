package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.IntelligentNote;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntelligentNoteDisplayEngine {
    private static final Logger LOG = LoggerFactory.getLogger(IntelligentNoteDisplayEngine.class);

    @Autowired
    private SecurityInfoProvider securityInfoProvider;
    private List<IntelligentNote> availableIntelligentNotes;

    public List<String> getIntelligentNotes(Claim c) {
        LOG.debug("Getting intelligent notes for claim {} with status {}", c.getChoReference(), c.getStatus());
        List<String> intelligentNotes = new ArrayList<String>();

        if (checkClaimStatus(c)) {
            LOG.debug("Checking notes.");
            for (IntelligentNote intelligentNote : availableIntelligentNotes) {
                LOG.debug("Checking note: '{}'", intelligentNote.getNote());
                if (intelligentNote.isShowingFor(c, getSecurityInfoProvider())) {
                    LOG.debug("Note added: ", intelligentNote.getNote());
                    intelligentNotes.add(intelligentNote.getNote());
                }
            }
        }
        else
            LOG.debug("No initelligent notes for claim due to status.");

        LOG.debug("Returning {} notes", intelligentNotes.size());

        return intelligentNotes;
    }

    public List<String> getAllIntelligentNotes(Claim c) {
        LOG.debug("Getting all intelligent notes for claim {} with status {}", c.getChoReference(), c.getStatus());
        List<String> intelligentNotes = new ArrayList<String>();

        LOG.debug("Checking notes.");
        for (IntelligentNote intelligentNote : availableIntelligentNotes) {
            LOG.debug("Checking note: '{}'", intelligentNote.getNote());
            if (intelligentNote.isShowingFor(c, getSecurityInfoProvider())) {
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

    /**
     * @return the securityInfoProvider
     */
    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    /**
     * @param securityInfoProvider the securityInfoProvider to set
     */
    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }
}
