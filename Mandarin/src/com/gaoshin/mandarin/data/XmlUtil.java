package com.gaoshin.mandarin.data;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {
    public static String getValue(Node node, String tag) {
        if (node == null)
            return null;
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getNodeName().equals("tag"))
                return item.getNodeValue();
        }
        return null;
    }
}
