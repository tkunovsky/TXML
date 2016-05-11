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
 * This is the base event interface for handling markup events.
 * Events are value objects that are used to communicate the
 * XML 1.0 InfoSet to the Application. Events may be cached
 * and referenced after the parse has completed.
 *
 * @author Tomas Kunovsky
 */
public interface XMLEvent extends javax.xml.stream.XMLStreamConstants {

    /**
     * Returns an integer code for this event.
     * @see #START_ELEMENT
     * @see #END_ELEMENT
     * @see #CHARACTERS
     * @see #ATTRIBUTE
     * @see #START_DOCUMENT
     * @see #END_DOCUMENT
     */
    int getEventType();

    /**
     * A utility function to check if this event is a StartElement.
     * @see StartElement
     */
    boolean isStartElement();

    /**
     * A utility function to check if this event is a EndElement.
     * @see EndElement
     */
    boolean isEndElement();

    /**
     * A utility function to check if this event is Characters.
     * @see Characters
     */
    boolean isCharacters();

    /**
     * A utility function to check if this event is a StartDocument.
     * @see StartDocument
     */
    boolean isStartDocument();

    /**
     * A utility function to check if this event is an EndDocument.
     * @see EndDocument
     */
    boolean isEndDocument();

    /**
     * Returns this event as a start element event, may result in
     * a class cast exception if this event is not a start element.
     */
    StartElement asStartElement();

    /**
     * Returns this event as an end  element event, may result in
     * a class cast exception if this event is not a end element.
     */
    EndElement asEndElement();

    /**
     * Returns this event as Characters, may result in
     * a class cast exception if this event is not Characters.
     */
    Characters asCharacters();
}
