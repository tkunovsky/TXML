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
import txml.interpreter.model.SymbolTableItem;
import txml.interpreter.model.SymbolTableType;
import txml.TXmlException;
import txml.interpreter.InstructionsInterpreter;
import txml.load.model.XmlNodeTypeEnum;
import txml.xpath.model.TNode;
import txml.xpath.model.TNodeList;

public class CommandSetParentInDocument implements Command {
    
    @Override
    public void execute(InstructionsInterpreter interpreter, String operand1, String operand2, String operand3, String result) throws SQLException {
        
        SymbolTableItem tableItem = interpreter.getSymbolTable().getTable().get(operand2);
        if (tableItem.getType() != SymbolTableType.TNODE) {
            throw new TXmlException("Variable " + operand2 + " has bad type.");
        }
        
        TNode child = (TNode) tableItem.getAttribute();
        
        tableItem = interpreter.getSymbolTable().getTable().get(operand3);
        if (tableItem.getType() != SymbolTableType.TNODE_LIST) {
            throw new TXmlException("Variable " + operand3 + " has bad type.");
        }
        
        TNodeList tNodeList = (TNodeList) tableItem.getAttribute();
        
        TNode parent = null;
        for (int i = 0; i < tNodeList.getLength(); i++) {
            if (tNodeList.item(i).getToAsLong() == tNodeList.getSettings().getDbApi().getNOW()) {
                if (parent != null) {
                    throw new TXmlException("Ambiguous parent node.");
                } else {
                    parent = tNodeList.item(i);
                }
            }
        }
        
        if (parent == null) {
            throw new TXmlException("No parent node.");
        }
        
        if (parent.getNodeType() != XmlNodeTypeEnum.ELEMENT.getShortValue()) {
            throw new TXmlException("Parent node isn't element node.");
        }
        
        if (child.hasDescendant(parent) || child.getId().equals(parent.getId())) {
            throw new TXmlException("Cycle in document detected, bad parent.");
        }
        
        if (operand1 == null) {
            child.setParentInDocument(false, parent, -1); 
        } else {
            Integer position;
            try {
                position = Integer.valueOf(operand1);
            } catch (NumberFormatException e) {
                throw new TXmlException(e.getLocalizedMessage());
            }
            child.setParentInDocument(false, parent, position);
        }
    }
}
