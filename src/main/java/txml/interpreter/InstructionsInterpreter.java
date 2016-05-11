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
package txml.interpreter;

import txml.interpreter.command.CommandFilterNeq;
import txml.interpreter.command.CommandIteratorNext;
import txml.interpreter.command.CommandGoto;
import txml.interpreter.command.CommandDeclareOptionTimeFormat;
import txml.interpreter.command.CommandGetUndirectStep;
import txml.interpreter.command.CommandDeinitSchema;
import txml.interpreter.command.CommandEraseTXmlNodes;
import txml.interpreter.command.CommandGoBackToStartPaths;
import txml.interpreter.command.CommandGetDirectStep;
import txml.interpreter.command.CommandDeclareOptionSort;
import txml.interpreter.command.CommandFilterPosition;
import txml.interpreter.command.CommandFilterContains;
import txml.interpreter.command.CommandLabel;
import txml.interpreter.command.CommandReturnSnapshot;
import txml.interpreter.command.CommandFilterRMeets;
import txml.interpreter.command.CommandSetTNodeListInCycle;
import txml.interpreter.command.Command;
import txml.interpreter.command.CommandFilterFollows;
import txml.interpreter.command.CommandGotoIfNoNext;
import txml.interpreter.command.CommandFilterPrecedes;
import txml.interpreter.command.CommandFilterEq;
import txml.interpreter.command.CommandStoreDocument;
import txml.interpreter.command.CommandFilterLess;
import txml.interpreter.command.CommandRemoveDuplicates;
import txml.interpreter.command.CommandSetParentInDocument;
import txml.interpreter.command.CommandFilterLMeets;
import txml.interpreter.command.CommandReturnList;
import txml.interpreter.command.CommandGetValues;
import txml.interpreter.command.CommandInsertIntoDocument;
import txml.interpreter.command.CommandNewList;
import txml.interpreter.command.CommandFilterIn;
import txml.interpreter.command.CommandFilterOverlaps;
import txml.interpreter.command.CommandFilterMore;
import txml.interpreter.command.CommandInitSchema;
import txml.interpreter.command.CommandFilterGe;
import txml.interpreter.command.CommandDeclareOptionConnection;
import txml.interpreter.command.CommandParentList;
import txml.interpreter.command.CommandCheckSort;
import txml.interpreter.command.CommandSaveStartPaths;
import txml.interpreter.command.CommandDeleteInDocument;
import txml.interpreter.command.CommandFilterLe;
import txml.interpreter.command.CommandIteratorInit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import txml.database.DbApi;
import txml.interpreter.model.Instruction;
import txml.interpreter.model.SymbolTable;
import txml.TXmlException;
import txml.TXmlResult;
import txml.xpath.model.NodeGlobalSettings;

public class InstructionsInterpreter {
    private final SymbolTable symbolTable;
    private final List<Instruction> instructions;
    private int ip;
    
    private Connection connection;
    private final DbApi sqlStatements;
    private boolean newConnection;
    private static final Map<String, Command> commants = initCommands();
    private TXmlResult result;
    private NodeGlobalSettings lastSettings;
    private final Map<String, Integer> labels;
    private String skipUntilLabel = null;
    private boolean sort;
    
    public InstructionsInterpreter(Connection connection, DbApi sqlStatements, boolean sort) {
        this.sort = sort;
        labels = new HashMap<>();
        this.symbolTable = new SymbolTable();
        this.instructions = new ArrayList<>();
        ip = 0;
        this.connection = connection;
        this.sqlStatements = sqlStatements;
        this.sqlStatements.refreshCurrentTime();
        newConnection = false;
        result = new TXmlResult();
        lastSettings = null;
    }

