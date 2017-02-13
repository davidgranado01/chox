package idas.chox.core.util;

import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author john
 */
public class VirusCheckerUtility {
    private static final Logger LOG = LoggerFactory.getLogger(VirusCheckerUtility.class);

    private static final String clamscanLocation = "/usr/local/bin/clamscan";

    public static boolean isVirusPresent(byte[] content) throws Exception {

        boolean fileDirty = true;
        Process clamscanProcess;
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
                throw new Exception("Error from clamscan: " + err.toString());
            }
        } catch (IOException | InterruptedException e) {
            throw new Exception(e.getMessage());
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
                LOG.error("IOException thrown checking for virus: {}", e.getMessage(), e);
            }
        }

    }
}
