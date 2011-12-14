package idas.chox.core.util;

import idas.chox.core.model.LookupItem;
import java.util.Comparator;

public class LookupItemValueComparator implements Comparator {

    @Override
    public int compare(Object lookupItem1, Object lookupItem2) {

        String value1 = ((LookupItem) lookupItem1).getValue();
        String value2 = ((LookupItem) lookupItem2).getValue();

        return value1.compareTo(value2);

    }
}
