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
package txml.load.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Namespace;

public class XmlNode {
    private final QName name;
    private final XmlNodeTypeEnum type;
    private List<XmlNode> children;
    private final XmlNode parent;
    private String value;
    private final List<Namespace> namespaces;
    
    public XmlNode(QName name, XmlNodeTypeEnum type, XmlNode parent, List<Namespace> namespaces) {
        this.name = name;
        this.type = type;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.namespaces = namespaces;
    }

    public List<XmlNode> getChildren() {
        return children;
    }

    public void setChildren(List<XmlNode> children) {
        this.children = children;
    }

    public XmlNode getParent() {
        return parent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public XmlNodeTypeEnum getType() {
        return type;
    }

    public QName getName() {
        return name;
    }

    public List<Namespace> getNamespaces() {
        return namespaces;
    } 
}
