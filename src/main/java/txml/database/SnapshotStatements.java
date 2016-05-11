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

public class SnapshotStatements {
    
    private final String cpFromInstanceAndDepthQuery = 
        "SELECT cp.valid as valid "
        + "FROM %s.CP_CLASS as cp_class, %s.CP as cp, %s.DOCUMENT as document "
        + "where document.id = cp_class.document and cp.cp_class = cp_class.id and document.id = ? and cp.\"to\" >= ? and ? >= cp.\"from\" and cp_class.depth = ?";
    
    private final String lcpFromInstanceAndDepthQuery = 
        "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id  "
        + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and lcp.id = ? and lcp.\"to\" >= ? and ? >= lcp.\"from\" and lcp.LCP_CLASS = lcp_class.ID and lcp_class.depth = ?";
    
    private final String documentHeaderQuery = 
        "SELECT document.header "
        + "FROM %s.document as document where document.id = ?";
    
    private final String namespacesFromDocumentQuery = 
        "SELECT namespace.prefix as prefix, namespace.uri as uri, lcp_namespace.node_id as node_id "
        + "FROM %s.namespace as namespace, %s.lcp_namespace as lcp_namespace where document_id = ? and namespace.id = lcp_namespace.namespace_id";
    
    
    public PreparedStatement getDocumentHeaderStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(documentHeaderQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getCpFromInstanceAndDepthStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(cpFromInstanceAndDepthQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getLcpFromInstanceAndDepthStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(lcpFromInstanceAndDepthQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getNamespacesFromDocumentStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(namespacesFromDocumentQuery, schema, schema, schema);
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }
    
}
