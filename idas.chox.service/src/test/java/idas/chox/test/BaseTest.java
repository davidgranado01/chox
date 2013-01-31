package idas.chox.test;

import idas.chox.core.bre.RulesEngine;
import idas.chox.core.services.*;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.service.admin.AdminUserService;
import idas.chox.service.intelligentNotes.IntelligentNoteDisplayEngine;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.xml.readers.BordereauReader;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author seeni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:applicationContext-Filters-test.xml",
    "classpath:applicationContext-Workflow-test.xml",
    "classpath:applicationContext-test.xml",
    "classpath:applicationContext-services-test.xml",
    "classpath:applicationContext-BRE-test.xml", 
    "classpath:applicationContext-IntelligentNote-test.xml",
    "classpath:applicationContext-XMLReader-test.xml"})
public abstract class BaseTest {

    @Autowired
    protected AdminChorganisationService adminChorganisationService;
    @Autowired
    protected ChorganisationService chorganisationService;
    @Autowired
    protected UploadClaimXMLService uploadClaimXMLService;
    @Autowired
    protected BordereauService bordereauService;
    @Autowired
    protected AdminInsurerService adminInsurerService;
    @Autowired
    protected InsurerService insurerService;
    @Autowired
    protected BreBandOrganisationService breBandOrganisationService;
    @Autowired
    protected BreBandService breBandService;
    @Autowired
    protected ClaimService claimService;
    @Autowired
    protected VehicleClassService vehicleClassService;
    @Autowired
    protected BordereauReader bordereauReader;
    @Autowired
    protected ActivityFactory activityFactory;
    @Autowired
    protected WorkgroupService workgroupService;
    @Autowired
    protected AdminUserService adminUserService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected WebUserUserRoleService webUserUserRoleService;
    @Autowired
    protected UserWorkgroupService userWorkgroupService;
    @Autowired
    protected VehicleClassPriceService vehicleClassPriceService;
    @Autowired
    protected InsurerChorganisationService insurerChorganisationService;
    @Autowired
    protected RulesEngine rulesEngine;
    @Autowired
    protected FilterService filterService;
    @Autowired
    protected LocalSessionFactoryBean sessionFactory;
    @Autowired
    protected ReasonOfDelayService reasonOfDelayService;
    @Autowired
    protected ReasonOfRejectionService reasonOfRejectionService;
    @Autowired
    protected AuditTrailService auditTrailService;
    @Autowired
    protected FakeSecurityInfoProvider fakeSecurityInfoProvider;
    @Autowired
    protected InvoiceService invoiceService;
    @Autowired
    protected AdminFeeService adminFeeService;
    @Autowired
    protected InsurerIntelligentNoteService insurerIntelligentNoteService;
    @Autowired
    protected IntelligentNoteDisplayEngine displayEngine;
    @Autowired
    protected CommentService commentService;
    @Autowired
    protected NotificationService notificationService;
    @Autowired
    protected HireMonitoringDetailService hireMonitoringDetailService;
    @Autowired
    protected CustomerService customerService;
}
