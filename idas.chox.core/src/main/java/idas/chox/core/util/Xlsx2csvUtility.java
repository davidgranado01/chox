package idas.chox.core.util;

import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Xlsx2csvUtility {
    private static final Logger LOG = LoggerFactory.getLogger(Xlsx2csvUtility.class);

    public static boolean convert(String filePath, String xlsx2csvLocation) throws Exception {

        boolean result = false;
        Process xlsx2csvProcess = null;
        String outputFile = filePath.replace(".xlsx", ".csv");
        // First check for xlsx2csv
        if (xlsx2csvLocation == null || xlsx2csvLocation.isEmpty() || !Files.exists(Paths.get(xlsx2csvLocation))) {
            LOG.warn("Not converting file as xlsx2csv not found at location '{}',", xlsx2csvLocation);
        } else
            try {
                xlsx2csvProcess = Runtime.getRuntime().exec(new String[] { xlsx2csvLocation, "-i",  filePath, outputFile});

                StringBuilder err = new StringBuilder();

                ProcessReader p2 = new ProcessReader(err, xlsx2csvProcess.getErrorStream());
                p2.start();

                int exitState = xlsx2csvProcess.waitFor();
                switch (exitState) {
                case 0:
                    result = true;
                    break;
                default:
                    LOG.error("Error {} on exit from xlsx2csv: {}", exitState, err.toString());
                    throw new Exception("Error from xlsx2csv: " + err.toString());
                }
            } catch (IOException | InterruptedException e) {
                LOG.error("Exception calling xlsx2csv: {}", e.getMessage());
                throw new Exception(e.getMessage());
            } finally {
                if (xlsx2csvProcess != null) {
                    try {
                        xlsx2csvProcess.getErrorStream().close();
                    } catch (IOException ex) { // do nothing
                        LOG.error("Exception closing error stream: {}", ex.getMessage());
                    }
                    xlsx2csvProcess.destroy();
                }
            }

        return result;
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
                LOG.warn("IOException thrown converting to csv: {}", e.getMessage(), e);
            }
        }

    }
}
