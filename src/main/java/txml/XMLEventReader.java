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
 *
 * This is the top level interface for parsing XML Events.  It provides
 * the ability to peek at the next event and returns configuration
 * information through the property interface.
 * 
 * @author Tomas Kunovsky
 */
public interface XMLEventReader {    
    /**
     * Get the next XMLEvent
     * @see XMLEvent
     * @throws SQLException
     * @throws TXmlException
     */
    XMLEvent nextEvent() throws SQLException, TXmlException;

    /**
     * Check if there are more events.
     * Returns true if there are more events and false otherwise.
     * @return true if the event reader has more events, false otherwise
     */
    boolean hasNext();

    /**
     * Free database sources (statements) and close database connection.
     * @throws SQLException
     */
    void closeDbConnection() throws SQLException;

    /**
     * Free database sources (statements).
     * @throws SQLException
     */
    void free() throws SQLException;

    /**
     *
     * Returns whole XML document.
     * @throws SQLException
     * @throws TXmlException
     */
    String getXMLDocFormatA() throws SQLException, TXmlException;

    /**
     *
     * Returns whole XML document.
     * @throws SQLException
     * @throws TXmlException
     */
    String getXMLDocFormatB() throws SQLException, TXmlException;
}
