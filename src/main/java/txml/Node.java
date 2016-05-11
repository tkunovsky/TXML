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
package txml;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * The <code>Node</code> interface is the primary datatype for the entire
 * TXML model. It represents a single node in the document tree.
 * While all objects implementing the <code>Node</code> interface expose
 * methods for dealing with children, not all objects implementing the
 * <code>Node</code> interface may have children. For example,
 * <code>Text</code> nodes may not have children, and adding children to
 * such nodes results in a <code>TXmlException</code> being raised.
 * <p>The attributes <code>nodeName</code>, <code>nodeValue</code> and
 * <code>attributes</code> are included as a mechanism to get at node
 * information without casting down to the specific derived interface. In
 * cases where there is no obvious mapping of these attributes for a
 * specific <code>nodeType</code> (e.g. <code>nodeValue</code> for an
 * <code>Element</code>), this returns empty result.</p>
 * @author Tomas Kunovsky
 */
public interface Node {
    // NodeType
    /**
     * The node is an <code>Element</code>.
     */
    public static final short ELEMENT_NODE              = org.w3c.dom.Node.ELEMENT_NODE;
    /**
     * The node is an <code>Attribute</code>.
     */
    public static final short ATTRIBUTE_NODE            = org.w3c.dom.Node.ATTRIBUTE_NODE;
    /**
     * The node is a <code>Text</code> node.
     */
    public static final short TEXT_NODE                 = org.w3c.dom.Node.TEXT_NODE;
    /**
     * The node is an TXML <code>Attribute</code>.
     */
    public static final short TXML_ATTRIBUTE_NODE       = 100;
    /**
     * The node is an TXML <code>Text</code> with <code>Id</code> of node.
     */
    public static final short TXML_VALUE_ID             = 101;
    /**
     * The node is an TXML <code>Text</code> with <code>From</code> of node.
     */
    public static final short TXML_VALUE_FROM           = 102;
    /**
     * The node is an TXML <code>Text</code> with <code>To</code> of node.
     */
    public static final short TXML_VALUE_TO             = 103;
    
    /**
     * A <code>NodeList</code> that contains all children of this node. If
     * there are no children, this is a <code>NodeList</code> containing no
     * nodes.
     * @throws TXmlException
     */
    NodeList getChildNodes() throws TXmlException;
    
    /**
     * The last child of this node. If there is no such node, this returns
     * <code>null</code>.
     * @throws TXmlException
     */
    Node getLastChild() throws TXmlException;
    
    /**
     * The first child of this node. If there is no such node, this returns
     * <code>null</code>.
     * @throws TXmlException
     */
    Node getFirstChild() throws TXmlException;
    
    /**
     * The name of this node.
     */
    String getNodeName();
    
    /**
     * Returns the local part of the qualified name of this node.
     */
    String getLocalName();
    
    /**
     * The namespace prefix of this node.
     */
    String getPrefix();
    
    /**
     * The namespace URI of this node.
     */
    String getNamespaceURI();
    
    /**
     * A code representing the type of the underlying object, as defined above.
     */
    short getNodeType();
    
    /**
     * A <code>NamedNodeMap</code> containing the attributes of this node.
     */
    NamedNodeMap getAttributes();
    
    /**
     * The value of this node. If node hasn't type <code>TEXT_NODE</code>, <code>TXML_VALUE_FROM</code> or
     * <code>TXML_VALUE_TO</code>, returns <code>null</code>.
     */
    String getNodeValue();

    /**
     * The start point of validity interval of this node.
     * @return Milliseconds since January 1, 1970, 00:00:00 GMT.
     */
    Long getFromAsLong();

    /**
     * The end point of validity interval of this node.
     * @return Milliseconds since January 1, 1970, 00:00:00 GMT.
     */
    Long getToAsLong();

    /**
     * The start point of validity interval of parent node.
     * @return Milliseconds since January 1, 1970, 00:00:00 GMT.
     */
    Long getParentFromAsLong();

    /**
     * The end point of validity interval of parent node.
     * @return Milliseconds since January 1, 1970, 00:00:00 GMT.
     */
    Long getParentToAsLong();

    /**
     * The start point of validity interval of parent node.
     */
    Timestamp getParentFromAsTimestamp();

    /**
     * The end point of validity interval of parent node.
     */
    Timestamp getParentToAsTimestamp();

    /**
     * The start point of validity interval of parent node.
     * @return Formatted a Date into a date/time string.
     */
    String getParentFromAsString();

    /**
     * The end point of validity interval of parent node.
     * @return Formatted a Date into a date/time string.
     */
    String getParentToAsString();

    /**
     * The start point of validity interval of this node.
     * @return Formatted a Date into a date/time string.
     */
    String getFromAsString();

    /**
     * The end point of validity interval of this node.
     * @return Formatted a Date into a date/time string.
     */
    String getToAsString();

    /**
     * The start point of validity interval of this node.
     */
    Timestamp getFromAsTimestamp();

    /**
     * The end point of validity interval of this node.
     */
    Timestamp getToAsTimestamp();

    /**
     * The identifier of this node, same for whole its history.
     */
    Long getId();
    
    /**
     * Text representation of XML subtree, where this node is root.
     * @param sort True for sorting of tree.
     * @throws SQLException
     * @throws TXmlException
     */
    String getTree(boolean sort) throws SQLException, TXmlException;

    /**
     * Close database connection.
     * @throws SQLException
     */
    void closeDbConnection() throws SQLException;

    /**
     * The identifier of parent node.
     */
    Long getParentId();

    /**
     * The depth of this node in xml tree.
     * @return Zero for root, 1 for root childs etc.
     */
    Integer getDepth();
    
    /**
     * The parent of this node.
     * @throws SQLException
     * @throws TXmlException
     */
    Node getParentNode() throws SQLException, TXmlException;

    /**
     * Reload data from database for this node.
     */
    void refresh();

    /**
     * Delete this node in document and reload this node.
     * @throws SQLException
     * @throws TXmlException
     */
    void deleteInDocument() throws SQLException, TXmlException;

    /**
     * Insert new descendant(s) of this node.
     * @param path Path of new descendant(s) separated by char "/".
     * @param value Text node of last new descendant.
     * @throws SQLException
     * @throws TXmlException
     */
    void insertIntoDocument(String path, String value) throws SQLException, TXmlException;

    /**
     * Insert new descendant(s) of this node.
     * @param path Path of new descendant(s) separated by char "/"
     * @param value Text node of last new descendant.
     * @param position Position of first inserted node in <code>path</code> in subtree of this node. 
     * Value 1 for first node, -1 for last node etc.
     * @throws SQLException
     * @throws TXmlException
     */
    void insertIntoDocument(String path, String value, Integer position) throws SQLException, TXmlException;

    /**
     * Move this node with all its descendants under <code>parent</code>.
     * @param parent New parent of this node.
     * @throws SQLException
     * @throws TXmlException
     */
    void setParentInDocument(Node parent) throws SQLException, TXmlException;

    /**
     * Move this node with all its descendants under <code>parent</code>.
     * @param parent New parent of this node.
     * @param position Position of this node in subtree <code>parent</code>. 
     * @throws SQLException
     * @throws TXmlException
     */
    void setParentInDocument(Node parent, Integer position) throws SQLException, TXmlException;
}
