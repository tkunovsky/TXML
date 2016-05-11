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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadDocumentToDbStatements {
    private long documentId;
    
    private PreparedStatement nextValDocumentIdSequenceStatement = null;
    private PreparedStatement nextValLCPClassIdSequenceStatement = null;
    private PreparedStatement nextValCPClassIdSequenceStatement = null;
    private PreparedStatement nextValNodeIdSequenceStatement = null;
    private PreparedStatement nextValNamespaceIdSequenceStatement = null;
    
    private PreparedStatement selectIdLCPClassStatement = null;
    private PreparedStatement selectIdCPClassStatement = null;
    private PreparedStatement selectPkCPStatement = null;
    private PreparedStatement selectRootIdLCPClassStatement = null;
    private PreparedStatement selectIdNamespaceStatement = null;
    
    private PreparedStatement insertLCPClassTableStatement = null;
    private PreparedStatement insertCPClassTableStatement = null;
    private PreparedStatement insertDocumentTableStatement = null;
    private PreparedStatement insertCPTableStatement = null;
    private PreparedStatement insertLCPTableStatement = null;
    private PreparedStatement insertNamespaceTableStatement = null;
    private PreparedStatement insertLcpNamespaceTableStatement = null;
    
    private PreparedStatement updateValidCPStatement = null;
    
    private String nextValDocumentIdSequenceQuery;
    private String nextValLCPClassIdSequenceQuery;
    private String nextValCPClassIdSequenceQuery;
    private String nextValNodeIdSequenceQuery;
    private String nextValNamespaceIdSequenceQuery;
    
    private final String selectIdLCPClassQuery = "SELECT id from %s.lcp_class where type=? and local_part=? and depth=? and document=? and parent = ? and namespace_id = ? LIMIT 1";
    private final String selectRootIdLCPClassQuery = "SELECT id from %s.lcp_class where type=? and local_part=? and depth=? and document=? and parent is null and namespace_id = ? LIMIT 1";
    private final String selectPkCPQuery = "SELECT valid from %s.cp where \"from\"=? and \"to\"=? and cp_class=? LIMIT 1";
    private final String selectIdCPClassQuery = "SELECT id from %s.cp_class where depth=? and document=? LIMIT 1";
    private final String selectIdNamespaceQuery = "SELECT id from %s.namespace where uri=? and prefix=? and document_id=? LIMIT 1";
    
    private final String updateValidCPQuery = "UPDATE %s.cp SET valid = ? where \"from\" = ? and \"to\" = ? and cp_class = ?";

    private final String insertLCPClassTableQuery = "INSERT INTO %s.LCP_Class (id, type, local_part, depth, parent, document, namespace_id) VALUES (?,?,?,?,?,?,?)";
    private final String insertCPClassTableQuery = "INSERT INTO %s.CP_Class (id, depth, parent, document) VALUES (?,?,?,?)";
    private final String insertDocumentTableQuery = "INSERT INTO %s.Document (id, name, header) VALUES (?,?,?)";    
    private final String insertCPTableQuery = "INSERT INTO %s.CP (\"from\", \"to\", valid, CP_Class) VALUES (?,?,?,?)";    
    private final String insertLCPTableQuery = "INSERT INTO %s.LCP (id, \"from\", \"to\", parentId, parentFrom, parentTo, value, LCP_Class) VALUES (?,?,?,?,?,?,?,?)";  
    private final String insertNamespaceTableQuery = "INSERT INTO %s.namespace (id, uri, prefix, document_id) VALUES (?,?,?,?)"; 
    private final String insertLcpNamespaceTableQuery = "INSERT INTO %s.lcp_namespace (node_id, node_from, node_to, namespace_id) VALUES (?,?,?,?)"; 
    
    private DatabaseEnum databaseEnum;

    public DatabaseEnum getDatabaseEnum() {
        return databaseEnum;
    }
    
    void init(String schema, Connection connection) throws SQLException {
        databaseEnum = DatabaseEnum.getDatabase(connection);
        
        if (databaseEnum == DatabaseEnum.POSTGRE) {
            nextValDocumentIdSequenceQuery = "SELECT nextval('%s.document_id_seq') as document_id";
            nextValLCPClassIdSequenceQuery = "SELECT nextval('%s.lcp_class_id_seq') as lcp_class_id";
            nextValCPClassIdSequenceQuery = "SELECT nextval('%s.cp_class_id_seq') as cp_class_id";
            nextValNodeIdSequenceQuery = "SELECT nextval('%s.node_id_seq') as node_id";
            nextValNamespaceIdSequenceQuery = "SELECT nextval('%s.namespace_id_seq') as namespace_id";
        } else {
            nextValDocumentIdSequenceQuery = "SELECT %s.document_id_seq.NEXTVAL as document_id FROM dual";
            nextValLCPClassIdSequenceQuery = "SELECT %s.lcp_class_id_seq.NEXTVAL as lcp_class_id FROM dual";
            nextValCPClassIdSequenceQuery = "SELECT %s.cp_class_id_seq.NEXTVAL as cp_class_id FROM dual";
            nextValNodeIdSequenceQuery = "SELECT %s.node_id_seq.NEXTVAL as node_id FROM dual";
            nextValNamespaceIdSequenceQuery = "SELECT %s.namespace_id_seq.NEXTVAL as namespace_id FROM dual";
        }
        
        selectIdLCPClassStatement = connection.prepareStatement(String.format(selectIdLCPClassQuery, schema));
        selectIdCPClassStatement = connection.prepareStatement(String.format(selectIdCPClassQuery, schema));
        selectRootIdLCPClassStatement = connection.prepareStatement(String.format(selectRootIdLCPClassQuery, schema));
        selectPkCPStatement = connection.prepareStatement(String.format(selectPkCPQuery, schema));
        selectIdNamespaceStatement = connection.prepareStatement(String.format(selectIdNamespaceQuery, schema));
        nextValDocumentIdSequenceStatement = connection.prepareStatement(String.format(nextValDocumentIdSequenceQuery, schema));
        nextValLCPClassIdSequenceStatement = connection.prepareStatement(String.format(nextValLCPClassIdSequenceQuery, schema)); 
        nextValCPClassIdSequenceStatement = connection.prepareStatement(String.format(nextValCPClassIdSequenceQuery, schema));
        nextValNodeIdSequenceStatement = connection.prepareStatement(String.format(nextValNodeIdSequenceQuery, schema));
        nextValNamespaceIdSequenceStatement = connection.prepareStatement(String.format(nextValNamespaceIdSequenceQuery, schema));
        insertLCPClassTableStatement = connection.prepareStatement(String.format(insertLCPClassTableQuery, schema));
        insertCPClassTableStatement = connection.prepareStatement(String.format(insertCPClassTableQuery, schema));
        insertDocumentTableStatement = connection.prepareStatement(String.format(insertDocumentTableQuery, schema));
        insertCPTableStatement = connection.prepareStatement(String.format(insertCPTableQuery, schema));
        insertLCPTableStatement = connection.prepareStatement(String.format(insertLCPTableQuery, schema));
        insertNamespaceTableStatement = connection.prepareStatement(String.format(insertNamespaceTableQuery, schema));
        insertLcpNamespaceTableStatement = connection.prepareStatement(String.format(insertLcpNamespaceTableQuery, schema));
        updateValidCPStatement = connection.prepareStatement(String.format(updateValidCPQuery, schema));
        
        ResultSet documentIdRs = nextValDocumentIdSequenceStatement.executeQuery();
        documentIdRs.next();
        documentId = documentIdRs.getLong("document_id");
    }
    
    public void closeAll() throws SQLException {
        if (selectIdLCPClassStatement != null) {
            selectIdLCPClassStatement.close();
        }
        
        if (selectIdCPClassStatement != null) {
            selectIdCPClassStatement.close();
        }
        
        if (selectPkCPStatement != null) {
            selectPkCPStatement.close();
        }
        
        if (selectIdNamespaceStatement != null) {
            selectIdNamespaceStatement.close();
        }
        
        if (nextValLCPClassIdSequenceStatement != null) {
            nextValLCPClassIdSequenceStatement.close();
        }
        
        if (nextValCPClassIdSequenceStatement != null) {
            nextValCPClassIdSequenceStatement.close();
        }
        
        if (nextValNodeIdSequenceStatement != null) {
            nextValNodeIdSequenceStatement.close();
        }
        
        if (nextValNamespaceIdSequenceStatement != null) {
            nextValNamespaceIdSequenceStatement.close();
        }
        
        if (insertLCPClassTableStatement != null) {
            insertLCPClassTableStatement.close();
        }
        
        if (insertCPClassTableStatement != null) {
            insertCPClassTableStatement.close();
        }
        
        if (insertDocumentTableStatement != null) {
            insertDocumentTableStatement.close();
        }
        
        if (insertCPTableStatement != null) {
            insertCPTableStatement.close();
        }
        
        if (insertLCPTableStatement != null) {
            insertLCPTableStatement.close();
        }
        
        if (insertNamespaceTableStatement != null) {
            insertNamespaceTableStatement.close();
        }
        
        if (insertLcpNamespaceTableStatement != null) {
            insertLcpNamespaceTableStatement.close();
        }
        
        if (updateValidCPStatement != null) {
            updateValidCPStatement.close();
        }
        
        if (selectRootIdLCPClassStatement != null) {
            selectRootIdLCPClassStatement.close();
        }
        
    }    
    
    public PreparedStatement getNextValDocumentIdSequenceStatement() {
        return nextValDocumentIdSequenceStatement;
    }

    public PreparedStatement getSelectIdLCPClassStatement() {
        return selectIdLCPClassStatement;
    }

    public PreparedStatement getSelectIdCPClassStatement() {
        return selectIdCPClassStatement;
    }

    public PreparedStatement getNextValLCPClassIdSequenceStatement() {
        return nextValLCPClassIdSequenceStatement;
    }

    public PreparedStatement getNextValCPClassIdSequenceStatement() {
        return nextValCPClassIdSequenceStatement;
    }

    public PreparedStatement getInsertLCPClassTableStatement() {
        return insertLCPClassTableStatement;
    }

    public PreparedStatement getInsertCPClassTableStatement() {
        return insertCPClassTableStatement;
    }

    public PreparedStatement getInsertDocumentTableStatement() {
        return insertDocumentTableStatement;
    }

    public PreparedStatement getNextValNodeIdSequenceStatement() {
        return nextValNodeIdSequenceStatement;
    }

    public PreparedStatement getSelectPkCPStatement() {
        return selectPkCPStatement;
    }

    public PreparedStatement getInsertCPTableStatement() {
        return insertCPTableStatement;
    }

    public PreparedStatement getInsertLCPTableStatement() {
        return insertLCPTableStatement;
    }

    public PreparedStatement getUpdateValidCPStatement() {
        return updateValidCPStatement;
    }

    public PreparedStatement getSelectRootIdLCPClassStatement() {
        return selectRootIdLCPClassStatement;
    }

    public PreparedStatement getNextValNamespaceIdSequenceStatement() {
        return nextValNamespaceIdSequenceStatement;
    }

    public PreparedStatement getSelectIdNamespaceStatement() {
        return selectIdNamespaceStatement;
    }

    public PreparedStatement getInsertNamespaceTableStatement() {
        return insertNamespaceTableStatement;
    }

    public PreparedStatement getInsertLcpNamespaceTableStatement() {
        return insertLcpNamespaceTableStatement;
    }

    public long getDocumentId() {
        return documentId;
    }
    
}
