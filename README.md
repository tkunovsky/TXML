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
    *  [Attribute delete](#attribute_delete)
    *  [XPath after delete](#XPath_after_delete) 
    *  [Element move](#element_move) 
    *  [XPath after move](#XPath_after_move) 
    *  [Snapshot](#snapshot) 

##<a name="desc"></a> TXML description
TXML is library for temporal storing your XML documents in relational database. This library is implemented in Java and supports PostgreSQL and H2 databases.

Temporal attributes have namespace https://github.com/tkunovsky/TXML, see https://github.com/tkunovsky/TXML/blob/master/src/main/resources/txml_schema.xsd

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
This example deletes schema txml and its document tables from database.
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
This example inserts new element into document.
```
import txml.TXml;
import txml.Node;

public class TXMLInEXamples {
   public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            Node rootNode = txml.eval("txml:doc('txml', 'example.xml')/*", connection, false).asNodeList().item(0);
            rootNode.insertIntoDocument("book/title", "Ferdinand Peroutka. Život v novinách");
            
            /* alternative
            txml.eval(
                "for $n in txml:doc('txml', 'example.xml')/*                          " +
                "    insert $n/book/title VALUE 'Ferdinand Peroutka. Život v novinách'", connection, false);*/
            
        }
   }
}
```
###<a name="XPath_after_insert"></a> XPath after insert
This example shows temporal XPath query after previous example.
```
import txml.TXml;
import txml.Node;

public class TXMLInEXamples {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            Node node = txml.eval("txml:doc('txml', 'example.xml')/library/book[title = 'Ferdinand Peroutka. Život v novinách']", connection, false).asNodeList().item(0);
            System.out.println(node.getTree(false));
            
            /* alternative
            Node node = txml.eval("txml:doc('txml', 'example.xml')/library/book[txml:id = '70']", connection, false).asNodeList().item(0);*/
        }
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

###<a name="attribute_delete"></a> Attribute delete
This example deletes attribute "available" in document example.xml.
```
import txml.TXml;
import txml.Node;

public class TXMLInEXamples {

    public static void main(String[] args) throws SQLException, ClassNotFoundException  {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            Node node = txml.eval("txml:doc('txml', 'example.xml')/library/book[@id='b0836217462']/@available", connection, false).asNodeList().item(0);
            node.deleteInDocument();
            
            /* alternative
            txml.eval("for $n in txml:doc('txml', 'example.xml')/library/book[@id='b0836217462'] "
                    + "    delete node $n/@available                                             "
                    , connection, false);*/
        }
    }
}
```
###<a name="XPath_after_delete"></a> XPath after delete
This example shows temporal XPath query after previous example.
```
import txml.TXml;
import txml.Node;

public class TXMLInEXamples {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            Node node = txml.eval("txml:doc('txml', 'example.xml')/library/book/@available", connection, false).asNodeList().item(0);
            System.out.println(node.getTree(false));
            
            /* alternative
            Node node = txml.eval("txml:doc('txml', 'example.xml')//@available", connection, false).asNodeList().item(0);*/
        }
    }
}
```
This is output on stdout:
```
<txml:attribute name="available" value="true" txml:from="12. 5. 2016 19:27:28" txml:to="13. 5. 2016 9:40:39" txml:id="5"/>
```
###<a name="element_move"></a> Element move
This example move element in document.
```
import txml.TXml;
import txml.Node;

public class TXMLInEXamples {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {
            Node childNode = txml.eval("txml:doc('txml', 'example.xml')//isbn[. = '8090119964']", connection, false).asNodeList().item(0);
            Node parentNode = txml.eval("txml:doc('txml', 'example.xml')/library/book[title = 'Ferdinand Peroutka. Život v novinách']", connection, false).asNodeList().item(0);
            childNode.setParentInDocument(parentNode, 1);
            
            /* alternative
            txml.eval("for $n in txml:doc('txml', 'example.xml')//isbn[. = '8090119964']                              "
                    + "    SET PARENT $n AS /library/book[title = 'Ferdinand Peroutka. Život v novinách'] POSITION '1'"
                    , connection, false);*/
        }
    }
}
```
###<a name="XPath_after_move"></a> XPath after move
This example shows temporal XPath query after previous example.
```
import txml.TXml;
import txml.NodeList;

public class TXMLInEXamples {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, FileNotFoundException, ParserConfigurationException, XMLStreamException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {            
            NodeList nodes = txml.eval(
                    "declare option txml:time-format 'H:mm:ss:SSS';" +
                    "txml:doc('txml', 'example.xml')//isbn[. = '8090119964']/..", connection, true).asNodeList();
            System.out.println(nodes.getTrees(true));
        }
    }
}
```
This is output on stdout:
```
<book id="8090119964" txml:from="11:01:25:907" txml:to="Now" txml:id="58">
  <isbn txml:from="11:01:25:907" txml:to="11:11:29:681" txml:id="61">
    8090119964
  </isbn>
  <title xml:lang="cz">
    Babička
  </title>
  <hr:author>
    <hr:name>
      Božena Němcová
    </hr:name>
  </hr:author>
