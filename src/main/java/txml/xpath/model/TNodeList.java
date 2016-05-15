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

import txml.NodeList;
import txml.Node;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import txml.database.model.NamespaceInfo;
import txml.TXmlException;
import txml.TXmlResult;
import txml.load.model.XmlNodeTypeEnum;
import txml.xpath.sort.XmlTreeComparator;


public class TNodeList extends TXmlResult implements NodeList {

    private List<TNode> items;
    private NodeGlobalSettings settings;
    private boolean documentNode;
    private LinkedList<TNode> iterator;
    
    public TNodeList(NodeGlobalSettings settings) {
        items = new ArrayList<>();
        this.settings = settings;
        this.documentNode = true;
    }
    
    public TNodeList(NodeGlobalSettings settings, List<TNode> items) {
        this.items = items;
        this.settings = settings;
        this.documentNode = false;
    }

    public NodeGlobalSettings getSettings() {
        return settings;
    }
    
    @Override
    public TNodeList sort() throws SQLException {
        if (items.size() <= 1)
            return new TNodeList(settings, new ArrayList<>(items));
        XmlTreeComparator comparator = new XmlTreeComparator();
        TNodeList result = new TNodeList(settings, comparator.sort(items, settings.getDbApi(), settings.getConnection(), settings.getSchemaName(), settings.getDocumentName()));
        
        return result;
    }
    
    public void initIterator() {
        this.iterator = new LinkedList<>(this.items);
    }
    
    public TNode next() {
        return this.iterator.poll();
    }
    
    public boolean hasNext() {
        return !this.iterator.isEmpty();
    }
    
    private TNodeList getTXmlDescendants(String label) throws SQLException {
        label = label.replaceAll("@", "").replaceAll("attribute::", "");
        List<TNode> temporalNodes = new ArrayList<>(); 
        List<TNode> firstNodes = new ArrayList<>();
        TNodeList result;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<TNode> newItems = new ArrayList<>();
        List<TNode> newTemporalNodes = new ArrayList<>();
        List<TNode> valueList = new ArrayList<>();
        
        if (this.documentNode) {
            TNode tNode = getDocumentRoot();
            firstNodes.add(tNode);
            temporalNodes.add(tNode);
        } else {
            for (int i = 0; i < this.getLength(); i++) {
                temporalNodes.add(this.item(i));
                firstNodes.add(this.item(i));
            }
        }

        short txmlType;
        switch (label) {
            case "txml:id":
                txmlType = XmlNodeTypeEnum.TXML_VALUE_ID.getShortValue();
                break;
            case "txml:from":
                txmlType = XmlNodeTypeEnum.TXML_VALUE_FROM.getShortValue();
                break;
            default:
                txmlType = XmlNodeTypeEnum.TXML_VALUE_TO.getShortValue();
                break;
        }
        
        preparedStatement = this.settings.getDbApi().getChildren(this.settings.getConnection(), this.settings.getSchemaName());
        while(!temporalNodes.isEmpty()) {            
            for (TNode tempNode:temporalNodes) {
                preparedStatement.setLong(1, tempNode.getId());
                preparedStatement.setLong(2, tempNode.getFromAsLong());
                preparedStatement.setLong(3, tempNode.getToAsLong());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    TNode tNode1 = new TNode(
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
                    settings);  
                    tNode1.getLocalSettings().setSavedNodes(tempNode.getLocalSettings().getSavedNodes());

                    newTemporalNodes.add(tNode1);   
                    valueList = new ArrayList<>();
                    String local_part;
                    
                    String value;
                    switch (label) {
                        case "txml:id":
                            value = resultSet.getString("id");
                            local_part = "id";
                            break;
                        case "txml:from":
                            value = settings.getDbApi().getTime(resultSet.getLong("from"));
                            local_part = "from";
                            break;
                        default:
                            value = settings.getDbApi().getTime(resultSet.getLong("to"));
                            local_part = "to";
                            break;
                    }

                    TNode tNode2 = new TNode(
                        resultSet.getLong("from"), 
                        resultSet.getLong("to"), 
                        resultSet.getLong("id") * (-1), 
                        null, 
                        resultSet.getInt("depth") + 1, 
                        0l,
                        XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue(),
                        local_part,
                        "",
                        "txml",
                        -1l,    
                        resultSet.getLong("id"),
                        resultSet.getLong("from"),    
                        resultSet.getLong("to"),
                        tNode1, 
                        new TNodeList(settings, valueList),       
                        settings);  
                    newItems.add(tNode2);
                    tNode2.getLocalSettings().setSavedNodes(tempNode.getLocalSettings().getSavedNodes());


                    TNode tValue = new TNode(
                        resultSet.getLong("from"), 
                        resultSet.getLong("to"), 
                        Short.MIN_VALUE + resultSet.getLong("id"), 
                        value, 
                        resultSet.getInt("depth") + 2, 
                        0l,
                        txmlType,
                        "value",
                        "",
                        "",
                        -2l, 
                        resultSet.getLong("id") * (-1),
                        resultSet.getLong("from"), 
                        resultSet.getLong("to"),    
                        tNode2,
                        settings);  
                    tValue.getLocalSettings().setSavedNodes(tempNode.getLocalSettings().getSavedNodes());

                    valueList.add(tValue);
                }
                
                resultSet.close();
            }
            
            temporalNodes = newTemporalNodes;
            newTemporalNodes = new ArrayList<>();
            
        }
        
