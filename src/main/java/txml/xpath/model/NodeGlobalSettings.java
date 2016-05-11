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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import txml.database.DbApi;
import txml.TXmlException;

public class NodeGlobalSettings {
    
    private final Connection connection;
    private final String schemaName;    
    private final String documentName;
    private final DbApi dbApi;
    private final Boolean sort;
    
    public NodeGlobalSettings(Connection connection, String schemaName, String documentName, DbApi dbApi, Boolean sort) {
        this.connection = connection;
        this.schemaName = schemaName;
        this.documentName = documentName;
        this.dbApi = dbApi;
        this.sort = sort;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public DbApi getDbApi() {
        return dbApi;
    }

    public boolean isSort() {
        return sort;
    }
    
    public Long getDocumentId() throws SQLException {
        Long documentId;
        try (PreparedStatement preparedStatement = this.dbApi.getSelectDocumentStatement(connection, schemaName, documentName); ResultSet resultSet = preparedStatement.executeQuery()) {
            if (!resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                throw new TXmlException("Unknown document name: " + documentName);
            }   documentId = resultSet.getLong("id");
        }
        
        return documentId;
    }
}
