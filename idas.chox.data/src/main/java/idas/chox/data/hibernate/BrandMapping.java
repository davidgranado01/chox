package idas.chox.data.hibernate;

import idas.chox.core.model.Branding;

public class BrandMapping extends IntEnumCustomType<Branding> {

    public BrandMapping() {
        super(Branding.class, Branding.values());
    }
}
