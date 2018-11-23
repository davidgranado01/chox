package idas.chox.core.util;

import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author john
 */
public class VirusCheckerUtility {
    private static final Logger LOG = LoggerFactory.getLogger(VirusCheckerUtility.class);

    public static boolean isVirusPresent(byte[] content, String clamscanLocation) throws Exception {

        boolean fileDirty = true;
        Process clamscanProcess = null;
        
        // Succeed if clamscan not present
        if (clamscanLocation == null || clamscanLocation.isEmpty() || !Files.exists(Paths.get(clamscanLocation))) {
            LOG.warn("Not scanning file as clamscan not found at location '{}',", clamscanLocation);
            fileDirty = false;
        } else
            try {
                clamscanProcess = Runtime.getRuntime().exec(new String[] { clamscanLocation, "-" });

                StringBuilder out = new StringBuilder();
                StringBuilder err = new StringBuilder();

                ProcessReader p1 = new ProcessReader(out, clamscanProcess.getInputStream());
                ProcessReader p2 = new ProcessReader(err, clamscanProcess.getErrorStream());
                p1.start();
                p2.start();

                clamscanProcess.getOutputStream().write(content);
                clamscanProcess.getOutputStream().close();
                int exitState = clamscanProcess.waitFor();
                switch (exitState) {
                case 0:
                    fileDirty = false;
                    break;
                case 1:
                    fileDirty = true;
                    break;
                case 2:
                    LOG.error("Error on exit from clamscan: {}", err.toString());
                    throw new Exception("Error from clamscan: " + err.toString());
                }
            } catch (IOException | InterruptedException e) {
                LOG.error("Exception callingt clamscan: {}", e.getMessage());
                throw new Exception(e.getMessage());
            } finally {
                if (clamscanProcess != null) {
                    try {
                        clamscanProcess.getInputStream().close();
                    } catch (IOException ex) { // do nothing
                        LOG.error("Exception closing input stream: {}", ex.getMessage());
                    }
                    try {
                        clamscanProcess.getErrorStream().close();
                    } catch (IOException ex) { // do nothing
                        LOG.error("Exception closing error stream: {}", ex.getMessage());
                    }
                    clamscanProcess.destroy();
                }
            }

        return fileDirty;
    }

    private static class ProcessReader extends Thread {

        private final StringBuilder sb;
        private final InputStream is;

        public ProcessReader(StringBuilder sb, InputStream is) {
            this.sb = sb;
            this.is = is;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            try {
                int i = is.read();
                while (i != -1) {
                    sb.append((char) i);
                    i = is.read();
                }
            } catch (IOException e) {
                LOG.warn("IOException thrown checking for virus: {}", e.getMessage(), e);
            }
        }

    }
}
