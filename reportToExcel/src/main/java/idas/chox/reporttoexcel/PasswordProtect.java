package idas.chox.reporttoexcel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//[03/10/2014 14:07:33] Elliot Roberts:  DLG is 2014Sweets!
//[03/10/2014 14:07:41] Elliot Roberts: Octagon - Octag0n
//[03/10/2014 14:07:48] Elliot Roberts: Equity - Equitybord
//[03/10/2014 14:07:57] Elliot Roberts: Quindell - Qu1ndell
/**
 *
 * @author John
 */
public class PasswordProtect {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordProtect.class);
    private static final String DEFAULT_PASSWORD = "C0mpliance";

    private static String getPassword(String filename) {
        String password = DEFAULT_PASSWORD;

        if (filename.startsWith("DLG")) {
            password = "2014Sweets!";
        } else if (filename.startsWith("Octagon")) {
            password = "Octag0n";
        } else if (filename.startsWith("ERS")) {
            password = "Equitybord";
        } else if (filename.startsWith("QBPS") || filename.startsWith("Quindell")) {
            password = "Qu1ndell";
        } else if (filename.startsWith("RSA")) {
            password = "Royal1";
        } else if (filename.startsWith("Motability")) {
            password = "Mot1";
        } else if (filename.startsWith("Helphire")) {
            password = "Help1";
        } else if (filename.startsWith("AccidentExchange")) {
            password = "AE1";
        }

        return password;
    }

    static void protect(String filename) {
        FileInputStream fileInput;
        BufferedInputStream bufferInput;
        POIFSFileSystem poiFileSystem;
        FileOutputStream fileOut = null;

        try {

            fileInput = new FileInputStream(filename);
            bufferInput = new BufferedInputStream(fileInput);
            poiFileSystem = new POIFSFileSystem(bufferInput);
            
            Biff8EncryptionKey.setCurrentUserPassword(getPassword((new File(filename)).getName()));
            HSSFWorkbook workbook = new HSSFWorkbook(poiFileSystem, true);

            try {
                bufferInput.close();
            } catch (IOException ex) {
                LOG.error("Error closing input file '{}'", filename);
            }

            fileOut = new FileOutputStream(filename);
            workbook.writeProtectWorkbook(Biff8EncryptionKey.getCurrentUserPassword(), "");
            workbook.write(fileOut);
        } catch (Exception ex) {
            LOG.error("Exception thrown adding password protection : {}", ex.getMessage(), ex);
        } finally {
            try {
                fileOut.close();
            } catch (IOException ex) {
                LOG.error("Exception thrown closing password protected file : {}", ex.getMessage(), ex);
            }
        }

    }
}
