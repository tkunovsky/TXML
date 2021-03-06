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
import txml.Characters;

public class TCharacters extends TXMLEvent implements Characters {
    
    private final Long from; 
    private final Long to; 
    private final Long id;
    private final String value;

    public TCharacters(int eventType, String value, Long from, Long to, Long id) {
        super(eventType);
        this.from = from;
        this.to = to;
        this.id = id;
        this.value = value;
    }
    
    @Override
    public String getData() {
        return value;
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
