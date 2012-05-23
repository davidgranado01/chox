package idas.chox.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class DeleteOnCloseFileInputStream extends FileInputStream {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteOnCloseFileInputStream.class);
    private File file;

    public DeleteOnCloseFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        try {
            LOG.debug("Closing FileInputStream on file {}", file.getAbsolutePath());
            super.close();
        } finally {
            if (file != null) {
                LOG.debug("Deleting file {}", file.getAbsolutePath());
                file.delete();
                file = null;
            }
        }
    }
}
