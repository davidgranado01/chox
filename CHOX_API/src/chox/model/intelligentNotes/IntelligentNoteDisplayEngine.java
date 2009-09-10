/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.intelligentNotes;

import chox.data.SecurityInfoProvider;
import chox.model.Claim;
import chox.model.IntelligentNote;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author emmanuel
 */
public class IntelligentNoteDisplayEngine {

    @Autowired
    private SecurityInfoProvider securityInfoProvider;

    private List<IntelligentNote> availableIntelligentNotes;

    public List<String> getIntelligentNotes(Claim c)
    {
        List<String> intelligentNotestes = new ArrayList<String>();

        for(IntelligentNote intelligentNote : availableIntelligentNotes)
        {
            if(intelligentNote.isShowingFor(c,getSecurityInfoProvider()))
            {
                intelligentNotestes.add(intelligentNote.getNote());
            }
        }

        return intelligentNotestes;
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
