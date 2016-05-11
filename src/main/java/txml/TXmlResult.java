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

/**
 * This is result XPath or snapshot query or database operation.
 * Database operation returns empty result.
 *
 * @author Tomas Kunovsky
 */
public class TXmlResult {

    /**
     * A utility function to check if this result is a NodeList (XPath).
     *
     */
    public boolean isNodeList() {
        return (this instanceof NodeList);
    }
    
    /**
     * A utility function to check if this result is a XMLEventReader (snapshot).
     *
     */
    public boolean isXMLEventReader() {
        return (this instanceof XMLEventReader);
    }
    
    /**
     * A utility function to check if this result is empty.
     *
     */
    public boolean isEmpty() {
        return !(this instanceof XMLEventReader || this instanceof NodeList);
    }
    
    /**
     * Returns result as a NodeList, may result in
     * a class cast exception if this result is not a NodeList.
     */
    public NodeList asNodeList() {
        return (NodeList) this;
    }

    /**
     * Returns result as a XMLEventReader, may result in
     * a class cast exception if this result is not a XMLEventReader.
     */
    public XMLEventReader asXMLEventReader() {
        return (XMLEventReader) this;
    }
}
