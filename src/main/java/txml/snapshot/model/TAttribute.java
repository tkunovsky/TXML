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
import javax.xml.namespace.QName;
import txml.Attribute;

public class TAttribute implements Attribute  {
    private final QName qName;
    private final String value;
    private final Long nameFrom;
    private final Long nameTo;
    private final Long nameId;
    private final Long valueFrom;
    private final Long valueTo;
    private final Long valueId;

    public TAttribute(QName qName, String value, Long nameFrom, Long nameTo, Long nameId, Long valueFrom, Long valueTo, Long valueId) {
        this.qName = qName;
        this.value = value;
        this.nameFrom = nameFrom;
        this.nameTo = nameTo;
        this.nameId = nameId;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
        this.valueId = valueId;
    }
    
    @Override
    public QName getName() {
        return qName;
    }
    
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Timestamp getNameFrom() {
        return new Timestamp(nameFrom);
    }

    @Override
    public Timestamp getNameTo() {
        return new Timestamp(nameTo);
    }

    @Override
    public Long getNameId() {
        return nameId;
    }

    @Override
    public Timestamp getValueFrom() {
        return new Timestamp(valueFrom);
    }

    @Override
    public Timestamp getValueTo() {
        return new Timestamp(valueTo);
    }

    @Override
    public Long getValueId() {
        return valueId;
    }  
}
