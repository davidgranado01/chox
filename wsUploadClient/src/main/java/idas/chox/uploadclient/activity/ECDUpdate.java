package idas.chox.uploadclient.activity;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idaschox.services.chox.EcdParam;
import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;

import idas.chox.uploadclient.utility.XMLGregorianCalendarConverter;
import idas.chox.uploadclient.utility.XlsFileParser;

/**
 *
 * @author John
 */
public class ECDUpdate {

    private static final Logger LOG = LoggerFactory.getLogger(ECDUpdate.class);
    private static SimpleDateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");
    private static String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";

    public static void process(UploadService uploadService, String file) {

        // Argument is a file containing CHO reference numbers
        LOG.info("Processing file '{}'", file);
        try {
            FileInputStream fstream = new FileInputStream(file);
            Map<Integer, List<String>> xlsDataMap = null;
            XlsFileParser xlsFileParser = new XlsFileParser();
            try {
                xlsDataMap = xlsFileParser.readExcelFile(fstream);
                Set<Integer> rowNumbers = xlsDataMap.keySet();

                for (Integer row : rowNumbers) {
                    // first row is header
                    if (row.intValue() != 0) { // ignore first row - should contain header
                        List<String> cells = xlsDataMap.get(row);

                        if (cells.size() < 4) {
                            //ignore row
                            LOG.debug("Ignoring row {} - only has {} cells.", row, cells.size());
                            continue;
                        }

                        StringBuilder statusString = new StringBuilder();

                        String referenceNumber = cells.get(0).trim();

                        if (!regexExpressionChecker(REG_ALPHANUMERIC, referenceNumber)) {
                            statusString.append(" No Claim Reference Provided.");
                        }

                        Date ecdDate = null;
                        dateFormate.setLenient(false);
                        if (cells.get(1).trim().isEmpty()) {
                            statusString.append(" No ECD Date Provided.");
                        } else if (cells.get(1).trim().length() != dateFormate.toPattern().length()) {
                            statusString.append(" Invalid Format For ECD Date.");
                        } else {
                            try {
                                ecdDate = dateFormate.parse(cells.get(1).trim());
                            } catch (ParseException ex) {
                                statusString.append(" Invalid Format For ECD Date.");
                                LOG.error("could not parse the given date string to java date {}", cells.get(1).trim());
                            }
                        }

                        String ecdDelayReason = cells.get(2).trim();
                        if (ecdDelayReason.isEmpty()) {
                            statusString.append(" No ECD Delay Reason Provided.");
                        }
                        else if (!regexExpressionChecker(REG_ALPHANUMERIC, ecdDelayReason)) {
                            statusString.append(" ECD Delay Reason Must Start With An Alpha-numeric Character.");
                        } else if (ecdDelayReason.length() > 50) {
                                statusString.append(" ECD Delay Reason Exceeds The Maximum Allowed Length of 50 Character.");
                        }

                        String ecdDelaySuppNote = cells.get(3).trim();
                        if (ecdDelaySuppNote.isEmpty()) {
                            statusString.append(" No Supporting Note provided.");
                        }
                        else if (!regexExpressionChecker(REG_ALPHANUMERIC, ecdDelaySuppNote)) {
                            statusString.append(" Supporting Note Must Start With an Alpha-numeric Character.");
                        }

                        if (statusString.toString().isEmpty()) {
                            try {
                                EcdParam eCDParam = new EcdParam();
                                eCDParam.setSupplierReference(referenceNumber);
                                eCDParam.setEcdDate(XMLGregorianCalendarConverter.asXMLGregorianCalendar(ecdDate));
                                eCDParam.setDelayReason(ecdDelayReason);
                                eCDParam.setSupportingNote(ecdDelaySuppNote);
//                                LOG.debug("ref {} ecd date {} ecd reason {} ecd supportnote {}", new Object[]{eCDParam.getSupplierReference(), eCDParam.getEcdDate().toString(), eCDParam.getDelayReason(), eCDParam.getSupportingNote()});
                                updateECD(uploadService, eCDParam);
                            } catch (Exception ex) {
                                statusString.append(" An Internal Error Occurred.");
                                LOG.error("Exception occurred when adding new ECD via email scheduler ecd update job", ex);
                            }
                        } else {
                            statusString.insert(0, "Failed:");
                            LOG.info(statusString.toString());
                        }
                    }
                }

            } catch (Exception ex) {
                LOG.error("Exception thrown while processing excel file", ex);
            }

        } catch (Exception e) {//Catch exception if any
            LOG.error("Error processing input file '{}': ", file, e.getMessage());
        }


    }

    public static void updateECD(UploadService uploadService, EcdParam ecdParam) {
        Result result = null;

        LOG.debug("Calling ECD update Web Service for claim with CHO reference '{}'...", ecdParam.getSupplierReference());
        try {
            result = uploadService.updateECD(ecdParam);
        } catch (Exception ex) {
            LOG.error("Error calling ECD update web service: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            return;
        }

        if (!result.isStatus()) {
            LOG.error("Failed : '{}' : {} ", ecdParam.getSupplierReference(), result.getErrorMessage());
        } else {
            LOG.info("Success : '{}' : Updated", ecdParam.getSupplierReference());
        }
    }
    
    public static boolean regexExpressionChecker(String regex, String dataValue) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(dataValue);

        if (!m.find()) {
            LOG.debug("Invalid data for regex '{}': {}", regex, dataValue);
            return false;
        }
        return true;
    }
}
