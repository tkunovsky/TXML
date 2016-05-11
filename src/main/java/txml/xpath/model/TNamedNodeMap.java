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
package txml.xpath.model;

import txml.NamedNodeMap;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TNamedNodeMap implements NamedNodeMap {
    
    private final List<TNode> items;
    private final NodeGlobalSettings settings;

    TNamedNodeMap(NodeGlobalSettings settings, List<TNode> items) {
        this.settings = settings;
        this.items = items;
    }
        
    @Override
    public TNamedNodeMap sort() throws SQLException {
        TNodeList tNodeList = new TNodeList(settings, items);
        tNodeList = tNodeList.sort();
        List<TNode> result = new ArrayList<>(); 
        for (int i = 0; i < tNodeList.getLength(); i++) {
            result.add(tNodeList.item(i));
        }
        return new TNamedNodeMap(settings, result);
    }        
    
    @Override
    public TNode getNamedItem(String name) {
        for (TNode node:items) {
            if (node.getNodeName().equals(name)) {
                return node;
            }  
        }
        
        return null;
    }

    @Override
    public TNode item(int index) {
        return this.items.get(index);
    }

    @Override
    public int getLength() {
        return this.items.size();
    }
    
}
