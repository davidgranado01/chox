package scsbre.tests.sample;

import scsbre.model.ICHOrganisationInfo;

public class CHOrganisationInfo implements ICHOrganisationInfo {

    private Boolean isDelegatedAuthority;

    /* (non-Javadoc)
     * @see scsbre.model.ICHOrganisationInfo#getIsDelegatedAuthority()
     */
    public boolean getIsDelegatedAuthority() {
        return isDelegatedAuthority;
    }
    /* (non-Javadoc)
     * @see scsbre.model.ICHOrganisationInfo#setIsDelegatedAuthority(boolean)
     */

    public void setIsDelegatedAuthority(boolean isDelegatedAuthority) {
        this.isDelegatedAuthority = isDelegatedAuthority;
    }
}
