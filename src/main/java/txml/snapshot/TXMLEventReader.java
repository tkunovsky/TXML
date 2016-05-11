/* 
 * Copyright 2016 Tomas Kunovsky.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package txml.snapshot;

import txml.snapshot.model.TXMLEvent;
import txml.snapshot.model.TStartElement;
import txml.snapshot.model.TNamespace;
import txml.snapshot.model.TEndElement;
import txml.snapshot.model.TCharacters;
import txml.snapshot.model.TAttribute;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import txml.database.DbApi;
import java.util.NoSuchElementException;
import javax.xml.namespace.QName;
import txml.Attribute;
import txml.Characters;
import txml.EndElement;
import txml.Namespace;
import txml.StartElement;
import txml.TXmlException;
import txml.TXmlResult;
import txml.XMLEvent;
import txml.XMLEventReader;
import txml.load.model.XmlNodeTypeEnum;
import txml.xpath.model.NodeGlobalSettings;
import txml.xpath.model.TNode;

public class TXMLEventReader extends TXmlResult implements XMLEventReader {
    private final Connection connection;
    private final String schemaName;    
    private final Long documentId;
    private final String documentName;
    private final DbApi dbApi;
    private boolean noNextEvent;
    private List<Integer> aPos;
    private List<List<Long>> nodes;
    private LinkedList<TNode> elementStack;
    private LinkedList<TXMLEvent> eventQueue;
    private final String xmlHeader;
    private int aDepth;
    private int maxDepth; //includes time event
    private final Long timeEvent;
    private PreparedStatement cpFromInstanceAndDepthStatement = null;
    private PreparedStatement lcpFromInstanceAndDepthStatement = null;
    private Map<Long, List<Namespace>> namespaceMap;

    public TXMLEventReader(Connection connection, String schemaName, Long documentId, String documentName, DbApi dbApi, Long timeEvent, String xmlHeader) throws SQLException {
        this.connection = connection;
        this.schemaName = schemaName;
        this.documentId = documentId;
        this.documentName = documentName;
        this.timeEvent = timeEvent;
        this.xmlHeader = xmlHeader;
        this.dbApi = dbApi;
        init();
    }
    
    private void init() throws SQLException {
        noNextEvent = false;
        aPos = new ArrayList<>();
        nodes = new ArrayList<>();
        elementStack = new LinkedList<>();
        eventQueue = new LinkedList<>();
        this.cpFromInstanceAndDepthStatement = dbApi.getCpFromInstanceAndDepthStatement(this.connection, this.schemaName);
        this.lcpFromInstanceAndDepthStatement = dbApi.getLcpFromInstanceAndDepthStatement(this.connection, this.schemaName);
        aDepth = 0;
        List<Long> rootCp = getCp(0);

        if (rootCp == null) {
            throw new TXmlException("Root of document isn't valid in input time instance.");
        }

        nodes.add(rootCp);
        aPos.add(0);
        
        namespaceMap = new HashMap<>();
        
        try (PreparedStatement namespacesFromDocumentStatement = dbApi.getNamespacesFromDocument(this.connection, this.schemaName, documentId); ResultSet resultSet = namespacesFromDocumentStatement.executeQuery()) {
            while (resultSet.next()) {
                Long nodeId = resultSet.getLong("node_id");
                Namespace namespace = new TNamespace(resultSet.getString("uri"), resultSet.getString("prefix"));
                List<Namespace> namespaces;
                if (namespaceMap.containsKey(nodeId)) {
                    namespaces = namespaceMap.get(nodeId);
                } else {
                    namespaces = new ArrayList<>();
                    namespaceMap.put(nodeId, namespaces);
                    
                }
                namespaces.add(namespace);
            }

        }
        
        eventQueue.add(new TXMLEvent(TXMLEvent.START_DOCUMENT));
    }
    
    private List<Long> getCp(int depth) throws SQLException, TXmlException {
        this.cpFromInstanceAndDepthStatement.setLong(1, documentId);
        this.cpFromInstanceAndDepthStatement.setLong(2, timeEvent);
        this.cpFromInstanceAndDepthStatement.setLong(3, timeEvent);
        this.cpFromInstanceAndDepthStatement.setInt(4, depth);
        List<Long> result;
        try (ResultSet resultSet = this.cpFromInstanceAndDepthStatement.executeQuery()) {
            if (!resultSet.next()) {
                return null;
            }   
            
            Object[] validObjectArray = (Object[])resultSet.getArray("valid").getArray();
            result = new ArrayList<>();
            for (Object validObject : validObjectArray) {
                result.add((Long) validObject);
            }
            
            if (resultSet.next()) {
                throw new TXmlException("To many cp records.");
            }
        }
        
        if (result.isEmpty()) {
            return null;
        }
        
        return result;
    }
    
    private TNode getLcp(Long id, int depth) throws SQLException {
        this.lcpFromInstanceAndDepthStatement.setLong(1, id);
        this.lcpFromInstanceAndDepthStatement.setLong(2, timeEvent);
        this.lcpFromInstanceAndDepthStatement.setLong(3, timeEvent);
        this.lcpFromInstanceAndDepthStatement.setInt(4, depth);
        TNode result;
        NodeGlobalSettings nodeGlobalSettings = new NodeGlobalSettings(connection, schemaName, documentName, this.dbApi, false);
        try (ResultSet resultSet = this.lcpFromInstanceAndDepthStatement.executeQuery()) {
            if (!resultSet.next()) {
                return null;
            }   
            
            result = new TNode(resultSet.getLong("from"), resultSet.getLong("to"), resultSet.getLong("id"), resultSet.getString("value"), resultSet.getInt("depth"), resultSet.getLong("lcp_class"), 
            resultSet.getShort("type"), resultSet.getString("local_part"), resultSet.getString("uri"), resultSet.getString("prefix"), resultSet.getLong("namespace_id"), resultSet.getLong("parentId"), resultSet.getLong("parentFrom"), resultSet.getLong("parentTo"), nodeGlobalSettings);
            
            if (resultSet.next()) {
                throw new TXmlException("To many cp records.");
            }
        }
        
        return result;
    }
    
    private List<Attribute> readAttributes() throws SQLException {
        int posInADepth = aPos.get(aDepth);
        Long nodeIdInADepth = nodes.get(aDepth).get(posInADepth);
        
        List<Attribute> attributes = new ArrayList<>();
        int attributeNameDepth = aDepth + 1;
        int attributeValueDepth = aDepth + 2;
        int aAttributeNamePos;
        int aAttributeValuePos;
        List<Long> attributeNameCp = null;
        List<Long> attributeValueCp = null;

        if (aPos.size() <= attributeNameDepth) {
            attributeNameCp = getCp(attributeNameDepth);
            if (attributeNameCp != null) {
                nodes.add(attributeNameCp);
                aPos.add(0);
                aAttributeNamePos = 0;
            } else {
                return attributes;
            }
        } else {
            aAttributeNamePos = aPos.get(attributeNameDepth);
            attributeNameCp = nodes.get(attributeNameDepth);
        }
        
        if (aPos.size() <= attributeValueDepth) {
            attributeValueCp = getCp(attributeValueDepth);
            if (attributeValueCp != null) {
                nodes.add(attributeValueCp);
                aPos.add(0);
                aAttributeValuePos = 0;
            } else {
                aAttributeValuePos = -1;// == undefined
            }
        } else {
            aAttributeValuePos = aPos.get(attributeValueDepth);
            attributeValueCp = nodes.get(attributeValueDepth);
        }

        int newAAttributeNamePos = aAttributeNamePos;
        int newAAttributeValuePos = aAttributeValuePos;
        for (; aAttributeNamePos < nodes.get(attributeNameDepth).size(); aAttributeNamePos++) {
            Long nextAttributeNameId = nodes.get(attributeNameDepth).get(aAttributeNamePos);
            TNode attributeNameNode = getLcp(nextAttributeNameId, attributeNameDepth);
            if (attributeNameNode.getNodeType() == XmlNodeTypeEnum.ATTRIBUTE.getShortValue() && attributeNameNode.getParentId().equals(nodeIdInADepth)) {
                QName qName = new QName(attributeNameNode.getNamespaceURI(), attributeNameNode.getLocalName(), attributeNameNode.getPrefix());
                String value = null;
                Long valueFrom = null;
                Long valueTo = null;
                Long valueId = null;
                if (newAAttributeValuePos != -1 && newAAttributeValuePos < nodes.get(attributeValueDepth).size()) {
                    Long nextAttributeValueId = nodes.get(attributeValueDepth).get(newAAttributeValuePos);
                    TNode attributeValueNode = getLcp(nextAttributeValueId, attributeValueDepth);
                    if (attributeValueNode.getNodeType() == XmlNodeTypeEnum.TEXT.getShortValue() && attributeValueNode.getParentId().equals(attributeNameNode.getId()) ) {
                        value = attributeValueNode.getNodeValue();
                        newAAttributeValuePos++;
                        valueFrom = attributeValueNode.getFromAsLong();
                        valueTo = attributeValueNode.getToAsLong(); 
                        valueId = attributeValueNode.getId(); 
                    }
                }
                attributes.add(new TAttribute(
                        qName, 
                        value, 
                        attributeNameNode.getFromAsLong(), 
                        attributeNameNode.getToAsLong(), 
                        attributeNameNode.getId(),
                        valueFrom,
                        valueTo,
                        valueId
                ));
                newAAttributeNamePos++;
            } else {
                break;
            }
            
        }
        
        aPos.set(attributeNameDepth, newAAttributeNamePos);
        
        if (newAAttributeValuePos != -1) {
            aPos.set(attributeValueDepth, newAAttributeValuePos);
        }

        return attributes;
    }
    
    private TNode tryGoDown(TNode tNodeInADepth) throws SQLException {
        int succDepth = aDepth + 1;
        Integer posInSuccDepth = null;
        Long nodeIdInSuccDepth = null;
        TNode tNodeInSuccDepth = null;
        
        if (succDepth < aPos.size() && aPos.get(succDepth) < nodes.get(succDepth).size()) {//I can go down
            posInSuccDepth = aPos.get(succDepth);
            nodeIdInSuccDepth = nodes.get(succDepth).get(posInSuccDepth);
            tNodeInSuccDepth = getLcp(nodeIdInSuccDepth, succDepth);
            if (tNodeInSuccDepth.getParentId().equals(tNodeInADepth.getId())) {
                aDepth = succDepth;
                return tNodeInSuccDepth;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    private TNode tryGoRight() throws SQLException {
        TNode tNodeInADepth;
        for (; aDepth >= 0; aDepth--) {
            int posInADepth = aPos.get(aDepth);
            Long nodeIdInADepth;

            if (posInADepth < nodes.get(aDepth).size()) {//I can go right
                nodeIdInADepth = nodes.get(aDepth).get(posInADepth);
                tNodeInADepth = getLcp(nodeIdInADepth, aDepth);
                TNode parentTNodeElement;

                while ((parentTNodeElement = elementStack.peek()).getDepth() >= aDepth) {
                    elementStack.poll();
                    eventQueue.add(makeEndElement(parentTNodeElement));
                }
                
                if (tNodeInADepth.getParentId().equals(parentTNodeElement.getId())) {
                    return tNodeInADepth;
                }
            }
            
        }
        return null;
    }
    
    private TEndElement makeEndElement(TNode tNode) {
        QName qName = new QName(tNode.getNamespaceURI(), tNode.getLocalName(), tNode.getPrefix());
        return new TEndElement(TXMLEvent.END_ELEMENT, qName, tNode.getFromAsLong(), tNode.getToAsLong(), tNode.getId());
    }
    
    private TStartElement makeStartElement(TNode tNode, List<Attribute> atributes) {
        QName qName = new QName(tNode.getNamespaceURI(), tNode.getLocalName(), tNode.getPrefix());
        return new TStartElement(TXMLEvent.START_ELEMENT, qName, atributes, tNode.getFromAsLong(), tNode.getToAsLong(), tNode.getId(), this.namespaceMap.get(tNode.getId()));
    }

    public String getHeader() {
        return xmlHeader;
    }
    
    @Override
    public TXMLEvent nextEvent() throws SQLException, TXmlException {
        if (!eventQueue.isEmpty()) {
            return eventQueue.poll();
        }
        
        if (noNextEvent) {
            throw new NoSuchElementException();
        }
        
        int posInADepth = aPos.get(aDepth);
        Long nodeIdInADepth = nodes.get(aDepth).get(posInADepth);
        TNode tNodeInADepth = getLcp(nodeIdInADepth, aDepth);

        if (tNodeInADepth.getNodeType() == XmlNodeTypeEnum.ELEMENT.getShortValue()) {
            List<Attribute> atributes = readAttributes();
            aPos.set(aDepth, posInADepth + 1);
            elementStack.push(tNodeInADepth);
            TNode tNodeInNextDepth = tryGoDown(tNodeInADepth);
            if (tNodeInNextDepth == null) {
                TNode rTNode = tryGoRight();
                if (rTNode == null) {
                    TNode elementTNode;
                    while ((elementTNode = elementStack.poll()) != null) {
                        eventQueue.add(makeEndElement(elementTNode));
                    }
                    eventQueue.add(new TXMLEvent(TXMLEvent.END_DOCUMENT));
                    noNextEvent = true;
                }
            }

            return makeStartElement(tNodeInADepth, atributes);
        } else if (tNodeInADepth.getNodeType() == XmlNodeTypeEnum.TEXT.getShortValue()) {
            aPos.set(aDepth, posInADepth + 1);
            
            TNode rTNode = tryGoRight();

            if (rTNode == null) {
                TNode elementTNode;
                while ((elementTNode = elementStack.poll()) != null) {
                    eventQueue.add(makeEndElement(elementTNode));
                }
                eventQueue.add(new TXMLEvent(TXMLEvent.END_DOCUMENT));
                noNextEvent = true;
            }

            return new TCharacters(TXMLEvent.CHARACTERS, tNodeInADepth.getNodeValue(), tNodeInADepth.getFromAsLong(), tNodeInADepth.getParentToAsLong(), tNodeInADepth.getId());
        }
        
        return null;
    }
    
    @Override
    public boolean hasNext() {
        if (!eventQueue.isEmpty()) {
            return true;
        }
        
        if (noNextEvent) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void closeDbConnection() throws SQLException {
        free();
        connection.close();
        
    }
    
    @Override
    public void free() throws SQLException {
        if (cpFromInstanceAndDepthStatement != null) {
            cpFromInstanceAndDepthStatement.close();
            cpFromInstanceAndDepthStatement = null;
        }
        
        if (lcpFromInstanceAndDepthStatement != null) {
            lcpFromInstanceAndDepthStatement.close();
            lcpFromInstanceAndDepthStatement = null;
        }
    }

    @Override
    public String getXMLDocFormatB() throws SQLException, TXmlException {
        StringBuilder result = new StringBuilder(this.getHeader().trim());
        int spaceOffset = 0;
        boolean valuePrinted = false;
        String indentation = "";
        while (this.hasNext()) {
            XMLEvent event = this.nextEvent();

            if (event.isStartElement()) {
                valuePrinted = false;
                StartElement startElement = event.asStartElement();
                result.append("\n").append(indentation).append("<").append(startElement.getName().getPrefix().equals("") ? startElement.getName().getLocalPart() : startElement.getName().getPrefix() + ":" + startElement.getName().getLocalPart());
                Iterator<Namespace> namespaces = startElement.getNamespaces();
                while (namespaces.hasNext()) {
                    Namespace namespace = namespaces.next();
                    result.append(" ").append("xmlns").append(namespace.getPrefix().equals("") ? "" : ":" + namespace.getPrefix()).append("=\"").append(namespace.getNamespaceURI()).append("\"");
                }
                
                Iterator<Attribute> attributes = startElement.getAttributes();
                while (attributes.hasNext()) {
                    Attribute attr = attributes.next();
                    result.append(" ").append(attr.getName().getPrefix().equals("") ? attr.getName().getLocalPart() : attr.getName().getPrefix() + ":" + attr.getName().getLocalPart()).append("=\"").append(attr.getValue()).append("\"");
                }
                result.append(">");
                spaceOffset++;
                indentation = "";
                for (int i = 0; i< spaceOffset; i++) indentation += "  ";
            } else if (event.isEndElement()) {
                spaceOffset--;
                indentation = "";
                for (int i = 0; i< spaceOffset; i++) indentation += "  ";
                EndElement endElement = event.asEndElement();
                result.append(valuePrinted ? "" : "\n" + indentation).append("</").append(endElement.getName().getPrefix().equals("") ? endElement.getName().getLocalPart() : endElement.getName().getPrefix() + ":" + endElement.getName().getLocalPart()).append(">");
                valuePrinted = false;
            } else if (event.isCharacters()) {
                valuePrinted  = true;
                Characters characters = event.asCharacters();
                String value = characters.getData().trim();
                if (value.isEmpty()) {
                    continue;
                }
                result.append(value);
            }
        }
        free();
        init();
        return result.toString().trim();
    }
    
    @Override
    public String getXMLDocFormatA() throws SQLException, TXmlException {
        StringBuilder result = new StringBuilder(this.getHeader().trim()+ "\n");
        int spaceOffset = 0;
        String indentation = "";
        while (this.hasNext()) {
            TXMLEvent event = this.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                result.append(indentation).append("<").append(startElement.getName().getPrefix().equals("") ? startElement.getName().getLocalPart() : startElement.getName().getPrefix() + ":" + startElement.getName().getLocalPart());
                
                Iterator<Namespace> namespaces = startElement.getNamespaces();
                while (namespaces.hasNext()) {
                    Namespace namespace = namespaces.next();
                    result.append(" ").append("xmlns").append(namespace.getPrefix().equals("") ? "" : ":" + namespace.getPrefix()).append("=\"").append(namespace.getNamespaceURI()).append("\"");
                }
                
                Iterator<Attribute> attributes = startElement.getAttributes();
                while (attributes.hasNext()) {
                    Attribute attr = attributes.next();
                    result.append(" ").append(attr.getName().getPrefix().equals("") ? attr.getName().getLocalPart() : attr.getName().getPrefix() + ":" + attr.getName().getLocalPart()).append("=\"").append(attr.getValue()).append("\"");
                }
                result.append(">").append("\n");
                spaceOffset++;
                indentation = "";
                for (int i = 0; i< spaceOffset; i++) indentation += "  ";
            } else if (event.isEndElement()) {
                spaceOffset--;
                indentation = "";
                for (int i = 0; i< spaceOffset; i++) indentation += "  ";
                EndElement endElement = event.asEndElement();
                result.append(indentation).append("</").append(endElement.getName().getPrefix().equals("") ? endElement.getName().getLocalPart() : endElement.getName().getPrefix() + ":" + endElement.getName().getLocalPart()).append(">").append("\n");
            } else if (event.isCharacters()) {
                Characters characters = event.asCharacters();
                String value = characters.getData().trim();
                if (value.isEmpty()) {
                    continue;
                }
                result.append(indentation).append(value).append("\n");
            }
        }
        free();
        init();
        return result.toString().trim();
    }

}
