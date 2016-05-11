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
package txml.interpreter.model;

public class SymbolTableItem {
    private SymbolTableType type;
    private Object attribute;

    public SymbolTableItem(SymbolTableType type, Object attribute) {
        this.type = type;
        this.attribute = attribute;
    }
    
    public SymbolTableType getType() {
        return type;
    }

    public Object getAttribute() {
        return attribute;
    }

    public void setType(SymbolTableType type) {
        this.type = type;
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
    }
    
}
