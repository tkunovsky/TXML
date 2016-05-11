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
package txml.database;

import txml.database.DatabaseEnum;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.stream.events.Namespace;
import txml.database.model.NamespaceInfo;
import txml.TXmlException;
import txml.load.model.XmlNode;
import txml.load.model.XmlDataModel;
import txml.load.model.XmlNodeTypeEnum;

public class DbApi {
    private final long NOW = 9223372036854775807l;
    private long current_time; 
    private final XPathStatements xPathStatements;
    private final UpdateStatements updateStatements;
    private final SnapshotStatements snapshotStatements;
    private SimpleDateFormat timeFormat;
    
    public DbApi() {
        this.xPathStatements = new XPathStatements();
        this.updateStatements = new UpdateStatements();
        this.snapshotStatements = new SnapshotStatements();
        this.timeFormat = new SimpleDateFormat("d. M. yyyy H:mm:ss");
    }

    public void setTimeFormat(SimpleDateFormat timeFormat) {
        this.timeFormat = timeFormat;
    }
    
    public long getNOW() {
        return NOW;
    }
    
    public void refreshCurrentTime() {
        this.current_time = System.currentTimeMillis();
    }
    
    public String getCurrTime() {
        return this.timeFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public Long getCurrTimeAsLong() {
        return this.current_time;
    }
    
    public String getTime(long millis) {
        if (this.current_time >= millis) {
            return this.timeFormat.format(new Timestamp(millis));
        } else {
            return "Now";
        }
    }
    
    public Long getTimePrecision() {
        String pattern = this.timeFormat.toPattern();
        long factor = 1;
        if (pattern.contains("S")) {
            factor = 1;
        } else if (pattern.contains("s")) {
            factor = 1000;
        } else if (pattern.contains("m")) {
            factor = 1000 * 60;
        } else if (pattern.contains("h") || pattern.contains("H")) {
            factor = 1000 * 60 * 60;
        } else if (pattern.contains("d") || pattern.contains("E") || pattern.contains("D") || pattern.contains("F")) {
            factor = 1000 * 60 * 60 * 24;
        }
        return factor;
    }
    
    
    public Timestamp convertStringToTimestamp(String str_date) {
        if (str_date.equalsIgnoreCase("now")) {
            return new Timestamp(getNOW());
        }
        
        try {
          Date date = this.timeFormat.parse(str_date);
          java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
          return timeStampDate;
        } catch (ParseException e) {
            throw new TXmlException(e.getLocalizedMessage());
        }
    }
    
    public NamespaceInfo getNamespaceInfoFromPrefix(Connection connection, String schema, String label, Long documentId, short nodeType) throws SQLException {
        String prefix;
        String localPart;
        String uri;
        Long namespaceId;
        
        if (label.contains(":")) {
            String[] labelParts = label.split(":");
            if (labelParts.length != 2) {
                throw new TXmlException("Bad element or attribute name: " + label);
            }
            prefix = labelParts[0];
            localPart = labelParts[1];
        } else {
            prefix = "";
            localPart = label;
        }
        
        PreparedStatement preparedStatement = getNamespaceFromPrefixStatement(connection, schema, prefix, documentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            throw new TXmlException("Unknown prefix '" + prefix  + "'");
        } else {
            uri = resultSet.getString("uri");
            namespaceId = resultSet.getLong("id");
            if (resultSet.next()) {
                if (prefix.equals("")) {
                    if (XmlNodeTypeEnum.TEXT.getShortValue() == nodeType || XmlNodeTypeEnum.ATTRIBUTE.getShortValue() == nodeType) {
                        if (!uri.equals("")) {
                            uri = resultSet.getString("uri");
                            namespaceId = resultSet.getLong("id");
                        }
                    } else {
                        if (uri.equals("")) {
                            uri = resultSet.getString("uri");
                            namespaceId = resultSet.getLong("id");
                        }
                    }
                    
                    if (resultSet.next()) {
                        preparedStatement.close();
                        resultSet.close();
                        throw new TXmlException("Ambiguous prefix '" + prefix  + "'");
                    }
                }
            }
            preparedStatement.close();
            resultSet.close();
        }

        return new NamespaceInfo(namespaceId, uri, prefix, localPart);
    }
    
    public PreparedStatement getIdNamespaceStatement(Connection connection, String schema, String prefix, String uri, Long documentId) throws SQLException {
        PreparedStatement idNamespaceStatement = this.xPathStatements.getIdNamespaceStatement(connection, schema);
        idNamespaceStatement.setString(1, prefix);
        idNamespaceStatement.setString(2, uri);
        idNamespaceStatement.setLong(3, documentId);
        
        return idNamespaceStatement;
    }
    
    private PreparedStatement getNamespaceFromPrefixStatement(Connection connection, String schema, String prefix, Long documentId) throws SQLException {
        PreparedStatement namespaceFromPrefixStatement = this.xPathStatements.getNamespaceFromPrefixStatement(connection, schema);
        namespaceFromPrefixStatement.setString(1, prefix);
        namespaceFromPrefixStatement.setLong(2, documentId);
        
        return namespaceFromPrefixStatement;
    }
    
    public PreparedStatement getCpFromInstanceAndDepthStatement(Connection connection, String schema) throws SQLException {
        PreparedStatement cpFromInstanceAndDepthStatement = this.snapshotStatements.getCpFromInstanceAndDepthStatement(connection, schema);
        return cpFromInstanceAndDepthStatement;
    }
    
    public PreparedStatement getLcpFromInstanceAndDepthStatement(Connection connection, String schema) throws SQLException {
        PreparedStatement lcpFromInstanceAndDepthStatement = this.snapshotStatements.getLcpFromInstanceAndDepthStatement(connection, schema);
        return lcpFromInstanceAndDepthStatement;
    }
    
    public PreparedStatement getCPWithoutId(Connection connection, String schema, Long to, Long cpClass) throws SQLException {
        PreparedStatement cpFromCPClassAndToStatement = this.updateStatements.getCpFromCPClassAndToStatement(connection, schema);
        cpFromCPClassAndToStatement.setLong(1, to);
        cpFromCPClassAndToStatement.setLong(2, cpClass);
        return cpFromCPClassAndToStatement;
    }
    
    public PreparedStatement getNamespacesFromDocument(Connection connection, String schema, Long documentId) throws SQLException {
        PreparedStatement namespacesFromDocumentStatement = this.snapshotStatements.getNamespacesFromDocumentStatement(connection, schema);
        namespacesFromDocumentStatement.setLong(1, documentId);
        return namespacesFromDocumentStatement;
    }
    
    public Long getNextValLCPClassIdSequence(Connection connection, String schema) throws SQLException {
        Long newId;
        try (PreparedStatement nextValLCPClassIdSequenceStatement = this.updateStatements.getNextValLCPClassIdSequenceStatement(connection, schema); ResultSet resultSet = nextValLCPClassIdSequenceStatement.executeQuery();) {
            resultSet.next();
            newId = resultSet.getLong("lcp_class_id");
        }
        
        return newId;
    }
    
    public Long getNextValCPClassIdSequence(Connection connection, String schema) throws SQLException {
        Long newId;
        try (PreparedStatement nextValCPClassIdSequenceStatement = this.updateStatements.getNextValCPClassIdSequenceStatement(connection, schema); ResultSet resultSet = nextValCPClassIdSequenceStatement.executeQuery();) {
            resultSet.next();
            newId = resultSet.getLong("cp_class_id");
        }
        
        return newId;
    }

    public Long getNextValNodeIdSequence(Connection connection, String schema) throws SQLException {
        Long newId;
        try (PreparedStatement nextValNodeIdSequenceStatement = this.updateStatements.getNextValNodeIdSequenceStatement(connection, schema); ResultSet resultSet = nextValNodeIdSequenceStatement.executeQuery();) {
            resultSet.next();
            newId = resultSet.getLong("node_id");
        }
        
        return newId;
    }

    public void insertLCPClassTableStatement(Connection connection, String schema, Long id, short type, String localPart, Long namespaceId, int depth, Long parent, Long document) throws SQLException {
        try (PreparedStatement insertLCPClassTableStatementStatement = this.updateStatements.getInsertLCPClassTableStatement(connection, schema)) {
            insertLCPClassTableStatementStatement.setLong(1, id);
            insertLCPClassTableStatementStatement.setShort(2, type);
            insertLCPClassTableStatementStatement.setString(3, localPart);
            insertLCPClassTableStatementStatement.setLong(4, namespaceId);
            insertLCPClassTableStatementStatement.setInt(5, depth);
            insertLCPClassTableStatementStatement.setLong(6, parent);
            insertLCPClassTableStatementStatement.setLong(7, document);
            
            insertLCPClassTableStatementStatement.execute();
        }
    }
    
    public void insertCPClassTableStatement(Connection connection, String schema, Long id, int depth, Long parent, Long document) throws SQLException {
        try (PreparedStatement insertCPClassTableStatementStatement = this.updateStatements.getInsertCPClassTableStatement(connection, schema)) {
            insertCPClassTableStatementStatement.setLong(1, id);
            insertCPClassTableStatementStatement.setInt(2, depth);
            insertCPClassTableStatementStatement.setLong(3, parent);
            insertCPClassTableStatementStatement.setLong(4, document);
            
            insertCPClassTableStatementStatement.execute();
        }
    }
    
    public PreparedStatement getSelectDocumentStatement(Connection connection, String schema, String documentName) throws SQLException {
        PreparedStatement selectDocumentStatement = this.updateStatements.getSelectDocumentStatement(connection, schema);
        selectDocumentStatement.setString(1, documentName);
        
        return selectDocumentStatement;
    }
    
    public PreparedStatement getDocumentHeaderStatement(Connection connection, String schema, Long documentId) throws SQLException {
        PreparedStatement selectDocumentStatement = this.snapshotStatements.getDocumentHeaderStatement(connection, schema);
        selectDocumentStatement.setLong(1, documentId);
        
        return selectDocumentStatement;
    }
    
    public PreparedStatement getCPClassWithoutId(Connection connection, String schema, Integer depth, Long document) throws SQLException {
        PreparedStatement cpClassWithoutIdStatement = this.updateStatements.getCpClassWithoutIdStatement(connection, schema);
        cpClassWithoutIdStatement.setInt(1, depth);
        cpClassWithoutIdStatement.setLong(2, document);
        return cpClassWithoutIdStatement;
    }

    public PreparedStatement getLCPClassWithoutId(Connection connection, String schema, String localPart, Long namespaceId, Integer depth, Long parent, short type) throws SQLException {
        PreparedStatement lcpClassWithoutIdStatement = this.updateStatements.getLcpClassWithoutIdStatement(connection, schema);
        lcpClassWithoutIdStatement.setString(1, localPart);
        lcpClassWithoutIdStatement.setLong(2, namespaceId);
        lcpClassWithoutIdStatement.setInt(3, depth);
        lcpClassWithoutIdStatement.setLong(4, parent);
        lcpClassWithoutIdStatement.setShort(5, type);
        return lcpClassWithoutIdStatement;
    }
    
    public PreparedStatement getDocumentRoot(Connection connection, String schema, String documentName) throws SQLException {
        PreparedStatement documentRootStatement = this.xPathStatements.getDocumentRootStatement(connection, schema);
        documentRootStatement.setString(1, documentName);
        return documentRootStatement;
    }
    
    public PreparedStatement getChildNodesWithoutAttributes(Connection connection, String schema, short elementType, short textType, Long parentId, Long parentFrom, Long parentTo) throws SQLException {
        PreparedStatement childNodesWithoutAttributesStatement = this.xPathStatements.getChildNodesWithoutAttributesStatement(connection, schema);
        childNodesWithoutAttributesStatement.setLong(1, parentId);
        childNodesWithoutAttributesStatement.setLong(2, parentFrom);
        childNodesWithoutAttributesStatement.setLong(3, parentTo);
        childNodesWithoutAttributesStatement.setShort(4, elementType);
        childNodesWithoutAttributesStatement.setShort(5, textType);
        return childNodesWithoutAttributesStatement;
    }
    
    public PreparedStatement getChildNodesOnlyAttributes(Connection connection, String schema, short attributeType, Long parentId, Long parentFrom, Long parentTo) throws SQLException {
        PreparedStatement childNodesOnlyAttributesStatement = this.xPathStatements.getChildNodesOnlyAttributesStatement(connection, schema);
        childNodesOnlyAttributesStatement.setLong(1, parentId);
        childNodesOnlyAttributesStatement.setLong(2, parentFrom);
        childNodesOnlyAttributesStatement.setLong(3, parentTo);
        childNodesOnlyAttributesStatement.setShort(4, attributeType);
        return childNodesOnlyAttributesStatement;
    }
    
    public PreparedStatement getChildrenByTypeAndLabel(Connection connection, String schema) throws SQLException {
        PreparedStatement childrenStatement = this.xPathStatements.getChildrenByTypeAndLabelStatement(connection, schema);
        return childrenStatement;
    }
    
    public PreparedStatement getChildrenByDepth(Connection connection, String schema) throws SQLException {
        PreparedStatement childrenByDepthStatement = this.xPathStatements.getChildrenByDepthStatement(connection, schema);
        return childrenByDepthStatement;
    }
    
    public PreparedStatement getChildren(Connection connection, String schema) throws SQLException {
        PreparedStatement childrenStatement = this.xPathStatements.getChildrenStatement(connection, schema);
        return childrenStatement;
    }
    
    public PreparedStatement getChildrenByType(Connection connection, String schema) throws SQLException {
        PreparedStatement childrenByTypeStatement = this.xPathStatements.getChildrenByTypeStatement(connection, schema);
        return childrenByTypeStatement;
    }
    
    public PreparedStatement getParent(Connection connection, String schema) throws SQLException {
        PreparedStatement parentsStatement = this.xPathStatements.getParentStatement(connection, schema);
        return parentsStatement;
    }
    
    public PreparedStatement getLastNode(Connection connection, String schema, Long nodeId) throws SQLException {
        PreparedStatement lastStatement = this.updateStatements.getLastNodeStatement(connection, schema);
        lastStatement.setLong(1, nodeId);
        return lastStatement;
    }
    
    public PreparedStatement getChildrenOfOneType(Connection connection, String schema, short type, Long[] idArray) throws SQLException {
        PreparedStatement childrenOfOneTypeStatement = this.xPathStatements.getChildrenOfOneTypeStatement(connection, schema, Arrays.toString(idArray).replace('[', '(').replace(']', ')'));
        childrenOfOneTypeStatement.setShort(1, type);
        return childrenOfOneTypeStatement;
    }
    
    public PreparedStatement getValues(Connection connection, String schema, short type, Long[] idArray) throws SQLException {
        PreparedStatement valuesStatement = this.xPathStatements.getValuesStatement(connection, schema, Arrays.toString(idArray).replace('[', '(').replace(']', ')'));
        valuesStatement.setShort(1, type);
        return valuesStatement;
    }
    
    public PreparedStatement getMaxDepth(Connection connection, String schema, String localPart, Long namespaceId) throws SQLException {
        PreparedStatement maxDepthStatement = this.xPathStatements.getDepthMaxStatement(connection, schema);
        maxDepthStatement.setString(1, localPart);
        maxDepthStatement.setLong(2, namespaceId);
        return maxDepthStatement;
    }    
    
    public PreparedStatement getValuesOfDescendants(Connection connection, String schema, Long[] idArray) throws SQLException {
        PreparedStatement valuesOfDescendantsStatement = this.xPathStatements.getValuesOfDescendantsStatement(connection, schema, Arrays.toString(idArray).replace('[', '(').replace(']', ')'));

        return valuesOfDescendantsStatement;
    }
    
    public PreparedStatement getParentInInterval(Connection connection, String schema, Long id, Long from, Long to, Integer depth) throws SQLException {
        PreparedStatement parentInIntervalStatement = this.xPathStatements.getParentInIntervalStatement(connection, schema);
        parentInIntervalStatement.setLong(1, id);
        parentInIntervalStatement.setLong(2, from);
        parentInIntervalStatement.setLong(3, to);
        parentInIntervalStatement.setInt(4, depth);
        
        return parentInIntervalStatement;
    }
    
    public PreparedStatement getValidsFromIntervalAndDepth(Connection connection, String schema, Long from, Long to, int depth, String documentName) throws SQLException {
        PreparedStatement validsFromIntervalAndDepthStatement = this.xPathStatements.getValidsFromIntervalAndDepthStatement(connection, schema);
        validsFromIntervalAndDepthStatement.setLong(1, from);
        validsFromIntervalAndDepthStatement.setLong(2, to);
        validsFromIntervalAndDepthStatement.setInt(3, depth);
        validsFromIntervalAndDepthStatement.setString(4, documentName);

        return validsFromIntervalAndDepthStatement;
    }
    
    public void splitCpInterval(Connection connection, String schema, Long from, Long to, Long cp_class, Long newTo, Object[] validObjectArray) throws SQLException {
        try (PreparedStatement splitCpIntervalUpdateStatement = this.updateStatements.getSplitCpIntervalUpdateStatement(connection, schema)) {
            splitCpIntervalUpdateStatement.setLong(1, newTo - 1);
            splitCpIntervalUpdateStatement.setLong(2, from);
            splitCpIntervalUpdateStatement.setLong(3, to);
            splitCpIntervalUpdateStatement.setLong(4, cp_class);
            
            int changed = splitCpIntervalUpdateStatement.executeUpdate();
            if (changed != 1) {
                throw new TXmlException("update in splitCpInterval failed, updated rows: " + changed);
            }
        }
        
        getInsertCPTableStatement(connection, schema, newTo, to, cp_class, validObjectArray);
    }
    
    public void getInsertCPTableStatement(Connection connection, String schema, Long from, Long to, Long cp_class, Object[] validObjectArray) throws SQLException {
        try (PreparedStatement insertCPTableStatement = this.updateStatements.getInsertCPTableStatement(connection, schema)) {
            
            insertCPTableStatement.setLong(1, from);
            insertCPTableStatement.setLong(2, to);
            setArray(insertCPTableStatement, 3, validObjectArray, connection);
            insertCPTableStatement.setLong(4, cp_class);
            
            insertCPTableStatement.executeUpdate();
        } 
    }

    public void insertLCPTable(Connection connection, String schema, Long id, Long from, Long to, Long parentId, Long parentFrom, Long parentTo, String value,  Long LCP_Class) throws SQLException {
        try (PreparedStatement insertLCPTableStatement = this.updateStatements.getInsertLCPTableStatement(connection, schema)) {
            insertLCPTableStatement.setLong(1, id);
            insertLCPTableStatement.setLong(2, from);
            insertLCPTableStatement.setLong(3, to);
            insertLCPTableStatement.setLong(4, parentId);
            insertLCPTableStatement.setLong(5, parentFrom);
            insertLCPTableStatement.setLong(6, parentTo);
            insertLCPTableStatement.setString(7, value);
            insertLCPTableStatement.setLong(8, LCP_Class);
            
            insertLCPTableStatement.executeUpdate();
        } 
    }
    
    
    public void insertNodeInValidInPos(Connection connection, String schema, Long from, Long to, Long cp_class,Object[] validObjectArray, Long nodeId, Long parentId, Long parentFrom, Long parentTo, Integer pos, Short nodeType, Long NOW, Long documentId, Integer depth) throws SQLException {
        Short types[];
        if (nodeType.equals(XmlNodeTypeEnum.ATTRIBUTE.getShortValue())) {
            types = new Short[1];
            types[0] = XmlNodeTypeEnum.ATTRIBUTE.getShortValue();
        } else if (nodeType.equals(XmlNodeTypeEnum.ELEMENT.getShortValue()) || nodeType.equals(XmlNodeTypeEnum.TEXT.getShortValue())) {
            types = new Short[2];
            types[0] = XmlNodeTypeEnum.ELEMENT.getShortValue();
            types[1] = XmlNodeTypeEnum.TEXT.getShortValue();
        } else {
            throw new TXmlException("Type " + nodeType + " isn't supported.");
        }
        PreparedStatement preparedStatement = this.updateStatements.getNodesOfParentStatement(connection, schema, Arrays.toString(types).replace("[", "(").replace("]", ")"));
        preparedStatement.setLong(1, parentId);
        preparedStatement.setLong(2, NOW);
        preparedStatement.setLong(3, NOW);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Long> nodesOfParent = new ArrayList<>();
        Object[] newValidObjectArray = new Object[validObjectArray.length + 1];
        
        while (resultSet.next()) {
            Long sibling = resultSet.getLong("id");
            if (sibling.equals(nodeId)) continue;
            nodesOfParent.add(resultSet.getLong("id"));
        }
        
        resultSet.close();
        preparedStatement.close();
        
        if (pos < 0) {
            pos = nodesOfParent.size() + pos + 1;
        } else if (pos == 0) {
            throw new TXmlException("Position out of order.");
        } else {
            pos--;
        }
        
        if (pos > nodesOfParent.size()) {
            throw new TXmlException("Position out of order.");
        }
        
        if (pos == 0 && nodesOfParent.isEmpty()) {
            if (nodeType.equals(XmlNodeTypeEnum.ATTRIBUTE.getShortValue())) {// node is attribute, insert before element and text
                types = new Short[2];
                types[0] = XmlNodeTypeEnum.ELEMENT.getShortValue();
                types[1] = XmlNodeTypeEnum.TEXT.getShortValue();
                
                preparedStatement = this.updateStatements.getNodesOfParentStatement(connection, schema, Arrays.toString(types).replace("[", "(").replace("]", ")"));
                preparedStatement.setLong(1, parentId);
                preparedStatement.setLong(2, NOW);
                preparedStatement.setLong(3, NOW);
                resultSet = preparedStatement.executeQuery();
                List<Long> noAttributeNodesOfParent = new ArrayList<>();

                while (resultSet.next()) {
                    Long sibling = resultSet.getLong("id");
                    noAttributeNodesOfParent.add(resultSet.getLong("id"));
                }

                resultSet.close();
                preparedStatement.close();
                
                if (noAttributeNodesOfParent.isEmpty()) {

                    insertNodeInValidToEnd(connection, schema, from, to, cp_class, validObjectArray, nodeId, parentId, parentFrom, parentTo, depth, documentId, NOW);
                    return;
                }
                
                Set<Long> setNoAtributeNodesOfParent = new HashSet<>(noAttributeNodesOfParent);
                
                int j = 0;
                boolean inserted = false;
                for (int i = 0; i < newValidObjectArray.length; i++) {
                    if (!inserted && setNoAtributeNodesOfParent.contains((Long) validObjectArray[j])) {
                        newValidObjectArray[i] = nodeId;
                        inserted = true;
                    } else {
                        newValidObjectArray[i] = validObjectArray[j];
                        j++;
                    }
                }
                
            } else if (nodeType.equals(XmlNodeTypeEnum.ELEMENT.getShortValue()) || nodeType.equals(XmlNodeTypeEnum.TEXT.getShortValue())) {
                //no nodes -> insert to end
                insertNodeInValidToEnd(connection, schema, from, to, cp_class, validObjectArray, nodeId, parentId, parentFrom, parentTo, depth, documentId, NOW);
                
                return;
            } else {
                throw new TXmlException("Type " + nodeType + " isn't supported.");
            }
            
        } else {
        
            Set<Long> setNodesOfParent = new HashSet<>(nodesOfParent);

            //check if db result is valid
            for (int i = 0; i < nodesOfParent.size(); i++) {
                boolean found = false;
                for (int j = 0; j < validObjectArray.length; j++) {
                    if (validObjectArray[j].equals(nodesOfParent.get(i))) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new TXmlException("In valid array missing node " +  nodesOfParent.get(i));
                }
            }

            //check if new node id will not duplicite
            for (int i = 0; i < validObjectArray.length; i++) {
                if (nodeId.equals(validObjectArray[i])) {
                    throw new TXmlException("Duplicite node for valid array.");
                }
            }

            if (pos != nodesOfParent.size()) {
                Integer aPos = -1;
                int j = 0;
                boolean found = false;
                for (int i = 0; i < newValidObjectArray.length; i++) {
                    if (!found && setNodesOfParent.contains((Long) validObjectArray[j]) && (++aPos).equals(pos)) {
                        newValidObjectArray[i] = nodeId;
                        found = true;
                    } else {
                        newValidObjectArray[i] = validObjectArray[j];
                        j++;
                    }
                }
            } else {
                Integer aPos = 0;
                int j = 0;
                boolean found = false;
                for (int i = 0; i < newValidObjectArray.length; i++) {
                    if (!found && aPos.equals(pos)) {
                        newValidObjectArray[i] = nodeId;
                        found = true;
                    } else {
                        newValidObjectArray[i] = validObjectArray[j];
                        if (setNodesOfParent.contains((Long) validObjectArray[j])) {
                            aPos++;
                        }
                        j++;
                    }
                }
            }
        }
        
        try (PreparedStatement validUpdateStatement = this.updateStatements.getValidUpdateStatement(connection, schema)) {
            setArray(validUpdateStatement, 1, newValidObjectArray, connection);
            validUpdateStatement.setLong(2, from);
            validUpdateStatement.setLong(3, to);
            validUpdateStatement.setLong(4, cp_class);
            validUpdateStatement.executeUpdate();
        } 
    }
    
    
    public void insertNodeInValidToEnd(Connection connection, String schema, Long from, Long to, Long cp_class, Object[] validObjectArray, Long nodeId, Long parentId, Long parentFrom, Long parentTo, Integer depth, Long documentId, Long NOW) throws SQLException {
        PreparedStatement preparedStatement = getCPClassWithoutId(connection, schema, depth - 1, documentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new TXmlException("Missing cp class record.");
        }
        
        Long parentCpClass = resultSet.getLong("id");
        resultSet.close();
        preparedStatement.close();
        
        preparedStatement = getCPWithoutId(connection, schema, NOW, parentCpClass);
        resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new TXmlException("Missing parent cp record.");
        }
        
        Object[] parentValidObjectArray = (Object[])resultSet.getArray("valid").getArray();
        boolean parentIsLast = parentValidObjectArray[parentValidObjectArray.length - 1].equals(parentId);
        List<Long> parentNeighbors = new ArrayList<>();
        if (!parentIsLast) {
            for (int i = 0; i < parentValidObjectArray.length; i++) {
                if (parentValidObjectArray[i].equals(parentId)) {
                    for (int j = i + 1; j < parentValidObjectArray.length; j++) {
                        parentNeighbors.add((Long) parentValidObjectArray[j]);
                    }
                }
            }
            if (parentNeighbors.isEmpty()) {
                throw new TXmlException("Missing some parent neighbor id.");
            }
        }
        
        resultSet.close();
        preparedStatement.close();
        Object[] newValidObjectArray = new Object[validObjectArray.length + 1];
        List<Long> nodesOfParentNeighbors = new ArrayList<>();
        if (!parentIsLast) {
            preparedStatement = this.updateStatements.getNodesOfParentsStatement(connection, schema, Arrays.toString(parentNeighbors.toArray()).replace("[", "(").replace("]", ")"));
            preparedStatement.setLong(1, NOW);
            preparedStatement.setLong(2, NOW);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long neighborChild = resultSet.getLong("id");
                nodesOfParentNeighbors.add(neighborChild);
            }
            
            resultSet.close();
            preparedStatement.close(); 
            
            if (!nodesOfParentNeighbors.isEmpty()) {
                Set<Long> setNodesOfParentNeighbors = new HashSet<>(nodesOfParentNeighbors);

                int j = 0;
                boolean inserted = false;
                for (int i = 0; i < newValidObjectArray.length; i++) {
                    if (!inserted && setNodesOfParentNeighbors.contains((Long) validObjectArray[j])) {
                        newValidObjectArray[i] = nodeId;
                        inserted = true;
                    } else {
                        newValidObjectArray[i] = validObjectArray[j];
                        j++;
                    }
                }

                if (!inserted) {
                    throw new TXmlException("Missing childs of neighbor parent in array.");
                }
            } 
        }
        
