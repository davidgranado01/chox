/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.intelligentNotes;

import chox.data.SecurityInfoProvider;
import chox.model.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;

public class IntelligentNoteDisplayEngine {

    @Autowired
    private SecurityInfoProvider securityInfoProvider;

    private List<IntelligentNote> availableIntelligentNotes;

    public List<String> getIntelligentNotes(Claim c)
    {
        List<String> intelligentNotestes = new ArrayList<String>();

        if(checkClaimStatus(c))
        {
            for(IntelligentNote intelligentNote : availableIntelligentNotes)
            {
                if(intelligentNote.isShowingFor(c,getSecurityInfoProvider()))
                {
                    intelligentNotestes.add(intelligentNote.getNote());
                }
            }
        }

        return intelligentNotestes;
    }

    //moved the claim status check from individual intelligent note object to display engine
    //due to all of the intelligent notes
    private Boolean checkClaimStatus(Claim c)
    {
         String[] statuses = {ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
            ClaimStatus.CLAIM_PENDING,ClaimStatus.CLAIM_REJECTION_CONTESTED,
            ClaimStatus.CLAIM_UPDATE_BY_ENG,ClaimStatus.CLAIM_REF_TO_ENG};

        List<String> statusList  = Arrays.asList(statuses);

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