        preparedStatement.close();

        //nodes in origin list
        for (TNode tNode1:firstNodes) {
            valueList = new ArrayList<>();

            String value;
            String local_part;
            
            switch (label) {
                case "txml:id":
                    value = tNode1.getId().toString();
                    local_part = "id";
                    break;
                case "txml:from":
                    value = settings.getDbApi().getTime(tNode1.getFromAsLong());
                    local_part = "from";
                    break;
                default:
                    value = settings.getDbApi().getTime(tNode1.getToAsLong());
                    local_part = "to";
                    break;
            }

            TNode tNode2 = new TNode(
                tNode1.getFromAsLong(), 
                tNode1.getToAsLong(), 
                tNode1.getId() * (-1), 
                null, 
                tNode1.getDepth() + 1, 
                0l,
                XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue(),
                local_part,
                "",
                "txml",
                -1l,
                tNode1.getId(),
                tNode1.getFromAsLong(), 
                tNode1.getToAsLong(),
                tNode1, 
                new TNodeList(settings, valueList),       
                settings);  
            newItems.add(tNode2);
            tNode2.getLocalSettings().setSavedNodes(tNode1.getLocalSettings().getSavedNodes());

            TNode tValue = new TNode(
                tNode1.getFromAsLong(), 
                tNode1.getToAsLong(), 
                Short.MIN_VALUE + tNode1.getId(), 
                value, 
                tNode1.getDepth() + 2, 
                0l,
                txmlType,
                "value",
                "",
                "",
                -2l,
                tNode1.getId() * (-1),
                tNode1.getFromAsLong(), 
                tNode1.getToAsLong(),
                tNode2,
                settings);  

            valueList.add(tValue);
            tValue.getLocalSettings().setSavedNodes(tNode1.getLocalSettings().getSavedNodes());
        }

