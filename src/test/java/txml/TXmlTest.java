package txml;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import txml.database.DbApi;
import txml.xpath.model.TNodeList;
import txml.xpath.model.TNode;

public class TXmlTest {    
    private TXml instance;
    static private Connection dbConn;
    private final String schemaName;
    private final String document1;
    private final String document2;
    private Long document1CreationTime;
    private Long document2CreationTime;
    private final DbApi dbApi;
    
    public TXmlTest() {     
        schemaName = "txml";
        document1 = "book-without-namespaces.xml";
        document2 = "book-with-namespaces.xml";
        dbApi = new DbApi();
    }
    
    @BeforeClass
    static public void setUpBeforeClass() throws SQLException {
        TXmlTest.dbConn =  DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;USER=sa;PASSWORD=123");
        //TXmlTest.dbConn =  DriverManager.getConnection("jdbc:postgresql://localhost/txml?user=txml&password=774333153");
    }
    
    @AfterClass
    static public void tearDownAfterClass() throws SQLException {
        TXmlTest.dbConn.close();
    }
    
    @Before
    public void setUp() throws SQLException, IOException, FileNotFoundException, ParserConfigurationException, XMLStreamException, NoSuchFieldException {
        instance = new TXml();
        instance.initSchema(dbConn, schemaName);
        
        InputStream stream = new ByteArrayInputStream(xmlDocument1.getBytes(StandardCharsets.UTF_8));
        instance.loadDocumentToDb(dbConn, schemaName, document1, stream);
        stream.close();
        document1CreationTime = instance.lastTimeChanges();
        
        stream = new ByteArrayInputStream(xmlDocument2.getBytes(StandardCharsets.UTF_8));
        instance.loadDocumentToDb(dbConn, schemaName, document2, stream);
        stream.close();
        document2CreationTime = instance.lastTimeChanges();
    }
    
    @After
    public void tearDown() throws SQLException {
        instance.deinitSchema(dbConn, schemaName);
    }

