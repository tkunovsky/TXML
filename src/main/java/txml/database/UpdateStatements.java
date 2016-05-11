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

public class UpdateStatements {
    private final String cpFromIntervalAndDepthQuery = 
            "SELECT * "
            + "FROM %s.CP_CLASS as cp_class, %s.CP as cp, %s.DOCUMENT as document "
            + "where document.id = cp_class.document and cp.cp_class = cp_class.id and document.name = ? and cp.\"to\" = ? and depth = ?";
    
    private final String cpFromCPClassAndToQuery = 
            "SELECT * "
            + "FROM %s.CP as cp "
            + "where \"to\" = ? and cp_class = ?";
    
    private final String splitCpIntervalUpdateQuery = 
            "update %s.cp as cp set \"to\" = ? "
            + "where \"from\" = ? and \"to\" = ? and cp_class = ?" ;
    
    private final String validUpdateQuery = 
            "update %s.cp as cp set valid = ? "
            + "where \"from\" = ? and \"to\" = ? and cp_class = ?" ;
    
    private final String toLcpUpdateQuery = 
            "update %s.LCP as lcp \n" +
            "SET\n" +
            "  \"to\" = CASE \n" +
            "    WHEN id = ? THEN ?\n" +
            "    else \"to\"\n" +
            "  end,\n" +
            "parentTo = CASE \n" +
            "    WHEN parentId = ? THEN ?\n" +
            "    else parentTo\n" +
            "  end\n" +
            "where ((id = ? and \"to\" = ?) or (parentId = ? and parentTo = ?))";
    
    private final String toLcpUpdateWithFromQuery = 
            "update %s.LCP as lcp \n" +
            "SET\n" +
            "  \"to\" = CASE \n" +
            "    WHEN id = ? THEN ?\n" +
            "    else \"to\"\n" +
            "  end,\n" +
            "parentTo = CASE \n" +
            "    WHEN parentId = ? THEN ?\n" +
            "    else parentTo\n" +
            "  end\n" +
            "where ((id = ? and \"from\" = ? and \"to\" = ?) or (parentId = ? and parentFrom = ? and parentTo = ?))";
    
    private final String parentLcpUpdateWithFromQuery = 
            "update %s.LCP as lcp \n" +
            "SET\n" +
            "  parentId = ?, " +
            "  parentFrom = ?, " +
            "  parentTo = ?, " +
            "  lcp_class = ? " +
            "where id = ? and \"from\" = ? and \"to\" = ?";

    private final String lcpDeleteQuery = 
            "delete from %s.LCP where id = ? and \"to\" = ?" ;
    
    private final String insertCPTableQuery = "INSERT INTO %s.CP (\"from\", \"to\", valid, CP_Class) VALUES (?,?,?,?)";
    
    
    private final String selectLCPClassWithoutIdQuery = "Select * from %s.LCP_CLASS as lcp_class, %s.namespace as namespace where namespace.id = lcp_class.namespace_id and local_part = ? and namespace_id = ? and depth = ? and parent = ? and type = ?";
    private final String selectDocumentQuery = "Select * from %s.document as document where document.name = ?";
    
    private final String insertLCPClassTableQuery = "INSERT INTO %s.LCP_Class (id, type, local_part, namespace_id, depth, parent, document) VALUES (?,?,?,?,?,?,?)";
    private final String insertCPClassTableQuery = "INSERT INTO %s.CP_Class (id, depth, parent, document) VALUES (?,?,?,?)";
    private final String insertLCPTableQuery = "INSERT INTO %s.LCP (id, \"from\", \"to\", parentId, parentFrom, parentTo, value, LCP_Class) VALUES (?,?,?,?,?,?,?,?)"; 
    
    private final String selectCPClassWithoutIdQuery = "Select * from %s.CP_CLASS as cp_class where cp_class.depth = ? and cp_class.document = ?";
        
    private final String nodesOfParentsQuery = 
            "SELECT * FROM %s.LCP as lcp where lcp.parentId in %s and lcp.parentTo = ? and lcp.\"to\" = ?";
    
    private final String nodesOfParentQuery = 
            "SELECT * FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class where lcp.lcp_class = lcp_class.id and lcp.parentId = ? and lcp.parentTo = ? and lcp.\"to\" = ? and lcp_class.type in %s";
    
