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

public class InitAndDeinitStatements {
    private final String createSchemaQuery = "CREATE SCHEMA %s";
    
    /*private final String dropTableDocumentQuery = "DROP TABLE IF EXISTS %s.document cascade";
    private final String dropTableLcpClassQuery = "DROP TABLE IF EXISTS %s.lcp_class cascade";
    private final String dropTableCpClassQuery = "DROP TABLE IF EXISTS %s.cp_class cascade";
    private final String dropTableNamespaceQuery = "DROP TABLE IF EXISTS %s.namespace cascade";
    private final String dropTableLcpQuery = "DROP TABLE IF EXISTS %s.lcp cascade";
    private final String dropTableCpQuery = "DROP TABLE IF EXISTS %s.cp cascade";
    private final String dropTableLcpNamespaceQuery = "DROP TABLE IF EXISTS %s.lcp_namespace cascade";
    
    private final String dropNodeIdSequenceQuery = "drop SEQUENCE IF EXISTS %s.node_id_seq";
    private final String dropCPIdSequenceQuery = "drop SEQUENCE IF EXISTS %s.cp_id_seq";
    private final String dropCPClassIdSequenceQuery = "drop SEQUENCE IF EXISTS %s.cp_class_id_seq";
    private final String dropLCPClassIdSequenceQuery = "drop SEQUENCE IF EXISTS %s.lcp_class_id_seq";
    private final String dropDocumentIdSequenceQuery = "drop SEQUENCE IF EXISTS %s.document_id_seq";
    private final String dropNamespaceIdSequenceQuery = "drop SEQUENCE IF EXISTS %s.namespace_id_seq";*/
    
    private final String createCPClassTableQuery = "CREATE TABLE %s.CP_Class(id bigint primary key, depth int not null, parent bigint, document bigint not null)";
    private final String createLCPTableQuery = "CREATE TABLE %s.LCP(id bigint, \"from\" bigint not null, \"to\" bigint not null, parentId bigint, parentFrom bigint, parentTo bigint, LCP_Class bigint not null, value text, CONSTRAINT pk_lcp PRIMARY KEY (id, \"from\", \"to\"), CONSTRAINT chk_2 CHECK (parentFrom <= \"from\" AND \"to\" <= parentTo))";
    private final String createLCPClassTableQuery = "CREATE TABLE %s.LCP_Class(id bigint primary key, type SMALLINT not null, local_part varchar(64) not null, depth int not null, parent bigint, document bigint not null, namespace_id bigint)";
    private final String createDocumentTableQuery = "CREATE TABLE %s.Document(id bigint primary key, name varchar(64) not null unique, header text)";
    private final String createNamespaceTableQuery = "CREATE TABLE %s.Namespace(id bigint primary key, uri varchar(64), prefix varchar(32), document_id bigint not null)";
    private final String createLcpNamespaceTableQuery = "CREATE TABLE %s.lcp_namespace(node_id bigint, node_from bigint, node_to bigint, namespace_id bigint, CONSTRAINT pk_lcp_namespace PRIMARY KEY (node_id, node_from, node_to, namespace_id))";
    
    private final String createNodeIdSequenceQuery = "CREATE SEQUENCE %s.node_id_seq";
    private final String createCPIdSequenceQuery = "CREATE SEQUENCE %s.cp_id_seq";
    private final String createCPClassIdSequenceQuery = "CREATE SEQUENCE %s.cp_class_id_seq";
    private final String createLCPClassIdSequenceQuery = "CREATE SEQUENCE %s.lcp_class_id_seq";
    private final String createDocumentIdSequenceQuery = "CREATE SEQUENCE %s.document_id_seq";
    private final String createNamespaceIdSequenceQuery = "CREATE SEQUENCE %s.namespace_id_seq";

    private final String alterCPClassCPTableQuery = "ALTER TABLE %s.cp ADD FOREIGN KEY (CP_Class) REFERENCES %s.CP_Class(id)";
    private final String alterLCPClassLCPTableQuery = "ALTER TABLE %s.lcp ADD FOREIGN KEY (LCP_Class) REFERENCES %s.LCP_Class(id)";
    private final String alterParentLCPTableQuery = "ALTER TABLE %s.lcp ADD FOREIGN KEY (parentId, parentFrom, parentTo) REFERENCES %s.LCP(id, \"from\", \"to\")";
    private final String alterDocumentCPClassTableQuery = "ALTER TABLE %s.cp_class ADD FOREIGN KEY (document) REFERENCES %s.Document(id)";
    private final String alterDocumentLCPClassTableQuery = "ALTER TABLE %s.lcp_class ADD FOREIGN KEY (document) REFERENCES %s.Document(id)";
    private final String alterParentLCPClassTableQuery = "ALTER TABLE %s.lcp_class ADD FOREIGN KEY (parent) REFERENCES %s.lcp_class(id)";
    private final String alterParentCPClassTableQuery = "ALTER TABLE %s.cp_class ADD FOREIGN KEY (parent) REFERENCES %s.cp_class(id)";
    private final String alterNamespaceLCPClassTableQuery = "ALTER TABLE %s.lcp_class ADD FOREIGN KEY (namespace_id) REFERENCES %s.namespace(id)";
    private final String alterLcpNamespaceFkNamespaceTableQuery = "ALTER TABLE %s.lcp_namespace ADD FOREIGN KEY (namespace_id) REFERENCES %s.namespace(id)";
    private final String alterLcpNamespaceFkLcpTableQuery = "ALTER TABLE %s.lcp_namespace ADD FOREIGN KEY (node_id, node_from, node_to) REFERENCES %s.lcp(id, \"from\", \"to\")";
    private final String alterNamespaceTableQuery = "ALTER TABLE %s.namespace ADD FOREIGN KEY (document_id) REFERENCES %s.document(id)";
    