    public void setSkipUntilLabel(String skipUntilLabel) {
        this.skipUntilLabel = skipUntilLabel;
    }
    
    
    public Map<String, Integer> getLabels() {
        return labels;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public NodeGlobalSettings getLastSettings() {
        return lastSettings;
    }

    public void setLastSettings(NodeGlobalSettings lastSettings) {
        this.lastSettings = lastSettings;
    }
    
    public TXmlResult getResult() {
        return result;
    }

    public void setResult(TXmlResult result) {
        this.result = result;
    }
    
    public void add(Instruction instruction) {
        instructions.add(instruction);
        try {
            evalAll();
        } catch ( XMLStreamException | SQLException | TXmlException | IOException | ParserConfigurationException ex) {
            throw new CancellationException(ex.getLocalizedMessage());
        }
    }
    
    private void evalAll() throws SQLException, TXmlException, IOException, ParserConfigurationException, FileNotFoundException, XMLStreamException {
        while (ip != instructions.size()) {
            Instruction i = instructions.get(ip);
            if (this.skipUntilLabel == null)
                evalInstruction(i);
            else {
                if (i.getName().equals("LABEL") && i.getResult().equals(skipUntilLabel)) {
                    skipUntilLabel = null;
                    evalInstruction(i);
                } else {
                    ip++;
                }
            }
        }
    }
    
    private void evalInstruction(Instruction instruction) throws SQLException, TXmlException, IOException, ParserConfigurationException, FileNotFoundException, XMLStreamException {

        Command command = commants.get(instruction.getName());
        if (command != null) {
            //System.out.println(instruction); 
            command.execute(this, instruction.getOperand1(), instruction.getOperand2(), instruction.getOperand3(), instruction.getResult());
        } else {
            System.err.println("unknown instruction: " + instruction);    
        }
        ip++;
    }

    public DbApi getSqlStatements() {
        return sqlStatements;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setNewConnection(boolean closeConnection) {
        this.newConnection = closeConnection;
    }

    public boolean isNewConnection() {
        return newConnection;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }
    
    static private Map<String, Command> initCommands() {
        Map<String, Command> comMap = new HashMap<>();
        comMap.put("INIT_SCHEMA", new CommandInitSchema());
        comMap.put("DEINIT_SCHEMA", new CommandDeinitSchema());
        comMap.put("DECLARE_OPTION_CONNECTION", new CommandDeclareOptionConnection());
        comMap.put("DECLARE_OPTION_TIME_FORMAT", new CommandDeclareOptionTimeFormat());
        comMap.put("DECLARE_OPTION_SORT", new CommandDeclareOptionSort());
        comMap.put("STORE_DOCUMENT", new CommandStoreDocument());
        comMap.put("SET_EMPTY_TNODE_LIST", new CommandNewList());
        comMap.put("RETURN_LIST", new CommandReturnList());
        comMap.put("GET_DIRECT_STEP", new CommandGetDirectStep());
        comMap.put("GET_UNDIRECT_STEP", new CommandGetUndirectStep());
        comMap.put("SAVE_START_PATHS", new CommandSaveStartPaths());
        comMap.put("GO_BACK_TO_START_PATHS", new CommandGoBackToStartPaths());
        comMap.put("GET_VALUES", new CommandGetValues());
        comMap.put("FILTER_EQ", new CommandFilterEq());
        comMap.put("FILTER_MORE", new CommandFilterMore());
        comMap.put("FILTER_LESS", new CommandFilterLess());
        comMap.put("FILTER_NEQ", new CommandFilterNeq());
        comMap.put("FILTER_LE", new CommandFilterLe());
        comMap.put("FILTER_GE", new CommandFilterGe());
        comMap.put("FILTER_OVERLAPS", new CommandFilterOverlaps());
        comMap.put("FILTER_CONTAINS", new CommandFilterContains());
        comMap.put("FILTER_IN", new CommandFilterIn());
        comMap.put("FILTER_PRECEDES", new CommandFilterPrecedes());
        comMap.put("FILTER_FOLLOWS", new CommandFilterFollows());
        comMap.put("FILTER_LMEETS", new CommandFilterLMeets());
        comMap.put("FILTER_RMEETS", new CommandFilterRMeets());
        comMap.put("ITERATOR_INIT", new CommandIteratorInit());
        comMap.put("LABEL", new CommandLabel());
        comMap.put("GOTO_IF_NO_NEXT", new CommandGotoIfNoNext());
        comMap.put("ITERATOR_NEXT", new CommandIteratorNext());
        comMap.put("SET_TNODE_LIST_IN_CYCLE", new CommandSetTNodeListInCycle());
        comMap.put("SET_LIST_FOR_PARENT", new CommandParentList());
        comMap.put("SET_PARENT_IN_DOCUMENT", new CommandSetParentInDocument());
        comMap.put("GOTO", new CommandGoto());
        comMap.put("DELETE_IN_DOCUMENT", new CommandDeleteInDocument());
        comMap.put("INSERT_INTO_DOCUMENT", new CommandInsertIntoDocument());
        comMap.put("REMOVE_DUPLICATES_IN_LIST", new CommandRemoveDuplicates());
        comMap.put("FILTER_POSITION", new CommandFilterPosition());
        comMap.put("RETURN_SNAPSHOT", new CommandReturnSnapshot());
        comMap.put("CHECK_SORT", new CommandCheckSort());
        comMap.put("ERASE_TXML_NODES", new CommandEraseTXmlNodes());
        
        return comMap;
    }  

    public SymbolTable getSymbolTable() {
        return symbolTable;
    } 
}
