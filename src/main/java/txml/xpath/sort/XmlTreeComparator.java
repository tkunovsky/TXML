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
package txml.xpath.sort;

import txml.xpath.sort.model.LevelValue;
import txml.xpath.sort.model.TreeKey;
import txml.xpath.sort.model.LevelKey;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import txml.database.DbApi;
import txml.TXmlException;
import txml.load.model.XmlNodeTypeEnum;
import txml.xpath.model.NodeGlobalSettings;
import txml.xpath.model.TNode;

public class XmlTreeComparator implements Comparator<TNode> {
    
    private final Map<LevelKey, LevelValue> levels;
    private final Map<TreeKey, List<TNode>> tree;
    private DbApi dbApi;
    private Connection connection; 
    private String schema; 
    private String documentName;
    
    public XmlTreeComparator() {
        levels = new HashMap<>();
        tree = new HashMap<>();
    }
    
    public TNode checkTxmlTNode(TNode n) {
        if (
                (n.getNodeType() == XmlNodeTypeEnum.TXML_VALUE_FROM.getShortValue()) ||
                (n.getNodeType() == XmlNodeTypeEnum.TXML_VALUE_TO.getShortValue()) ||
                (n.getNodeType() == XmlNodeTypeEnum.TXML_VALUE_ID.getShortValue())
           ) {
            return (TNode) n.getParentNode().getParentNode();
        } else if (n.getNodeType() == XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue()) {
            return (TNode) n.getParentNode();
        } else {
            return n;                
        }  
    }

    @Override
    public int compare(TNode o1, TNode o2)  {
        o1 = checkTxmlTNode(o1);
        o2 = checkTxmlTNode(o2);
        
        if  (
                (o1.getFromAsLong() <= o2.getFromAsLong() &&  o2.getToAsLong() <= o1.getToAsLong()) ||
                (o2.getFromAsLong() <= o1.getFromAsLong() &&  o1.getToAsLong() <= o2.getToAsLong())
            ) {
            if (o1.getDepth().equals(o2.getDepth())) {
                int result = compareSameLevel(o1, o2, o1.getDepth(), o2.getDepth());
                return result;
            } else {
                int result = compareDiffLevel(o1, o2, o1.getDepth(), o2.getDepth());
                return result;
            }
            
        } else {
            int result;
            
            if (o1.getDepth().equals(o2.getDepth())) {
                LevelValue o1LevelValue = levels.get(new LevelKey(o1.getDepth(), o1.getId(), o1.getFromAsLong()));
                LevelValue o2LevelValue = levels.get(new LevelKey(o2.getDepth(), o2.getId(), o2.getFromAsLong()));
                if (!o1LevelValue.getOrder().equals(o2LevelValue.getOrder())) {
                    result = o1LevelValue.getOrder().compareTo(o2LevelValue.getOrder());
                } else {
                    result = o1.getFromAsLong().compareTo(o2.getFromAsLong());
                }
            } else {
                result = o1.getDepth().compareTo(o2.getDepth());
            }

            return result;
        }
    }
    
    private int compareSameLevel(TNode o1, TNode o2, Integer o1OrigDepth, Integer o2OrigDepth) {
        Long targetFrom = Math.max(o1.getFromAsLong(), o2.getFromAsLong());
        LevelValue o1LevelValue = levels.get(new LevelKey(o1.getDepth(), o1.getId(), targetFrom));
        LevelValue o2LevelValue = levels.get(new LevelKey(o2.getDepth(), o2.getId(), targetFrom));

        if (o1LevelValue == null || o2LevelValue == null) {
            throw new TXmlException("Valid array for compare can't be found.");
        }

        int result = o1LevelValue.getOrder().compareTo(o2LevelValue.getOrder());
        if (result != 0) {
            return result;
        } else {
            return o1OrigDepth.compareTo(o2OrigDepth);
        }
    }
    