    private final String lastNodeQuery = 
            "SELECT lcp.id as id, lcp.\"from\" as \"from\", lcp.\"to\" as to, lcp.parentId as parentId, lcp.parentFrom as parentFrom, lcp.parentTo as parentTo, lcp.value as value, lcp_class.depth as depth, lcp.lcp_class as lcp_class, lcp_class.type as type, lcp_class.local_part as local_part, namespace.prefix as prefix, namespace.uri as uri, namespace.id as namespace_id  "
            + "FROM %s.LCP as lcp, %s.LCP_CLASS as lcp_class, %s.namespace as namespace where lcp_class.namespace_id = namespace.id and lcp.id = ? and lcp.LCP_CLASS = lcp_class.ID order by lcp.\"to\" desc limit 1";

    public PreparedStatement getLastNodeStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(lastNodeQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getNodesOfParentsStatement(Connection connection, String schema, String parents) throws SQLException {
        String query = String.format(nodesOfParentsQuery, schema, parents);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getNodesOfParentStatement(Connection connection, String schema, String types) throws SQLException {
        String query = String.format(nodesOfParentQuery, schema, schema, types);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getCpFromCPClassAndToStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(cpFromCPClassAndToQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }

    public PreparedStatement getCpClassWithoutIdStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(selectCPClassWithoutIdQuery, schema);
        return connection.prepareStatement(query);
    }

    public PreparedStatement getSelectDocumentStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(selectDocumentQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getInsertLCPClassTableStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(insertLCPClassTableQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getInsertCPClassTableStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(insertCPClassTableQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getNextValNodeIdSequenceStatement(Connection connection, String schema) throws SQLException {
        String nextValNodeIdSequenceQuery;
        if (DatabaseEnum.getDatabase(connection) == DatabaseEnum.POSTGRE) {
            nextValNodeIdSequenceQuery = "SELECT nextval('%s.node_id_seq') as node_id";
        } else {
            nextValNodeIdSequenceQuery = "SELECT %s.node_id_seq.NEXTVAL as node_id FROM dual";
        }
        
        String query = String.format(nextValNodeIdSequenceQuery, schema);
        
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getNextValCPClassIdSequenceStatement(Connection connection, String schema) throws SQLException { 
        String nextValCPClassIdSequenceQuery;
        
        if (DatabaseEnum.getDatabase(connection) == DatabaseEnum.POSTGRE) {
            nextValCPClassIdSequenceQuery = "SELECT nextval('%s.cp_class_id_seq') as cp_class_id";
        } else {
            nextValCPClassIdSequenceQuery = "SELECT %s.cp_class_id_seq.NEXTVAL as cp_class_id FROM dual";
        }
                
        String query = String.format(nextValCPClassIdSequenceQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getNextValLCPClassIdSequenceStatement(Connection connection, String schema) throws SQLException {
        String nextValLCPClassIdSequenceQuery;
        
        if (DatabaseEnum.getDatabase(connection) == DatabaseEnum.POSTGRE) {
            nextValLCPClassIdSequenceQuery = "SELECT nextval('%s.lcp_class_id_seq') as lcp_class_id";
        } else {
            nextValLCPClassIdSequenceQuery = "SELECT %s.lcp_class_id_seq.NEXTVAL as lcp_class_id FROM dual";
        }
        
        String query = String.format(nextValLCPClassIdSequenceQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getLcpClassWithoutIdStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(selectLCPClassWithoutIdQuery, schema, schema);
        return connection.prepareStatement(query);
    }

    public PreparedStatement getCpFromIntervalAndDepthStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(cpFromIntervalAndDepthQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getSplitCpIntervalUpdateStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(splitCpIntervalUpdateQuery, schema, schema, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getInsertCPTableStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(insertCPTableQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getInsertLCPTableStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(insertLCPTableQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getValidUpdateStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(validUpdateQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getToLcpUpdateStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(toLcpUpdateQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getToLcpUpdateWithFromStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(toLcpUpdateWithFromQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getParentLcpUpdateWithFromStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(parentLcpUpdateWithFromQuery, schema);
        return connection.prepareStatement(query);
    }
    
    public PreparedStatement getLcpDeleteStatement(Connection connection, String schema) throws SQLException {
        String query = String.format(lcpDeleteQuery, schema);
        return connection.prepareStatement(query);
    }
    
}
