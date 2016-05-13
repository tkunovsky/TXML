grammar XQuery;

main
    :   prolog body EOF
    ;

prolog  
    :   declareOption* 
    ;

declareOption
    :   declareOptionConnection
    |   declareOptionTimeFormat
    |   declareOptionSort
    ;

declareOptionConnection
    :   DECLARE OPTION TXML_COLON_CONNECTION connectionUrl SEMICOLON
    ;

declareOptionTimeFormat
    :   DECLARE OPTION TXML_COLON_TIME_FORMAT timeFormat SEMICOLON
    ;

declareOptionSort
    :   DECLARE OPTION TXML_COLON_SORT sort SEMICOLON
    ;

documentName
    :   StringLiteral
    ;

schemaName
    :   StringLiteral
    ;

timeFormat
    :   StringLiteral
    ;

sort
    :   StringLiteral
    ;

connectionUrl
    :   StringLiteral
    ;

fileFullName
    :   StringLiteral   
    ;
        
body
    :   statement+
    ;

statement
    :   update
    |   xPathQuery
    |   snapshotQuery
    |   initSchema
    |   deinitSchema
    |   storeDocument
    ;

initSchema
    :   TXML_COLON_INITSCHEMA LPAR schemaName RPAR
    ;

deinitSchema
    :   TXML_COLON_DEINITSCHEMA LPAR schemaName RPAR
    ;

storeDocument
    :   storeDocumentWithName
    |   storeDocumentWithoutName
    ;

storeDocumentWithName
    :   TXML_COLON_STORE LPAR schemaName COMMA fileFullName COMMA documentName RPAR
    ;
    
storeDocumentWithoutName
    :   TXML_COLON_STORE LPAR schemaName COMMA fileFullName RPAR
    ;

doc
    :   TXML_COLON_DOC LPAR schemaName COMMA documentName RPAR
    ;

snapshotQuery
    :   TXML_COLON_DOCSNAPSHOT LPAR schemaName COMMA documentName COMMA time RPAR
    ; 

absPathExprWithDoc
    :  doc  absPathExpr   
    ;

xPathQuery
    :  absPathExprWithDoc
    ;

update     	     
    :   forClause updateExpr
    |   forClause LCURBRAC updateExpr+ RCURBRAC
    ;

forClause	       
    :   FOR variable IN absPathExprWithDoc
    ;

updateExpr
    :   insertExpr 
    |   deleteExpr 
    |   parentExpr
    ;
 
insertExpr       
    :   INSERT insertXPathExpression (VALUE value)? (POSITION insert_pos)?
    ;

deleteExpr
    :   DELETE NODE childXPathExpression
    ;

parentExpr       
    :   SET PARENT parentXPathExpression (POSITION insert_pos)?
    ;


parentXPathExpression 
    :   variable AS steps
    ;

childXPathExpression
    :   variable steps
    ;

simpleXPathExpression
    :   ('/' elementName)* ('/' attributeName)?
    ;

insertXPathExpression
    :   variable simpleXPathExpression
    ;
  
absPathExpr        
    :   steps
    ;

steps 
    :   (directStep|undirectStep) steps*
    ;
    

undirectStep
    :   '//' nodeGenerator predicate*
    ;
    
directStep
    :   '/' nodeGenerator predicate*
    ;

predicate	       
    :   '[' expr ']'
    ;

xPathInExpr
    :   nodeGenerator predicate* steps?
    ;

expr             
    :   xPathInExpr operator value
    |   value operator xPathInExpr
    |   position
    ;
            
position
    :   integerNumber
    |   last
    ;

last
    :   LAST
    ;

integerNumber
    :   DIGIT+
    ;

operator         
    :   OpEq
    |   OpNEq
    |   OpLess
    |   OpMore
    |   OpLe
    |   OpGe 
    |   OpPrecedes
    |   OpFollows
    |   OpLMeets
    |   OpRMeets
    |   OpOverlaps
    |   OpContains
    |   OpIn
    ;
        
variable
    :   '$' Name
    ;

nodeGenerator    
    :   elementName
    |   attributeName
    |   nodesByType
    |   currentNodes
    |   parentNodes
    |   childsElements
    ;

currentNodes 
    :   DOT
    ;

parentNodes
    : TWO_DOTS
    ;

childsElements
    :   STAR
    ;

nodesByType
    :   textNodes
    ;

