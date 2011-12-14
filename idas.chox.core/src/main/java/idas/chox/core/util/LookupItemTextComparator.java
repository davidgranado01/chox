package idas.chox.core.util;

import idas.chox.core.model.LookupItem;
import java.util.Comparator;

public class LookupItemTextComparator implements Comparator {

    @Override
    public int compare(Object lookupItem1, Object lookupItem2) {

        String text1 = ((LookupItem) lookupItem1).getText();
        String text2 = ((LookupItem) lookupItem2).getText();

        return text1.compareTo(text2);

    }
}
