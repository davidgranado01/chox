package idas.chox.core.common;

public class OrganisationType {

    public static final String CHOX = "Chox";
    public static final String CHO = "CreditHire";
    public static final String INS = "Insurer";

    public static String getOrganisationType(int iOrganisationTypeCode){

        if (iOrganisationTypeCode == 1) {
            return OrganisationType.CHOX;
        } else if (iOrganisationTypeCode == 2) {
            return OrganisationType.INS;
        } else if (iOrganisationTypeCode == 3) {
            return OrganisationType.CHO;
        }

        return "";
    }
        public static int getOrganisationTypeId(String organisationTypeCode){
            if (organisationTypeCode.equals(CHOX))
                return 1;
            else if (organisationTypeCode.equals(INS))
                return 2;
            else if (organisationTypeCode.equals(CHO))
                return 3;

            return 0;
        }

}
