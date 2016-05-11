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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XPathStatements {
    private final String documentRootQuery = 
            "SELECT lcp.id as id, lcp_class.id as lcp_class, "
            + "lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp_class.depth as depth, "
            + "lcp.value as value, lcp_class.type as type, lcp_class.local_part as local_part, namespace.uri as uri, namespace.prefix as prefix, namespace.id as namespace_id, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo "
            + "FROM %s.DOCUMENT as document, %s.lcp_class as lcp_class, %s.lcp as lcp, %s.namespace as namespace "
            + "where namespace.id = lcp_class.namespace_id and document.name = ? and lcp_class.depth = 0 and document.id = lcp_class.document and lcp.lcp_class = lcp_class.id";

    private final String childNodesWithoutAttributesQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id  "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.parentId = ? and lcp.parentFrom = ? and lcp.parentTo = ? and lcp.LCP_CLASS = lcp_class.ID and (lcp_class.type = ? or lcp_class.type = ?)";
    
    private final String childNodesOnlyAttributesQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id  "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.parentId = ? and lcp.parentFrom = ? and lcp.parentTo = ? and lcp.LCP_CLASS = lcp_class.ID and lcp_class.type = ?";
    
    private final String childrenByTypeAndLabelQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.parentId = ? and lcp.parentFrom = ? and lcp.parentTo = ? and lcp.LCP_CLASS = lcp_class.ID and lcp_class.type = ? and lcp_class.local_part = ? and lcp_class.namespace_id = ?";
    
    private final String childrenByTypeQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.parentId = ? and lcp.parentFrom = ? and lcp.parentTo = ? and lcp.LCP_CLASS = lcp_class.ID and lcp_class.type = ?";
    
    private final String childrenOfOneTypeQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parent as parent, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix and namespace.uri as uri and namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class where lcp.parent in %s and lcp.LCP_CLASS = lcp_class.ID and lcp_class.type = ?";
    
    private final String childrenQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.parentId = ? and lcp.parentFrom = ? and lcp.parentTo = ? and lcp.LCP_CLASS = lcp_class.ID";
    
    private final String childrenByDepthQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.parentId = ? and lcp.parentFrom = ? and lcp.parentTo = ? and lcp.LCP_CLASS = lcp_class.ID and lcp_class.depth <= ?";
    
    private final String valuesQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parent as parent, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix and namespace.uri as uri and namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class where lcp.parent in %s and lcp.LCP_CLASS = lcp_class.ID and lcp_class.type = ?";
    
    private final String depthMaxQuery = 
            "SELECT max(lcp_class.depth) as max_depth "
            + "FROM %s.LCP_CLASS as lcp_class where lcp_class.local_part = ? and namespace_id = ?";
    
    private final String valuesOfDescendantsQuery =
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parent as parent, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class where lcp.parent in %s and lcp.LCP_CLASS = lcp_class.ID";
    
    private final String validsFromIntervalAndDepthQuery =
            "SELECT cp.valid, cp.\"from\", cp.\"to\" \n" +
            "FROM %s.CP as cp, %s.CP_CLASS as cp_class, %s.document as document\n" +
            " where cp.cp_class = cp_class.id and cp.\"from\" >= ? and cp.\"to\" <= ? and cp_class.depth = ? and document.id = cp_class.document and document.name = ?";
    
    private final String parentInIntervalQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.id = ? and lcp.LCP_CLASS = lcp_class.ID and lcp.\"from\" <= ? and ? <= lcp.\"to\" and lcp_class.depth = ?";
    
    private final String parentQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id  "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where lcp_class.namespace_id = namespace.id and lcp.id = ? and lcp.\"from\" = ? and lcp.\"to\" = ? and lcp.LCP_CLASS = lcp_class.ID";
    
    private final String namespaceFromPrefixQuery = "SELECT id, uri from %s.namespace where prefix=? and document_id=?";
    
    private final String idNamespaceQuery = "SELECT id from %s.namespace where prefix=? and uri=? and document_id=?";
    
    public PreparedStatement getIdNamespaceStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(idNamespaceQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getNamespaceFromPrefixStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(namespaceFromPrefixQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getParentStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(parentQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getParentInIntervalStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(parentInIntervalQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getValidsFromIntervalAndDepthStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(validsFromIntervalAndDepthQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getDepthMaxStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(depthMaxQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getValuesOfDescendantsStatement(Connection connection, String schema, String idArray) throws SQLException {
        String query = String.format(valuesOfDescendantsQuery, schema, schema, idArray);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getChildrenByTypeAndLabelStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(childrenByTypeAndLabelQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getChildrenByDepthStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(childrenByDepthQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getChildrenStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(childrenQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getChildrenByTypeStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(childrenByTypeQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getChildrenOfOneTypeStatement(Connection connection, String schema, String idArray) throws SQLException {
        String query = String.format(childrenOfOneTypeQuery, schema, schema, idArray);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getValuesStatement(Connection connection, String schema, String idArray) throws SQLException {
        String query = String.format(valuesQuery, schema, schema, idArray);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getDocumentRootStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(documentRootQuery, schema, schema, schema, schema, schema, 
                schema, schema, schema, schema, schema, schema, schema, schema, 
                schema, schema, schema, schema, schema, schema, schema, schema, schema);   
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getChildNodesWithoutAttributesStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(childNodesWithoutAttributesQuery, schema, schema, schema);   
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getChildNodesOnlyAttributesStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(childNodesOnlyAttributesQuery, schema, schema, schema);   
        return connection.prepareStatement(query);
    }
}
