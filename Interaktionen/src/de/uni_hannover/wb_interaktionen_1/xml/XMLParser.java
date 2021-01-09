package de.uni_hannover.wb_interaktionen_1.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/** This class can parse a XML text that is passed as a string.
 *
 * @author David Sasse
 */
public class XMLParser {

    /** Finds a attribute in a XML string.
     *
     * This methode creates a xml document from a string and searches for a attribute in the XML document.
     *
     * @param file The string with the XML text.
     * @param attribute The attribute that has to be searches for.
     * @return The value of the searched attribute
     */
    public String getAttribute(String file, String attribute){
        try {
            //Creates a document out of the string
            Document doc = createXMLDoc(file);
            doc.getDocumentElement().normalize();

            //In the XML from the BBB-Server the response is tagged as "response"
            NodeList nList = doc.getElementsByTagName("response");

            //Searches for the attribute in the XML document and returns the value.
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    return eElement.getElementsByTagName(attribute).item(0).getTextContent();
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /** Creates a XML document
     *
     * This methode creates a XML document out of a string with the xml text.
     *
     * @param input The string with the xml text.
     * @return The XML document
     */
    private Document createXMLDoc(String input){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(input)));
            return doc;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
