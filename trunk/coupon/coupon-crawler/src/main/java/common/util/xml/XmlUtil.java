package common.util.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtil {
    private static Map<Class, JAXBContext> jaxbMap = new HashMap<Class, JAXBContext>();
    
    public static synchronized JAXBContext getJaxbContext(Class cls) throws JAXBException {
        JAXBContext context = jaxbMap.get(cls);
        if(context == null) {
            context = JAXBContext.newInstance(cls);
            jaxbMap.put(cls, context);
        }
        return context;
    }
    
    public static String toXml(Object obj) throws JAXBException {
        JAXBContext jaxbContext = getJaxbContext(obj.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        marshaller.marshal(obj, sw);
        return sw.toString();
    }
    
    public static <T> T toObject(String xml, Class<T> cls) throws JAXBException {
        JAXBContext jaxbContext = getJaxbContext(cls);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Object object = unmarshaller.unmarshal(new StringReader(xml));
        return (T) object;
    }
    
    public static Document parse(String content) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(content.getBytes()));
        return document;
    }

    public static XPath getXpath() {
        XPathFactory factory = XPathFactory.newInstance();
        return factory.newXPath();
    }

    public static XPathExpression getXpathExpr(String path) throws XPathExpressionException {
        XPath xpath = getXpath();
        XPathExpression xPathExpression = xpath.compile(path);
        return xPathExpression;
    }

    public static String getNodeValue(Node node) {
        if(node == null) return null;
        return node.getTextContent();
    }
    
    public static Integer getIntNodeValue(Node node) {
        try {
            return Integer.parseInt(node.getTextContent());
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static Float getFloatNodeValue(Node node) {
        try {
            return Float.parseFloat(node.getTextContent());
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static Node getChild(Node node, String name) {
        if(node == null) return null;
        
        short type = node.getNodeType();
        if (type != Node.ELEMENT_NODE) {
            throw new RuntimeException("Invalid node");
        }
        
        if(!node.hasChildNodes()) return null;
        
        NodeList list = node.getChildNodes();
        for(int i=0; i<list.getLength(); i++) {
            Node child = list.item(i);
            if(child.getNodeName().equals(name)) {
                return child;
            }
        }
        
        return null;
    }

    public static List<Node> getChildren(Node node, String name) {
        List<Node> nodes = new ArrayList<Node>();
        if(node == null) return nodes;
        
        short type = node.getNodeType();
        if (type != Node.ELEMENT_NODE) {
            throw new RuntimeException("Invalid node");
        }
        
        if(!node.hasChildNodes()) return nodes;
        
        NodeList list = node.getChildNodes();
        for(int i=0; i<list.getLength(); i++) {
            Node child = list.item(i);
            if(child.getNodeName().equals(name)) {
                nodes.add(child);
            }
        }
        
        return nodes;
    }

    public static Long getDateNodeValue(Node node) {
        if(node == null) return null;
        try {
            String str = getNodeValue(node);
            if(str == null) return null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
            return sdf.parse(str).getTime();
        }
        catch (ParseException e) {
            return null;
        }
    }

    
}
