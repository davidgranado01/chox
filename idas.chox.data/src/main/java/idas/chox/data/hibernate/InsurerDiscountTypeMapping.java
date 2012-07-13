package idas.chox.data.hibernate;

import idas.chox.core.model.InsurerDiscountType;

/**
 *
 * @author John
 */
public class InsurerDiscountTypeMapping extends IntEnumCustomType<InsurerDiscountType> {
    public InsurerDiscountTypeMapping(){
        super(InsurerDiscountType.class, InsurerDiscountType.values());
    }
}
