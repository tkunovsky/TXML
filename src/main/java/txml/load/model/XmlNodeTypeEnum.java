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
package txml.load.model;

import txml.Node;

public enum XmlNodeTypeEnum {
    ATTRIBUTE((short) Node.ATTRIBUTE_NODE), 
    ELEMENT((short) Node.ELEMENT_NODE), 
    TEXT((short) Node.TEXT_NODE),
    TXML_ATTRIBUTE(Node.TXML_ATTRIBUTE_NODE),
    TXML_VALUE_ID(Node.TXML_VALUE_ID),
    TXML_VALUE_FROM(Node.TXML_VALUE_FROM),
    TXML_VALUE_TO(Node.TXML_VALUE_TO);
    
    private final short intValue;
    
    private XmlNodeTypeEnum(short intValue) {
        this.intValue = intValue;
    }

    public short getShortValue() {
        return intValue;
    }
    
}
