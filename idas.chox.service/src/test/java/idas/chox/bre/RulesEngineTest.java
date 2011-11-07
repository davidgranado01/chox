package idas.chox.bre;

import idas.chox.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;

public class RulesEngineTest extends BaseTest {

    @Test
    public void testSpringInjection() throws Exception {
        Assert.assertNotNull(rulesEngine);
        Assert.assertNotNull(rulesEngine.getBusinessRules());
        Assert.assertFalse(rulesEngine.getBusinessRules().isEmpty());
    }

}
