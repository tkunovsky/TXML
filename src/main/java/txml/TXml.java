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

import txml.database.DbApi;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import txml.parser.*;
import javax.xml.parsers.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.xml.stream.XMLStreamException;
import txml.interpreter.InstructionsInterpreter;
import txml.load.model.XmlDataModel;
import txml.load.XmlDocumentReader;
import txml.snapshot.TXMLEventReader;

/**
 *
 * This is the main class for TXml library.  It provides
 * API for document store, XPath query, document modifications
 * and read of documents.
 * 
 * @author Tomas Kunovsky
 */
public class TXml {

    private final DbApi sqlStatements = new DbApi();
    
    /**
     * Creates database schema with empty tables for future documents.
     * @param connection connection of target database
     * @param schema schema name
     * @throws SQLException
     */
    public void initSchema(Connection connection, String schema) throws SQLException {
        sqlStatements.initSchema(connection, schema);
    }
    
    /**
     * Removes schema and its documents from database.
     * @param connection connection of target database
     * @param schema schema name
     * @throws SQLException
     */
    public void deinitSchema(Connection connection, String schema) throws SQLException {
        sqlStatements.deinitSchema(connection, schema);
    }
    
    /**
     * Interprets source code <code>code</code>.
     * @param code source code for interpreter
     * @return XPath result or snapshot result or empty result
     * @throws TXmlException
     * @throws SQLException
     */
    public TXmlResult eval(String code) throws TXmlException, SQLException {
        return eval(code, null, true);
    }
    
    /**
     * Interprets source code <code>code</code>.
     * @param code source code for interpreter
     * @param connection connection of target database
     * @return XPath result or snapshot result or empty result
     * @throws TXmlException
     * @throws SQLException
     */
    public TXmlResult eval(String code, Connection connection) throws TXmlException, SQLException {
        return eval(code, connection, true);
    }
    
    /**
     * Interprets source code <code>code</code>.
     * @param code source code for interpreter
     * @param sort true for sorted XPath result
     * @return XPath result or snapshot result or empty result
     * @throws TXmlException
     * @throws SQLException
     */
    public TXmlResult eval(String code, Boolean sort) throws TXmlException, SQLException {
        return eval(code, null, sort);
    }
      
    /**
     * Interprets source code <code>code</code>.
     * @param code source code for interpreter
     * @param connection connection of target database
     * @param sort true for sorted XPath result
     * @return XPath result or snapshot result or empty result
     * @throws TXmlException
     * @throws SQLException
     */
    public TXmlResult eval(String code, Connection connection, Boolean sort) throws TXmlException, SQLException {
        InstructionsInterpreter instructionsInterpreter = null;
        try {
            if (connection != null) connection.setAutoCommit(false);
            Class.forName("org.postgresql.Driver");
            Class.forName("org.h2.Driver");
            XQueryErrorListener xQueryErrorListener = new XQueryErrorListener();

            // Get our lexer
            XQueryLexer lexer = new XQueryLexer(new ANTLRInputStream(code));
            lexer.removeErrorListeners();
            lexer.addErrorListener(xQueryErrorListener);

            // Get a list of matched tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Pass the tokens to the parser
            XQueryParser parser = new XQueryParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(xQueryErrorListener);

            // Specify our entry point
            XQueryParser.MainContext mainContext = parser.main();

            // Walk it and attach our listener
            ParseTreeWalker walker = new ParseTreeWalker();
            instructionsInterpreter = new InstructionsInterpreter(connection, sqlStatements, sort);
            XQueryInterpreter listener = new XQueryInterpreter(instructionsInterpreter);
            walker.walk(listener, mainContext);
            instructionsInterpreter.getConnection().commit();
        } catch(Exception ex) {
            if (instructionsInterpreter != null && instructionsInterpreter.getConnection() != null) {
                instructionsInterpreter.getConnection().rollback();
                System.err.println("DB ROLLBACK");
            }
            throw new TXmlException(ex.getLocalizedMessage());
        } finally {
            if ((instructionsInterpreter != null) && instructionsInterpreter.isNewConnection() && instructionsInterpreter.getResult() == null) {
                instructionsInterpreter.getConnection().close();
            }
        }
        
        if (instructionsInterpreter != null)
            return instructionsInterpreter.getResult();
        else 
            return null;
    }
    