        if (parentIsLast || nodesOfParentNeighbors.isEmpty()) {
            for (int i = 0; i < validObjectArray.length; i++) {
                newValidObjectArray[i] = validObjectArray[i];
            }

            newValidObjectArray[validObjectArray.length] = nodeId;      
        }
        
        try (PreparedStatement validUpdateStatement = this.updateStatements.getValidUpdateStatement(connection, schema)) {
            setArray(validUpdateStatement, 1, newValidObjectArray, connection);
            validUpdateStatement.setLong(2, from);
            validUpdateStatement.setLong(3, to);
            validUpdateStatement.setLong(4, cp_class);
            validUpdateStatement.executeUpdate();
        } 
    }
    
    public void deleteNodeInValid(Connection connection, String schema, Long from, Long to, Long cp_class, Object[] validObjectArray, Long nodeId) throws SQLException {
        Object[] newValidObjectArray = new Object[validObjectArray.length - 1];
        for (int i = 0, j = 0; i < validObjectArray.length; i++) {
            if (validObjectArray[i].equals(nodeId)) {
                continue;
            } else {
                newValidObjectArray[j++] = validObjectArray[i];
            }
        }
        
        try (PreparedStatement validUpdateStatement = this.updateStatements.getValidUpdateStatement(connection, schema)) {
            setArray(validUpdateStatement, 1, newValidObjectArray, connection);
            validUpdateStatement.setLong(2, from);
            validUpdateStatement.setLong(3, to);
            validUpdateStatement.setLong(4, cp_class);
            validUpdateStatement.executeUpdate();
        }
    }
    
    public PreparedStatement getCpFromIntervalAndDepth(Connection connection, String schema, String documentName, Long to, int depth) throws SQLException {
        PreparedStatement cpFromIntervalAndDepthStatement = this.updateStatements.getCpFromIntervalAndDepthStatement(connection, schema);
        cpFromIntervalAndDepthStatement.setString(1, documentName);
        cpFromIntervalAndDepthStatement.setLong(2, to);
        cpFromIntervalAndDepthStatement.setInt(3, depth);

        return cpFromIntervalAndDepthStatement;
    }

    public void toLcpUpdate(Connection connection, String schema, Long to, Long newTo, Long nodeId) throws SQLException {        
        try (PreparedStatement toLcpUpdateStatement = this.updateStatements.getToLcpUpdateStatement(connection, schema)) {
            toLcpUpdateStatement.setLong(1, nodeId);
            toLcpUpdateStatement.setLong(2, newTo);
            toLcpUpdateStatement.setLong(3, nodeId);
            toLcpUpdateStatement.setLong(4, newTo);
            toLcpUpdateStatement.setLong(5, nodeId);
            toLcpUpdateStatement.setLong(6, to);
            toLcpUpdateStatement.setLong(7, nodeId);
            toLcpUpdateStatement.setLong(8, to);
            toLcpUpdateStatement.executeUpdate();
        }
    }
    
    public void parentLcpUpdate(Connection connection, String schema, Long from, Long to, Long nodeId, Long newParentId, Long newParentFrom, Long newParentTo, Long lcpClass) throws SQLException {        
        try (PreparedStatement parentLcpUpdateWithFromStatement = this.updateStatements.getParentLcpUpdateWithFromStatement(connection, schema)) {
            parentLcpUpdateWithFromStatement.setLong(1, newParentId);
            parentLcpUpdateWithFromStatement.setLong(2, newParentFrom);
            parentLcpUpdateWithFromStatement.setLong(3, newParentTo);
            parentLcpUpdateWithFromStatement.setLong(4, lcpClass);
            parentLcpUpdateWithFromStatement.setLong(5, nodeId);
            parentLcpUpdateWithFromStatement.setLong(6, from);
            parentLcpUpdateWithFromStatement.setLong(7, to);
            parentLcpUpdateWithFromStatement.executeUpdate();
        }
    }
    
    public void toLcpUpdateWithFrom(Connection connection, String schema, Long from, Long to, Long newTo, Long nodeId) throws SQLException {        
        try (PreparedStatement toLcpUpdateStatement = this.updateStatements.getToLcpUpdateWithFromStatement(connection, schema)) {
            toLcpUpdateStatement.setLong(1, nodeId);
            toLcpUpdateStatement.setLong(2, newTo);
            toLcpUpdateStatement.setLong(3, nodeId);
            toLcpUpdateStatement.setLong(4, newTo);
            toLcpUpdateStatement.setLong(5, nodeId);
            toLcpUpdateStatement.setLong(6, from);
            toLcpUpdateStatement.setLong(7, to);
            toLcpUpdateStatement.setLong(8, nodeId);
            toLcpUpdateStatement.setLong(9, from);
            toLcpUpdateStatement.setLong(10, to);
            toLcpUpdateStatement.executeUpdate();
        }
    }
    
    public void lcpDelete(Connection connection, String schema, Long to, Long nodeId) throws SQLException {        
        try (PreparedStatement lcpDeleteStatement = this.updateStatements.getLcpDeleteStatement(connection, schema)) {
            lcpDeleteStatement.setLong(1, nodeId);
            lcpDeleteStatement.setLong(2, to);
            lcpDeleteStatement.executeUpdate();
        }
    }
    
    public void initSchema(Connection connection, String schema) throws SQLException {
        connection.setAutoCommit(false);
        InitAndDeinitStatements statements = new InitAndDeinitStatements();
        statements.initSchema(connection, schema);
        connection.commit();
    }
    
    public void deinitSchema(Connection connection, String schema) throws SQLException {
        connection.setAutoCommit(false);
        InitAndDeinitStatements statements = new InitAndDeinitStatements();
        statements.deinitSchema(connection, schema);
        connection.commit();
    }
    
    public void loadDocumentToDb(Connection connection, String schema, XmlDataModel dataModel) throws SQLException {
        connection.setAutoCommit(false);
        LoadDocumentToDbStatements statements = new LoadDocumentToDbStatements();

        try {
            statements.init(schema, connection);
            statements.getInsertDocumentTableStatement().setLong(1, statements.getDocumentId());
            statements.getInsertDocumentTableStatement().setString(2, dataModel.getDocumentName());
            statements.getInsertDocumentTableStatement().setString(3, dataModel.getHeader());
            statements.getInsertDocumentTableStatement().executeUpdate();
            
            ResultSet namespaceIdRs = statements.getNextValNamespaceIdSequenceStatement().executeQuery();
            namespaceIdRs.next();
            Long emptyNamespaceId  = namespaceIdRs.getLong("namespace_id");
            namespaceIdRs.close();

            statements.getInsertNamespaceTableStatement().setLong(1, emptyNamespaceId);
            statements.getInsertNamespaceTableStatement().setString(2, "");
            statements.getInsertNamespaceTableStatement().setString(3, "");
            statements.getInsertNamespaceTableStatement().setLong(4, statements.getDocumentId());
            statements.getInsertNamespaceTableStatement().executeUpdate();
            
            insertNodeToDb(
                    connection, 
                    schema, 
                    dataModel.getRoot(), 
                    0, 
                    statements,
                    null,
                    null,
                    null);
        
        } catch(Exception ex) {
            System.err.println("DB ROLLBACK");
            connection.rollback();
            throw ex;
        } finally {
            statements.closeAll();
        }

        connection.commit();        
    }
    
    private void setArray(PreparedStatement preparedStatement, int pos, Object array[], Connection connection) throws SQLException {
        if (DatabaseEnum.getDatabase(connection) == DatabaseEnum.H2)
            preparedStatement.setObject(pos, array);
        else
            preparedStatement.setArray(pos, connection.createArrayOf("bigint", array));
    }
    
    private void insertNodeToDb(
            Connection connection, 
            String schema, 
            XmlNode node, 
            int depth,
            LoadDocumentToDbStatements statements,
            Long lcpParentId,
            Long lcpClassParentId,
            Long cpClassParentId
        ) throws SQLException {    


        ResultSet nodeIdRs = statements.getNextValNodeIdSequenceStatement().executeQuery();
        nodeIdRs.next();
        Long nodeId = nodeIdRs.getLong("node_id");
        nodeIdRs.close();
        
        List<Long> declaredNamespaceIds = new ArrayList<Long>();

        for (Namespace namespace:node.getNamespaces()) {
            Long namespaceId;
            ResultSet namespaceIdRs;
            
            statements.getSelectIdNamespaceStatement().setString(1, namespace.getNamespaceURI());
            statements.getSelectIdNamespaceStatement().setString(2, namespace.getPrefix());
            statements.getSelectIdNamespaceStatement().setLong(3, statements.getDocumentId());
            namespaceIdRs = statements.getSelectIdNamespaceStatement().executeQuery();
            if (namespaceIdRs.next()) {
                namespaceId = namespaceIdRs.getLong("id");
                namespaceIdRs.close();
            } else {
                namespaceIdRs.close();
                namespaceIdRs = statements.getNextValNamespaceIdSequenceStatement().executeQuery();
                namespaceIdRs.next();
                namespaceId = namespaceIdRs.getLong("namespace_id");
                namespaceIdRs.close();
                                
                statements.getInsertNamespaceTableStatement().setLong(1, namespaceId);
                statements.getInsertNamespaceTableStatement().setString(2, namespace.getNamespaceURI());
                statements.getInsertNamespaceTableStatement().setString(3, namespace.getPrefix());
                statements.getInsertNamespaceTableStatement().setLong(4, statements.getDocumentId());
                statements.getInsertNamespaceTableStatement().executeUpdate();
            }
            
            declaredNamespaceIds.add(namespaceId);       
        }
        
        Long namespaceId;
        ResultSet namespaceIdRs;
        statements.getSelectIdNamespaceStatement().setString(1, node.getName().getNamespaceURI());
        statements.getSelectIdNamespaceStatement().setString(2, node.getName().getPrefix());
        statements.getSelectIdNamespaceStatement().setLong(3, statements.getDocumentId());
        namespaceIdRs = statements.getSelectIdNamespaceStatement().executeQuery();
        
        if (namespaceIdRs.next()) {
            namespaceId = namespaceIdRs.getLong("id");
            namespaceIdRs.close();
        } else {
            namespaceIdRs.close();
            namespaceIdRs = statements.getNextValNamespaceIdSequenceStatement().executeQuery();
            namespaceIdRs.next();
            namespaceId = namespaceIdRs.getLong("namespace_id");
            namespaceIdRs.close();

            statements.getInsertNamespaceTableStatement().setLong(1, namespaceId);
            statements.getInsertNamespaceTableStatement().setString(2, node.getName().getNamespaceURI());
            statements.getInsertNamespaceTableStatement().setString(3, node.getName().getPrefix());
            statements.getInsertNamespaceTableStatement().setLong(4, statements.getDocumentId());
            statements.getInsertNamespaceTableStatement().executeUpdate();
        }
        
        Long lcpClassId;
        ResultSet lcpClassIdRs;

        if (depth != 0) {
            statements.getSelectIdLCPClassStatement().setShort(1, node.getType().getShortValue());
            statements.getSelectIdLCPClassStatement().setString(2, node.getName().getLocalPart());
            statements.getSelectIdLCPClassStatement().setInt(3, depth);
            statements.getSelectIdLCPClassStatement().setLong(4, statements.getDocumentId());
            statements.getSelectIdLCPClassStatement().setLong(5, lcpClassParentId);
            statements.getSelectIdLCPClassStatement().setLong(6, namespaceId);
            lcpClassIdRs = statements.getSelectIdLCPClassStatement().executeQuery();
        } else {
            statements.getSelectRootIdLCPClassStatement().setShort(1, node.getType().getShortValue());
            statements.getSelectRootIdLCPClassStatement().setString(2, node.getName().getLocalPart());
            statements.getSelectRootIdLCPClassStatement().setInt(3, depth);
            statements.getSelectRootIdLCPClassStatement().setLong(4, statements.getDocumentId());
            statements.getSelectRootIdLCPClassStatement().setLong(5, namespaceId);
            lcpClassIdRs = statements.getSelectRootIdLCPClassStatement().executeQuery();
        }

        if (lcpClassIdRs.next()) {
            lcpClassId = lcpClassIdRs.getLong("id");
        } else {
            lcpClassIdRs = statements.getNextValLCPClassIdSequenceStatement().executeQuery();
            lcpClassIdRs.next();
            lcpClassId = lcpClassIdRs.getLong("lcp_class_id");
            
            statements.getInsertLCPClassTableStatement().setLong(1, lcpClassId);
            statements.getInsertLCPClassTableStatement().setShort(2, node.getType().getShortValue());
            statements.getInsertLCPClassTableStatement().setString(3, node.getName().getLocalPart());
            statements.getInsertLCPClassTableStatement().setInt(4, depth);
            statements.getInsertLCPClassTableStatement().setLong(6, statements.getDocumentId());
            statements.getInsertLCPClassTableStatement().setLong(7, namespaceId);
            
            if (depth == 0) {
                statements.getInsertLCPClassTableStatement().setNull(5, java.sql.Types.BIGINT);
            } else {
                statements.getInsertLCPClassTableStatement().setLong(5, lcpClassParentId);
            }
            statements.getInsertLCPClassTableStatement().executeUpdate();
        }

        Long cpClassId;
        statements.getSelectIdCPClassStatement().setInt(1, depth);
        statements.getSelectIdCPClassStatement().setLong(2, statements.getDocumentId());
        
        ResultSet cpClassIdRs = statements.getSelectIdCPClassStatement().executeQuery();
        if (cpClassIdRs.next()) {
            cpClassId = cpClassIdRs.getLong("id");
        } else {
            cpClassIdRs = statements.getNextValCPClassIdSequenceStatement().executeQuery();
            cpClassIdRs.next();
            cpClassId = cpClassIdRs.getLong("cp_class_id");
            
            statements.getInsertCPClassTableStatement().setLong(1, cpClassId);
            statements.getInsertCPClassTableStatement().setInt(2, depth);
            statements.getInsertCPClassTableStatement().setLong(4, statements.getDocumentId());
            
            if (depth == 0) {
                statements.getInsertCPClassTableStatement().setNull(3, java.sql.Types.BIGINT);
            } else {
                statements.getInsertCPClassTableStatement().setLong(3, cpClassParentId); 
            }
            
            statements.getInsertCPClassTableStatement().executeUpdate();
        }

        Long cpId;
        statements.getSelectPkCPStatement().setLong(1, this.current_time);
        statements.getSelectPkCPStatement().setLong(2, NOW);
        statements.getSelectPkCPStatement().setLong(3, cpClassId);
        ResultSet cpPkRs = statements.getSelectPkCPStatement().executeQuery();
        if (cpPkRs.next()) {
            Array validArray = cpPkRs.getArray("valid");
            Object[] validObjectArray = (Object[])validArray.getArray();
            Object[] newValidObjectArray = Arrays.copyOf(validObjectArray, validObjectArray.length + 1);
            newValidObjectArray[newValidObjectArray.length - 1] = nodeId;
            setArray(statements.getUpdateValidCPStatement(), 1, newValidObjectArray, connection);
            statements.getUpdateValidCPStatement().setLong(2, this.current_time);
            statements.getUpdateValidCPStatement().setLong(3, NOW);
            statements.getUpdateValidCPStatement().setLong(4, cpClassId);
            statements.getUpdateValidCPStatement().executeUpdate();

        } else {
            statements.getInsertCPTableStatement().setLong(1, this.current_time);
            statements.getInsertCPTableStatement().setLong(2, NOW);
            setArray(statements.getInsertCPTableStatement(), 3, new Object[] { nodeId }, connection);
            statements.getInsertCPTableStatement().setLong(4, cpClassId);
            statements.getInsertCPTableStatement().executeUpdate();
        }

        statements.getInsertLCPTableStatement().setLong(1, nodeId);
        statements.getInsertLCPTableStatement().setLong(2, this.current_time);
        statements.getInsertLCPTableStatement().setLong(3, NOW);
        statements.getInsertLCPTableStatement().setString(7, node.getValue());
        statements.getInsertLCPTableStatement().setLong(8, lcpClassId);

        if (depth == 0) {
            statements.getInsertLCPTableStatement().setNull(4, java.sql.Types.BIGINT);
            statements.getInsertLCPTableStatement().setNull(5, java.sql.Types.BIGINT);
            statements.getInsertLCPTableStatement().setNull(6, java.sql.Types.BIGINT);
        } else {
            statements.getInsertLCPTableStatement().setLong(4, lcpParentId);
            statements.getInsertLCPTableStatement().setLong(5, this.current_time);
            statements.getInsertLCPTableStatement().setLong(6, NOW);
        }
        statements.getInsertLCPTableStatement().executeUpdate();

        for (Long nId:declaredNamespaceIds) {
            statements.getInsertLcpNamespaceTableStatement().setLong(1, nodeId);
            statements.getInsertLcpNamespaceTableStatement().setLong(2, this.current_time);
            statements.getInsertLcpNamespaceTableStatement().setLong(3, NOW);
            statements.getInsertLcpNamespaceTableStatement().setLong(4, nId);
            statements.getInsertLcpNamespaceTableStatement().executeUpdate(); 
        }

        for (XmlNode n:node.getChildren()) {
            insertNodeToDb(
                    connection, 
                    schema, 
                    n, 
                    depth + 1, 
                    statements,
                    nodeId,
                    lcpClassId,
                    cpClassId);
        } 
    }
}
