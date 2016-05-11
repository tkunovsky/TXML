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
package txml.snapshot.model;

import txml.XMLEvent;

public class TXMLEvent implements XMLEvent  {
    int eventType;

    public TXMLEvent(int eventType) {
        this.eventType = eventType;
    }

    @Override
    public int getEventType() {
        return this.eventType;
    }

    @Override
    public boolean isStartElement() {
        return this.eventType == XMLEvent.START_ELEMENT;
    }

    @Override
    public boolean isEndElement() {
        return this.eventType == XMLEvent.END_ELEMENT;
    }

    @Override
    public boolean isCharacters() {
        return this.eventType == XMLEvent.CHARACTERS;
    }
    
    @Override
    public boolean isStartDocument() {
        return this.eventType == XMLEvent.START_DOCUMENT;
    }
    
    @Override
    public boolean isEndDocument() {
        return this.eventType == XMLEvent.END_DOCUMENT;
    }

    @Override
    public TStartElement asStartElement() {
        return (TStartElement) this;
    }

    @Override
    public TEndElement asEndElement() {
        return (TEndElement) this;
    }

    @Override
    public TCharacters asCharacters() {
        return (TCharacters) this;
    }
}
