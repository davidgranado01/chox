package idas.chox.uploadclient.activity;

import java.io.FileInputStream;
import javax.activation.DataHandler;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idaschox.services.chox.Attachment;
import com.idaschox.services.chox.AttachmentCategory;
import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;

public class AddAttachment {

    private static final Logger LOG = LoggerFactory.getLogger(AddAttachment.class);

    public static void process(UploadService uploadService, String choRef, String file, String category, Boolean notify, String remark) {

        // Argument is a file containing CHO reference numbers
        LOG.info("Processing file '{}'", file);
        try {
            FileInputStream fstream = new FileInputStream(file);
            Attachment attachment = new Attachment();
            attachment.setAttachment(new DataHandler(new InputStreamDataSource(fstream)));
            attachment.setCategory(AttachmentCategory.valueOf(category));
            attachment.setFileType(FilenameUtils.getExtension(file));
            attachment.setFilename(FilenameUtils.getBaseName(file));
            attachment.setNotify(notify);
            attachment.setRemark((remark == null) ? "Attachment added by web service call." : remark);
            attachment.setSupplierReference(choRef);
            LOG.info("Adding attachment: choRef={}, category={}, fileType={}, fileName={}, remark={}, notify={}",
                    new Object[]{attachment.getSupplierReference(), attachment.getCategory(), attachment.getFileType(),
                    attachment.getFilename(), attachment.getRemark(), attachment.isNotify()});
            try {
                addAttachment(uploadService, attachment);
            } catch (Exception ex) {
                LOG.error("Exception thrown while processing excel file", ex);
            }

        } catch (Exception ex) {//Catch exception if any
            LOG.error("Error processing input file '{}': ", file, ex);
        }
    }

    public static void addAttachment(UploadService uploadService, Attachment attachment) {
        Result result;

        LOG.debug("Calling Add Attachment Web Service for claim with CHO reference '{}'...", attachment.getSupplierReference());
        try {
            result = uploadService.addAttachment(attachment);
        } catch (Exception ex) {
            LOG.error("Error calling Add Attachment web service: ", ex);
            return;
        }

        if (!result.isStatus()) {
            LOG.error("Failed : '{}' : {} ", attachment.getSupplierReference(), result.getErrorMessage());
        } else {
            LOG.info("Success : '{}' : Updated", attachment.getSupplierReference());
        }
    }
    
}
