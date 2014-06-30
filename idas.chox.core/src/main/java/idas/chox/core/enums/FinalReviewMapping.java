package idas.chox.core.enums;

import java.util.ArrayList;
import java.util.List;

public enum FinalReviewMapping {

    /* For Chox Admin different description is used for some description at the LookupServiceImpl.java located in the data package.*//* For Chox Admin different description is used for some description at the LookupServiceImpl.java located in the data package.*/
    CHECK_NOT_REQUIRED  (0, "N/A"),
    CHO_TRUE            (1, "Yes"),
    CHO_FALSE           (2, "No"),
    INS_TRUE            (3, "Yes"),
    INS_FALSE           (4, "No"),
    CHO_OR_INS_TRUE    (5, "Ins or Cho Yes"),
    CHO_AND_INS_FALSE   (6, "Ins & Cho No");

    private final int value;
    private final String description;

    FinalReviewMapping(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return description;
    }

    public static List<FinalReviewMapping> getChoFinalReviewMappings() {
        List<FinalReviewMapping> choFinalReviewMappings = new ArrayList<FinalReviewMapping>();
//        choFinalReviewMappings.add(CHECK_NOT_REQUIRED);
        choFinalReviewMappings.add(CHO_TRUE);
        choFinalReviewMappings.add(CHO_FALSE);
        return choFinalReviewMappings;
    }

    public static List<FinalReviewMapping> getInsFinalReviewMappings() {
        List<FinalReviewMapping> insFinalReviewMappings = new ArrayList<FinalReviewMapping>();
//        insFinalReviewMappings.add(CHECK_NOT_REQUIRED);
        insFinalReviewMappings.add(INS_TRUE);
        insFinalReviewMappings.add(INS_FALSE);
        return insFinalReviewMappings;
    }

    public static List<FinalReviewMapping> getChoxAdminFinalReviewMappings() {
        List<FinalReviewMapping> choxAdminFinalReviewMappings = new ArrayList<FinalReviewMapping>();
//        choxAdminFinalReviewMappings.add(CHECK_NOT_REQUIRED);
        choxAdminFinalReviewMappings.add(CHO_TRUE);
        choxAdminFinalReviewMappings.add(CHO_FALSE);
        choxAdminFinalReviewMappings.add(INS_TRUE);
        choxAdminFinalReviewMappings.add(INS_FALSE);
        choxAdminFinalReviewMappings.add(CHO_OR_INS_TRUE);
        choxAdminFinalReviewMappings.add(CHO_AND_INS_FALSE);
        return choxAdminFinalReviewMappings;
    }
}
