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
package txml.interpreter.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import txml.interpreter.model.SymbolTableItem;
import txml.interpreter.model.SymbolTableType;
import txml.TXmlException;
import txml.interpreter.InstructionsInterpreter;
import txml.load.model.XmlNodeTypeEnum;
import txml.xpath.model.TNode;
import txml.xpath.model.TNodeList;

public class CommandFilterLess implements Command {
    
    @Override
    public void execute(InstructionsInterpreter interpreter, String operand1, String operand2, String operand3, String result) throws SQLException, TXmlException, IOException, ParserConfigurationException, FileNotFoundException, XMLStreamException {
        if (interpreter.getConnection() == null) {
            throw new TXmlException("No database connection");
        }
        SymbolTableItem tableItem = interpreter.getSymbolTable().getTable().get(operand2);
        if (tableItem.getType() != SymbolTableType.TNODE_LIST) {
            throw new TXmlException("Variable " + operand2 + " has bad type.");
        }
        
        TNodeList tNodeListOld = (TNodeList) tableItem.getAttribute();
        List<TNode> newItems = new ArrayList<>();
        for (int i = 0; i < tNodeListOld.getLength(); i++) {
            if (tNodeListOld.item(i).getNodeValue() != null) {
                short type = tNodeListOld.item(i).getNodeType();
                if (type == XmlNodeTypeEnum.TXML_VALUE_FROM.getShortValue() ||
                    type == XmlNodeTypeEnum.TXML_VALUE_TO.getShortValue()) {
                    String variable = tNodeListOld.item(i).getNodeValue();
                    String literal = operand3;
                    Timestamp variableTimestamp;
                    Timestamp literalTimestamp;
                    
                    if (literal.equals("Now")) {
                        literal = interpreter.getSqlStatements().getCurrTime();
                    }
                    
                    if (variable.equals("Now")) {
                        variable = interpreter.getSqlStatements().getCurrTime();
                    }
                    literalTimestamp  = interpreter.getSqlStatements().convertStringToTimestamp(literal);
                    variableTimestamp = interpreter.getSqlStatements().convertStringToTimestamp(variable);

                    if (variableTimestamp.before(literalTimestamp)) {
                        newItems.add(tNodeListOld.item(i));
                    }
                } else if (type == XmlNodeTypeEnum.TXML_VALUE_ID.getShortValue() ||
                           type == XmlNodeTypeEnum.TEXT.getShortValue()
                        ) {
                    String variable = tNodeListOld.item(i).getNodeValue();
                    String literal = operand3;
                    if (variable.matches("-?\\d+(.\\d+)?") && literal.matches("-?\\d+(.\\d+)?")) {
                        Double variableDouble = Double.parseDouble(variable);
                        Double literalDouble = Double.parseDouble(literal);
                        if (variableDouble.compareTo(literalDouble) < 0) {
                            newItems.add(tNodeListOld.item(i));
                        }
                    }
                }
            }
        }
        
        TNodeList tNodeListNew = new TNodeList(interpreter.getLastSettings(), newItems);
        interpreter.getSymbolTable().getTable().put(result, new SymbolTableItem(SymbolTableType.TNODE_LIST, tNodeListNew));
    }
    
}
