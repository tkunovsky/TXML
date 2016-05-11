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
package txml.xpath.model;

import txml.Node;
import txml.NamedNodeMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import txml.database.model.NamespaceInfo;
import txml.TXmlException;
import txml.load.model.XmlNodeTypeEnum;
import txml.NodeList;

public class TNode implements Node {
    private NodeGlobalSettings settings;
    private NodeLocalSettings privateSettings;
    private Long from;
    private Long to;
    private Long id;
    private Short type;
    private String value;
    private Integer depth;
    private Long lcp_class;
    private TNode parentNode;
    private TNodeList childNodes;
    private String localPart;
    private String prefix;
    private String namespaceURI;
    private Long namespaceId;
    private Long parentId;
    private Long parentFrom;
    private Long parentTo;
    private TNamedNodeMap attributes;

    private static final int SPACE_SIZE = 2;
    
    public TNode(Long from, Long to, Long id, String value, Integer depth, Long lcp_class, 
            Short type, String localPart, String namespaceUri, String prefix, Long namespaceId, Long parentId, Long parentFrom, Long parentTo, NodeGlobalSettings settings) {
        this.from = from;
        this.to = to;
        this.id = id;
        this.value = value;
        this.depth = depth;
        this.lcp_class = lcp_class;
        this.type = type;
        this.localPart = localPart;
        this.namespaceURI = namespaceUri;
        this.prefix = prefix;
        childNodes = null;
        parentNode = null;
        this.parentId = parentId;
        this.parentFrom = parentFrom;
        this.parentTo = parentTo;
        this.settings = settings;
        this.attributes = null;
        this.privateSettings = new NodeLocalSettings();
        this.namespaceId = namespaceId;
    }

    public TNode(Long from, Long to, Long id, String value, Integer depth, Long lcp_class, 
            Short type, String localPart, String namespaceUri, String prefix, Long namespaceId, Long parentId, Long parentFrom, Long parentTo, TNodeList childNodes, NodeGlobalSettings settings) {
        this(from, to, id, value, depth, lcp_class, type, localPart, namespaceUri, prefix, namespaceId, parentId, parentFrom, parentTo, settings);
        this.childNodes = childNodes;
    }
    
    public TNode(Long from, Long to, Long id, String value, Integer depth, Long lcp_class, 
            Short type, String localPart, String namespaceUri, String prefix, Long namespaceId, Long parentId, Long parentFrom, Long parentTo, TNode parentNode, NodeGlobalSettings settings) {
        this(from, to, id, value, depth, lcp_class, type, localPart, namespaceUri, prefix, namespaceId, parentId, parentFrom, parentTo, settings);
        this.parentNode = parentNode;
    }
    
    public TNode(Long from, Long to, Long id, String value, Integer depth, Long lcp_class, 
            Short type, String localPart, String namespaceUri, String prefix, Long namespaceId, Long parentId, Long parentFrom, Long parentTo, TNode parentNode, TNodeList childNodes, NodeGlobalSettings settings) {
        this(from, to, id, value, depth, lcp_class, type, localPart, namespaceUri, prefix, namespaceId, parentId, parentFrom, parentTo, settings);
        this.childNodes = childNodes;
        this.parentNode = parentNode;
        this.parentId = parentId;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }
    
    @Override
    public void refresh() {
        if (!this.to.equals(this.settings.getDbApi().getNOW())) {
            return;
        } else {
            this.parentNode = null;
            this.childNodes = null;
            PreparedStatement preparedStatement;
            try {
                preparedStatement = this.settings.getDbApi().getLastNode(this.settings.getConnection(), this.settings.getSchemaName(), this.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    throw new TXmlException("Missing node.");
                } else {
                    TNode tNode = new TNode(
                        resultSet.getLong("from"), 
                        resultSet.getLong("to"), 
                        resultSet.getLong("id"), 
                        resultSet.getString("value"), 
                        resultSet.getInt("depth"), 
                        resultSet.getLong("lcp_class"),
                        resultSet.getShort("type"),
                        resultSet.getString("local_part"),
                        resultSet.getString("uri"),
                        resultSet.getString("prefix"),        
                        resultSet.getLong("namespace_id"),         
                        resultSet.getLong("parentId"),
                        resultSet.getLong("parentFrom"),
                        resultSet.getLong("parentTo"),
                        settings
                        );  
                    if (resultSet.next()) {//never
                        throw new TXmlException("Too many node.");
                    }
                    resultSet.close();
                    preparedStatement.close();

                    this.from = tNode.from; 
                    this.to = tNode.to; 
                    this.id = tNode.id; 
                    this.value = tNode.value; 
                    this.depth = tNode.depth; 
                    this.lcp_class = tNode.lcp_class;
                    this.type = tNode.type;
                    this.localPart = tNode.localPart;
                    this.namespaceURI = tNode.namespaceURI;
                    this.prefix = tNode.prefix;        
                    this.namespaceId = tNode.namespaceId;         
                    this.parentId = tNode.parentId;
                    this.parentFrom = tNode.parentFrom;
                    this.parentTo = tNode.parentTo;
                }
            } catch (SQLException ex) {
                throw new TXmlException(ex.getLocalizedMessage());
            } 
        }
    }
    
