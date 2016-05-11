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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import txml.Attribute;
import txml.Namespace;
import txml.StartElement;

public class TStartElement extends TXMLEvent implements StartElement {
    private final QName qName;
    private final List<Attribute> attributes;
    private final Long from; 
    private final Long to; 
    private final Long id;
    private final List<Namespace> namespaces;
    
    public TStartElement(int eventType, QName qName, List<Attribute> attributes, Long from, Long to, Long id, List<Namespace> namespaces) {
        super(eventType);
        this.qName = qName;
        this.attributes = new ArrayList<>(attributes);
        this.from = from;
        this.to = to;
        this.id = id;
        if (namespaces == null) {
            this.namespaces = new ArrayList<>();
        } else {
            this.namespaces = namespaces;
        }
    }
    
    @Override
    public Iterator<Namespace> getNamespaces() {
        return namespaces.iterator();
    }
    
    @Override
    public QName getName() {
        return qName;
    }
    
    @Override
    public Iterator<Attribute> getAttributes() {
        return attributes.iterator();
    }

    @Override
    public Timestamp getFrom() {
        return new Timestamp(from);
    }

    @Override
    public Timestamp getTo() {
        return new Timestamp(to);
    }

    @Override
    public Long getId() {
        return id;
    }
}
