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
package txml.database;

import java.sql.Connection;
import java.sql.SQLException;

public enum DatabaseEnum {
    H2, POSTGRE, OTHER;
    
    static public DatabaseEnum getDatabase(Connection connection) throws SQLException {
        if (connection.getMetaData().getDriverName().contains("H2"))
            return H2;
        else if (connection.getMetaData().getDriverName().contains("Postgre"))
            return POSTGRE;
        else
            return OTHER;
    }
}
