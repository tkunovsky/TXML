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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import txml.interpreter.model.SymbolTableItem;
import txml.interpreter.model.SymbolTableType;
import txml.TXmlException;
import txml.interpreter.InstructionsInterpreter;
import txml.xpath.model.NodeGlobalSettings;
import txml.xpath.model.TNode;
import txml.xpath.model.TNodeList;

public class CommandGoBackToStartPaths implements Command {
    
    @Override
    public void execute(InstructionsInterpreter interpreter, String operand1, String operand2, String operand3, String result) throws SQLException, TXmlException {
        if (interpreter.getConnection() == null) {
            throw new TXmlException("No database connection");
        }
        
        NodeGlobalSettings settings = interpreter.getLastSettings();
        SymbolTableItem tableItem = interpreter.getSymbolTable().getTable().get(result);
        if (tableItem.getType() != SymbolTableType.TNODE_LIST) {
            throw new TXmlException("Variable " + result + " has bad type.");
        }
        
        TNodeList tNodeList = (TNodeList) tableItem.getAttribute();
        List<TNode> startTNodes = new ArrayList<>();
        for (int i = 0; i < tNodeList.getLength(); i++) {
            startTNodes.add(tNodeList.item(i).getLocalSettings().getSavedNodes().pop());
            startTNodes.get(i).getLocalSettings().setSavedNodes(tNodeList.item(i).getLocalSettings().getSavedNodes());
        }
        
        TNodeList tNodeListNew = new TNodeList(settings, startTNodes);
        interpreter.getSymbolTable().getTable().put(result, new SymbolTableItem(SymbolTableType.TNODE_LIST, tNodeListNew));

    }
    
}
