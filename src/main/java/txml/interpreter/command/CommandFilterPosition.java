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
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import txml.interpreter.model.SymbolTableItem;
import txml.interpreter.model.SymbolTableType;
import txml.TXmlException;
import txml.interpreter.InstructionsInterpreter;
import txml.xpath.model.TNode;
import txml.xpath.model.TNodeList;

public class CommandFilterPosition implements Command {
    
    @Override
    public void execute(InstructionsInterpreter interpreter, String operand1, String operand2, String operand3, String result) throws SQLException, TXmlException, IOException, ParserConfigurationException, FileNotFoundException, XMLStreamException{
        if (interpreter.getConnection() == null) {
            throw new TXmlException("No database connection");
        }
        SymbolTableItem tableItem = interpreter.getSymbolTable().getTable().get(operand3);
        if (tableItem.getType() != SymbolTableType.TNODE_LIST) {
            throw new TXmlException("Variable " + operand3 + " has bad type.");
        }
        
        TNodeList tNodeListOld = ((TNodeList) tableItem.getAttribute()).sort();
        List<TNode> newItems = new ArrayList<>();
        
        if (operand2.equals("last()")) {
            if (tNodeListOld.getLength() != 0) {
                newItems.add(tNodeListOld.item(tNodeListOld.getLength() - 1));
            }
        } else {
            int pos = (Integer.parseInt(operand2) - 1);
            if (pos < tNodeListOld.getLength() && pos >= 0) {
                newItems.add(tNodeListOld.item(pos));
            }
            
        }
        
        TNodeList tNodeListNew = new TNodeList(interpreter.getLastSettings(), newItems);
        interpreter.getSymbolTable().getTable().put(result, new SymbolTableItem(SymbolTableType.TNODE_LIST, tNodeListNew));
    }
    
}
