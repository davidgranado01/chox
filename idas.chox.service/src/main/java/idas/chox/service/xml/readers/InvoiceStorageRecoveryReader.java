package idas.chox.service.xml.readers;

import org.w3c.dom.Element;

import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;

public class InvoiceStorageRecoveryReader extends BaseEntityReader {

    protected static String sectionName = "Invoice Storage Recovery";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        Element element = XMLUtils.getElement(root, "storage-recovery");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)) {

            isAllowToReadData = true;

            claimResult = NodeHelper.nodeValidate(sectionName, "net", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vat", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "gross", element, claimResult, getDataValidationParameter());

            if (!claimResult.isCheckDataValid()) {
                isAllowToReadData = false;
                claimResult.setValid(false);
                claimResult.setDataValid(false);
            }

        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {
        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        Element element = XMLUtils.getElement(root, "storage-recovery");

        claimResult.getInvoice().setStorageRecoveryGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        claimResult.getInvoice().setStorageRecoveryNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        claimResult.getInvoice().setStorageRecoveryVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
    }
}
