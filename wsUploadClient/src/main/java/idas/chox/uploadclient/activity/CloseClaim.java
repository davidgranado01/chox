package idas.chox.uploadclient.activity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;

/**
 *
 * @author John
 */
public class CloseClaim {

    private static final Logger LOG = LoggerFactory.getLogger(CloseClaim.class);
    
    public static void process(UploadService uploadService, String file) {

        File filename = new File(file);

        if (filename.exists()) {
            // Argument is a file containing CHO reference numbers
            LOG.info("Processing file '{}'", file);
            try {
                FileInputStream fstream = new FileInputStream(file);
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String choReference;
                //Read File Line By Line
                while ((choReference = br.readLine()) != null) {
                    closeClaim(uploadService, choReference);
                }
                //Close the input stream
                in.close();
            } catch (Exception e) {//Catch exception if any
                LOG.error("Error processing input file '{}': ", file, e.getMessage());
            }

        } else {
            // Argument is a CHO Reference number
            LOG.info("Processing CHO reference '{}'", file);
            closeClaim(uploadService, file);
        }

    }

    public static void closeClaim(UploadService uploadService, String choReference) {
        Result result;

        LOG.debug("Calling closeClaim Web Service for claim with CHO reference '{}'...", choReference);
        try {
            result = uploadService.closeClaim(choReference);
        } catch (Exception ex) {
            LOG.error("Error calling closeClaim web service: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            return;
        }

        if (!result.isStatus()) {
            LOG.error("Error closing claim '{}' : {}", choReference, result.getErrorMessage());
        } else {
            LOG.info("Claim with CHO reference '{}' has been updated to 'ClaimClosed'.", choReference);
        }
    }
}