</book>
<book txml:from="11:03:21:001" txml:to="Now" txml:id="70">
  <isbn txml:from="11:11:29:682" txml:to="Now" txml:id="61">
    8090119964
  </isbn>
  <title>
    Ferdinand Peroutka. Život v novinách
  </title>
</book>
```
###<a name="snapshot"></a> Snapshot
This example shows snapshot your XML document.
```
import txml.TXml;

public class TXMLInEXamples {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        TXml txml = new TXml();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=txml")) {  
            String xmlDoc = txml.getSnapshot(connection, "txml", "example.xml", "Now").getXMLDocFormatA();
            System.out.println(xmlDoc);
            /* alternative
            txml.eval("txml:doc-snapshot('txml', 'example.xml', 'Now')", connection);*/
        }
    }
}
```
This is output on stdout:
```
<?xml version="1.0" encoding="UTF-8"?>
<library xmlns="http://eric.van-der-vlist.com/ns/library" xmlns:hr="http://eric.van-der-vlist.com/ns/person">
  <book id="b0836217462">
    <isbn>
      0836217462
    </isbn>
    <title xml:lang="en">
      Being a Dog Is a Full-Time Job
    </title>
    <hr:author id="CMS">
      <hr:name>
        Charles M Schulz
      </hr:name>
      <hr:born>
        1922-11-26
      </hr:born>
      <hr:dead>
        2000-02-12
      </hr:dead>
    </hr:author>
    <character id="PP">
      <hr:name>
        Peppermint Patty
      </hr:name>
      <hr:born>
        1966-08-22
      </hr:born>
      <qualification>
        bold, brash and tomboyish
      </qualification>
    </character>
    <character id="Snoopy">
      <hr:name>
        Snoopy
      </hr:name>
      <hr:born>
        1950-10-04
      </hr:born>
      <qualification>
        extroverted beagle
      </qualification>
    </character>
    <character id="Schroeder">
      <hr:name>
        Schroeder
      </hr:name>
      <hr:born>
        1951-05-30
      </hr:born>
      <qualification>
        brought classical music to the Peanuts strip
      </qualification>
    </character>
    <character id="Lucy">
      <hr:name>
        Lucy
      </hr:name>
      <hr:born>
        1952-03-03
      </hr:born>
      <qualification>
        bossy, crabby and selfish
      </qualification>
    </character>
  </book>
  <book id="8090119964">
    <title xml:lang="cz">
      Babička
    </title>
    <hr:author>
      <hr:name>
        Božena Němcová
      </hr:name>
    </hr:author>
  </book>
  <book>
    <isbn>
      8090119964
    </isbn>
    <title>
      Ferdinand Peroutka. Život v novinách
    </title>
  </book>
</library>
```
