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
import txml.TXmlException;
import txml.interpreter.InstructionsInterpreter;

public class CommandDeclareOptionSort implements Command {
    
    @Override
    public void execute(InstructionsInterpreter interpreter, String operand1, String operand2, String operand3, String result) throws SQLException, TXmlException {
        if (operand2.toLowerCase().equals("false")) {
            interpreter.setSort(false);
        } else if (operand2.toLowerCase().equals("true")) {
            interpreter.setSort(true);
        } else {
            throw new TXmlException("ilegal value for sort option: '" + operand2 + "'");
        }
    }
    
}
