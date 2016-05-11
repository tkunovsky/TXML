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
package txml.load;

import txml.load.model.XmlNodeTypeEnum;
import txml.load.model.XmlNode;
import txml.load.model.XmlDataModel;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import txml.TXmlException;

public class XmlDocumentReader {
    
    public XmlDataModel loadDocumentToDb(String filename) throws FileNotFoundException, IOException, XMLStreamException {
        XmlDataModel result = null;
        try (InputStream in = new FileInputStream(filename)) {
            result = loadDocumentToDb(in);
        }
                    
        return result;
    } 
        
    public XmlDataModel loadDocumentToDb(InputStream in) throws IOException, XMLStreamException {
        InputStream inb = new BufferedInputStream(in);
        inb.mark(Integer.MAX_VALUE);
        XmlDataModel dataModel = new XmlDataModel();
        dataModel.setHeader(getHeader(inb));
        inb.reset();

        final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        final XMLEventReader eventReader = inputFactory.createXMLEventReader(inb);
        
        XmlNode root = null;
        XmlNode aNode = null;
        
        while (eventReader.hasNext()) {
            final XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                List<Namespace> namespaces = new ArrayList<Namespace>();
                
                Iterator<Namespace> namespaceIterator = startElement.getNamespaces();
                while (namespaceIterator.hasNext()) {
                    Namespace namespace = namespaceIterator.next();
                    namespaces.add(namespace);
                }
                
                if (root != null) {
                    if (!namespaces.isEmpty()) {
                        throw new TXmlException("Attribute \"xmlns\" is supported only for root element.");
                    }
                    
                    XmlNode node = new XmlNode(startElement.getName(), XmlNodeTypeEnum.ELEMENT, aNode, namespaces);
                    aNode.getChildren().add(node);
                    aNode = node;
                } else {
                    root = new XmlNode(startElement.getName(), XmlNodeTypeEnum.ELEMENT, null, namespaces);  
                    aNode = root;
                }
                Iterator<Attribute> attributeIterator = startElement.getAttributes();
                while (attributeIterator.hasNext()) {
                    Attribute attr = attributeIterator.next();
                    XmlNode attributeNode = new XmlNode(attr.getName(), XmlNodeTypeEnum.ATTRIBUTE, aNode, new ArrayList<Namespace>());
                    aNode.getChildren().add(attributeNode);
                    XmlNode valueNode = new XmlNode(new QName("value"), XmlNodeTypeEnum.TEXT, attributeNode, new ArrayList<Namespace>());
                    valueNode.setValue(attr.getValue());
                    attributeNode.getChildren().add(valueNode);
                }
            } else if (event.isEndElement()) {
                aNode = aNode.getParent();
            } else if (event.isCharacters()) {
                Characters characters = event.asCharacters();
                String value = characters.getData().trim();
                if (value.isEmpty()) {
                    continue;
                }
                XmlNode valueNode = new XmlNode(new QName("value"), XmlNodeTypeEnum.TEXT, aNode, new ArrayList<Namespace>());
                valueNode.setValue(value);
                aNode.getChildren().add(valueNode);
            }
        }
        
        dataModel.setRoot(root);

        return dataModel;
    }
    
    private String getHeader(InputStream inb) throws IOException {
        int prevChar = '\0';
        int aChar = '\0';
        StringBuilder header = new StringBuilder();
        
        do {
            prevChar = aChar;
            aChar = inb.read();  
            
            if ((prevChar == '<') && (aChar != '!') && (aChar != '?')) {
                return header.substring(0, header.length() - 1);
            } else {
                header.append(Character.toChars(aChar));
            }
        } while (aChar != -1);

        return "";
    }
    
}