    void initSchema(Connection connection, String schema) throws SQLException {
        DatabaseEnum databaseEnum = DatabaseEnum.getDatabase(connection);
        String createCPTableQuery;
        if (databaseEnum == DatabaseEnum.H2) {
            createCPTableQuery = "CREATE TABLE %s.CP(\"from\" bigint not null, \"to\" bigint not null, valid array not null, CP_Class bigint not null, CONSTRAINT pk_cp PRIMARY KEY (\"from\", \"to\", CP_Class))";
        } else {
            createCPTableQuery = "CREATE TABLE %s.CP(\"from\" bigint not null, \"to\" bigint not null, valid bigint array not null, CP_Class bigint not null, CONSTRAINT pk_cp PRIMARY KEY (\"from\", \"to\", CP_Class))";
        }

        deinitSchema(connection, schema);              
        try (PreparedStatement createSchemaStatement = connection.prepareStatement(String.format(createSchemaQuery, schema))) {
            createSchemaStatement.executeUpdate();
        }

        try (PreparedStatement createCPTableStatement = connection.prepareStatement(String.format(createCPTableQuery, schema))) {
            createCPTableStatement.executeUpdate();
        }    

        try (PreparedStatement createCPClassTableStatement = connection.prepareStatement(String.format(createCPClassTableQuery, schema))) {
            createCPClassTableStatement.executeUpdate();
        } 
        
        try (PreparedStatement createLCPTableStatement = connection.prepareStatement(String.format(createLCPTableQuery, schema))) {
            createLCPTableStatement.executeUpdate();
        }
        
        try (PreparedStatement createLCPClassTableStatement = connection.prepareStatement(String.format(createLCPClassTableQuery, schema))) {
            createLCPClassTableStatement.executeUpdate();
        }
        
        try (PreparedStatement createDocumentTableStatement = connection.prepareStatement(String.format(createDocumentTableQuery, schema))) {
            createDocumentTableStatement.executeUpdate();
        }

        try (PreparedStatement createNamespaceTableStatement = connection.prepareStatement(String.format(createNamespaceTableQuery, schema))) {
            createNamespaceTableStatement.executeUpdate();
        }
        

        try (PreparedStatement createLcpNamespaceTableStatement = connection.prepareStatement(String.format(createLcpNamespaceTableQuery, schema))) {
            createLcpNamespaceTableStatement.executeUpdate();
        }
        
        try (PreparedStatement createNodeIdSequenceStatement = connection.prepareStatement(String.format(createNodeIdSequenceQuery, schema))) {
            createNodeIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement createCPIdSequenceStatement = connection.prepareStatement(String.format(createCPIdSequenceQuery, schema))) {
            createCPIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement createCPClassIdSequenceStatement = connection.prepareStatement(String.format(createCPClassIdSequenceQuery, schema))) {
            createCPClassIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement createLCPClassIdSequenceStatement = connection.prepareStatement(String.format(createLCPClassIdSequenceQuery, schema))) {
            createLCPClassIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement createDocumentIdSequenceStatement = connection.prepareStatement(String.format(createDocumentIdSequenceQuery, schema))) {
            createDocumentIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement createNamespaceIdSequenceStatement = connection.prepareStatement(String.format(createNamespaceIdSequenceQuery, schema))) {
            createNamespaceIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement alterCPClassCPTableStatement = connection.prepareStatement(String.format(alterCPClassCPTableQuery, schema, schema))) {
            alterCPClassCPTableStatement.executeUpdate();
        } 
        
        try (PreparedStatement alterLCPClassLCPTableStatement = connection.prepareStatement(String.format(alterLCPClassLCPTableQuery, schema, schema))) {
            alterLCPClassLCPTableStatement.executeUpdate();
        } 

        try (PreparedStatement alterParentLCPTableStatement = connection.prepareStatement(String.format(alterParentLCPTableQuery, schema, schema))) {
            alterParentLCPTableStatement.executeUpdate();
        } 
        
        try (PreparedStatement alterDocumentCPClassTableStatement = connection.prepareStatement(String.format(alterDocumentCPClassTableQuery, schema, schema))) {
            alterDocumentCPClassTableStatement.executeUpdate();
        } 
        
        try (PreparedStatement alterDocumentLCPClassTableStatement = connection.prepareStatement(String.format(alterDocumentLCPClassTableQuery, schema, schema))) {
            alterDocumentLCPClassTableStatement.executeUpdate();
        }

        try (PreparedStatement alterParentLCPClassTableStatement = connection.prepareStatement(String.format(alterParentLCPClassTableQuery, schema, schema))) {
            alterParentLCPClassTableStatement.executeUpdate();
        }
        
        try (PreparedStatement alterParentCPClassTableStatement = connection.prepareStatement(String.format(alterParentCPClassTableQuery, schema, schema))) {
            alterParentCPClassTableStatement.executeUpdate();
        }
        
        try (PreparedStatement alterNamespaceLCPClassTableStatement = connection.prepareStatement(String.format(alterNamespaceLCPClassTableQuery, schema, schema))) {
            alterNamespaceLCPClassTableStatement.executeUpdate();
        }
        
        try (PreparedStatement alterLcpNamespaceFkNamespaceTableStatement = connection.prepareStatement(String.format(alterLcpNamespaceFkNamespaceTableQuery, schema, schema))) {
            alterLcpNamespaceFkNamespaceTableStatement.executeUpdate();
        }
        
        try (PreparedStatement alterLcpNamespaceFkLcpTableStatement = connection.prepareStatement(String.format(alterLcpNamespaceFkLcpTableQuery, schema, schema))) {
            alterLcpNamespaceFkLcpTableStatement.executeUpdate();
        }
        
        try (PreparedStatement alterNamespaceTableStatement = connection.prepareStatement(String.format(alterNamespaceTableQuery, schema, schema))) {
            alterNamespaceTableStatement.executeUpdate();
        }
    }

