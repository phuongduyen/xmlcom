package com.example.comparefilexml;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLFileCompare {
    String TAG = "XMLFileCompare ntp_";
    String NOTE_RESOURCE = "NoteResource";
    File mFile;
    Document doc = null;

    public XMLFileCompare(File file) {
        mFile = file;
        createDocument();
    }

    public void createDocument() {
        DocumentBuilderFactory dbf;
        DocumentBuilder db = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setCoalescing(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setIgnoringComments(true);

            db = dbf.newDocumentBuilder();
            doc = db.parse(mFile);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            Log.e(TAG, "createDocument  :" + e);
        } catch (SAXException e) {
            Log.e(TAG, "createDocument  :" + e);
        } catch (IOException e) {
            Log.e(TAG, "createDocument  :" + e);
        }
    }

    public void checkTagXML() {

        Element e = doc.getElementById("36");
        if (e == null) Log.d(TAG, "checkTagXML e NULL " + doc.getChildNodes());
        else Log.d(TAG, "checkTagXML getTagName :" + e.getNodeName());
        Log.d(TAG, "checkTagXML getChildNodes :" + doc.getChildNodes().getLength());
        Element root = doc.getDocumentElement();
        Log.d(TAG, "checkTagXML getTagName :" + root.getNodeName());
        Log.d(TAG, "checkTagXML getNodeName :" + root.getNodeValue());
        Log.d(TAG, "checkTagXML getChildNodes :" + root.getChildNodes().getLength());
        Log.d(TAG, "checkTagXML myFile :" + root.getAttribute("id"));
        Log.d(TAG, "checkTagXML myFile :" + root.getElementsByTagName("page").getLength());

//        NamedNodeMap namedNodeMap = element.getAttributes();
//        Log.d(TAG, "checkTagXML namedNodeMap.getNamedItem :" + namedNodeMap.getNamedItem("NoteResource"));
//        for(int i = 0; i < namedNodeMap.getLength(); i++){
//            Log.d(TAG, "checkTagXML namedNodeMap.item(i).getNodeName :" + namedNodeMap.item(i).getNodeName());
//        }

        NodeList noteResource = root.getElementsByTagName(NOTE_RESOURCE);
        Log.d(TAG, "checkTagXML noteResource :" + noteResource.getLength());
        Log.d(TAG, "checkTagXML noteResource leng atr :" + noteResource.item(0).getAttributes().getLength());
        Log.d(TAG, "checkTagXML noteResource :" + noteResource.item(0).getAttributes().getNamedItem("noteTree"));  // false
        Log.d(TAG, "checkTagXML noteResource :" + noteResource.item(0).getAttributes().getNamedItem("createdTime").getNodeValue());
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Log.d(TAG, "checkTagXML getNodeName :" + node.getNodeName());
        }

        NodeList nodel = root.getElementsByTagName("layer");
        Log.d(TAG, "checkTagXML nodel :" + nodel.getLength());
        for (int i = 0; i < nodel.getLength(); i++) {
            Node node = nodel.item(i); // layer
            Log.d(TAG, "checkTagXML nodel :" + node.getNodeName() + "/" + node.getAttributes().getNamedItem("id").getNodeValue());

            for (int ii = 0; ii < node.getChildNodes().getLength(); ii++) {
                Node nodee = node.getChildNodes().item(ii); // objectList
                Log.d(TAG, "checkTagXML nodel :" + nodee.getNodeName() + "/" + nodee.getChildNodes().getLength());
            }
        }

        nodel = root.getElementsByTagName("objectList");
        Log.d(TAG, "checkTagXML nodel 2 :" + nodel.getLength());
        for (int i = 0; i < nodel.getLength(); i++) {
            Node node = nodel.item(i); //objectList
            Log.d(TAG, "checkTagXML nodel 2 :" + node.getNodeName());

            for (int ii = 0; ii < node.getChildNodes().getLength(); ii++) {
                Node nodee = node.getChildNodes().item(ii); // object

                Log.d(TAG, "checkTagXML nodel 2 :" + nodee.getNodeName() + "/" + (nodee.getAttributes() != null? nodee.getAttributes().getNamedItem("id").getNodeValue(): "null"));
            }
        }
    }


    private boolean nodeTypeDiff = true;
    private boolean nodeValueDiff = true;

    public boolean diff(String xml1, String xml2, List<String> diffs) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();


        Document doc1 = db.parse(new ByteArrayInputStream(xml1.getBytes()));
        Document doc2 = db.parse(new ByteArrayInputStream(xml2.getBytes()));

        doc1.normalizeDocument();
        doc2.normalizeDocument();

        return diff(doc1, doc2, diffs);

    }

    /**
     * Diff 2 nodes and put the diffs in the list
     */
    public boolean diff(Node node1, Node node2, List<String> diffs) throws Exception {
        if (diffNodeExists(node1, node2, diffs)) {  // check path -> exist
            return true;
        }

        if (nodeTypeDiff) {
            diffNodeType(node1, node2, diffs);  // check type
        }

        if (nodeValueDiff) {
            diffNodeValue(node1, node2, diffs); // check value
        }

        System.out.println(node1.getNodeName() + "/" + node2.getNodeName());

        diffAttributes(node1, node2, diffs); // getAttributes of this note
        diffChild(node1, node2, diffs); // children -> children more

        return diffs.size() > 0;
    }

    /**
     * Diff the nodes
     */
    public boolean diffChild(Node node1, Node node2, List<String> diffs) throws Exception {
        //Sort by Name
        Map<String, Node> children1 = new LinkedHashMap<String, Node>();
        for (Node child1 = node1.getFirstChild(); child1 != null; child1 = child1.getNextSibling()) {
            children1.put(child1.getNodeName(), child1);
        }

        //Sort by Name
        Map<String, Node> children2 = new LinkedHashMap<String, Node>();
        for (Node child2 = node2.getFirstChild(); child2 != null; child2 = child2.getNextSibling()) {
            children2.put(child2.getNodeName(), child2);
        }

        //Diff all the children1
        for (Node child1 : children1.values()) {
            Node child2 = children2.remove(child1.getNodeName());
            diff(child1, child2, diffs);
        }

        //Diff all the children2 left over
        for (Node child2 : children2.values()) {
            Node child1 = children1.get(child2.getNodeName());
            diff(child1, child2, diffs);
        }

        return diffs.size() > 0;
    }


    /**
     * Diff the nodes
     */
    public boolean diffAttributes(Node node1, Node node2, List<String> diffs) throws Exception {
        //Sort by Name
        NamedNodeMap nodeMap1 = node1.getAttributes();
        Map<String, Node> attributes1 = new LinkedHashMap<String, Node>();
        for (int index = 0; nodeMap1 != null && index < nodeMap1.getLength(); index++) {
            attributes1.put(nodeMap1.item(index).getNodeName(), nodeMap1.item(index));
        }

        //Sort by Name
        NamedNodeMap nodeMap2 = node2.getAttributes();
        Map<String, Node> attributes2 = new LinkedHashMap<String, Node>();
        for (int index = 0; nodeMap2 != null && index < nodeMap2.getLength(); index++) {
            attributes2.put(nodeMap2.item(index).getNodeName(), nodeMap2.item(index));
        }

        //Diff all the attributes1
        for (Node attribute1 : attributes1.values()) {
            Node attribute2 = attributes2.remove(attribute1.getNodeName());
            diff(attribute1, attribute2, diffs);
        }

        //Diff all the attributes2 left over
        for (Node attribute2 : attributes2.values()) {
            Node attribute1 = attributes1.get(attribute2.getNodeName());
            diff(attribute1, attribute2, diffs);
        }
        return diffs.size() > 0;
    }

    public boolean diffNodeExists(Node node1, Node node2, List<String> diffs){
        if (node1 == null && node2 == null) {
            diffs.add(getPath(node2) + ":node " + node1 + "!=" + node2 + "\n");
            return true;
        }
        if (node1 == null && node2 != null) {
            diffs.add(getPath(node2) + ":node " + node1 + "!=" + node2.getNodeName());
            return true;
        }
        if (node1 != null && node2 == null) {
            diffs.add(getPath(node1) + ":node " + node1.getNodeName() + "!=" + node2);
            return true;
        }
        return false;
    }

    public boolean diffNodeType(Node node1, Node node2, List<String> diffs){
        if (node1.getNodeType() != node2.getNodeType()) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeType() + "!=" + node2.getNodeType());
            return true;
        }
        return false;
    }

    public boolean diffNodeValue(Node node1, Node node2, List<String> diffs){
        if (node1.getNodeValue() == null && node2.getNodeValue() == null) {
            return false;
        }
        if (node1.getNodeValue() == null && node2.getNodeValue() != null) {
            diffs.add(getPath(node1) + ":type " + node1 + "!=" + node2.getNodeValue());
            return true;
        }
        if (node1.getNodeValue() != null && node2.getNodeValue() == null) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeValue() + "!=" + node2);
            return true;
        }
        if (!node1.getNodeValue().equals(node2.getNodeValue())) {
            diffs.add(getPath(node1) + ":type " + node1.getNodeValue() + "!=" + node2.getNodeValue());
            return true;
        }
        return false;
    }

    public String getPath(Node node) {
        StringBuilder path = new StringBuilder();
        do {
            path.insert(0, node.getNodeName());
            path.insert(0, "/");
        }
        while ((node = node.getParentNode()) != null);
        return path.toString();
    }


}