    /**
     * Returns XML document valided in time <code>time</code>. 
     * @see XMLEventReader
     * 
     * @param connection connection of target database
     * @param schema schema name
     * @param documentName document name in database
     * @param time time instance formatted as "d. M. yyyy H:mm:ss"
     * @return document snapshot
     * @throws SQLException
     */
    public XMLEventReader getSnapshot(Connection connection, String schema, String documentName, String time) throws SQLException {
        return getSnapshot(connection, schema, documentName, time, null);
    }
    
    /**
     * Returns XML document valided in time <code>time</code>. 
     * @see XMLEventReader
     * 
     * @param connection connection of target database
     * @param schema schema name
     * @param documentName document name in database
     * @param time time instance formatted as <code>timeFormat</code>
     * @param timeFormat time format for <code>time</code>
     * @return document snapshot
     * @throws SQLException
     */
    public XMLEventReader getSnapshot(Connection connection, String schema, String documentName, String time, String timeFormat) throws SQLException {
        if (connection == null) {
            throw new TXmlException("No database connection");
        }
        DbApi sqlStatements = new DbApi();
        
        if (timeFormat != null) {
            sqlStatements.setTimeFormat(new SimpleDateFormat(timeFormat));
        }
        
        Long documentId;
        try (PreparedStatement preparedStatement = sqlStatements.getSelectDocumentStatement(connection, schema, documentName); 
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                documentId = resultSet.getLong("id");
            } else {
                resultSet.close();
                throw new TXmlException("unknown document name: '" + documentName + "'");
            }
            resultSet.close();
        }
        
        String xmlHeader;
        try (PreparedStatement preparedStatement = sqlStatements.getDocumentHeaderStatement(connection, schema, documentId); 
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                xmlHeader = resultSet.getString("header");
            } else {
                resultSet.close();
                throw new TXmlException("unknown document id: '" + documentId + "'");
            }
            resultSet.close();
        }
        
        TXMLEventReader reader = new TXMLEventReader(connection, schema, documentId, documentName, sqlStatements, sqlStatements.convertStringToTimestamp(time).getTime(), xmlHeader);;
        return reader;
    }
    
    /**
     * Stores document to database.
     * @param connection connection of target database
     * @param schema database schema with inicialized tables
     * @param documentName document name in database
     * @param filename path of document file
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws SQLException
     * @throws XMLStreamException
     */
    public void loadDocumentToDb(Connection connection, String schema, String documentName, String filename) throws IOException, FileNotFoundException, ParserConfigurationException, SQLException, XMLStreamException {
        sqlStatements.refreshCurrentTime();
        XmlDocumentReader reader = new XmlDocumentReader();
        XmlDataModel dataModel = reader.loadDocumentToDb(filename);
        dataModel.setDocumentName(documentName);
        sqlStatements.loadDocumentToDb(connection, schema, dataModel);
    }
    
    /**
     * Stores document to database.
     * @param connection connection of target database
     * @param schema database schema with inicialized tables
     * @param documentName document name in database
     * @param in content of document
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws SQLException
     * @throws XMLStreamException
     */
    public void loadDocumentToDb(Connection connection, String schema, String documentName, InputStream in) throws IOException, FileNotFoundException, ParserConfigurationException, SQLException, XMLStreamException {
        sqlStatements.refreshCurrentTime();
        XmlDocumentReader reader = new XmlDocumentReader();
        XmlDataModel dataModel = reader.loadDocumentToDb(in);
        dataModel.setDocumentName(documentName);
        sqlStatements.loadDocumentToDb(connection, schema, dataModel);
    }
    
    /**
     * Interprets source code from file (needs file path in args[0]).  
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("TXML.jar <file with code>");
            return;
        }
        TXml tXml = new TXml();

        try {
            String content = readFile(args[0], Charset.forName("UTF-8"));
            TXmlResult result = tXml.eval(content);
            if (!result.isEmpty()) {
                if (result.isNodeList()) {
                    System.out.println(result.asNodeList().getTrees(result.asNodeList().isSorted()));
                    result.asNodeList().closeDbConnection();
                } else if (result.isXMLEventReader()) {
                    System.out.println(result.asXMLEventReader().getXMLDocFormatA());
                    result.asXMLEventReader().closeDbConnection();
                }
            }
        } catch (IOException | TXmlException | SQLException ex) {
            System.err.println("Error:" + "\n" + ex.getLocalizedMessage());
        }
    }
                
    Long lastTimeChanges() {
        return this.sqlStatements.getCurrTimeAsLong();
    }
    
    private static String readFile(String path, Charset encoding) throws IOException 
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
}
