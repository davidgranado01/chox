package idas.chox.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Misc XML utilities
 */
public final class XMLUtils {
    private static final Logger LOG = LoggerFactory.getLogger(XMLUtils.class);

    private XMLUtils() {
    }

    /**
     * Retrieve the text contained within an element
     *<p>
     *@param element The element within an XML document whose content is required.
     *</p>
     *<p>
     *@return The contents of the <code>element</code> as a <code>String</code>
     *</p>
     *<p>
     *@throw DOMException if an error occurs accessing the XML document
     *</p>
     */
    public static String getElementText(Element element)
    throws DOMException
    {
        StringBuilder sb=new StringBuilder();

        NodeList nl=element.getChildNodes();
        int nodes=nl.getLength();
        for(int i=0;i<nodes;i++)    {
            Node n=nl.item(i);
            short nt=n.getNodeType();
            if(nt==Node.TEXT_NODE || nt==Node.CDATA_SECTION_NODE) {
                sb.append(n.getNodeValue());
            }
        }
        return sb.toString().trim();
    }

    public static String getElementTextNoTrim(Element element)
    throws DOMException
    {
        StringBuilder sb=new StringBuilder();

        NodeList nl=element.getChildNodes();
        int nodes=nl.getLength();
        for(int i=0;i<nodes;i++)    {
            Node n=nl.item(i);
            short nt=n.getNodeType();
            if(nt==Node.TEXT_NODE || nt==Node.CDATA_SECTION_NODE) {
                sb.append(n.getNodeValue());
            }
        }
        return sb.toString();
    }

    /**
     * Retrieve the text contained within an element which is searched for.
     * <br />
     *<p>
     *@param root The element which a search is to be performed from.
     *@param tag The name of the element which is to be searched for.
     *</p>
     *<p>
     *@return The contents of the first matching element found as a <code>String</code>
     *</p>
     *<p>
     *@throw DOMException if an error occurs accessing the XML document
     *</p>
     */
    public static String getElementValue(Element root,String tag)
    throws DOMException
    {
        NodeList nl=root.getElementsByTagName(tag);
        if(nl.getLength()==0) {
            return null;
        }
        return StringEscapeUtils.unescapeHtml4(Jsoup.clean(getElementText((Element)nl.item(0)), Whitelist.basic()));
    }


    public static Element makeElement(Document doc,String tag,String value)
    throws DOMException
    {
        Element e=doc.createElement(tag);
        if(value!=null) {
            e.appendChild(doc.createCDATASection(value));
        }
        return e;
    }

    public static Element makeElementAppend(Document doc,Element parent,String tag,String value)
    throws DOMException
    {
        Element e=doc.createElement(tag);
        if(value!=null) {
            e.appendChild(doc.createCDATASection(value));
        }
        parent.appendChild(e);
        return e;
    }

    public static Element makeElementAppendTextNode(Document doc,Element parent,String tag,String value)
    throws DOMException
    {
        Element e=doc.createElement(tag);
        if(value!=null) {
            e.appendChild(doc.createTextNode(value));
        }
        parent.appendChild(e);
        return e;
    }



    public static Element makeElementAppend(Document doc,Element parent,String tag)
    throws DOMException
    {
        Element e=doc.createElement(tag);
        parent.appendChild(e);
        return e;
    }


    /**
     * Convert an XML document to plain text
     *<p>
     *@param xmlDocument The document to be converted.
     *</p>
     *<p>
     *@return The <code>xmlDocument</code> as a text string.
     *</p>
     */
    public static String toString(Document xmlDocument)
    throws DOMException,IOException,TransformerConfigurationException,TransformerException
    {
        TransformerFactory tf=TransformerFactory.newInstance();
        Transformer t=tf.newTransformer();

        String xmlText;
        try (StringWriter sw = new StringWriter()) {
            t.transform(new DOMSource(xmlDocument),new StreamResult(sw));
            xmlText = sw.toString();
        }
        return xmlText;
    }

    public static String toStringNoXMLHeader(Document xmlDocument)
            throws DOMException,IOException,TransformerConfigurationException,TransformerException
    {
        String s=toString(xmlDocument);
        int i=s.indexOf("?>");
        return s.substring(i+2);
    }