    @Override
    public TNode getParentNode() throws TXmlException {
        if (this.depth == 0) {
            return null;
        } else if (this.parentNode != null) {
            return parentNode;
        } else {
            PreparedStatement preparedStatement;
            try {
                preparedStatement = this.settings.getDbApi().getParent(this.settings.getConnection(), this.settings.getSchemaName());
                preparedStatement.setLong(1, this.getParentId());
                preparedStatement.setLong(2, this.getParentFromAsLong());
                preparedStatement.setLong(3, this.getParentToAsLong());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    throw new TXmlException("Missing parent.");
                } else {
                    TNode tNode = new TNode(
                        resultSet.getLong("from"), 
                        resultSet.getLong("to"), 
                        resultSet.getLong("id"), 
                        resultSet.getString("value"), 
                        resultSet.getInt("depth"), 
                        resultSet.getLong("lcp_class"),
                        resultSet.getShort("type"),
                        resultSet.getString("local_part"),
                        resultSet.getString("uri"),
                        resultSet.getString("prefix"),        
                        resultSet.getLong("namespace_id"),         
                        resultSet.getLong("parentId"),
                        resultSet.getLong("parentFrom"),
                        resultSet.getLong("parentTo"),
                        settings
                        );  
                    if (resultSet.next()) {
                        throw new TXmlException("Too many parents.");
                    }
                    resultSet.close();
                    preparedStatement.close();
                    parentNode = tNode;
                    return parentNode;
                }
            } catch (SQLException ex) {
                throw new TXmlException(ex.getLocalizedMessage());
            }
        }
    }

    @Override
    public Integer getDepth() {
        return depth;
    }
    
    @Override
    public Timestamp getFromAsTimestamp() {
        return new Timestamp(from);
    }
    
    @Override
    public Timestamp getToAsTimestamp() {
        return new Timestamp(to);
    }
    
    @Override
    public Long getFromAsLong() {
        return from;
    }
    
    @Override
    public Long getToAsLong() {
        return to;
    }

    @Override
    public Long getParentFromAsLong() {
        return parentFrom;
    }

    @Override
    public Long getParentToAsLong() {
        return parentTo;
    }
    
    @Override
    public Timestamp getParentFromAsTimestamp() {
        return new Timestamp(parentFrom);
    }
    
    @Override
    public Timestamp getParentToAsTimestamp() {
        return new Timestamp(to);
    }
    
    @Override
    public String getParentFromAsString() {
        return settings.getDbApi().getTime(parentFrom);
    }

    @Override
    public String getParentToAsString() {
        return settings.getDbApi().getTime(parentTo);
    }
    
    @Override
    public String getFromAsString() {
        return settings.getDbApi().getTime(from);
    }

    @Override
    public String getToAsString() {
        return settings.getDbApi().getTime(to);
    }

    @Override
    public Long getId() {
        return id;
    }
    
    public boolean hasDescendant(TNode descendant) throws SQLException {
        List<TNode> thisNodeInList = new ArrayList<>();
        thisNodeInList.add(this);
        TNodeList tNodeList = new TNodeList(descendant.getSettings(), thisNodeInList);
        TNodeList resultList = tNodeList.getDescendantsByTypeAndLabel(descendant.getNodeName(), descendant.type);
        for (int i = 0; i < resultList.getLength(); i++) {
            if (resultList.item(i).equals(descendant)) {
                return true;
            } 
        }
        return false;
    }
    
    @Override
    public void deleteInDocument() throws SQLException, TXmlException {
        try {
            this.settings.getConnection().setAutoCommit(false);
            refresh();
            deleteInDocument(true);
            refresh();
        } catch (SQLException | TXmlException ex) {
            this.settings.getConnection().rollback();
            throw ex;
        }
        this.getSettings().getConnection().commit();
    }
    
    public void deleteInDocument(boolean refreshCurrTime) throws SQLException {
        if (refreshCurrTime) {
            settings.getDbApi().refreshCurrentTime();
        }
        
        if (this.to < settings.getDbApi().getCurrTimeAsLong())
            return;
        
        PreparedStatement preparedStatement = settings.getDbApi().getCpFromIntervalAndDepth(settings.getConnection(), settings.getSchemaName(), settings.getDocumentName(), settings.getDbApi().getNOW(), depth);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            throw new TXmlException("Missing valid array");
        }
        
        Object[] validObjectArray = (Object[])resultSet.getArray("valid").getArray();
        Long cpClass = resultSet.getLong("cp_class");
                               
        Long lastCpFrom = resultSet.getLong("from");
        if (!lastCpFrom.equals(settings.getDbApi().getCurrTimeAsLong())) {
            settings.getDbApi().splitCpInterval(settings.getConnection(), settings.getSchemaName(), lastCpFrom, settings.getDbApi().getNOW(), cpClass, settings.getDbApi().getCurrTimeAsLong(), validObjectArray);
        }
        
        settings.getDbApi().deleteNodeInValid(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), cpClass, validObjectArray, this.id); 
        
        preparedStatement.close();
        resultSet.close();
        
        TNamedNodeMap tNamedNodeMap = this.getAttributes();
        for (int i = 0; i < tNamedNodeMap.getLength(); i++) {
            tNamedNodeMap.item(i).deleteInDocument(false);
        }
        
        TNodeList tNodeList = this.getChildNodes();
        for (int i = 0; i < tNodeList.getLength(); i++) {
            tNodeList.item(i).deleteInDocument(false);
        }
        
        if (!Objects.equals(settings.getDbApi().getCurrTimeAsLong(), from)) {
            settings.getDbApi().toLcpUpdate(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getNOW(), settings.getDbApi().getCurrTimeAsLong() - 1, id);
        } else {
            settings.getDbApi().lcpDelete(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getNOW(), id);
        }
    }
    
    @Override
    public void setParentInDocument(Node parent) throws SQLException, TXmlException {
        setParentInDocument(parent, -1);
    }
    
    @Override
    public void setParentInDocument(Node p, Integer position) throws SQLException, TXmlException {
        if (p == null) {
            throw new TXmlException("No parent node.");
        }
        
        if (!(p instanceof TNode)) {
            throw new TXmlException("Type of parent isn't supported.");
        }
        
        TNode parent = (TNode) p;

        parent.refresh();
        this.refresh();
        
        if (parent.getNodeType() != XmlNodeTypeEnum.ELEMENT.getShortValue()) {
            throw new TXmlException("Parent node isn't element node.");
        }
        
        if (this.hasDescendant(parent) || this.getId().equals(parent.getId())) {
            throw new TXmlException("Cycle in document detected, bad parent.");
        }
        
        try {
            this.settings.getConnection().setAutoCommit(false);
            setParentInDocument(true, parent, position);
            refresh();
            parent.refresh();
        } catch (SQLException | TXmlException ex) {
            this.settings.getConnection().rollback();
            throw ex;
        }
        this.getSettings().getConnection().commit();  
    }
    
    public void setParentInDocument(boolean refreshCurrTime, TNode parent, Integer position) throws SQLException {
        if (refreshCurrTime) {
            settings.getDbApi().refreshCurrentTime();
        }
        
        if (this.to != settings.getDbApi().getNOW() || parent.getToAsLong() != settings.getDbApi().getNOW())
            return;

        Long documentId;
        try (PreparedStatement preparedStatement = settings.getDbApi().getSelectDocumentStatement(settings.getConnection(), settings.getSchemaName(), settings.getDocumentName()); 
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                documentId = resultSet.getLong("id");
            } else {
                throw new TXmlException("unknown document name: '" + settings.getDocumentName() + "'");
            }
        }
        
        List<Pair<TNode, TNode>> subtree = new ArrayList<>();
        subtree.add(new Pair<>(parent, this));
        getSubTreeInDocument(this, subtree);
        
        Collections.sort(subtree, new Comparator<Pair<TNode, TNode>>(){
            @Override
            public int compare(Pair<TNode, TNode> o1, Pair<TNode, TNode> o2) {
                return o1.getSecond().depth.compareTo(o2.getSecond().depth);
            }
        });
        
        setParentInDocumentPhaseDelete(subtree);
        setParentInDocumentPhaseInsert(subtree, documentId, position);
    }
    
    private void setParentInDocumentPhaseDelete(List<Pair<TNode, TNode>> subtree) throws SQLException {
        for (int i = subtree.size() - 1; i >= 0; i--) {
            TNode child = subtree.get(i).getSecond();
            PreparedStatement preparedStatement;
            ResultSet resultSet;

            //get CP for origin node
            preparedStatement = settings.getDbApi().getCpFromIntervalAndDepth(settings.getConnection(), settings.getSchemaName(), settings.getDocumentName(), settings.getDbApi().getNOW(), child.getDepth());
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                throw new TXmlException("Missing valid array");
            }

            Object[] originValidObjectArray = (Object[])resultSet.getArray("valid").getArray();
            Long originCpClass = resultSet.getLong("cp_class");                    
            Long originLastCpFrom = resultSet.getLong("from");
            if (!originLastCpFrom.equals(settings.getDbApi().getCurrTimeAsLong())) {
                settings.getDbApi().splitCpInterval(settings.getConnection(), settings.getSchemaName(), originLastCpFrom, settings.getDbApi().getNOW(), originCpClass, settings.getDbApi().getCurrTimeAsLong(), originValidObjectArray);
            }

            //delete node in origin cp
            settings.getDbApi().deleteNodeInValid(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), originCpClass, originValidObjectArray, child.getId()); 
            resultSet.close();
            preparedStatement.close();
            
            //delete node in LCP
            if (!Objects.equals(settings.getDbApi().getCurrTimeAsLong(), child.getFromAsLong())) {
                settings.getDbApi().toLcpUpdateWithFrom(settings.getConnection(), settings.getSchemaName(), child.from, settings.getDbApi().getNOW(), settings.getDbApi().getCurrTimeAsLong() - 1, child.getId());
            } else {
                settings.getDbApi().lcpDelete(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getNOW(), child.getId());
            }
        }
    }
    
    private void setParentInDocumentPhaseInsert(List<Pair<TNode, TNode>> subtree, Long documentId, Integer position) throws SQLException {
        int levelDiff = subtree.get(0).getSecond().getDepth() - subtree.get(0).getFirst().getDepth();
        for (int i = 0; i < subtree.size(); i++) {
            TNode child = subtree.get(i).getSecond();
            TNode parent = subtree.get(i).getFirst();
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            String newLocalPart = child.localPart;
            String newPrefix = child.prefix;
            String newNamespaceURI = child.namespaceURI;
            Long newNamespaceId = child.namespaceId;
            short newType = child.type;
            Long newLcpClass = null;
            Long newCpClass = null;
            Long newParentCpClass = null;
            Long newParentFrom;
            Long parentLcpClass = parent.lcp_class;
            int newDepth;
            
            if (i > 0) {
                newParentFrom = settings.getDbApi().getCurrTimeAsLong();
                newDepth = parent.depth + 1 + (1 - levelDiff);
            } else {
                newDepth = parent.depth + 1;
                newParentFrom = parent.from;
            }

            //get LCP CLASS for new node
            preparedStatement = settings.getDbApi().getLCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), newLocalPart, newNamespaceId, newDepth, parentLcpClass, newType);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                newLcpClass = settings.getDbApi().getNextValLCPClassIdSequence(settings.getConnection(), settings.getSchemaName());
                settings.getDbApi().insertLCPClassTableStatement(settings.getConnection(), settings.getSchemaName(), newLcpClass, newType, newLocalPart, newNamespaceId, newDepth, parentLcpClass , documentId);
            } else {
                newLcpClass = resultSet.getLong("id");
                resultSet.close();
                preparedStatement.close();
            }

            //insert node into LCP table
            settings.getDbApi().insertLCPTable(settings.getConnection(), settings.getSchemaName(), child.id, settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), parent.getId(), newParentFrom, parent.getToAsLong(), child.getNodeValue(), newLcpClass);

            //get CP CLASS for new Node
            preparedStatement = settings.getDbApi().getCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), newDepth, documentId);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                preparedStatement = settings.getDbApi().getCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), newDepth - 1, documentId);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    newParentCpClass = resultSet.getLong("id");
                    resultSet.close();
                    preparedStatement.close();
                    newCpClass = settings.getDbApi().getNextValCPClassIdSequence(settings.getConnection(), settings.getSchemaName());
                    settings.getDbApi().insertCPClassTableStatement(settings.getConnection(), settings.getSchemaName(), newCpClass, newDepth, newParentCpClass, documentId);
                } else {
                    resultSet.close();
                    preparedStatement.close();
                    throw new TXmlException("missing parent cp class record");
                }

            } else {
                newCpClass = resultSet.getLong("id");
                resultSet.close();
                preparedStatement.close();
            }

            //get CP for new Node, if it doesn't exist, create it + split it if needed
            Object[] newValidObjectArray; //valid without new node
            preparedStatement = settings.getDbApi().getCPWithoutId(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getNOW(), newCpClass);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long cpFrom = resultSet.getLong("from");
                newValidObjectArray = (Object[])resultSet.getArray("valid").getArray();           
                if (!cpFrom.equals(settings.getDbApi().getCurrTimeAsLong())) {//bad from
                    settings.getDbApi().splitCpInterval(settings.getConnection(), settings.getSchemaName(), cpFrom, settings.getDbApi().getNOW(), newCpClass, settings.getDbApi().getCurrTimeAsLong(), newValidObjectArray);
                }

            } else {
                resultSet.close();
                preparedStatement.close();
                newValidObjectArray = new Object[0];
                settings.getDbApi().getInsertCPTableStatement(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newCpClass, newValidObjectArray);
            }

            //insert new node into CP table
            if (i == 0) {
                settings.getDbApi().insertNodeInValidInPos(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newCpClass, newValidObjectArray, child.getId(), parent.getId(), newParentFrom, parent.getToAsLong(), position, newType, settings.getDbApi().getNOW(), documentId, newDepth);
            } else {
                settings.getDbApi().insertNodeInValidToEnd(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newCpClass, newValidObjectArray, child.getId(), parent.getId(), newParentFrom, parent.getToAsLong(), newDepth, documentId, settings.getDbApi().getNOW());
            }
        }
    }
    
    private void getSubTreeInDocument(TNode child, List<Pair<TNode, TNode>> subtree) throws SQLException {
        if (child.getToAsLong() != settings.getDbApi().getNOW())
            return;

        List<TNode> nextNodesList = new ArrayList<>();
        TNamedNodeMap tNamedNodeMap = child.getAttributes();
        for (int i = 0; i < tNamedNodeMap.getLength(); i++) {
            nextNodesList.add(tNamedNodeMap.item(i));
        }
        
        TNodeList tNodeList = child.getChildNodes();
        for (int i = 0; i < tNodeList.getLength(); i++) {
            nextNodesList.add(tNodeList.item(i));
        }
        
        TNodeList tNodeListNextNodes = new TNodeList(child.getSettings(), nextNodesList);
        tNodeListNextNodes = tNodeListNextNodes.sort();

        for (int i = 0; i < tNodeListNextNodes.getLength(); i++) {
            subtree.add(new Pair<>(child, tNodeListNextNodes.item(i)));
            getSubTreeInDocument(tNodeListNextNodes.item(i), subtree);
        } 
    }
    
    @Override
    public void insertIntoDocument(String path, String value) throws SQLException, TXmlException {
        insertIntoDocument(path, value, -1);
    }
    
    @Override
    public void insertIntoDocument(String path, String value, Integer position) throws SQLException, TXmlException {
        try {
            this.settings.getConnection().setAutoCommit(false);
            refresh();
            insertIntoDocument(true, path, value, position);
            refresh();
        } catch (SQLException | TXmlException ex) {
            this.settings.getConnection().rollback();
            throw ex;
        }
        this.getSettings().getConnection().commit();
    }
    
    public void insertIntoDocument(boolean refreshCurrTime, String path, String value, Integer position) throws SQLException {
        if (refreshCurrTime) {
            settings.getDbApi().refreshCurrentTime();
        }
        
        if (this.to < settings.getDbApi().getCurrTimeAsLong())
            return;
        
        Long documentId;
        try (PreparedStatement preparedStatement = settings.getDbApi().getSelectDocumentStatement(settings.getConnection(), settings.getSchemaName(), settings.getDocumentName()); 
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                documentId = resultSet.getLong("id");
            } else {
                throw new TXmlException("unknown document name: '" + settings.getDocumentName() + "'");
            }
        }
        
        if (path.isEmpty()) {
            if (value != null) {
                insertIntoDocumentValue(this, value, documentId, position);
            }
            
            return;
        }

        if (path.substring(0, 1).equals("/")) {
            path = path.substring(1);
        }
        
        List<String> list = new ArrayList<>(Arrays.asList(path.split("/")));
        insertIntoDocument(list, this, value, 0, documentId, position); 
    }
    
    private void insertIntoDocument(List<String> path, TNode parent, String value, int pos, Long documentId, Integer position) throws SQLException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String newLabel = path.get(pos);
        int newDepth = parent.depth + 1;
        Long parentLcpClass = parent.lcp_class;
        short newType;
        Long newLcpClass = null;
        Long newCpClass = null;
        Long newParentCpClass = null;
        
        //type attribute vs element
        if (newLabel.contains("@")) {
            newType = XmlNodeTypeEnum.ATTRIBUTE.getShortValue();
            newLabel = newLabel.replace("@", "");
        } else if (newLabel.contains("attribute::")) {//todo label
            newType = XmlNodeTypeEnum.ATTRIBUTE.getShortValue();
            newLabel = newLabel.replace("attribute::", "");
        } else {
            newType = XmlNodeTypeEnum.ELEMENT.getShortValue();
        }
        
        NamespaceInfo namespaceInfo = this.settings.getDbApi().getNamespaceInfoFromPrefix(this.settings.getConnection(), this.settings.getSchemaName(), newLabel, documentId, newType);
 
        //get LCP CLASS
        preparedStatement = settings.getDbApi().getLCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), namespaceInfo.getLocalPart(), namespaceInfo.getId(), newDepth, parentLcpClass, newType);
        resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();

            newLcpClass = settings.getDbApi().getNextValLCPClassIdSequence(settings.getConnection(), settings.getSchemaName());
            settings.getDbApi().insertLCPClassTableStatement(settings.getConnection(), settings.getSchemaName(), newLcpClass, newType, namespaceInfo.getLocalPart(), namespaceInfo.getId(), newDepth, parentLcpClass , documentId);
        } else {
            newLcpClass = resultSet.getLong("id");
            resultSet.close();
            preparedStatement.close();
        }
        
        //get CP CLASS
        preparedStatement = settings.getDbApi().getCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), newDepth, documentId);
        resultSet = preparedStatement.executeQuery();
        
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            preparedStatement = settings.getDbApi().getCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), newDepth - 1, documentId);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                newParentCpClass = resultSet.getLong("id");
                resultSet.close();
                preparedStatement.close();
                newCpClass = settings.getDbApi().getNextValCPClassIdSequence(settings.getConnection(), settings.getSchemaName());
                settings.getDbApi().insertCPClassTableStatement(settings.getConnection(), settings.getSchemaName(), newCpClass, newDepth, newParentCpClass, documentId);
            } else {
                resultSet.close();
                preparedStatement.close();
                throw new TXmlException("missing parent cp class record");
            }

        } else {
            newCpClass = resultSet.getLong("id");
            resultSet.close();
            preparedStatement.close();
        }
        
        //get CP
        Object[] newValidObjectArray; //valid without new node
        preparedStatement = settings.getDbApi().getCPWithoutId(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getNOW(), newCpClass);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Long cpFrom = resultSet.getLong("from");
            newValidObjectArray = (Object[])resultSet.getArray("valid").getArray();           
            if (!cpFrom.equals(settings.getDbApi().getCurrTimeAsLong())) {//bad from
                settings.getDbApi().splitCpInterval(settings.getConnection(), settings.getSchemaName(), cpFrom, settings.getDbApi().getNOW(), newCpClass, settings.getDbApi().getCurrTimeAsLong(), newValidObjectArray);
            }
            
        } else {
            resultSet.close();
            preparedStatement.close();
            newValidObjectArray = new Object[0];
            settings.getDbApi().getInsertCPTableStatement(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newCpClass, newValidObjectArray);
        }
        
        //insert node into CP table
        Long newNodeId = settings.getDbApi().getNextValNodeIdSequence(settings.getConnection(), settings.getSchemaName());        
        settings.getDbApi().insertNodeInValidInPos(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newCpClass, newValidObjectArray, newNodeId, parent.getId(), parent.getFromAsLong(), parent.getToAsLong(), position, newType, settings.getDbApi().getNOW(), documentId, newDepth);

        //insert node into LCP table
        settings.getDbApi().insertLCPTable(settings.getConnection(), settings.getSchemaName(), newNodeId, settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), parent.getId(), parent.getFromAsLong(), parent.getToAsLong(), null,  newLcpClass);
        
        
        TNode thisNode = new TNode(settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newNodeId, null, newDepth, newLcpClass, 
            newType, namespaceInfo.getLocalPart(), namespaceInfo.getUri(), namespaceInfo.getPrefix(), namespaceInfo.getId(), parent.getId(), parent.from, parent.to, parent, settings);
        
        if (path.size() > (pos + 1)) {
            insertIntoDocument(path, thisNode, value, pos + 1, documentId, -1);
        } else {
            if (value != null) {
                insertIntoDocumentValue(thisNode, value, documentId, -1);
            }
        }
    }

    public NodeLocalSettings getLocalSettings() {
        return privateSettings;
    }

    public void setLocalSettings(NodeLocalSettings localSettings) {
        this.privateSettings = localSettings;
    }
    
    private void insertIntoDocumentValue(TNode parent, String value, Long documentId, Integer position) throws SQLException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        int newDepth = parent.depth + 1;
        Long parentLcpClass = parent.lcp_class;
        short newType;
        Long newLcpClass = null;
        Long newCpClass = null;
        Long newParentCpClass = null;
        
        newType = XmlNodeTypeEnum.TEXT.getShortValue();
        NamespaceInfo namespaceInfo = this.settings.getDbApi().getNamespaceInfoFromPrefix(this.settings.getConnection(), this.settings.getSchemaName(), "value", this.settings.getDocumentId(), newType);

        //get LCP CLASS
        preparedStatement = settings.getDbApi().getLCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), namespaceInfo.getLocalPart(), namespaceInfo.getId(), newDepth, parentLcpClass, newType);
        resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();

            newLcpClass = settings.getDbApi().getNextValLCPClassIdSequence(settings.getConnection(), settings.getSchemaName());
            settings.getDbApi().insertLCPClassTableStatement(settings.getConnection(), settings.getSchemaName(), newLcpClass, newType, namespaceInfo.getLocalPart(), namespaceInfo.getId(), newDepth, parentLcpClass , documentId);
        } else {
            newLcpClass = resultSet.getLong("id");
            resultSet.close();
            preparedStatement.close();
        }
        
        //get CP CLASS
        preparedStatement = settings.getDbApi().getCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), newDepth, documentId);
        resultSet = preparedStatement.executeQuery();
        
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            preparedStatement = settings.getDbApi().getCPClassWithoutId(settings.getConnection(), settings.getSchemaName(), newDepth - 1, documentId);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                newParentCpClass = resultSet.getLong("id");
                resultSet.close();
                preparedStatement.close();
                newCpClass = settings.getDbApi().getNextValCPClassIdSequence(settings.getConnection(), settings.getSchemaName());
                settings.getDbApi().insertCPClassTableStatement(settings.getConnection(), settings.getSchemaName(), newCpClass, newDepth, newParentCpClass, documentId);
            } else {
                resultSet.close();
                preparedStatement.close();
                throw new TXmlException("missing parent cp class record");
            }

        } else {
            newCpClass = resultSet.getLong("id");
            resultSet.close();
            preparedStatement.close();
        }
        
        //get CP
        Object[] newValidObjectArray; //valid without new node
        preparedStatement = settings.getDbApi().getCPWithoutId(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getNOW(), newCpClass);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Long cpFrom = resultSet.getLong("from");
            newValidObjectArray = (Object[])resultSet.getArray("valid").getArray();           
            if (!cpFrom.equals(settings.getDbApi().getCurrTimeAsLong())) {//bad from
                settings.getDbApi().splitCpInterval(settings.getConnection(), settings.getSchemaName(), cpFrom, settings.getDbApi().getNOW(), newCpClass, settings.getDbApi().getCurrTimeAsLong(), newValidObjectArray);
            }
            
        } else {
            resultSet.close();
            preparedStatement.close();
            newValidObjectArray = new Object[0];
            settings.getDbApi().getInsertCPTableStatement(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newCpClass, newValidObjectArray);
        }
        
        //insert node into CP table
        Long newNodeId = settings.getDbApi().getNextValNodeIdSequence(settings.getConnection(), settings.getSchemaName());
        settings.getDbApi().insertNodeInValidInPos(settings.getConnection(), settings.getSchemaName(), settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), newCpClass, newValidObjectArray, newNodeId, parent.getId(), parent.getFromAsLong(), parent.getToAsLong(), position, newType, settings.getDbApi().getNOW(), documentId, newDepth);
        
        settings.getDbApi().insertLCPTable(settings.getConnection(), settings.getSchemaName(), newNodeId, settings.getDbApi().getCurrTimeAsLong(), settings.getDbApi().getNOW(), parent.getId(), parent.getFromAsLong(), parent.getToAsLong(), value,  newLcpClass);
        
    }
    
    @Override 
    public boolean equals(Object aThat) { 
        if ( this == aThat ) return true;

        if ( !(aThat instanceof TNode) ) return false;

        TNode that = (TNode)aThat;
        return that.id.equals(this.id) &&
                that.from.equals(this.from) &&
                that.to.equals(this.to);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    @Override
    public void closeDbConnection() throws SQLException {
        this.settings.getConnection().close();
    }

    public NodeGlobalSettings getSettings() {
        return settings;
    }
    
    @Override
    public String getTree(boolean sort) throws SQLException, TXmlException {
        return getTree(this, null, sort);
    }
    
    public static String getTree(Node node, Node parent, boolean sort) throws SQLException {
        return getTree(node, parent, 0, sort);
    }
    
    public static String getTree(Node node, Node parent, int spaceSize, boolean sort) throws SQLException {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < spaceSize; i++) {
            spaces.append(" ");
        }
        
        StringBuilder spacesDiff = new StringBuilder();
        for (int i = 0; i < SPACE_SIZE; i++) {
            spacesDiff.append(" ");
        }
        
        StringBuilder result = new StringBuilder();
        if (node.getNodeType() == Node.ELEMENT_NODE   
           ) {
            result.append(spaces)
                    .append("<")
                    .append(node.getNodeName());
            
            NamedNodeMap namedNodeMap = node.getAttributes();
            
            if (sort) {
                namedNodeMap = namedNodeMap.sort();
            }
            
            boolean attributeFullPrint = false;
                
            for (int i = 0; i < namedNodeMap.getLength(); i++) {
                Node attributeNode = namedNodeMap.item(i);
                if (!node.getFromAsLong().equals(attributeNode.getFromAsLong()) ||
                    !node.getToAsLong().equals(attributeNode.getToAsLong()) ||
                    attributeNode.getChildNodes().getLength() != 1 ||
                    !node.getFromAsLong().equals(attributeNode.getChildNodes().item(0).getFromAsLong()) ||
                    !node.getToAsLong().equals(attributeNode.getChildNodes().item(0).getToAsLong())
                   ) 
                {
                    attributeFullPrint = true;
                    break;
                }
            }
            
            if (namedNodeMap != null && !attributeFullPrint) {
                for (int i = 0; i < namedNodeMap.getLength(); i++) {
                    Node attributeNode = namedNodeMap.item(i);
                    if (
                            attributeNode.getChildNodes().getLength() == 1 && 
                            attributeNode.getChildNodes().item(0).getNodeValue() != null
                       ) {
                    }
                    result.append(" ")
                          .append(attributeNode.getNodeName())
                          .append("=\"")
                          .append(attributeNode.getChildNodes().item(0).getNodeValue())
                          .append("\"");
                }
            }
                
            if (parent == null || 
                !parent.getFromAsLong().equals(node.getFromAsLong()) ||
                !parent.getToAsLong().equals(node.getToAsLong())
               ) 
            {
                result.append(" ")
                      .append("txml:from=\"")
                      .append(node.getFromAsString())
                      .append("\" txml:to=\"")
                      .append(node.getToAsString())
                      .append("\"");
                result.append(" txml:id=\"")
                      .append(node.getId());
                result.append("\"");
            }
            
            result.append(">\n");
            if (attributeFullPrint && namedNodeMap.getLength() != 0) {
                for (int i = 0; i < namedNodeMap.getLength(); i++) {
                    result.append(getTree(namedNodeMap.item(i), node, spaceSize + SPACE_SIZE, sort));
                }
            }
            result.append(TNodeList.getTrees(node.getChildNodes(), node, spaceSize + SPACE_SIZE, sort));
            result.append(spaces).append("</").append(node.getNodeName()).append(">\n");
        } else if (node.getNodeType() == XmlNodeTypeEnum.TEXT.getShortValue()) {
            if (node.getNodeValue() != null) {
                String val = node.getNodeValue().trim();
                if (!val.isEmpty()) {
                    boolean valueFullPrint = false;

                    if (parent == null || 
                        !parent.getFromAsLong().equals(node.getFromAsLong()) ||
                        !parent.getToAsLong().equals(node.getToAsLong())
                       ) 
                    {
                        result
                            .append(spaces)
                            .append("<txml:text");
                            result.append(" ")
                                  .append("txml:from=\"")
                                  .append(node.getFromAsString())
                                  .append("\" txml:to=\"")
                                  .append(node.getToAsString())
                                  .append("\" txml:id=\"")
                                  .append(node.getId())
                                  .append("\"");
                        result        
                            .append(">\n")    
                            .append(spaces)
                            .append(spacesDiff)
                            .append(val)
                            .append("\n")    
                            .append(spaces)
                            .append("</txml:text>\n"); 
                        valueFullPrint = true;
                    }

                    
                    if (!valueFullPrint) {
                        result           
                            .append(spaces)
                            .append(val)
                            .append("\n"); 
                    }
                }
            }
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE ||
                   node.getNodeType() == Node.TXML_ATTRIBUTE_NODE
                ) {
            NodeList valueNodeList = node.getChildNodes();
            if (valueNodeList.getLength() > 0) {
                for (int i = 0; i < valueNodeList.getLength(); i++) {
                    result
                        .append(spaces).append("<txml:attribute name=\"").append(node.getNodeName()).append("\" value=\"")
                        .append(valueNodeList.item(i).getNodeValue())
                        .append("\"");

                    if (parent == null || 
                        !parent.getFromAsLong().equals(valueNodeList.item(i).getFromAsLong()) ||
                        !parent.getToAsLong().equals(valueNodeList.item(i).getToAsLong())
                       ) 
                    {
                        result.append(" ")
                              .append("txml:from=\"")
                              .append(valueNodeList.item(i).getFromAsString())
                              .append("\" txml:to=\"")
                              .append(valueNodeList.item(i).getToAsString())
                              .append("\"");

                        if (node.getNodeType() != XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue()) { 
                            result.append(" txml:id=\"")
                                  .append(node.getId());
                            result.append("\"");
                        }

                    }
                    result.append("/>\n");
                }
            } else {
                result
                    .append(spaces).append("<txml:attribute name=\"").append(node.getNodeName()).append("\" value=\"")
                    .append("\"");

                if (parent == null || 
                    !parent.getFromAsLong().equals(node.getFromAsLong()) ||
                    !parent.getToAsLong().equals(node.getToAsLong())
                   ) 
                {
                    result.append(" ")
                          .append("txml:from=\"")
                          .append(node.getFromAsString())
                          .append("\" txml:to=\"")
                          .append(node.getToAsString())
                          .append("\"");

                    if (node.getNodeType() != XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue()) { 
                        result.append(" txml:id=\"")
                              .append(node.getId());
                        result.append("\"");
                    }
                }
                result.append("/>\n");
            }
        }
        return result.toString();
    }
    
    @Override
    public String getNodeName() {
        if (prefix == null || prefix.equals("")) {
            return this.localPart;
        } else {
            return this.prefix + ":" + this.localPart;
        }
    }

    @Override
    public String getNodeValue() {
        return this.value;
    }

    @Override
    public short getNodeType() {
        return this.type;
    }

    @Override
    public TNodeList getChildNodes() {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<TNode> childNodesList = new ArrayList<>();
        if (this.childNodes != null) {
            return this.childNodes;
        } else {
            try {
                preparedStatement = settings
                        .getDbApi()
                        .getChildNodesWithoutAttributes(
                            settings.getConnection(),
                            settings.getSchemaName(),
                            XmlNodeTypeEnum.ELEMENT.getShortValue(),
                            XmlNodeTypeEnum.TEXT.getShortValue(),
                            this.id,
                            this.from,
                            this.to
                );
                
                resultSet = preparedStatement.executeQuery();
                
                while(resultSet.next()) {
                    TNode tNode = new TNode(
                    resultSet.getLong("from"), 
                    resultSet.getLong("to"), 
                    resultSet.getLong("id"), 
                    resultSet.getString("value"), 
                    resultSet.getInt("depth"), 
                    resultSet.getLong("lcp_class"),
                    resultSet.getShort("type"),
                    resultSet.getString("local_part"),
                    resultSet.getString("uri"),
                    resultSet.getString("prefix"),
                    resultSet.getLong("namespace_id"),
                    this.id,
                    this.from,
                    this.to,
                    settings
                    );  
                    tNode.getLocalSettings().setSavedNodes(this.getLocalSettings().getSavedNodes());
                    childNodesList.add(tNode);
                }
                this.childNodes = new TNodeList(settings, childNodesList);
                return this.childNodes;
            } catch (SQLException ex) {
                throw new TXmlException(ex.getLocalizedMessage());
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        throw new TXmlException(ex.getLocalizedMessage());
                    } finally {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (SQLException ex) {
                                throw new TXmlException(ex.getLocalizedMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public TNode getFirstChild() throws TXmlException {
        if (this.getChildNodes().getLength() == 0) {
            return null;
        }
        return this.getChildNodes().item(0);
    }

    @Override
    public TNode getLastChild() throws TXmlException {
        if (this.getChildNodes().getLength() == 0) {
            return null;
        }
        return this.getChildNodes().item(this.getChildNodes().getLength() - 1);
    }

    @Override
    public TNamedNodeMap getAttributes() {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<TNode> attributesList = new ArrayList<>();
        if (this.attributes != null) {
            return this.attributes;
        } else {
            try {
                preparedStatement = settings
                        .getDbApi()
                        .getChildNodesOnlyAttributes(
                            settings.getConnection(),
                            settings.getSchemaName(),
                            XmlNodeTypeEnum.ATTRIBUTE.getShortValue(),
                            this.id,
                            this.from,
                            this.to
                );
                resultSet = preparedStatement.executeQuery();
                
                while(resultSet.next()) {
                    TNode tNode = new TNode(
                    resultSet.getLong("from"), 
                    resultSet.getLong("to"), 
                    resultSet.getLong("id"), 
                    resultSet.getString("value"), 
                    resultSet.getInt("depth"), 
                    resultSet.getLong("lcp_class"),
                    resultSet.getShort("type"),
                    resultSet.getString("local_part"),     
                    resultSet.getString("uri"),
                    resultSet.getString("prefix"),
                    resultSet.getLong("namespace_id"),
                    this.id,
                    this.from,
                    this.to,
                    settings
                    );  
                    tNode.getLocalSettings().setSavedNodes(this.getLocalSettings().getSavedNodes());
                    attributesList.add(tNode);
                }
                this.attributes = new TNamedNodeMap(settings, attributesList);
                return this.attributes;
            } catch (SQLException ex) {
                throw new TXmlException(ex.getLocalizedMessage());
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        throw new TXmlException(ex.getLocalizedMessage());
                    } finally {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (SQLException ex) {
                                throw new TXmlException(ex.getLocalizedMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    @Override
    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getLocalName() {
        return this.localPart;
    }
}
