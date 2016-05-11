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
 * The <code>NodeList</code> interface provides the abstraction of an ordered
 * collection of nodes, without defining or constraining how this collection
 * is implemented.
 * <p>The items in the <code>NodeList</code> are accessible via an integral
 * index, starting from 0.
 * @author Tomas Kunovsky
 */
public interface NodeList {

    /**
     * The number of nodes in the list. The range of valid child node indices
     * is 0 to <code>length-1</code> inclusive.
     */
    int getLength();

    /**
     * Returns the <code>index</code>th item in the collection.
     * @param index Index into the collection.
     * @return The node at the <code>index</code>th position in the
     *   <code>NodeList</code>.
     */
    Node item(int index);

    /**
     * Returns sorted collection these nodes.
     * @throws SQLException
     * @throws TXmlException
     */
    NodeList sort() throws SQLException, TXmlException;
    
    /**
     * XML subtrees of all nodes in this collection.
     * @param sort True for sorting of subtrees.
     * @throws SQLException
     * @throws TXmlException
     */
    String getTrees(boolean sort) throws SQLException, TXmlException;

    /**
     * Returns sort setting.
     * @return
     */
    boolean isSorted();

    /**
     * Close database connection.
     * @throws SQLException
     */
    void closeDbConnection() throws SQLException;
}