    private int compareDiffLevel(TNode o1, TNode o2, Integer o1OrigDepth, Integer o2OrigDepth) {
        if (o1.getDepth().equals(o2.getDepth())) {
            return compareSameLevel(o1, o2, o1OrigDepth, o2OrigDepth); 
        }
        
        TNode deeperTNode;
        if (o1.getDepth().compareTo(o2.getDepth()) < 0) {//o1.depth < o2.depth
            deeperTNode = o2;
        } else {//o1.depth > o2.depth
            deeperTNode = o1;
        }
        
        boolean inCache = false;
        List<TNode> tNodeList = this.tree.get(new TreeKey(deeperTNode.getParentId()));
        TNode parent = null;
        if (tNodeList != null) {
            for (TNode n:tNodeList) {
                if (n.getFromAsLong() <= deeperTNode.getFromAsLong() && deeperTNode.getToAsLong() <= n.getToAsLong() && (n.getDepth() + 1) == deeperTNode.getDepth()) {
                    parent = n;
                    inCache = true;
                    break;
                }
            }
        }
        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (!inCache) {
            try {
                preparedStatement = this.dbApi.getParentInInterval(connection, schema, deeperTNode.getParentId(), deeperTNode.getFromAsLong(), deeperTNode.getToAsLong(), deeperTNode.getDepth() - 1);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    throw new TXmlException("Node " + deeperTNode.getParentId() + " isn't in database.");
                }

                parent = new TNode(
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
                new NodeGlobalSettings(connection, schema, documentName, dbApi, null)
                );  
                
                parent.getLocalSettings().setSavedNodes(deeperTNode.getLocalSettings().getSavedNodes());
                
                if (tNodeList != null) {
                    tNodeList.add(parent);
                } else {
                    List<TNode> newListInCache = new ArrayList<>();
                    newListInCache.add(parent);
                    this.tree.put(new TreeKey(deeperTNode.getParentId()), newListInCache);
                }
 
                //vytahnou z cache, vytvorit uzel, hodit do cache, zavolat rekurzi
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
        
        if (o1.getDepth().compareTo(o2.getDepth()) < 0) {//o1.depth < o2.depth
            return compareDiffLevel(o1, parent, o1OrigDepth, o2OrigDepth);
        } else {//o1.depth > o2.depth
            return compareDiffLevel(parent, o2, o1OrigDepth, o2OrigDepth);
        }
                
    }
    
    public List<TNode> sort(List<TNode> tNodes, DbApi dbApi, Connection connection, String schema, String documentName) throws SQLException {
        this.dbApi = dbApi;
        this.connection = connection; 
        this.schema = schema; 
        this.documentName = documentName;
        
        for (TNode n:tNodes) {
            TNode tNode;
            if (
                    (n.getNodeType() == XmlNodeTypeEnum.TXML_VALUE_FROM.getShortValue()) ||
                    (n.getNodeType() == XmlNodeTypeEnum.TXML_VALUE_TO.getShortValue()) ||
                    (n.getNodeType() == XmlNodeTypeEnum.TXML_VALUE_ID.getShortValue())
               ) {
                tNode = (TNode) n.getParentNode().getParentNode();
            } else if (n.getNodeType() == XmlNodeTypeEnum.TXML_ATTRIBUTE.getShortValue()) {
                tNode = (TNode) n.getParentNode();
            } else {
                tNode = n;                
            }
            
            boolean inSortFound = false;
            Long currFrom = tNode.getFromAsLong();
            Long targetTo = tNode.getToAsLong();
            while (true) {
                LevelValue inSortLevel = levels.get(new LevelKey(tNode.getDepth(), tNode.getId(), currFrom));
                if (inSortLevel != null) {
                    currFrom = inSortLevel.getTo() + 1;
                    if (inSortLevel.getTo() == targetTo) {
                        inSortFound = true;
                        break;
                    }
                } else {
                    inSortFound = false;
                    break;
                }
            }
            
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            
            if (!inSortFound) {//TODO vezmu znovu vse
                preparedStatement = dbApi.getValidsFromIntervalAndDepth(connection, schema, tNode.getFromAsLong(), tNode.getToAsLong(), tNode.getDepth(), documentName);
                resultSet = preparedStatement.executeQuery();
                
                boolean noResult = true;
                while (resultSet.next()) {
                    noResult = false;
                    
                    Array validArray = resultSet.getArray("valid");
                    ResultSet resultSetArray = validArray.getResultSet();
                    Long from = resultSet.getLong("from");
                    Long to = resultSet.getLong("to");

                    while(resultSetArray.next()) {
                        boolean hasKey = levels.containsKey(new LevelKey(tNode.getDepth(), resultSetArray.getLong(2), from));
                        if (!hasKey) {
                            LevelValue levelValue = new LevelValue(to, resultSetArray.getInt(1));
                            levels.put(new LevelKey(tNode.getDepth(), resultSetArray.getLong(2), from), levelValue);
                        } 
                    }
                }
                
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
                
                if (noResult) {
                    throw new TXmlException("In table cp misses in column valid array.");
                }
            }
        }

        Collections.sort(tNodes, this);
        
        return tNodes;
    }
}
