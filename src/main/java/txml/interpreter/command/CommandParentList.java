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

import txml.interpreter.model.SymbolTableItem;
import txml.interpreter.model.SymbolTableType;
import txml.TXmlException;
import txml.interpreter.InstructionsInterpreter;
import txml.xpath.model.NodeGlobalSettings;
import txml.xpath.model.TNode;
import txml.xpath.model.TNodeList;

public class CommandParentList implements Command {
    
    @Override
    public void execute(InstructionsInterpreter interpreter, String operand1, String operand2, String operand3, String result) {
        
        SymbolTableItem tableItem = interpreter.getSymbolTable().getTable().get(operand2);
        if (tableItem.getType() != SymbolTableType.TNODE) {
            throw new TXmlException("Variable " + operand2 + " has bad type.");
        }
        
        TNode tNode = (TNode) tableItem.getAttribute();
        NodeGlobalSettings settings = tNode.getSettings();
        TNodeList tNodeList = new TNodeList(settings);
        interpreter.getSymbolTable().getTable().put(result, new SymbolTableItem(SymbolTableType.TNODE_LIST, tNodeList));
        interpreter.setLastSettings(settings);
    }
    
}