    void deinitSchema(Connection connection, String schema) throws SQLException { 
        /*try (PreparedStatement dropTableDocumentStatement = connection.prepareStatement(String.format(dropTableDocumentQuery, schema))) {
            dropTableDocumentStatement.executeUpdate();
        }
        
        try (PreparedStatement dropTableLcpClassStatement = connection.prepareStatement(String.format(dropTableLcpClassQuery, schema))) {
            dropTableLcpClassStatement.executeUpdate();
        }
        
        try (PreparedStatement dropTableCpClassStatement = connection.prepareStatement(String.format(dropTableCpClassQuery, schema))) {
            dropTableCpClassStatement.executeUpdate();
        }
        
        try (PreparedStatement dropTableNamespaceStatement = connection.prepareStatement(String.format(dropTableNamespaceQuery, schema))) {
            dropTableNamespaceStatement.executeUpdate();
        }
        
        try (PreparedStatement dropLcpStatement = connection.prepareStatement(String.format(dropTableLcpQuery, schema))) {
            dropLcpStatement.executeUpdate();
        }
        
        try (PreparedStatement dropCpStatement = connection.prepareStatement(String.format(dropTableCpQuery, schema))) {
            dropCpStatement.executeUpdate();
        }
        
        try (PreparedStatement dropTableLcpNamespaceStatement = connection.prepareStatement(String.format(dropTableLcpNamespaceQuery, schema))) {
            dropTableLcpNamespaceStatement.executeUpdate();
        }
        
        try (PreparedStatement dropNodeIdSequenceStatement = connection.prepareStatement(String.format(dropNodeIdSequenceQuery, schema))) {
            dropNodeIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement dropCPIdSequenceStatement = connection.prepareStatement(String.format(dropCPIdSequenceQuery, schema))) {
            dropCPIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement dropCPClassIdSequenceStatement = connection.prepareStatement(String.format(dropCPClassIdSequenceQuery, schema))) {
            dropCPClassIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement dropLCPClassIdSequenceStatement = connection.prepareStatement(String.format(dropLCPClassIdSequenceQuery, schema))) {
            dropLCPClassIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement dropDocumentIdSequenceStatement = connection.prepareStatement(String.format(dropDocumentIdSequenceQuery, schema))) {
            dropDocumentIdSequenceStatement.executeUpdate();
        } 
        
        try (PreparedStatement dropNamespaceIdSequenceStatement = connection.prepareStatement(String.format(dropNamespaceIdSequenceQuery, schema))) {
            dropNamespaceIdSequenceStatement.executeUpdate();
        } */
        
        DatabaseEnum databaseEnum = DatabaseEnum.getDatabase(connection);
        String dropSchemaQuery;
        if (databaseEnum == DatabaseEnum.H2) {
            dropSchemaQuery = "DROP SCHEMA IF EXISTS %s";
        } else {
            dropSchemaQuery = "DROP SCHEMA IF EXISTS %s cascade";
        }
        
        try (PreparedStatement dropSchemaStatement = connection.prepareStatement(String.format(dropSchemaQuery, schema))) {
            dropSchemaStatement.executeUpdate();
        }
    }
    
}