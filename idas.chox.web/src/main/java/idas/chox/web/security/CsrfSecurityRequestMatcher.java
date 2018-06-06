package idas.chox.web.security;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(CsrfSecurityRequestMatcher.class);
    // Do not apply csrf filter for the following http methods
    private static final Pattern ALLOWED_METHODS = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    // Do not apply csrf filter for the web service call.
    private static final String WEB_SERVICE_URL_STRING = "/services";
    private static final String DOWNLOAD_URL_STRING = "downloadExcelReport";
    private static final String ATTACHMENT_URL_STRING = "createNewAttachment";
    private static final String UPLOAD_URL_STRING = "uploadNewClaimsFile";
    private static final String INBOX_URL_STRING = "inbox.action";
    private static final String CLAIM_DETAILS_URL_STRING = "claimDetails.action";
    private static final String CLAIM_DETAILS2_URL_STRING = "openClaimDetail.action";
    private static final String SEARCH_CLAIM_URL_STRING = "doSearchClaim.action";
    private static final String WORKGROUP_URL_STRING = "WorkgroupDropDownActionByInsurer2.action";
    private static final String SEARCH_CLAIMHANDLER_URL_STRING = "SearchClaimHandlerRoleUserDropDownAction.action";
    private static final String SEARCH_SUPPLIER_URL_STRING = "SearchSupplierClaimOwnerDropDownAction.action";
    // Do not apply csrf filter for the Logback call.
    private static final String LOGBACK_LOGGING_URL_STRING = "/logBack";
        
    @Override
    public boolean matches(HttpServletRequest request) {
        boolean result;
        
        /* 
         * if the http request method is one of (GET|HEAD|TRACE|OPTIONS) then return false.
         * returning false will not apply csrf filter for this request. 
         */
        if (ALLOWED_METHODS.matcher(request.getMethod()).matches()) {
            result = false;
        }
        else if (request.getServletPath().contains(WEB_SERVICE_URL_STRING) || request.getServletPath().contains(DOWNLOAD_URL_STRING)
                || request.getServletPath().contains(ATTACHMENT_URL_STRING) || request.getServletPath().contains(UPLOAD_URL_STRING)
                || request.getServletPath().contains(INBOX_URL_STRING) || request.getServletPath().contains(CLAIM_DETAILS_URL_STRING)
                || request.getServletPath().contains(WORKGROUP_URL_STRING) || request.getServletPath().contains(SEARCH_CLAIMHANDLER_URL_STRING)
                || request.getServletPath().contains(SEARCH_CLAIM_URL_STRING) || request.getServletPath().contains(SEARCH_SUPPLIER_URL_STRING)
                || request.getServletPath().contains(CLAIM_DETAILS2_URL_STRING)) {
            result = false;
        } else {
            result =  !request.getServletPath().contains(LOGBACK_LOGGING_URL_STRING);
        }
        
        LOG.debug("CSRF request matcher returning '{}'", result);

        return result;
    }
}
