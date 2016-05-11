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

import java.sql.Timestamp;
import java.util.Iterator;
import javax.xml.namespace.QName;

/**
 * The StartElement interface provides access to information about
 * start elements.  A StartElement is reported for each Start Tag
 * in the document.
 *
 * @author Tomas Kunovsky
 */
public interface StartElement extends XMLEvent {

    /**
     * Returns an Iterator of namespaces declared on this element.
     * This Iterator does not contain previously declared namespaces
     * unless they appear on the current START_ELEMENT.
     * Therefore this list may contain redeclared namespaces and duplicate namespace
     * declarations.
     *
     * Returns an empty iterator if there are no namespaces.
     *
     * @return a readonly Iterator over Namespace interfaces, or an
     * empty iterator
     *
     */
    Iterator getNamespaces();

    /**
     * Get the name of this event
     * @return the qualified name of this event
     */
    QName getName();

    /**
     * Returns an Iterator.
     *
     * @return a readonly Iterator over Attribute interfaces, or an
     * empty iterator
     */
    Iterator<Attribute> getAttributes();

    /**
     * The start point of validity interval of element node.
     */
    Timestamp getFrom();

    /**
     * The end point of validity interval of element node.
     */
    Timestamp getTo();

    /**
     * The identifier of element node, same for whole its history.
     */
    Long getId();
}
