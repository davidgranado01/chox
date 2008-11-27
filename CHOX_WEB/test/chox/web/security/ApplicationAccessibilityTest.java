/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.security;

import chox.data.HibernateUtil;
import chox.model.Accessibility;
import chox.model.ClaimStatus;
import chox.services.AccessibilityService;
import chox.services.AccessibilityServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import static org.junit.Assert.*;

/**
 *
 * @author Emmanuel
 */
public class ApplicationAccessibilityTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Session currentSession = SessionFactoryUtils.getSession(HibernateUtil.getSessionFactory(), true);
        
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        Session currentSession = SessionFactoryUtils.getSession(HibernateUtil.getSessionFactory(), true);
        if (currentSession != null && currentSession.isOpen()) {
            currentSession.close();
        }
    }

    @Test
    public void testCheckTabAccessibility() {
        ApplicationAccessibility instance = ApplicationAccessibility.getInstance();
        System.out.println("checkTabAccessibility");
        String tabName = ApplicationAccessibility.TAB_CLAIM_DETAIL;
        GrantedAuthority[] grantedAuthorities = new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_CHO")};
        String claimStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED;
        Short expResult = 1;
        Short result = instance.checkTabAccessibility(tabName, grantedAuthorities, claimStatus);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckActionAccessibility() {
//        System.out.println("checkActionAccessibility");
//        String actionName = "";
//        GrantedAuthority[] grantedAuthorities = null;
//        String claimStatus = "";
//        ApplicationAccessibility instance = new ApplicationAccessibility();
//        Short expResult = null;
//        Short result = instance.checkActionAccessibility(actionName, grantedAuthorities, claimStatus);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
        
//        AccessibilityService service = new AccessibilityServiceImpl();
//        
//        List st = ClaimStatus.getStatus();
//        String[] statuses = new String[st.size()];
//        statuses = (String[]) st.toArray(statuses);
//        List<Accessibility> names = new ArrayList<Accessibility>();
//        short right = 1;
//        for (String s : statuses) {
//            String name = String.format("tab.%1$s.%2$s", "Notes", s);
//            
//            Accessibility a = new Accessibility();
//            a.setName(name);
//            names.add(a);
//        }
//        service.AddNewAccessibility(names, right); 
    }

    @Test
    public void testCheckFilterAccessibility() {
        System.out.println("checkFilterAccessibility");
        String filterName = "";
        GrantedAuthority[] grantedAuthorities = null;
        ApplicationAccessibility instance = ApplicationAccessibility.getInstance();
        Short expResult = null;
        Short result = instance.checkFilterAccessibility(filterName, grantedAuthorities);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}