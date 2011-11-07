package idas.chox.filters;

import idas.chox.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;

public class FilterServiceTest extends BaseTest {

    @Test
    public void testSpringInjection() throws Exception {
        Assert.assertNotNull(filterService);
        Assert.assertNotNull(filterService.getAvailableFilters(null));
        Assert.assertFalse(filterService.getAvailableFilters(null).isEmpty());
    }

    @Test
    public void testRejectedClaims() throws Exception {
//        Filter filter = filterService.getFilter("RejectedClaims");
//        Assert.assertEquals(Integer.valueOf(0), filter.getCount());
//        Assert.assertNotNull(filter.getResults(0, 10, "", ""));
//        System.out.println(filter.getDescription());
    }
}
