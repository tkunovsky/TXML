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

import java.util.LinkedList;

public class NodeLocalSettings {
    private LinkedList<TNode> savedNodes = new LinkedList<>();
    
    public LinkedList<TNode> getSavedNodes() {
        return savedNodes;
    }
    
    public void setSavedNodes(LinkedList<TNode> savedNodes) {
        this.savedNodes = new LinkedList<>(savedNodes);
    }
}
