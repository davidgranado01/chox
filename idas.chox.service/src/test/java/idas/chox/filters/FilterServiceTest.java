/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.filters;

import idas.chox.core.model.Filter;
import idas.chox.core.services.FilterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-Filters-test.xml"})
public class FilterServiceTest {

    @Autowired
    FilterService filterService;

    @Test
    public void testSpringInjection() throws Exception {
        Assert.assertNotNull(filterService);
        Assert.assertNotNull(filterService.getAvailableFilters(null));
        Assert.assertFalse(filterService.getAvailableFilters(null).isEmpty());
    }

    @Test
    public void testRejectedClaims() throws Exception {
        Filter filter = filterService.getFilter("RejectedClaims");
        Assert.assertEquals(Integer.valueOf(0), filter.getCount());
        Assert.assertNotNull(filter.getResults(0, 10, "", ""));
        System.out.println(filter.getDescription());
    }
}
