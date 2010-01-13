/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.bre;

import idas.chox.core.bre.RulesEngine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class RulesEngineTest {

    @Autowired
    RulesEngine rulesEngine;

    @Test
    public void testSpringInjection() throws Exception {
        Assert.assertNotNull(rulesEngine);
        Assert.assertNotNull(rulesEngine.getBusinessRules());
        Assert.assertFalse(rulesEngine.getBusinessRules().isEmpty());
    }

}