    @Test
    public void testSimpleFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//book[./././txml:id = '30']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        TNode tNode = new TNode(
                document1CreationTime, 
                dbApi.getNOW(), 
                30l, 
                null, null, null, null, null, null, null, null, null, null, null, null);
        
        List<TNode> expList = new ArrayList<>();
        expList.add(tNode);
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }

    @Test
    public void testDoubleFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//book[year = '2003'][name = 'XQuery Kick Start']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        TNode tNode = new TNode(
                document1CreationTime, 
                dbApi.getNOW(), 
                30l, 
                null, null, null, null, null, null, null, null, null, null, null, null);
        
        List<TNode> expList = new ArrayList<>();
        expList.add(tNode);
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }

    @Test
    public void testLongDirectFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore[book/author/name = 'Erik T. Ray']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        TNode tNode = new TNode(
                document1CreationTime, 
                dbApi.getNOW(), 
                1l, 
                null, null, null, null, null, null, null, null, null, null, null, null);
        
        List<TNode> expList = new ArrayList<>();
        expList.add(tNode);
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testFilterInFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore[book[name = 'Learning XML']/author/name = 'Erik T. Ray']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        TNode tNode = new TNode(
                document1CreationTime, 
                dbApi.getNOW(), 
                1l, 
                null, null, null, null, null, null, null, null, null, null, null, null);
        
        List<TNode> expList = new ArrayList<>();
        expList.add(tNode);
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testParentInFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore/book[../txml:id != '0']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 2l, null, null, null, null, null, null, null, null, null, null, null, null));
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 16l, null, null, null, null, null, null, null, null, null, null, null, null));
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 30l, null, null, null, null, null, null, null, null, null, null, null, null));
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 56l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testSimpleParent() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore/book/..", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 1l, null, null, null, null, null, null, null, null, null, null, null, null));
        
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testOverTop() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore/*/../../../../../..", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testRoot() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/*", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 1l, null, null, null, null, null, null, null, null, null, null, null, null));
        
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testOverTopInFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore/*[../../../../../../txml:id != \"7\"]", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testComplexFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore[*/../*/../*/../*/txml:to = \"Now\"]", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 1l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectPathInFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//book[.//name = \"Erik T. Ray\"]", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 56l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testOverBottomInFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore/*[*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/txml:id != \"7\"]", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testOverBottom() throws Exception {
        String code = String.format("txml:doc('%s', '%s')/bookstore/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/txml:id", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testDirectText() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//book/name/text()[.. = 'XQuery Kick Start']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 36l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectText() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//text()[.. = 'XQuery Kick Start']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 36l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testComplexUndirectStar() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//*[*/* = 'XQuery Kick Start']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 1l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testSimpleUndirectStar() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//*[* = 'XQuery Kick Start']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 30l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testFiltersInFilters() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//bookstore[book[../txml:id = '1'][name = 'Learning XML']/author[name[txml:id != '0'] = 'Erik T. Ray']/name = 'Erik T. Ray']/*/..", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        TNode tNode = new TNode(
                document1CreationTime, 
                dbApi.getNOW(), 
                1l, 
                null, null, null, null, null, null, null, null, null, null, null, null);
        
        List<TNode> expList = new ArrayList<>();
        expList.add(tNode);
        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }

    @Test
    public void testSimpleUndirectDot() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//.[.. = '2001']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 13l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testComplexUndirectDot() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//.[.. = '2001']/../../..//.[.. = '2003']/..", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 52l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testSimpleUndirectTwoDots() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//..[price = '29.99']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 16l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectTwoDotsInFilter() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//book[.//.. = 'Giada De Laurentiis']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 2l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectSimpleTXmlValue() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//txml:id/..[txml:id = '2']/..[.//txml:id = '2']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 1l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectComplexTXmlValue() throws Exception {
        String code = String.format("txml:doc('%s', '%s')//book[.//txml:id = '35']//txml:id/..[txml:id = '35']/..[.//txml:id = '35']", schemaName, document1);
        Boolean sort = true;
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, dbApi.getNOW(), 34l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectSimpleTemporalWithDelete() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc('%s', '%s')/bookstore delete node $n//*[txml:id = '5']", schemaName, document1);
        instance.eval(modificationCode, dbConn, sort);
        Long modificationTime = instance.lastTimeChanges();
        String code = String.format("txml:doc('%s', '%s')//*[txml:to != 'Now']", schemaName, document1);
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(document1CreationTime, modificationTime-1, 5l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectSimpleInsert() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc('%s', '%s')//book[author/name = \"J K. Rowling\"] insert $n/count VALUE '10'", schemaName, document1);
        instance.eval(modificationCode, dbConn, sort);
        Long modificationTime = instance.lastTimeChanges();
        String code = String.format("txml:doc('%s', '%s')//book[author/name = \"J K. Rowling\"]/count", schemaName, document1);
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(modificationTime, dbApi.getNOW(), 137l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testUndirectSimpleInsertWithoutTimeGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc('%s', '%s')//book[author/name = \"J K. Rowling\"] insert $n/count VALUE '10' ", schemaName, document1);
        String code = String.format("txml:doc('%s', '%s')//book[author/name = \"J K. Rowling\"]/count", schemaName, document1);
        NodeList result = instance.eval(modificationCode + code, dbConn, sort).asNodeList();
        Long modificationTime = instance.lastTimeChanges();
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(modificationTime, dbApi.getNOW(), 137l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testFormatSimpleSnapshot1() throws Exception {
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document1);
        Boolean sort = true;
        
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();

        assertEquals(xmlResult, xmlDocument1);
    }
    
    @Test
    public void testFormatSimpleSnapshot2() throws Exception {
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document2);
        Boolean sort = true;
        
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();

        assertEquals(xmlResult, xmlDocument2);
    }
    
    @Test
    public void testSetParentChangeOrderWithoutGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//book[isbn = '8090119964'] SET PARENT $n AS /* POSITION \"1\" ", schemaName, document2);
        String code = String.format("txml:doc('%s', '%s')//book[1]", schemaName, document2);
        NodeList result = instance.eval(modificationCode + code, dbConn, sort).asNodeList();
        Long modificationTime = instance.lastTimeChanges();
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(modificationTime, dbApi.getNOW(), 125l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testSetParentChangeOrderWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//book[isbn = '8090119964'] SET PARENT $n AS /* POSITION \"1\" ", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        Long modificationTime = instance.lastTimeChanges();
        String code = String.format("txml:doc('%s', '%s')//book[1]", schemaName, document2);
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(modificationTime, dbApi.getNOW(), 125l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);

        assertEquals(expResult, result);
    }
    
    @Test
    public void testSetParentNoChangeWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//book[isbn = '8090119964'] SET PARENT $n AS /* POSITION \"2\" ", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        Long modificationTime = instance.lastTimeChanges();
        String code = String.format("txml:doc('%s', '%s')//book[3]", schemaName, document2);
        NodeList result = instance.eval(code, dbConn, sort).asNodeList();
        List<TNode> expList = new ArrayList<>();        
        expList.add(new TNode(modificationTime, dbApi.getNOW(), 125l, null, null, null, null, null, null, null, null, null, null, null, null));

        NodeList expResult = new TNodeList(null, expList);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSetParentDownWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//book[isbn = '8090119964'] SET PARENT $n AS //hr:author[@id = 'CMS'] POSITION \"1\" ", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document2);
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();
        String expDocument =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<library xmlns=\"http://eric.van-der-vlist.com/ns/library\" xmlns:hr=\"http://eric.van-der-vlist.com/ns/person\">\n" +
            "  <book id=\"b0836217462\">\n" +
            "    <isbn>\n" +
            "      0836217462\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"en\">\n" +
            "      Being a Dog Is a Full-Time Job\n" +
            "    </title>\n" +
            "    <hr:author id=\"CMS\">\n" +
            "      <book id=\"8090119964\">\n" +
            "        <isbn>\n" +
            "          8090119964\n" +
            "        </isbn>\n" +
            "        <title xml:lang=\"cz\">\n" +
            "          Babička\n" +
            "        </title>\n" +
            "        <hr:author>\n" +
            "          <hr:name>\n" +
            "            Božena Němcová\n" +
            "          </hr:name>\n" +
            "        </hr:author>\n" +
            "      </book>\n" +
            "      <hr:name>\n" +
            "        Charles M Schulz\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1922-11-26\n" +
            "      </hr:born>\n" +
            "      <hr:dead>\n" +
            "        2000-02-12\n" +
            "      </hr:dead>\n" +
            "    </hr:author>\n" +
            "    <character id=\"PP\">\n" +
            "      <hr:name>\n" +
            "        Peppermint Patty\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1966-08-22\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bold, brash and tomboyish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Snoopy\">\n" +
            "      <hr:name>\n" +
            "        Snoopy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1950-10-04\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        extroverted beagle\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Schroeder\">\n" +
            "      <hr:name>\n" +
            "        Schroeder\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1951-05-30\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        brought classical music to the Peanuts strip\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Lucy\">\n" +
            "      <hr:name>\n" +
            "        Lucy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1952-03-03\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bossy, crabby and selfish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "  </book>\n" +
            "</library>";
        
        assertEquals(xmlResult, expDocument);
    }
    
    @Test
    public void testSetParentUpWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//book[isbn = '8090119964']//hr:name SET PARENT $n AS /* POSITION \"1\"", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document2);
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();
        String expDocument =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<library xmlns=\"http://eric.van-der-vlist.com/ns/library\" xmlns:hr=\"http://eric.van-der-vlist.com/ns/person\">\n" +
            "  <hr:name>\n" +
            "    Božena Němcová\n" +
            "  </hr:name>\n" +
            "  <book id=\"b0836217462\">\n" +
            "    <isbn>\n" +
            "      0836217462\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"en\">\n" +
            "      Being a Dog Is a Full-Time Job\n" +
            "    </title>\n" +
            "    <hr:author id=\"CMS\">\n" +
            "      <hr:name>\n" +
            "        Charles M Schulz\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1922-11-26\n" +
            "      </hr:born>\n" +
            "      <hr:dead>\n" +
            "        2000-02-12\n" +
            "      </hr:dead>\n" +
            "    </hr:author>\n" +
            "    <character id=\"PP\">\n" +
            "      <hr:name>\n" +
            "        Peppermint Patty\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1966-08-22\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bold, brash and tomboyish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Snoopy\">\n" +
            "      <hr:name>\n" +
            "        Snoopy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1950-10-04\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        extroverted beagle\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Schroeder\">\n" +
            "      <hr:name>\n" +
            "        Schroeder\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1951-05-30\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        brought classical music to the Peanuts strip\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Lucy\">\n" +
            "      <hr:name>\n" +
            "        Lucy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1952-03-03\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bossy, crabby and selfish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "  </book>\n" +
            "  <book id=\"8090119964\">\n" +
            "    <isbn>\n" +
            "      8090119964\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"cz\">\n" +
            "      Babička\n" +
            "    </title>\n" +
            "    <hr:author>\n" +
            "    </hr:author>\n" +
            "  </book>\n" +
            "</library>";
        
        assertEquals(xmlResult, expDocument);
    }
    
    @Test
    public void testSetParentTextWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//book[isbn = '8090119964']/isbn/text() SET PARENT $n AS /*  POSITION \"1\"", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document2);
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();
        String expDocument =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<library xmlns=\"http://eric.van-der-vlist.com/ns/library\" xmlns:hr=\"http://eric.van-der-vlist.com/ns/person\">\n" +
            "  8090119964\n" +
            "  <book id=\"b0836217462\">\n" +
            "    <isbn>\n" +
            "      0836217462\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"en\">\n" +
            "      Being a Dog Is a Full-Time Job\n" +
            "    </title>\n" +
            "    <hr:author id=\"CMS\">\n" +
            "      <hr:name>\n" +
            "        Charles M Schulz\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1922-11-26\n" +
            "      </hr:born>\n" +
            "      <hr:dead>\n" +
            "        2000-02-12\n" +
            "      </hr:dead>\n" +
            "    </hr:author>\n" +
            "    <character id=\"PP\">\n" +
            "      <hr:name>\n" +
            "        Peppermint Patty\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1966-08-22\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bold, brash and tomboyish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Snoopy\">\n" +
            "      <hr:name>\n" +
            "        Snoopy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1950-10-04\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        extroverted beagle\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Schroeder\">\n" +
            "      <hr:name>\n" +
            "        Schroeder\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1951-05-30\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        brought classical music to the Peanuts strip\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Lucy\">\n" +
            "      <hr:name>\n" +
            "        Lucy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1952-03-03\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bossy, crabby and selfish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "  </book>\n" +
            "  <book id=\"8090119964\">\n" +
            "    <isbn>\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"cz\">\n" +
            "      Babička\n" +
            "    </title>\n" +
            "    <hr:author>\n" +
            "      <hr:name>\n" +
            "        Božena Němcová\n" +
            "      </hr:name>\n" +
            "    </hr:author>\n" +
            "  </book>\n" +
            "</library>";
        
        assertEquals(xmlResult, expDocument);
    }
    
    @Test
    public void testSetParentAttributeWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//@id[. = 'PP'] SET PARENT $n AS /*  POSITION \"1\"", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document2);
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();
        String expDocument =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<library xmlns=\"http://eric.van-der-vlist.com/ns/library\" xmlns:hr=\"http://eric.van-der-vlist.com/ns/person\" id=\"PP\">\n" +
            "  <book id=\"b0836217462\">\n" +
            "    <isbn>\n" +
            "      0836217462\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"en\">\n" +
            "      Being a Dog Is a Full-Time Job\n" +
            "    </title>\n" +
            "    <hr:author id=\"CMS\">\n" +
            "      <hr:name>\n" +
            "        Charles M Schulz\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1922-11-26\n" +
            "      </hr:born>\n" +
            "      <hr:dead>\n" +
            "        2000-02-12\n" +
            "      </hr:dead>\n" +
            "    </hr:author>\n" +
            "    <character>\n" +
            "      <hr:name>\n" +
            "        Peppermint Patty\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1966-08-22\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bold, brash and tomboyish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Snoopy\">\n" +
            "      <hr:name>\n" +
            "        Snoopy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1950-10-04\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        extroverted beagle\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Schroeder\">\n" +
            "      <hr:name>\n" +
            "        Schroeder\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1951-05-30\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        brought classical music to the Peanuts strip\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Lucy\">\n" +
            "      <hr:name>\n" +
            "        Lucy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1952-03-03\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bossy, crabby and selfish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "  </book>\n" +
            "  <book id=\"8090119964\">\n" +
            "    <isbn>\n" +
            "      8090119964\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"cz\">\n" +
            "      Babička\n" +
            "    </title>\n" +
            "    <hr:author>\n" +
            "      <hr:name>\n" +
            "        Božena Němcová\n" +
            "      </hr:name>\n" +
            "    </hr:author>\n" +
            "  </book>\n" +
            "</library>";
        
        assertEquals(xmlResult, expDocument);
    }
    
    @Test
    public void testChangeTextWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//isbn[. = '0836217462'] {DELETE NODE $n/text() insert $n VALUE \"0836217463\" }", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document2);
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();
        String expDocument =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<library xmlns=\"http://eric.van-der-vlist.com/ns/library\" xmlns:hr=\"http://eric.van-der-vlist.com/ns/person\">\n" +
            "  <book id=\"b0836217462\">\n" +
            "    <isbn>\n" +
            "      0836217463\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"en\">\n" +
            "      Being a Dog Is a Full-Time Job\n" +
            "    </title>\n" +
            "    <hr:author id=\"CMS\">\n" +
            "      <hr:name>\n" +
            "        Charles M Schulz\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1922-11-26\n" +
            "      </hr:born>\n" +
            "      <hr:dead>\n" +
            "        2000-02-12\n" +
            "      </hr:dead>\n" +
            "    </hr:author>\n" +
            "    <character id=\"PP\">\n" +
            "      <hr:name>\n" +
            "        Peppermint Patty\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1966-08-22\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bold, brash and tomboyish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Snoopy\">\n" +
            "      <hr:name>\n" +
            "        Snoopy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1950-10-04\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        extroverted beagle\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Schroeder\">\n" +
            "      <hr:name>\n" +
            "        Schroeder\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1951-05-30\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        brought classical music to the Peanuts strip\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "    <character id=\"Lucy\">\n" +
            "      <hr:name>\n" +
            "        Lucy\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1952-03-03\n" +
            "      </hr:born>\n" +
            "      <qualification>\n" +
            "        bossy, crabby and selfish\n" +
            "      </qualification>\n" +
            "    </character>\n" +
            "  </book>\n" +
            "  <book id=\"8090119964\">\n" +
            "    <isbn>\n" +
            "      8090119964\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"cz\">\n" +
            "      Babička\n" +
            "    </title>\n" +
            "    <hr:author>\n" +
            "      <hr:name>\n" +
            "        Božena Němcová\n" +
            "      </hr:name>\n" +
            "    </hr:author>\n" +
            "  </book>\n" +
            "</library>";
        
        assertEquals(xmlResult, expDocument);
    }
    
    @Test
    public void testMultiSetParentWithGap() throws Exception {
        Boolean sort = true;
        
        String modificationCode = String.format("for $n in txml:doc(\"%s\", \"%s\")//character SET PARENT $n AS /*", schemaName, document2);
        instance.eval(modificationCode, dbConn, sort);
        String code = String.format("txml:doc-snapshot('%s', '%s', 'Now')", schemaName, document2);
        XMLEventReader reader = instance.eval(code, dbConn, sort).asXMLEventReader();
        String xmlResult = reader.getXMLDocFormatA();
        String expDocument =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<library xmlns=\"http://eric.van-der-vlist.com/ns/library\" xmlns:hr=\"http://eric.van-der-vlist.com/ns/person\">\n" +
            "  <book id=\"b0836217462\">\n" +
            "    <isbn>\n" +
            "      0836217462\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"en\">\n" +
            "      Being a Dog Is a Full-Time Job\n" +
            "    </title>\n" +
            "    <hr:author id=\"CMS\">\n" +
            "      <hr:name>\n" +
            "        Charles M Schulz\n" +
            "      </hr:name>\n" +
            "      <hr:born>\n" +
            "        1922-11-26\n" +
            "      </hr:born>\n" +
            "      <hr:dead>\n" +
            "        2000-02-12\n" +
            "      </hr:dead>\n" +
            "    </hr:author>\n" +
            "  </book>\n" +
            "  <book id=\"8090119964\">\n" +
            "    <isbn>\n" +
            "      8090119964\n" +
            "    </isbn>\n" +
            "    <title xml:lang=\"cz\">\n" +
            "      Babička\n" +
            "    </title>\n" +
            "    <hr:author>\n" +
            "      <hr:name>\n" +
            "        Božena Němcová\n" +
            "      </hr:name>\n" +
            "    </hr:author>\n" +
            "  </book>\n" +
            "  <character id=\"PP\">\n" +
            "    <hr:name>\n" +
            "      Peppermint Patty\n" +
            "    </hr:name>\n" +
            "    <hr:born>\n" +
            "      1966-08-22\n" +
            "    </hr:born>\n" +
            "    <qualification>\n" +
            "      bold, brash and tomboyish\n" +
            "    </qualification>\n" +
            "  </character>\n" +
            "  <character id=\"Snoopy\">\n" +
            "    <hr:name>\n" +
            "      Snoopy\n" +
            "    </hr:name>\n" +
            "    <hr:born>\n" +
            "      1950-10-04\n" +
            "    </hr:born>\n" +
            "    <qualification>\n" +
            "      extroverted beagle\n" +
            "    </qualification>\n" +
            "  </character>\n" +
            "  <character id=\"Schroeder\">\n" +
            "    <hr:name>\n" +
            "      Schroeder\n" +
            "    </hr:name>\n" +
            "    <hr:born>\n" +
            "      1951-05-30\n" +
            "    </hr:born>\n" +
            "    <qualification>\n" +
            "      brought classical music to the Peanuts strip\n" +
            "    </qualification>\n" +
            "  </character>\n" +
            "  <character id=\"Lucy\">\n" +
            "    <hr:name>\n" +
            "      Lucy\n" +
            "    </hr:name>\n" +
            "    <hr:born>\n" +
            "      1952-03-03\n" +
            "    </hr:born>\n" +
            "    <qualification>\n" +
            "      bossy, crabby and selfish\n" +
            "    </qualification>\n" +
            "  </character>\n" +
            "</library>";
        
        assertEquals(xmlResult, expDocument);
    }
    
    private final String xmlDocument1 =
        "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
        "<bookstore>\n" +
        "  <book category=\"COOKING\">\n" +
        "    <name lang=\"en\">\n" +
        "      Everyday Italian\n" +
        "    </name>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        Giada De Laurentiis\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <year>\n" +
        "      2001\n" +
        "    </year>\n" +
        "    <price>\n" +
        "      30.00\n" +
        "    </price>\n" +
        "  </book>\n" +
        "  <book category=\"CHILDREN\">\n" +
        "    <name lang=\"en\">\n" +
        "      Harry Potter\n" +
        "    </name>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        J K. Rowling\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <year>\n" +
        "      2002\n" +
        "    </year>\n" +
        "    <price>\n" +
        "      29.99\n" +
        "    </price>\n" +
        "  </book>\n" +
        "  <book category=\"WEB\">\n" +
        "    <name lang=\"en\">\n" +
        "      XQuery Kick Start\n" +
        "    </name>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        James McGovern\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        Per Bothner\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        Kurt Cagle\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        James Linn\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        Vaidyanathan Nagarajan\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <year>\n" +
        "      2003\n" +
        "    </year>\n" +
        "    <price>\n" +
        "      49.99\n" +
        "    </price>\n" +
        "  </book>\n" +
        "  <book category=\"WEB\">\n" +
        "    <name lang=\"en\">\n" +
        "      Learning XML\n" +
        "    </name>\n" +
        "    <author>\n" +
        "      <name>\n" +
        "        Erik T. Ray\n" +
        "      </name>\n" +
        "    </author>\n" +
        "    <year>\n" +
        "      2004\n" +
        "    </year>\n" +
        "    <price>\n" +
        "      39.95\n" +
        "    </price>\n" +
        "  </book>\n" +
        "</bookstore>";
    
    private final String xmlDocument2 =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<library xmlns=\"http://eric.van-der-vlist.com/ns/library\" xmlns:hr=\"http://eric.van-der-vlist.com/ns/person\">\n" +
        "  <book id=\"b0836217462\">\n" +
        "    <isbn>\n" +
        "      0836217462\n" +
        "    </isbn>\n" +
        "    <title xml:lang=\"en\">\n" +
        "      Being a Dog Is a Full-Time Job\n" +
        "    </title>\n" +
        "    <hr:author id=\"CMS\">\n" +
        "      <hr:name>\n" +
        "        Charles M Schulz\n" +
        "      </hr:name>\n" +
        "      <hr:born>\n" +
        "        1922-11-26\n" +
        "      </hr:born>\n" +
        "      <hr:dead>\n" +
        "        2000-02-12\n" +
        "      </hr:dead>\n" +
        "    </hr:author>\n" +
        "    <character id=\"PP\">\n" +
        "      <hr:name>\n" +
        "        Peppermint Patty\n" +
        "      </hr:name>\n" +
        "      <hr:born>\n" +
        "        1966-08-22\n" +
        "      </hr:born>\n" +
        "      <qualification>\n" +
        "        bold, brash and tomboyish\n" +
        "      </qualification>\n" +
        "    </character>\n" +
        "    <character id=\"Snoopy\">\n" +
        "      <hr:name>\n" +
        "        Snoopy\n" +
        "      </hr:name>\n" +
        "      <hr:born>\n" +
        "        1950-10-04\n" +
        "      </hr:born>\n" +
        "      <qualification>\n" +
        "        extroverted beagle\n" +
        "      </qualification>\n" +
        "    </character>\n" +
        "    <character id=\"Schroeder\">\n" +
        "      <hr:name>\n" +
        "        Schroeder\n" +
        "      </hr:name>\n" +
        "      <hr:born>\n" +
        "        1951-05-30\n" +
        "      </hr:born>\n" +
        "      <qualification>\n" +
        "        brought classical music to the Peanuts strip\n" +
        "      </qualification>\n" +
        "    </character>\n" +
        "    <character id=\"Lucy\">\n" +
        "      <hr:name>\n" +
        "        Lucy\n" +
        "      </hr:name>\n" +
        "      <hr:born>\n" +
        "        1952-03-03\n" +
        "      </hr:born>\n" +
        "      <qualification>\n" +
        "        bossy, crabby and selfish\n" +
        "      </qualification>\n" +
        "    </character>\n" +
        "  </book>\n" +
        "  <book id=\"8090119964\">\n" +
        "    <isbn>\n" +
        "      8090119964\n" +
        "    </isbn>\n" +
        "    <title xml:lang=\"cz\">\n" +
        "      Babička\n" +
        "    </title>\n" +
        "    <hr:author>\n" +
        "      <hr:name>\n" +
        "        Božena Němcová\n" +
        "      </hr:name>\n" +
        "    </hr:author>\n" +
        "  </book>\n" +
        "</library>";
}
