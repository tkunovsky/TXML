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
package txml.interpreter.command.model;

import txml.database.DbApi;

public class Interval {
    private final long from;
    private final long to;

    public Interval(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    static public Interval getInstance(String interval, DbApi dbApi) {
        if (interval.length() < 2 || !interval.contains(";")) return null;
        
        String noBrackets = interval.substring(1, interval.length() - 1);
        
        String fromString = noBrackets.split(";")[0].trim();
        String toString = noBrackets.split(";")[1].trim();
        long from;
        long to;
        
        if (fromString.toLowerCase().equals("now")) {
            from = dbApi.getNOW();
        } else {
            from = dbApi.convertStringToTimestamp(fromString).getTime();
        }
        
        if (toString.toLowerCase().equals("now")) {
            to = dbApi.getNOW();
        } else {
            to = dbApi.convertStringToTimestamp(toString).getTime();
        }
        
        return new Interval(from, to);
    }
}
