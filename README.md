# TXML

## Table of Contents
1.  [TXML description](#desc)
2.  [Howto build](#build)
    *  [maven build](#maven)
3.  [Documentation](#doc)
4.  [Examples](#examples)

##<a name="desc"></a> TXML description
TXML is library for temporal storing your XML documents in relational database. This library is implemented in Java and supports PostgreSQL and H2 databases.

##<a name="build"></a> Howto build
You can import project to Netbeans 7/8 or build with maven.

###<a name="maven"></a> maven build
```
git clone https://github.com/tkunovsky/TXML.git
cd TXML
mvn org.apache.maven.plugins:maven-compiler-plugin:compile
mvn install
```
After you can run TXML library with file containing source code, see examples.
```
java -jar ./target/TXml-1.0.0-jar-with-dependencies.jar file.example
```

##<a name="doc"></a> Documentation
https://htmlpreview.github.io/?https://github.com/tkunovsky/TXML/blob/master/target/site/apidocs/index.html

##<a name="examples"></a> Examples
TXML library offers two channel for working with your XML documents. You can use only object API and XPath or its DML and DDL.

