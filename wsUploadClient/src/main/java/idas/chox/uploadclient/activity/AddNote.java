package idas.chox.uploadclient.activity;

import com.idaschox.services.chox.Note;
import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;
import idas.chox.uploadclient.utility.XlsFileParser;
import java.io.FileInputStream;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddNote {

    private static final Logger LOG = LoggerFactory.getLogger(AddNote.class);
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
                    if (row != 0) { // ignore first row - should contain header
                        List<String> cells = xlsDataMap.get(row);

                        if (cells.size() < 3) {
                            //ignore row
                            LOG.debug("Ignoring row {} - only has {} cells.", row, cells.size());
                            continue;
                        }

                        StringBuilder statusString = new StringBuilder();

                        String referenceNumber = cells.get(0).trim();

                        if (!regexExpressionChecker(REG_ALPHANUMERIC, referenceNumber)) {
                            statusString.append(" No Claim Reference Provided.");
                        }
                        
                        String comment = cells.get(1).trim();
                        if (comment.isEmpty()) {
                            statusString.append(" No Note provided.");
                        }
                        else if (!regexExpressionChecker(REG_ALPHANUMERIC, comment)) {
                            statusString.append(" Note Must Start With an Alpha-numeric Character.");
                        }
                        
                        String visibilityType = cells.get(2).trim();
                        if (!visibilityType.isEmpty()) {
                            if (!regexExpressionChecker(REG_ALPHANUMERIC, visibilityType)) {
                                statusString.append("Visibility Type Must Start With An Alpha-numeric Character.");
                            } else if (visibilityType.length() > 10) {
                                statusString.append(" Visibility Type Exceeds The Maximum Allowed Length of 10 Character.");
                            } else if (!(visibilityType.equalsIgnoreCase("public") || visibilityType.equalsIgnoreCase("private"))) {
                                statusString.append(" Only 'Public'/'Private' Strings Are Allowed For Visibility Type.");
                            }
                        }
                        else { // setting visibilityType to public in the client is not necessery because the server anyway add as public note if the visibility not present. 
                            visibilityType = "public";
                        } 

                        if (statusString.toString().isEmpty()) {
                            try {
                                Note note = new Note();
                                note.setComment(comment);
                                note.setSupplierReference(referenceNumber);
                                note.setVisibility(visibilityType);

                                addNote(uploadService, note);
                            } catch (Exception ex) {
                                statusString.append(" An Internal Error Occurred.");
                                LOG.error("Exception occurred when adding new Note.", ex);
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

        } catch (Exception ex) {//Catch exception if any
            LOG.error("Error processing input file '{}': ", file, ex);
        }
    }

    public static void addNote(UploadService uploadService, Note note) {
        Result result = null;

        LOG.debug("Calling Add Note Web Service for claim with CHO reference '{}'...", note.getSupplierReference());
        try {
            result = uploadService.addNote(note);
        } catch (Exception ex) {
            LOG.error("Error calling Add Note web service: ", ex);
            return;
        }

        if (!result.isStatus()) {
            LOG.error("Failed : '{}' : {} ", note.getSupplierReference(), result.getErrorMessage());
        } else {
            LOG.info("Success : '{}' : Updated", note.getSupplierReference());
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
