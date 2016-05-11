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

/**
 * This describes the interface to Characters events.
 * All text events get reported as Characters events.
 * Content, CData and whitespace are all reported as
 * Characters events.
 * @author Tomas Kunovsky
 */
public interface Characters extends XMLEvent {

    /**
     * Get the character data of this event
     */
    String getData();

    /**
     * The start point of validity interval of text node.
     */
    Timestamp getFrom();

    /**
     * The end point of validity interval of text node.
     */
    Timestamp getTo();

    /**
     * The identifier of text node, same for whole its history.
     */
    Long getId();
}
