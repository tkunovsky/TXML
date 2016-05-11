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
import javax.xml.namespace.QName;

/**
 * An interface that contains information about an attribute.
 * @see StartElement
 * @author Tomas Kunovsky
 */
public interface Attribute {

    /**
     * Returns the QName for this attribute
     */
    QName getName();

    /**
     * Gets the value of this attribute
     */
    String getValue();

    /**
     * The start point of validity interval of attribute name.
     */
    Timestamp getNameFrom();

    /**
     * The end point of validity interval of attribute name.
     */
    Timestamp getNameTo();

    /**
     * The identifier of attribute name, same for whole its history.
     */
    Long getNameId();

    /**
     * The start point of validity interval of attribute value.
     */
    Timestamp getValueFrom();

    /**
     * The end point of validity interval of attribute value.
     */
    Timestamp getValueTo();

    /**
     * The identifier of attribute value, same for whole its history.
     */
    Long getValueId(); 
}
