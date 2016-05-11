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
package txml;

import java.sql.SQLException;

/**
 * Objects implementing the <code>NamedNodeMap</code> interface are used to
 * represent collections of nodes that can be accessed by name. Note that
 * <code>NamedNodeMap</code> does not inherit from <code>NodeList</code>.
 * Objects contained in an object implementing <code>NamedNodeMap</code> may
 * also be accessed by an ordinal index.
 * 
 * @author Tomas Kunovsky
 */
public interface NamedNodeMap {

    /**
     * The number of nodes in this map. The range of valid child node indices
     * is <code>0</code> to <code>length-1</code> inclusive.
     */
    int getLength();
    
    /**
     * Returns the <code>index</code>th item in the map.
     * @param index Index into this map.
     * @return The node at the <code>index</code>th position in the map.
     */
    Node item(int index);
    
    /**
     * Retrieves a node specified by name.
     * @param name The <code>nodeName</code> of a node to retrieve.
     * @return A <code>Node</code> (of any type) with the specified
     *   <code>nodeName</code>, or <code>null</code> if it does not identify
     *   any node in this map.
     */
    Node getNamedItem(String name);

    /**
     * Returns sorted collection these nodes.
     * @throws SQLException
     * @throws TXmlException
     */
    NamedNodeMap sort() throws SQLException, TXmlException;
}
