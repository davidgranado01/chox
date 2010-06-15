/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.xml.readers;

import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

/**
 *
 * @author emmanuel
 */
public class BordereauReader {
    private static final Logger LOG = LoggerFactory.getLogger(BordereauReader.class);

    private List<Reader> subEntityReaders;
    private BusinessRulesEngService businessRulesEngService;

    public BordereauResult execute(final File file) throws Exception {
        BordereauResult bordereauResult = new BordereauResult();
        Document document = DocumentHelper.getDocumentFromFile(file);
        execute(document, bordereauResult);

        return bordereauResult;
    }

    public void execute(final File file, BordereauResult bordereauResult) throws Exception {
        Document document = DocumentHelper.getDocumentFromFile(file);
        execute(document, bordereauResult);
    }

    //Make sure thr xml document processing here contain valid format and schema
    public void execute(Document document, BordereauResult bordereauResult) throws Exception {

        if (bordereauResult.isValid()) {
            List<ClaimResult> claimResults = formClaimResults(document);
            bordereauResult.setClaimResult(claimResults);

            for (ClaimResult claimResult : claimResults) {
                for (Reader r : subEntityReaders) {
                    r.execute(claimResult);
                }
            }
        } else {
            LOG.debug("Throwing exception: An attempt to read bordereau from XML failed due to the bordereauResult is invalid from previous state.");
            throw new Exception("An attempt to read bordereau from XML failed due to the bordereauResult is invalid from previous state.");
        }
    }

    private List<ClaimResult> formClaimResults(Document document) throws Exception {
        Element root = document.getDocumentElement();
        List<ClaimResult> claimElements = new ArrayList<ClaimResult>();

        List<Element> rentals = XMLUtils.getElements(document, root, "rental");

        if (rentals != null && rentals.size() > 0) {

            for (Element e : rentals) {

                ClaimResult claimResult = new ClaimResult();
                claimResult.setElement(e);
                claimResult.setCheckDataValid(true);
                claimResult.setDataValid(true);
                claimResult.setValid(true);
                claimElements.add(claimResult);
            }
        }
        return claimElements;
    }

    public void setSubEntityReaders(List<Reader> subEntityReaders) {
        this.subEntityReaders = subEntityReaders;
    }

    public void setBusinessRulesEngService(BusinessRulesEngService businessRulesEngService) {
        this.businessRulesEngService = businessRulesEngService;
    }
}