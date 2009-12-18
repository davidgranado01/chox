package idas.chox.dashboard;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerService;
import idas.chox.data.services.DataService;
import idas.chox.service.dashboard.ChoDashboardBuilder;
import idas.chox.service.dashboard.InsurerDashboardBuilder;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml"})
public class DashboardTest {

    @Autowired
    InsurerService insurerService;
    @Autowired
    ChorganisationService chorganisationService;
    @Autowired
    @Qualifier("dataService")
    DataService dataService;

    @Test
    public void testInsurerDashBoard(){
        //TODO: ADD UNIT TEST
/*
        String[] objectIds = {"-1"};
        Map extParameters = new HashMap();
        extParameters.put("supplierId", objectIds);

        Insurer insurer = insurerService.getInsurerByName("RSA");
        InsurerDashboardBuilder builder = new InsurerDashboardBuilder(dataService, insurer, extParameters);

        Assert.assertEquals((int)3809, (int)builder.getCumulative().getNoOfClaimNotificationsSubmitted());
        Assert.assertEquals((int)1367, (int)builder.getCumulative().getNoOfClaimNotificationsAcceptedAccumulative());
        Assert.assertEquals((int)2276, (int)builder.getCumulative().getNoOfClaimNotificationsRejectedAccumulative());
        Assert.assertEquals((int)215, (int)builder.getCumulative().getNoOfClaimNotificationsClosed());
        Assert.assertEquals((int)1062, (int)builder.getCumulative().getNoOfInvoicesSubmitted());
        Assert.assertEquals(BigDecimal.valueOf(6016.89), (BigDecimal)builder.getCumulative().getTotalValueOfPenaltyChargesApplied());
        */
        
    }

    @Test
    public void testCreditHireDashBoard(){

/*
        String[] objectIds = {"-1"};
        Map extParameters = new HashMap();
        extParameters.put("insurerId", objectIds);



        Chorganisation chorganisation = chorganisationService.getChorgByName("Drive Assist");
        ChoDashboardBuilder builder = new ChoDashboardBuilder(dataService, chorganisation, extParameters);

        Assert.assertEquals((int)3480, (int)builder.getCumulative().getNoOfClaimNotificationsSubmitted());
        Assert.assertEquals((int)1241, (int)builder.getCumulative().getNoOfClaimNotificationsAcceptedAccumulative());
        Assert.assertEquals((int)2106, (int)builder.getCumulative().getNoOfClaimNotificationsRejectedAccumulative());
        Assert.assertEquals((int)169, (int)builder.getCumulative().getNoOfClaimNotificationsClosed());
        Assert.assertEquals((int)986, (int)builder.getCumulative().getNoOfInvoicesSubmitted());
        Assert.assertEquals(BigDecimal.valueOf(747888.78), (BigDecimal)builder.getCumulative().getTotalValueOfPenaltyChargesApplied());
*/
    }

}
