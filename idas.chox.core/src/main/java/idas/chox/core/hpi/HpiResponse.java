package idas.chox.core.hpi;

import java.io.InputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.*;

import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class HpiResponse {

    private static final Logger LOG = LoggerFactory.getLogger(HpiResponse.class);
    private String sessionId;
    private String manufacturer;
    private String model;
    private String year;
    private String capacity;
    private String doorPlan;
    private String transmission;
    private Date firstRegistration;

    public static HpiResponse parseResponse(InputStream stream) throws HpiException {
        String response;
        
        // Convert the stream to a string
        LOG.debug("Parsing HPI response from input stream....");
        try {
            response = new java.util.Scanner(stream).useDelimiter("\\A").next();
        } catch (Exception ex) {
            LOG.error("Error parsing HPI response: {}", ex.getMessage(), ex);
            response="";
        }
        LOG.debug("Parsing HPI response from string: {}", response);
        return HpiResponse.parseResponse(response);
    }

    
    public static HpiResponse parseResponse(String responseBody) throws HpiException {
        HpiResponse response = new HpiResponse();

        DateFormat df = new SimpleDateFormat("dd/MM/yy");

        try {
            NodeList nodes;
            Element element;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(responseBody));

            Document doc = db.parse(is);

            // First, lets check for errors
            nodes = doc.getElementsByTagName("Section_02");
            if (nodes.getLength() > 0) { // Ok, we've received an error response
                nodes = doc.getElementsByTagName("description");
                element = (Element) nodes.item(0);
                String description = getCharacterDataFromElement(element);
                throw new HpiException(description);
            }

            // Now check if the VRN exists
            nodes = doc.getElementsByTagName("Veh_Regd");
            if (nodes.getLength() > 0) { // 
                element = (Element) nodes.item(0);
                String description = getCharacterDataFromElement(element);
                if (description.equalsIgnoreCase("Not Recorded")) {
                    throw new HpiException("Vehicle' VRN was not found");
                }
            }


            // Vehicle Year Of Registration added for bug no @ 405

            nodes = doc.getElementsByTagName("FirstReg");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                nodes = element.getElementsByTagName("Date");
                if (nodes.getLength() > 0) {
                    element = (Element) nodes.item(0);

                        response.firstRegistration = df.parse(getCharacterDataFromElement(element));
                        LOG.debug("Element before parsing {}", getCharacterDataFromElement(element));
                        LOG.debug("element after parsing: {}", response.firstRegistration.toString());
                        
                    
                }
            }




            nodes = doc.getElementsByTagName("Session");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                response.sessionId = getCharacterDataFromElement(element);
            }

            nodes = doc.getElementsByTagName("Make");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                response.manufacturer = getCharacterDataFromElement(element);
            }

            nodes = doc.getElementsByTagName("Model");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                response.model = getCharacterDataFromElement(element);
            }

            nodes = doc.getElementsByTagName("Mfr_Year");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                response.year = getCharacterDataFromElement(element);
                
            }

            nodes = doc.getElementsByTagName("Engine_Size");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                response.capacity = getCharacterDataFromElement(element);
            }

//            nodes = doc.getElementsByTagName("SMMT_Body_Plan_Description");
            nodes = doc.getElementsByTagName("DVLA_Body_Plan_Description");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                response.doorPlan = getCharacterDataFromElement(element);
            }

//            nodes = doc.getElementsByTagName("transmission_code");
            nodes = doc.getElementsByTagName("Transmission");
            if (nodes.getLength() > 0) {
                element = (Element) nodes.item(0);
                response.transmission = getCharacterDataFromElement(element);
            }
        } catch (HpiException ex) {
            LOG.info("Error response received from HPI: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error("Error parsing response: {}", ex.getMessage());
            throw new HpiException(ex.getMessage(), ex);
        }

        return response;
    }

    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return null;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getDoorPlan() {
        return doorPlan;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getYear() {
        return year;
    }

    public Date getFirstRegistration() {
        return firstRegistration;
    }
}