textNodes
    :   TEXT LPAR RPAR
    ;

elementName      
    :   (Name COLON)? Name
    ;

attributeName    
    :   ('@' | ATTRIBUTE TWO_COLON) Name
    |   ('@' | ATTRIBUTE TWO_COLON) Name COLON Name
    ;

value            
    :   StringLiteral
    ;
 
insert_pos
    :   StringLiteral
    ;

time
    :   StringLiteral  
    ;

format
    :   StringLiteral  
    ;

OpEq             
    :   '='
    ;

OpNEq            
    :   '!='
    ;

OpLess   
    :  '<';

OpMore   
    :  '>';

OpLe   
    :  '<=';

OpGe   
    :  '>=';

OpPrecedes             
    :   'PRECEDES'
    ;

OpFollows             
    :   'FOLLOWS'
    ;

OpLMeets             
    :   'MEETS'
    |   'LMEETS'
    ;

OpRMeets             
    :   'RMEETS'
    ;

OpOverlaps             
    :   'OVERLAPS'
    ;

OpContains             
    :   'CONTAINS'
    ;

OpIn             
    :   'IN'
    ;

StringLiteral    
    :   '"' ~'"'* '"'
    |   '\'' ~'\'' * '\''
    ;

Whitespace
    :   (' '|'\t'|'\n'|'\r')+ ->skip
    ;

COLON   
    :   ':';

AT   
    :   '@';

PATHSEP 
    :   '/';

ABRPATH   
    :   '//';

DASH   
    :   '-';

LBRAC   
    :   '[';

RBRAC   
    :   ']';

LPAR
    :   '(';

RPAR
    :   ')';

LCURBRAC
    :   '{';

RCURBRAC
    :   '}';

SEMICOLON 
    :   ';';

COMMA
    :   ',';

TXML_COLON_DOCSNAPSHOT
    :   'txml:doc-snapshot'
    |   'TXML:DOC-SNAPSHOT'
    ;

FOR
    :   'FOR'
    |   'for'
    ;

IN
    :   'IN'
    |   'in'
    ;

INSERT
    :   'INSERT'
    |   'insert'
    ;

VALUE
    :   'VALUE'
    |   'value'
    ;

POSITION
    :   'POSITION'
    |   'position'
    ;

DELETE
    :   'DELETE'
    |   'delete'
    ;

NODE
    :   'NODE'
    |   'node'
    ;

SET
    :   'SET'
    |   'set'
    ;

PARENT
    :   'PARENT'
    |   'parent'
    ;

AS
    :   'AS'
    |   'as'
    ;

DECLARE 
    :   'declare'
    |   'DECLARE'
    ;

OPTION
    :   'option'
    |   'OPTION'
    ;

DOCUMENT
    :   'document'
    |   'DOCUMENT'
    ;

SCHEMA
    :   'schema'
    |   'SCHEMA'
    ;

TXML_COLON_CONNECTION
    :   'txml:connection'
    |   'TXML:CONNECTION'
    ;

TXML_COLON_TIME_FORMAT
    :   'txml:time-format'
    |   'TXML:TIME-FORMAT'
    ;

TXML_COLON_SORT
    :   'txml:sort'
    |   'TXML:SORT'
    ;    

TXML_COLON_INITSCHEMA
    :   'txml:init-schema'
    |   'TXML:INIT-SCHEMA';

TXML_COLON_DEINITSCHEMA
    :   'txml:deinit-schema'
    |   'txml:DEINIT-SCHEMA';

TXML_COLON_STORE
    :   'TXML:STORE'
    |   'txml:store';

TXML_COLON_DOC
    :   'txml:doc'
    |   'TXML:DOC'
    ;

TEXT
    :   'text'
    |   'TEXT'
    ;

DOT
    :   '.'
    ;

STAR
    :   '*'
    ;

TWO_DOTS
    :   '..'
    ;

DIGIT   
    :   ('0'..'9')
    ;

LAST 
    :   'last()';

TWO_COLON
    :   '::';

ATTRIBUTE
    :   'attribute'
    |   'ATTRIBUTE'
    ;

BlockComment 
    : '(:' .*? ':)' -> skip
    ;

VariableStart
    :   '$';

Name	           
    :   NameStartChar NameChar*
    ;

NameChar	       
    :   NameStartChar | '-' | '.' | [0-9]
    ;

NameStartChar	   
    :   [A-Z] | '_' | [a-z]
    ;