    public static void toFile(Document xmlDocument,File outputFile)
    throws DOMException,IOException,TransformerConfigurationException,TransformerException,IOException
    {
        OutputFormat f=new OutputFormat(xmlDocument);
        f.setLineWidth(132);
        f.setIndenting(true);
        f.setIndent(1);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            XMLSerializer s=new XMLSerializer(fos,f);
            s.serialize(xmlDocument);
            fos.flush();
        }
    }

    public static void toGZipFile(Document xmlDocument,File outputFile)
    throws DOMException,IOException,TransformerConfigurationException,TransformerException,IOException
    {
        File tmp=File.createTempFile("tmp", "xml");
        tmp.deleteOnExit();
        OutputFormat f=new OutputFormat(xmlDocument);
        f.setLineWidth(132);
        f.setIndenting(true);
        f.setIndent(1);
        XMLSerializer s=new XMLSerializer(new FileOutputStream(tmp),f);
        s.serialize(xmlDocument);

        byte [] buffer=new byte[65536];
        FileInputStream is;
        try (GZIPOutputStream os = new GZIPOutputStream(new FileOutputStream(outputFile))) {
            is = new FileInputStream(tmp);
            int r=is.read(buffer);
            while(r!=-1){
                os.write(buffer, 0, r);
                r=is.read(buffer);
            }
        }
        is.close();

        tmp.delete();
    }


    /**
     * Parse an XML text document into a Document object
     *<p>
     *@param xmlText The XML document as a <code>String</code>
     *</p>
     *<p>
     *@return The parsed document as a <code>Document</code> object
     *</p>
     *<p>
     *@throws DOMException
     *@throws ParserConfigurationException
     *@throws SAXException
     *@throws IOException
     *</p>
     */
    public static Document toDocument(String xmlText)
    throws DOMException, ParserConfigurationException, SAXException, IOException
    {
        if(xmlText==null) {
            return null;
        }
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
               LOG.info("Disabling XXE Processing in toDocument(String)....");
         dbf.setExpandEntityReferences(false);
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            String FEATURE = "http://xml.org/sax/features/external-general-entities";
            dbf.setFeature(FEATURE, false);
 
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(FEATURE, true);
            DocumentBuilder db=dbf.newDocumentBuilder();
            doc=db.parse(new InputSource(new StringReader(xmlText)));
        } catch (ParserConfigurationException e) {
            // This should catch a failed setFeature feature
            LOG.error("ParserConfigurationException was thrown. The feature is probably not supported by your XML processor: {}\n", e.getMessage(), e);
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            LOG.error("A DOCTYPE was passed into the XML document");
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            LOG.error("IOException occurred, XXE may still possible: " + e.getMessage());
        }
        return doc;
    }

    public static Document toDocument(File xmlFile)
    throws DOMException, ParserConfigurationException, SAXException, IOException
    {
        if(xmlFile==null || !xmlFile.canRead()) {
            return null;
        }
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            LOG.info("Disabling XXE Processing in toDocument(File)....");
            dbf.setExpandEntityReferences(false);
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            String FEATURE = "http://xml.org/sax/features/external-general-entities";
            dbf.setFeature(FEATURE, false);
 
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(FEATURE, true);
            DocumentBuilder db=dbf.newDocumentBuilder();
            doc=db.parse(xmlFile);
        } catch (ParserConfigurationException e) {
            // This should catch a failed setFeature feature
            LOG.error("ParserConfigurationException was thrown. The feature is probably not supported by your XML processor: {}\n", e.getMessage(), e);
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            LOG.error("A DOCTYPE was passed into the XML document");
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            LOG.error("IOException occurred, XXE may still possible: " + e.getMessage());
        }
         return doc;
    }

    /**
     * Parse an XML text document into a Document object
     *<p>
     *@param xmlText The XML document as an array of bytes (7bit ASCII).
     *</p>
     *<p>
     *@return The parsed document as a <code>Document</code> object
     *</p>
     *<p>
     *@throws DOMException
     *@throws ParserConfigurationException
     *@throws SAXException
     *@throws IOException
     *</p>
     */
    public static Document toDocument(byte [] xmlData)
    throws Exception
    {
        if(xmlData==null) {
            return null;
        }
        StringBuilder sb=new StringBuilder(xmlData.length);
        for(int i=0;i<xmlData.length;i++)   {
            sb.setCharAt(i,(char)xmlData[i]);
        }
        return toDocument(sb.toString());
    }

    public static Element getElement(Element start,String tag)
    throws DOMException
    {
        NodeList nl=start.getElementsByTagName(tag);
        if(nl.getLength()==0) {
            return null;
        }
        return (Element)nl.item(0);
    }

    public static String getValue(Document doc,String path,String defaultValue)
    throws DOMException,XPathExpressionException
    {
        XPath xpath=XPathFactory.newInstance().newXPath();
        Element e=(Element)xpath.evaluate(path,doc,XPathConstants.NODE);
        if(e==null) {
            return defaultValue;
        }
        return getElementText(e);
    }

    public static Element getElement(Document doc,String path)
    throws DOMException,XPathExpressionException
    {
        XPath xpath=XPathFactory.newInstance().newXPath();
        Element e=(Element)xpath.evaluate(path,doc,XPathConstants.NODE);
        return e;
    }

    public static ArrayList<Element> getElements(Document doc,String parentPath,String tag)
    throws DOMException,XPathExpressionException
    {
        Element parent=getElement(doc,parentPath);
        if(parent==null) {
            return null;
        }
        NodeList nl=parent.getElementsByTagName(tag);
        ArrayList<Element> nodes=new ArrayList<>();
        for(int i=0;i<nl.getLength();i++)   {
            nodes.add((Element)nl.item(i));
        }
        return nodes;
    }

    public static ArrayList<Element> getElements(Document doc,Element parent,String tag)
    throws DOMException,XPathExpressionException
    {
        NodeList nl=parent.getElementsByTagName(tag);
        ArrayList<Element> nodes=new ArrayList<>();
        for(int i=0;i<nl.getLength();i++)   {
            nodes.add((Element)nl.item(i));
        }
        return nodes;
    }


}