        result = new TNodeList(this.settings, newItems);
        return result;
    }
    
    public TNodeList getDescendantsByTypeAndLabel(String label, short type) throws SQLException {
        TNodeList result;
        List<TNode> newItems = new ArrayList<>();
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        List<TNode> temporalNodes = new ArrayList<>();
        List<TNode> newTemporalNodes = new ArrayList<>();
        NamespaceInfo namespaceInfo = this.settings.getDbApi().getNamespaceInfoFromPrefix(this.settings.getConnection(), this.settings.getSchemaName(), label, this.settings.getDocumentId(), type);
        preparedStatement = this.settings.getDbApi().getMaxDepth(this.settings.getConnection(), this.settings.getSchemaName(), namespaceInfo.getLocalPart(), namespaceInfo.getId());
        resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            result = new TNodeList(this.settings, newItems);

            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                throw new TXmlException(ex.getLocalizedMessage());
            } finally {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    throw new TXmlException(ex.getLocalizedMessage());
                }
            }

        } else {
            int maxDepth = resultSet.getInt("max_depth");              

            if (this.documentNode) {
                TNode tNode = getDocumentRoot();
                temporalNodes.add(tNode);
                if (tNode.getNodeName().equals(label) && (tNode.getNodeType() == type)) {
                    newItems.add(tNode);
                }
                
            } else {
                for (int i = 0; i < this.getLength(); i++) {
                    if (this.item(i).getDepth() <= maxDepth) {
                        temporalNodes.add(this.item(i));
                    }
                }
            }

            while(!temporalNodes.isEmpty()) {
                newTemporalNodes = getChildrenByDepth(maxDepth, temporalNodes);
                for (TNode tNode:newTemporalNodes) {  
                    if (tNode.getLocalName().equals(namespaceInfo.getLocalPart()) && tNode.getNamespaceId().equals(namespaceInfo.getId()) && (tNode.getNodeType() == type)) {
                        newItems.add(tNode);
                    }
                }

                temporalNodes = newTemporalNodes;
            }

            result = new TNodeList(this.settings, newItems);
        }

        return result; 
    }
    
    public TNodeList getDescendantsByTypes(List<Short> types) throws SQLException {
        TNodeList result;
        List<TNode> newItems = new ArrayList<>();
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        List<TNode> temporalNodes = new ArrayList<>();
        List<TNode> newTemporalNodes = new ArrayList<>();

        if (this.documentNode) {
            TNode tNode = getDocumentRoot();
            temporalNodes.add(tNode);
            for (Short type:types) {
                if (tNode.getNodeType() == type) {
                    newItems.add(tNode);
                    break;
                }
            }
        } else {
            for (int i = 0; i < this.getLength(); i++) {
                for (Short type:types) {
                    if (this.item(i).getNodeType() == type) {
                        temporalNodes.add(this.item(i));
                        break;
                    }
                }
            }
        }

        preparedStatement = this.settings.getDbApi().getChildren(this.settings.getConnection(), this.settings.getSchemaName());
        while(!temporalNodes.isEmpty()) {
            for (TNode tempNode:temporalNodes) {
                preparedStatement.setLong(1, tempNode.getId());
                preparedStatement.setLong(2, tempNode.getFromAsLong());
                preparedStatement.setLong(3, tempNode.getToAsLong());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
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
                    settings);  
                    tNode.getLocalSettings().setSavedNodes(tempNode.getLocalSettings().getSavedNodes());
                    
                    for (Short type:types) {
                        if (tNode.getNodeType() == type) {
                            newItems.add(tNode);
                            newTemporalNodes.add(tNode);
                            break;
                        }
                    }
                }

                resultSet.close();
            }

            temporalNodes = newTemporalNodes;
            newTemporalNodes = new ArrayList<>();

        }
        preparedStatement.close();
        result = new TNodeList(this.settings, newItems);

        return result; 
    }

    private TNodeList getTXmlChildren(String label) throws SQLException {
        label = label.replaceAll("@", "").replaceAll("attribute::", "");
        TNodeList result;
        List<TNode> newItems = new ArrayList<>();
        short txmlType;
        String localPart;
        switch (label) {
            case "txml:id":
                txmlType = XmlNodeTypeEnum.TXML_VALUE_ID.getShortValue();
                localPart = "id";
                break;
            case "txml:from":
                txmlType = XmlNodeTypeEnum.TXML_VALUE_FROM.getShortValue();
                localPart = "from";
                break;
            default:
                txmlType = XmlNodeTypeEnum.TXML_VALUE_TO.getShortValue();
                localPart = "to";
                break;
        }

        List<TNode> temporalList = new ArrayList<>();

        if (this.documentNode) {
            TNode tNode = getDocumentRoot();
            temporalList.add(tNode);
        } else {
            for (int i = 0; i < this.getLength(); i++) {
                temporalList.add(this.item(i));
            }
        }

        for (TNode tNode1:temporalList) {
            String value;
            List<TNode> valueList = new ArrayList<>();
            switch (label) {
                case "txml:id":
                    value = tNode1.getId().toString();
                    break;
                case "txml:from":
                    value = tNode1.getFromAsString();
                    break;
                default:
                    value = tNode1.getToAsString();
                    break;
            }

            TNode tNode2 = new TNode(//parent of value
                tNode1.getFromAsLong(), 
                tNode1.getToAsLong(), 
                tNode1.getId() * (-1), 
                null, 
                tNode1.getDepth() + 1, 
                0l,
                XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue(),
                localPart,
                "",
                "txml",
                -1l,
                tNode1.getId(),
                tNode1.getFromAsLong(),
                tNode1.getToAsLong(),
                tNode1,
                new TNodeList(settings, valueList),
                settings
            );  
                tNode2.getLocalSettings().setSavedNodes(tNode1.getLocalSettings().getSavedNodes());
                newItems.add(tNode2);


            TNode tValue = new TNode(//value node
                tNode1.getFromAsLong(), 
                tNode1.getToAsLong(), 
                Short.MIN_VALUE + tNode1.getId(), 
                value, 
                tNode1.getDepth() + 2, 
                0l,
                txmlType,
                "value",
                "",
                "",
                -2l,    
                tNode1.getId() * (-1),
                tNode1.getFromAsLong(), 
                tNode1.getToAsLong(),
                tNode2,
                settings
            );
            tValue.getLocalSettings().setSavedNodes(tNode1.getLocalSettings().getSavedNodes());


            valueList.add(tValue);

        }
        result = new TNodeList(this.settings, newItems);
                
        return result;
    }
    
    private TNodeList getCurrentNodes() throws SQLException {
        List<TNode> newItems = new ArrayList<>();
        TNodeList result;

        if (this.documentNode) {
            TNode tNode = getDocumentRoot();
            newItems.add(tNode);
            
        } else {            
            for (int i = 0; i < this.getLength(); i++) {
                newItems.add(this.item(i));
            }
        }
        
        result = new TNodeList(this.settings, newItems);
        return result;
    }
    
    private TNodeList getParentNodes() throws SQLException {
        List<TNode> newItems = new ArrayList<>();
        TNodeList result;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        if (this.documentNode) {
            //document root haven't parent 
        } else {
            preparedStatement = this.settings.getDbApi().getParent(this.settings.getConnection(), this.settings.getSchemaName());
            
            for (int i = 0; i < this.getLength(); i++) {
                if (this.item(i).getParentId() == null) continue;
                preparedStatement.setLong(1, this.item(i).getParentId());
                preparedStatement.setLong(2, this.item(i).getParentFromAsLong());
                preparedStatement.setLong(3, this.item(i).getParentToAsLong());
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
                    resultSet.getLong("parentId"),
                    resultSet.getLong("parentFrom"),
                    resultSet.getLong("parentTo"),
                    settings
                    );  
                    tNode.getLocalSettings().setSavedNodes(this.item(i).getLocalSettings().getSavedNodes());

                    newItems.add(tNode);
                    
                }  
                resultSet.close();
            }
            preparedStatement.close();
        }
        
        result = new TNodeList(this.settings, newItems);
        return result;
    }
    
    private List<TNode> getChildrenByDepth(int depth, List<TNode> parents) throws SQLException {
        List<TNode> result = new ArrayList<>();
        List<TNode> copyParents = new ArrayList<>(parents);
        PreparedStatement preparedStatement = this.settings.getDbApi().getChildrenByDepth(this.settings.getConnection(), this.settings.getSchemaName());
        Collections.sort(copyParents, new Comparator<TNode>() {
            @Override
            public int compare(TNode o1, TNode o2) {
                if (o1.getId().compareTo(o2.getId()) != 0) {
                    return o1.getId().compareTo(o2.getId());
                } else {
                    return o1.getFromAsLong().compareTo(o2.getFromAsLong());
                }
            }
        });
        
        preparedStatement.setInt(1, depth);
        ResultSet resultSet = preparedStatement.executeQuery(); 
        int i = 0;
        boolean finish = !resultSet.next() || copyParents.size() <= i;

        while(!finish) {
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
            
            int compare;
            
            if (tNode.getParentId().compareTo(copyParents.get(i).getId()) != 0) {
                compare = tNode.getParentId().compareTo(copyParents.get(i).getId());
            } else {
                compare = tNode.getParentFromAsLong().compareTo(copyParents.get(i).getFromAsLong());
            }
            
            if (compare == 0) {
                tNode.getLocalSettings().setSavedNodes(copyParents.get(i).getLocalSettings().getSavedNodes());
                result.add(tNode);    
                finish = !resultSet.next();
            } else if (compare < 0) {
                finish = !resultSet.next();
            } else {
                i++;
                finish = copyParents.size() <= i;
            }
        }
        resultSet.close();
        preparedStatement.close();
        return result;
    }
    
    private List<TNode> getChildrenByType(short type, List<TNode> parents) throws SQLException {
        List<TNode> result = new ArrayList<>();
        List<TNode> copyParents = new ArrayList<>(parents);
        PreparedStatement preparedStatement = this.settings.getDbApi().getChildrenByType(this.settings.getConnection(), this.settings.getSchemaName());
        Collections.sort(copyParents, new Comparator<TNode>() {
            @Override
            public int compare(TNode o1, TNode o2) {
                if (o1.getId().compareTo(o2.getId()) != 0) {
                    return o1.getId().compareTo(o2.getId());
                } else {
                    return o1.getFromAsLong().compareTo(o2.getFromAsLong());
                }
            }
        });
        
        preparedStatement.setShort(1, type);
        ResultSet resultSet = preparedStatement.executeQuery(); 
        int i = 0;
        boolean finish = !resultSet.next() || copyParents.size() <= i;

        while(!finish) {
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
            
            int compare;
            
            if (tNode.getParentId().compareTo(copyParents.get(i).getId()) != 0) {
                compare = tNode.getParentId().compareTo(copyParents.get(i).getId());
            } else {
                compare = tNode.getParentFromAsLong().compareTo(copyParents.get(i).getFromAsLong());
            }
            
            if (compare == 0) {
                tNode.getLocalSettings().setSavedNodes(copyParents.get(i).getLocalSettings().getSavedNodes());
                result.add(tNode);    
                finish = !resultSet.next();
            } else if (compare < 0) {
                finish = !resultSet.next();
            } else {
                i++;
                finish = copyParents.size() <= i;
            }
        }
        resultSet.close();
        preparedStatement.close();
        return result;
    }
    
    private List<TNode> getChildrenByTypeAndLabel(short type, NamespaceInfo namespaceInfo, List<TNode> parents) throws SQLException {
        List<TNode> result = new ArrayList<>();
        List<TNode> copyParents = new ArrayList<>(parents);
        PreparedStatement preparedStatement = this.settings.getDbApi().getChildrenByTypeAndLabel(this.settings.getConnection(), this.settings.getSchemaName());
        Collections.sort(copyParents, new Comparator<TNode>() {
            @Override
            public int compare(TNode o1, TNode o2) {
                if (o1.getId().compareTo(o2.getId()) != 0) {
                    return o1.getId().compareTo(o2.getId());
                } else {
                    return o1.getFromAsLong().compareTo(o2.getFromAsLong());
                }
            }
        });
        
        preparedStatement.setShort(1, type);
        preparedStatement.setString(2, namespaceInfo.getLocalPart());
        preparedStatement.setLong(3, namespaceInfo.getId());
        ResultSet resultSet = preparedStatement.executeQuery(); 
        int i = 0;
        boolean finish = !resultSet.next() || copyParents.size() <= i;

        while(!finish) {
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
            
            int compare;
            
            if (tNode.getParentId().compareTo(copyParents.get(i).getId()) != 0) {
                compare = tNode.getParentId().compareTo(copyParents.get(i).getId());
            } else {
                compare = tNode.getParentFromAsLong().compareTo(copyParents.get(i).getFromAsLong());
            }
            
            if (compare == 0) {
                tNode.getLocalSettings().setSavedNodes(copyParents.get(i).getLocalSettings().getSavedNodes());
                result.add(tNode);    
                finish = !resultSet.next();
            } else if (compare < 0) {
                finish = !resultSet.next();
            } else {
                i++;
                finish = copyParents.size() <= i;
            }
        }
        resultSet.close();
        preparedStatement.close();
        return result;
    }
    
    private TNodeList getChildrenByTypeAndLabel(String label, short type) throws SQLException {
        List<TNode> newItems = new ArrayList<>();
        TNodeList result;
        NamespaceInfo namespaceInfo = this.settings.getDbApi().getNamespaceInfoFromPrefix(this.settings.getConnection(), this.settings.getSchemaName(), label, this.settings.getDocumentId(), type);
        if (this.documentNode) {
            TNode tNode = getDocumentRoot();
            if (tNode.getLocalName().equals(namespaceInfo.getLocalPart()) && tNode.getPrefix().equals(namespaceInfo.getPrefix()) && tNode.getNodeType() == type) {
                newItems.add(tNode);
            }
        } else {
            newItems = getChildrenByTypeAndLabel(type, namespaceInfo, this.items);
        }
        
        result = new TNodeList(this.settings, newItems);
        return result;
    }

    private TNodeList getChildrenByType(short type) throws SQLException {
        List<TNode> newItems = new ArrayList<>();
        TNodeList result;
        if (this.documentNode) {
            TNode tNode = getDocumentRoot();
            if (tNode.getNodeType() == type) {
                newItems.add(tNode);
            }
        } else {
            newItems = getChildrenByType(type, this.items);
        }
        
        result = new TNodeList(this.settings, newItems);
        return result;
    }
    
    public TNodeList getDirectStep(String label) throws SQLException {
        TNodeList result;

        if (label.equals(".")) {
            result = getCurrentNodes();
        } else if (label.equals("..")) {
            result = getParentNodes();
        } else if (label.equals("*")) { 
            result = getChildrenByType(XmlNodeTypeEnum.ELEMENT.getShortValue());
        } else if (label.contains("txml:")) {
            result = getTXmlChildren(label);
        } else if (label.contains("@")) {   
            label = label.replace("@", "");
            result = getChildrenByTypeAndLabel(label, XmlNodeTypeEnum.ATTRIBUTE.getShortValue());
        } else if (label.contains("attribute::")) {
            label = label.replace("attribute::", "");
            result = getChildrenByTypeAndLabel(label, XmlNodeTypeEnum.ATTRIBUTE.getShortValue());
        } else if (label.toLowerCase().contains("text(")) {
            result = getChildrenByTypeAndLabel("value", XmlNodeTypeEnum.TEXT.getShortValue());    
        } else {
            result = getChildrenByTypeAndLabel(label, XmlNodeTypeEnum.ELEMENT.getShortValue());
        }
        
        return result;
    }
    
    public TNodeList getUndirectStep(String label) throws SQLException {
        TNodeList result = null;
        
        if (label.equals(".")) {
            List<Short> types = new ArrayList<>();
            types.add(XmlNodeTypeEnum.ELEMENT.getShortValue());
            types.add(XmlNodeTypeEnum.TEXT.getShortValue());
            result = getDescendantsByTypes(types);
        } else if (label.equals("..")) {
            List<Short> types = new ArrayList<>();
            types.add(XmlNodeTypeEnum.ELEMENT.getShortValue());
            types.add(XmlNodeTypeEnum.TEXT.getShortValue());
            TNodeList result1 = getDescendantsByTypes(types);
            result = result1.getParentNodes();
        } else if (label.equals("*")) { 
            List<Short> types = new ArrayList<>();
            types.add(XmlNodeTypeEnum.ELEMENT.getShortValue());
            result = getDescendantsByTypes(types);
        } else if (label.contains("txml:")) {
            result = getTXmlDescendants(label);
        } else if (label.contains("@")) {   
            label = label.replace("@", "");
            result = getDescendantsByTypeAndLabel(label, XmlNodeTypeEnum.ATTRIBUTE.getShortValue());
        } else if (label.contains("attribute::")) {
            label = label.replace("attribute::", "");
            result = getDescendantsByTypeAndLabel(label, XmlNodeTypeEnum.ATTRIBUTE.getShortValue());
        } else if (label.toLowerCase().contains("text(")) {
            result = getDescendantsByTypeAndLabel("value", XmlNodeTypeEnum.TEXT.getShortValue());    
        } else {
            result = getDescendantsByTypeAndLabel(label, XmlNodeTypeEnum.ELEMENT.getShortValue());
        }
        
        return result;
    }
        
    public TNodeList getValues() throws SQLException, TXmlException {
        short type = XmlNodeTypeEnum.TEXT.getShortValue();
        TNodeList result;
        List<TNode> origItems = this.items;
        
        List<TNode> newItems = new ArrayList<>();
        if (this.documentNode) {
            origItems = new ArrayList<>();
            TNode tNode = getDocumentRoot();
            origItems.add(tNode); 
        } 
        
        List<TNode> parentList = new ArrayList<>();
        for (int i = 0; i < origItems.size(); i++) {
            if (origItems.get(i).getNodeType() == XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue() ) {
                String value;
                short txmlType;
                switch (origItems.get(i).getNodeName()) {
                    case "txml:id":
                        value = origItems.get(i).getParentId().toString();
                        txmlType = XmlNodeTypeEnum.TXML_VALUE_ID.getShortValue();
                        break;
                    case "txml:from":
                        value = origItems.get(i).getFromAsString();
                        txmlType = XmlNodeTypeEnum.TXML_VALUE_FROM.getShortValue();
                        break;
                    default:
                        value = origItems.get(i).getToAsString();
                        txmlType = XmlNodeTypeEnum.TXML_VALUE_TO.getShortValue();
                        break;
                }

                TNode tNode = new TNode(
                        origItems.get(i).getFromAsLong(),
                        origItems.get(i).getToAsLong(),
                        Short.MIN_VALUE - this.item(i).getId(),
                        value,
                        origItems.get(i).getDepth() + 1,
                        0l,
                        txmlType,
                        "value",
                        "",
                        "",
                        -2l,
                        origItems.get(i).getId(),
                        origItems.get(i).getFromAsLong(),
                        origItems.get(i).getToAsLong(),
                        origItems.get(i),  
                        settings
                );
                tNode.getLocalSettings().setSavedNodes(origItems.get(i).getLocalSettings().getSavedNodes());

                newItems.add(tNode);
            } else {
                parentList.add(origItems.get(i));
            }
        }

        List<TNode> valueNodes = getChildrenByType(type, parentList);
        newItems.addAll(valueNodes);    
        result = new TNodeList(this.settings, newItems);
                
        return result;
    }
    
    
    @Override
    public void closeDbConnection() throws SQLException {
        this.settings.getConnection().close();
    }
    
    public TNode getDocumentRoot() throws SQLException, TXmlException {
        items.clear();
        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        preparedStatement = settings.getDbApi().getDocumentRoot(settings.getConnection(), settings.getSchemaName(), settings.getDocumentName());
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Long parentId = resultSet.getLong("parentId");
            if (resultSet.wasNull()) {
                parentId = null;
            }
            
            Long parentFrom = resultSet.getLong("parentFrom");
            if (resultSet.wasNull()) {
                parentFrom = null;
            }
            
            Long parentTo = resultSet.getLong("parentTo");
            if (resultSet.wasNull()) {
                parentTo = null;
            }
            
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
                    parentId,
                    parentFrom,
                    parentTo,
                    settings);
            
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

            return tNode;
            
        } else {
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

            throw new TXmlException("No document.");
        }
    }

    public List<TNode> getItems() {
        return items;
    }
    
    @Override
    public String getTrees(boolean sort) throws SQLException, TXmlException {
        return getTrees(this, null, 0, sort);
    }
    
    public static String getTrees(NodeList nodes, Node parent, boolean sort) throws SQLException, TXmlException {
        return getTrees(nodes, parent, 0, sort);
    }
    
    public static String getTrees(NodeList nodes, Node parent, int spaceSize, boolean sort) throws SQLException, TXmlException {
        StringBuilder result = new StringBuilder();
        
        if (nodes instanceof TNodeList && sort) {
            nodes = ((TNodeList) nodes).sort();
        }
        
        for (int i = 0; i < nodes.getLength(); i++) {
            result.append(TNode.getTree(nodes.item(i), parent, spaceSize, sort));
        }

        return result.toString();
    }
    
    @Override
    public TNode item(int index) {
        return items.get(index);
    }

    @Override
    public int getLength() {
        return items.size();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TNodeList other = (TNodeList) obj;
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSorted() {
        return getSettings().isSort();
    }
}