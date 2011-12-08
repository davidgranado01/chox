package idas.chox.data.hibernate;

import idas.chox.core.model.ClaimType;

/**
 *
 * @author John
 */
public class ClaimTypeMapping extends IntEnumCustomType<ClaimType> {
    public ClaimTypeMapping(){
        super(ClaimType.class, ClaimType.values());
    }
}
