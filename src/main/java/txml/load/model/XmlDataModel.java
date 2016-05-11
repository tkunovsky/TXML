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

public class XmlDataModel {
    private XmlNode root = null;
    private String documentName = null;
    private String header = null;

    public XmlNode getRoot() {
        return root;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getHeader() {
        return header;
    }

    public void setRoot(XmlNode root) {
        this.root = root;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
