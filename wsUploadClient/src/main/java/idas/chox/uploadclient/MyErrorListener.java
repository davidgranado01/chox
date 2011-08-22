/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.uploadclient;

import javax.xml.transform.ErrorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.transform.TransformerException;

/**
 *
 * @author John
 */
public class MyErrorListener implements ErrorListener{
    
    static final Logger logger = LoggerFactory.getLogger(MyErrorListener.class);

    @Override
    public void warning(TransformerException e)
            throws TransformerException {
        show("Warning", e);
        throw (e);
    }

    @Override
    public void error(TransformerException e)
            throws TransformerException {
        show("Error", e);
        throw (e);
    }

    @Override
    public void fatalError(TransformerException e)
            throws TransformerException {
        show("Fatal Error", e);
        throw (e);
    }

    private void show(String type, TransformerException e) {
        System.out.println(type + ": " + e.getMessage());
        if (e.getLocationAsString() != null) {
            logger.error(e.getLocationAsString());
        }
    }

}
