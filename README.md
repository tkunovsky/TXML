# TXML

## Table of Contents
1.  [TXML description](#desc)
2.  [Howto build](#build)
    *  [maven build](#maven)
3.  [Documentation](#doc)
4.  [Examples](#examples)
    *  [Init schema](#init_schema) 

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

In next examples is used XML document https://github.com/tkunovsky/TXML/blob/master/src/main/resources/books2.xml

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
        }
    }
}
```
