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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import txml.interpreter.model.SymbolTableItem;
import txml.interpreter.model.SymbolTableType;
import txml.TXmlException;
import txml.interpreter.InstructionsInterpreter;
import txml.xpath.model.TNode;

public class CommandInsertIntoDocument implements Command {
    @Override
    public void execute(InstructionsInterpreter interpreter, String operand1, String operand2, String operand3, String result) throws SQLException, TXmlException, IOException, ParserConfigurationException, FileNotFoundException, XMLStreamException {
        if (interpreter.getConnection() == null) {
            throw new TXmlException("No database connection");
        }

        SymbolTableItem tableItem = interpreter.getSymbolTable().getTable().get(operand2);
        if (tableItem.getType() != SymbolTableType.TNODE) {
            throw new TXmlException("Variable " + operand2 + " has bad type.");
        }
        
        TNode tNode = (TNode) tableItem.getAttribute();
        
        if (tNode.getToAsString().equals("Now")) {
            if (operand1 == null) {
                tNode.insertIntoDocument(false, result, operand3, -1);
            } else {
                Integer position;
                try {
                    position = Integer.valueOf(operand1);
                } catch (NumberFormatException e) {
                    throw new TXmlException(e.getLocalizedMessage());
                }
                tNode.insertIntoDocument(false, result, operand3, position);
            }
        }
    } 
}
