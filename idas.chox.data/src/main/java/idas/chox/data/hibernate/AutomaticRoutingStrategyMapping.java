package idas.chox.data.hibernate;

import idas.chox.core.model.AutomaticRoutingStrategy;

/**
 *
 * @author John
 */
public class AutomaticRoutingStrategyMapping extends IntEnumCustomType<AutomaticRoutingStrategy> {
    public AutomaticRoutingStrategyMapping(){
        super(AutomaticRoutingStrategy.class, AutomaticRoutingStrategy.values());
    }
}
