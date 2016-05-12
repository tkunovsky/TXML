# TXML

## Table of Contents
1.  [TXML description](#desc)
2.  [Howto build](#build)
    *  [maven build](#maven)
3.  [Documentation](#doc)
4.  [Examples](#examples)
    *  [Init schema](#init_schema) 
    *  [Document store](#load_document)
    *  [Deinit schema](#deinit_schema) 
    *  [Element insert](#element_insert) 
    *  [XPath after insert](#XPath_after_insert) 

##<a name="desc"></a> TXML description
TXML is library for temporal storing your XML documents in relational database. This library is implemented in Java and supports PostgreSQL and H2 databases.

Temporal attributes have namespace txml, see https://github.com/tkunovsky/TXML/blob/master/src/main/resources/txml_schema.xsd

Documents in database are storing into schemas. This allows you to group them and setting access permissions.

##<a name="build"></a> Howto build
You can import project to Netbeans 7/8 or build with maven.

###<a name="maven"></a> maven build
```
git clone https://github.com/tkunovsky/TXML.git
cd TXML
mvn org.apache.maven.plugins:maven-compiler-plugin:compile
mvn install
```
After build you can run TXML library with file containing source code, see examples.
```
java -jar ./target/TXml-1.0.0-jar-with-dependencies.jar file.example
```

##<a name="doc"></a> Documentation
https://htmlpreview.github.io/?https://github.com/tkunovsky/TXML/blob/master/target/site/apidocs/index.html

##<a name="examples"></a> Examples
TXML library offers two channel for working with your XML documents. You can use only object API and XPath or its DML and DDL language.

In next examples is used XML document https://raw.githubusercontent.com/tkunovsky/TXML/master/src/main/resources/books2.xml

###<a name="init_schema"></a> Init schema example
This example creates in database all tables for XML documents under schema txml.
```
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import txml.TXml;

public class TXMLInEXamples {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        String schemaName = "txml";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            txml.initSchema(connection, schemaName);
            /* alternative 
            txml.eval("txml:init-schema('txml')", connection); */
        }
    }
}
```

###<a name="load_document"></a> Document store example
This example stores your XML document to database under name example.xml.
```
public static void main(String[] args) throws SQLException, ClassNotFoundException {
     Class.forName("org.postgresql.Driver");
     TXml txml = new TXml();
     String schemaName = "txml";
     try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
         txml.loadDocumentToDb(connection, schemaName, "example.xml", "/home/tomas/books2.xml");
         /* alternative 
         txml.eval("txml:store('txml', '/home/tomas/books2.xml', 'example.xml')", connection); */
     }
 }
```

###<a name="deinit_schema"></a> Deinit schema example
This example delete schema txml and its document tables from database.
```
public class TXMLInEXamples {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        String schemaName = "txml";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            txml.deinitSchema(connection, schemaName);
            /* alternative
            txml.eval("txml:deinit-schema('txml')", connection); */
        }
    }
}
```

###<a name="element_insert"></a> Element insert
This example insert new element into document.
```
 public static void main(String[] args) throws SQLException, ClassNotFoundException {
     Class.forName("org.postgresql.Driver");
     TXml txml = new TXml();
     try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
         Node rootNode = txml.eval("txml:doc('txml', 'example.xml')/*", connection, false).asNodeList().item(0);
         rootNode.insertIntoDocument("book/title", "Ferdinand Peroutka. Život v novinách");
     }
 }
```
###<a name="XPath_after_insert"></a> XPath after insert
This example show temporal XPath query after previous example.
```
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, FileNotFoundException, ParserConfigurationException, XMLStreamException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            Node node = txml.eval("txml:doc('txml', 'example.xml')/library/book[title = 'Ferdinand Peroutka. Život v novinách']", connection, false).asNodeList().item(0);
            System.out.println(node.getTree(false));
            
            /* alternative
            Node node = txml.eval("txml:doc('txml', 'example.xml')/library/book[txml:id = '70']", connection, false).asNodeList().item(0);*/
        }
    }
```
This is output on stdout:
```
<book txml:from="12. 5. 2016 19:28:26" txml:to="Now" txml:id="70">
  <title>
    Ferdinand Peroutka. Život v novinách
  </title>
</book>
```

