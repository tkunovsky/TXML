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
package txml.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XQueryParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OpEq=1, OpNEq=2, OpLess=3, OpMore=4, OpLe=5, OpGe=6, OpPrecedes=7, OpFollows=8, 
		OpLMeets=9, OpRMeets=10, OpOverlaps=11, OpContains=12, OpIn=13, StringLiteral=14, 
		Whitespace=15, COLON=16, AT=17, PATHSEP=18, ABRPATH=19, DASH=20, LBRAC=21, 
		RBRAC=22, LPAR=23, RPAR=24, LCURBRAC=25, RCURBRAC=26, SEMICOLON=27, COMMA=28, 
		TXML_COLON_DOCSNAPSHOT=29, FOR=30, IN=31, INSERT=32, VALUE=33, POSITION=34, 
		DELETE=35, NODE=36, SET=37, PARENT=38, AS=39, DECLARE=40, OPTION=41, DOCUMENT=42, 
		SCHEMA=43, TXML_COLON_CONNECTION=44, TXML_COLON_TIME_FORMAT=45, TXML_COLON_SORT=46, 
		TXML_COLON_INITSCHEMA=47, TXML_COLON_DEINITSCHEMA=48, TXML_COLON_STORE=49, 
		TXML_COLON_DOC=50, TEXT=51, DOT=52, STAR=53, TWO_DOTS=54, DIGIT=55, LAST=56, 
		TWO_COLON=57, ATTRIBUTE=58, BlockComment=59, VariableStart=60, Name=61, 
		NameChar=62, NameStartChar=63;
	public static final int
		RULE_main = 0, RULE_prolog = 1, RULE_declareOption = 2, RULE_declareOptionConnection = 3, 
		RULE_declareOptionTimeFormat = 4, RULE_declareOptionSort = 5, RULE_documentName = 6, 
		RULE_schemaName = 7, RULE_timeFormat = 8, RULE_sort = 9, RULE_connectionUrl = 10, 
		RULE_fileFullName = 11, RULE_body = 12, RULE_statement = 13, RULE_initSchema = 14, 
		RULE_deinitSchema = 15, RULE_storeDocument = 16, RULE_storeDocumentWithName = 17, 
		RULE_storeDocumentWithoutName = 18, RULE_doc = 19, RULE_snapshotQuery = 20, 
		RULE_absPathExprWithDoc = 21, RULE_xPathQuery = 22, RULE_update = 23, 
		RULE_forClause = 24, RULE_updateExpr = 25, RULE_insertExpr = 26, RULE_deleteExpr = 27, 
		RULE_parentExpr = 28, RULE_parentXPathExpression = 29, RULE_childXPathExpression = 30, 
		RULE_simpleXPathExpression = 31, RULE_insertXPathExpression = 32, RULE_absPathExpr = 33, 
		RULE_steps = 34, RULE_undirectStep = 35, RULE_directStep = 36, RULE_predicate = 37, 
		RULE_xPathInExpr = 38, RULE_expr = 39, RULE_position = 40, RULE_last = 41, 
		RULE_integerNumber = 42, RULE_operator = 43, RULE_variable = 44, RULE_nodeGenerator = 45, 
		RULE_currentNodes = 46, RULE_parentNodes = 47, RULE_childsElements = 48, 
		RULE_nodesByType = 49, RULE_textNodes = 50, RULE_elementName = 51, RULE_attributeName = 52, 
		RULE_value = 53, RULE_insert_pos = 54, RULE_time = 55, RULE_format = 56;
	public static final String[] ruleNames = {
		"main", "prolog", "declareOption", "declareOptionConnection", "declareOptionTimeFormat", 
		"declareOptionSort", "documentName", "schemaName", "timeFormat", "sort", 
		"connectionUrl", "fileFullName", "body", "statement", "initSchema", "deinitSchema", 
		"storeDocument", "storeDocumentWithName", "storeDocumentWithoutName", 
		"doc", "snapshotQuery", "absPathExprWithDoc", "xPathQuery", "update", 
		"forClause", "updateExpr", "insertExpr", "deleteExpr", "parentExpr", "parentXPathExpression", 
		"childXPathExpression", "simpleXPathExpression", "insertXPathExpression", 
		"absPathExpr", "steps", "undirectStep", "directStep", "predicate", "xPathInExpr", 
		"expr", "position", "last", "integerNumber", "operator", "variable", "nodeGenerator", 
		"currentNodes", "parentNodes", "childsElements", "nodesByType", "textNodes", 
		"elementName", "attributeName", "value", "insert_pos", "time", "format"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'='", "'!='", "'<'", "'>'", "'<='", "'>='", "'PRECEDES'", "'FOLLOWS'", 
		null, "'RMEETS'", "'OVERLAPS'", "'CONTAINS'", "'IN'", null, null, "':'", 
		"'@'", "'/'", "'//'", "'-'", "'['", "']'", "'('", "')'", "'{'", "'}'", 
		"';'", "','", null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "'.'", "'*'", "'..'", null, "'last()'", "'::'", null, null, "'$'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OpEq", "OpNEq", "OpLess", "OpMore", "OpLe", "OpGe", "OpPrecedes", 
		"OpFollows", "OpLMeets", "OpRMeets", "OpOverlaps", "OpContains", "OpIn", 
		"StringLiteral", "Whitespace", "COLON", "AT", "PATHSEP", "ABRPATH", "DASH", 
		"LBRAC", "RBRAC", "LPAR", "RPAR", "LCURBRAC", "RCURBRAC", "SEMICOLON", 
		"COMMA", "TXML_COLON_DOCSNAPSHOT", "FOR", "IN", "INSERT", "VALUE", "POSITION", 
		"DELETE", "NODE", "SET", "PARENT", "AS", "DECLARE", "OPTION", "DOCUMENT", 
		"SCHEMA", "TXML_COLON_CONNECTION", "TXML_COLON_TIME_FORMAT", "TXML_COLON_SORT", 
		"TXML_COLON_INITSCHEMA", "TXML_COLON_DEINITSCHEMA", "TXML_COLON_STORE", 
		"TXML_COLON_DOC", "TEXT", "DOT", "STAR", "TWO_DOTS", "DIGIT", "LAST", 
		"TWO_COLON", "ATTRIBUTE", "BlockComment", "VariableStart", "Name", "NameChar", 
		"NameStartChar"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "XQuery.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public XQueryParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class MainContext extends ParserRuleContext {
		public PrologContext prolog() {
			return getRuleContext(PrologContext.class,0);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode EOF() { return getToken(XQueryParser.EOF, 0); }
		public MainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_main; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterMain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitMain(this);
		}
	}

	public final MainContext main() throws RecognitionException {
		MainContext _localctx = new MainContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_main);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			prolog();
			setState(115);
			body();
			setState(116);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrologContext extends ParserRuleContext {
		public List<DeclareOptionContext> declareOption() {
			return getRuleContexts(DeclareOptionContext.class);
		}
		public DeclareOptionContext declareOption(int i) {
			return getRuleContext(DeclareOptionContext.class,i);
		}
		public PrologContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prolog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterProlog(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitProlog(this);
		}
	}

	public final PrologContext prolog() throws RecognitionException {
		PrologContext _localctx = new PrologContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_prolog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DECLARE) {
				{
				{
				setState(118);
				declareOption();
				}
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclareOptionContext extends ParserRuleContext {
		public DeclareOptionConnectionContext declareOptionConnection() {
			return getRuleContext(DeclareOptionConnectionContext.class,0);
		}
		public DeclareOptionTimeFormatContext declareOptionTimeFormat() {
			return getRuleContext(DeclareOptionTimeFormatContext.class,0);
		}
		public DeclareOptionSortContext declareOptionSort() {
			return getRuleContext(DeclareOptionSortContext.class,0);
		}
		public DeclareOptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declareOption; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDeclareOption(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDeclareOption(this);
		}
	}

	public final DeclareOptionContext declareOption() throws RecognitionException {
		DeclareOptionContext _localctx = new DeclareOptionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_declareOption);
		try {
			setState(127);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(124);
				declareOptionConnection();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(125);
				declareOptionTimeFormat();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(126);
				declareOptionSort();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclareOptionConnectionContext extends ParserRuleContext {
		public TerminalNode DECLARE() { return getToken(XQueryParser.DECLARE, 0); }
		public TerminalNode OPTION() { return getToken(XQueryParser.OPTION, 0); }
		public TerminalNode TXML_COLON_CONNECTION() { return getToken(XQueryParser.TXML_COLON_CONNECTION, 0); }
		public ConnectionUrlContext connectionUrl() {
			return getRuleContext(ConnectionUrlContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParser.SEMICOLON, 0); }
		public DeclareOptionConnectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declareOptionConnection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDeclareOptionConnection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDeclareOptionConnection(this);
		}
	}

	public final DeclareOptionConnectionContext declareOptionConnection() throws RecognitionException {
		DeclareOptionConnectionContext _localctx = new DeclareOptionConnectionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_declareOptionConnection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			match(DECLARE);
			setState(130);
			match(OPTION);
			setState(131);
			match(TXML_COLON_CONNECTION);
			setState(132);
			connectionUrl();
			setState(133);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclareOptionTimeFormatContext extends ParserRuleContext {
		public TerminalNode DECLARE() { return getToken(XQueryParser.DECLARE, 0); }
		public TerminalNode OPTION() { return getToken(XQueryParser.OPTION, 0); }
		public TerminalNode TXML_COLON_TIME_FORMAT() { return getToken(XQueryParser.TXML_COLON_TIME_FORMAT, 0); }
		public TimeFormatContext timeFormat() {
			return getRuleContext(TimeFormatContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParser.SEMICOLON, 0); }
		public DeclareOptionTimeFormatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declareOptionTimeFormat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDeclareOptionTimeFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDeclareOptionTimeFormat(this);
		}
	}

	public final DeclareOptionTimeFormatContext declareOptionTimeFormat() throws RecognitionException {
		DeclareOptionTimeFormatContext _localctx = new DeclareOptionTimeFormatContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_declareOptionTimeFormat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			match(DECLARE);
			setState(136);
			match(OPTION);
			setState(137);
			match(TXML_COLON_TIME_FORMAT);
			setState(138);
			timeFormat();
			setState(139);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclareOptionSortContext extends ParserRuleContext {
		public TerminalNode DECLARE() { return getToken(XQueryParser.DECLARE, 0); }
		public TerminalNode OPTION() { return getToken(XQueryParser.OPTION, 0); }
		public TerminalNode TXML_COLON_SORT() { return getToken(XQueryParser.TXML_COLON_SORT, 0); }
		public SortContext sort() {
			return getRuleContext(SortContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParser.SEMICOLON, 0); }
		public DeclareOptionSortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declareOptionSort; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDeclareOptionSort(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDeclareOptionSort(this);
		}
	}

	public final DeclareOptionSortContext declareOptionSort() throws RecognitionException {
		DeclareOptionSortContext _localctx = new DeclareOptionSortContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_declareOptionSort);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(DECLARE);
			setState(142);
			match(OPTION);
			setState(143);
			match(TXML_COLON_SORT);
			setState(144);
			sort();
			setState(145);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DocumentNameContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public DocumentNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_documentName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDocumentName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDocumentName(this);
		}
	}

	public final DocumentNameContext documentName() throws RecognitionException {
		DocumentNameContext _localctx = new DocumentNameContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_documentName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemaNameContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public SchemaNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterSchemaName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitSchemaName(this);
		}
	}

	public final SchemaNameContext schemaName() throws RecognitionException {
		SchemaNameContext _localctx = new SchemaNameContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_schemaName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimeFormatContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public TimeFormatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timeFormat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterTimeFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitTimeFormat(this);
		}
	}

	public final TimeFormatContext timeFormat() throws RecognitionException {
		TimeFormatContext _localctx = new TimeFormatContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_timeFormat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SortContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public SortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sort; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterSort(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitSort(this);
		}
	}

	public final SortContext sort() throws RecognitionException {
		SortContext _localctx = new SortContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_sort);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConnectionUrlContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public ConnectionUrlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connectionUrl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterConnectionUrl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitConnectionUrl(this);
		}
	}

	public final ConnectionUrlContext connectionUrl() throws RecognitionException {
		ConnectionUrlContext _localctx = new ConnectionUrlContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_connectionUrl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FileFullNameContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public FileFullNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fileFullName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterFileFullName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitFileFullName(this);
		}
	}

	public final FileFullNameContext fileFullName() throws RecognitionException {
		FileFullNameContext _localctx = new FileFullNameContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_fileFullName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BodyContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitBody(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(159);
				statement();
				}
				}
				setState(162); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TXML_COLON_DOCSNAPSHOT) | (1L << FOR) | (1L << TXML_COLON_INITSCHEMA) | (1L << TXML_COLON_DEINITSCHEMA) | (1L << TXML_COLON_STORE) | (1L << TXML_COLON_DOC))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public UpdateContext update() {
			return getRuleContext(UpdateContext.class,0);
		}
		public XPathQueryContext xPathQuery() {
			return getRuleContext(XPathQueryContext.class,0);
		}
		public SnapshotQueryContext snapshotQuery() {
			return getRuleContext(SnapshotQueryContext.class,0);
		}
		public InitSchemaContext initSchema() {
			return getRuleContext(InitSchemaContext.class,0);
		}
		public DeinitSchemaContext deinitSchema() {
			return getRuleContext(DeinitSchemaContext.class,0);
		}
		public StoreDocumentContext storeDocument() {
			return getRuleContext(StoreDocumentContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_statement);
		try {
			setState(170);
			switch (_input.LA(1)) {
			case FOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(164);
				update();
				}
				break;
			case TXML_COLON_DOC:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				xPathQuery();
				}
				break;
			case TXML_COLON_DOCSNAPSHOT:
				enterOuterAlt(_localctx, 3);
				{
				setState(166);
				snapshotQuery();
				}
				break;
			case TXML_COLON_INITSCHEMA:
				enterOuterAlt(_localctx, 4);
				{
				setState(167);
				initSchema();
				}
				break;
			case TXML_COLON_DEINITSCHEMA:
				enterOuterAlt(_localctx, 5);
				{
				setState(168);
				deinitSchema();
				}
				break;
			case TXML_COLON_STORE:
				enterOuterAlt(_localctx, 6);
				{
				setState(169);
				storeDocument();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InitSchemaContext extends ParserRuleContext {
		public TerminalNode TXML_COLON_INITSCHEMA() { return getToken(XQueryParser.TXML_COLON_INITSCHEMA, 0); }
		public TerminalNode LPAR() { return getToken(XQueryParser.LPAR, 0); }
		public SchemaNameContext schemaName() {
			return getRuleContext(SchemaNameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(XQueryParser.RPAR, 0); }
		public InitSchemaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initSchema; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterInitSchema(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitInitSchema(this);
		}
	}

	public final InitSchemaContext initSchema() throws RecognitionException {
		InitSchemaContext _localctx = new InitSchemaContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_initSchema);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(TXML_COLON_INITSCHEMA);
			setState(173);
			match(LPAR);
			setState(174);
			schemaName();
			setState(175);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeinitSchemaContext extends ParserRuleContext {
		public TerminalNode TXML_COLON_DEINITSCHEMA() { return getToken(XQueryParser.TXML_COLON_DEINITSCHEMA, 0); }
		public TerminalNode LPAR() { return getToken(XQueryParser.LPAR, 0); }
		public SchemaNameContext schemaName() {
			return getRuleContext(SchemaNameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(XQueryParser.RPAR, 0); }
		public DeinitSchemaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deinitSchema; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDeinitSchema(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDeinitSchema(this);
		}
	}

	public final DeinitSchemaContext deinitSchema() throws RecognitionException {
		DeinitSchemaContext _localctx = new DeinitSchemaContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_deinitSchema);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(TXML_COLON_DEINITSCHEMA);
			setState(178);
			match(LPAR);
			setState(179);
			schemaName();
			setState(180);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StoreDocumentContext extends ParserRuleContext {
		public StoreDocumentWithNameContext storeDocumentWithName() {
			return getRuleContext(StoreDocumentWithNameContext.class,0);
		}
		public StoreDocumentWithoutNameContext storeDocumentWithoutName() {
			return getRuleContext(StoreDocumentWithoutNameContext.class,0);
		}
		public StoreDocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_storeDocument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterStoreDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitStoreDocument(this);
		}
	}

	public final StoreDocumentContext storeDocument() throws RecognitionException {
		StoreDocumentContext _localctx = new StoreDocumentContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_storeDocument);
		try {
			setState(184);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(182);
				storeDocumentWithName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
				storeDocumentWithoutName();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StoreDocumentWithNameContext extends ParserRuleContext {
		public TerminalNode TXML_COLON_STORE() { return getToken(XQueryParser.TXML_COLON_STORE, 0); }
		public TerminalNode LPAR() { return getToken(XQueryParser.LPAR, 0); }
		public SchemaNameContext schemaName() {
			return getRuleContext(SchemaNameContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public FileFullNameContext fileFullName() {
			return getRuleContext(FileFullNameContext.class,0);
		}
		public DocumentNameContext documentName() {
			return getRuleContext(DocumentNameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(XQueryParser.RPAR, 0); }
		public StoreDocumentWithNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_storeDocumentWithName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterStoreDocumentWithName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitStoreDocumentWithName(this);
		}
	}

	public final StoreDocumentWithNameContext storeDocumentWithName() throws RecognitionException {
		StoreDocumentWithNameContext _localctx = new StoreDocumentWithNameContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_storeDocumentWithName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(TXML_COLON_STORE);
			setState(187);
			match(LPAR);
			setState(188);
			schemaName();
			setState(189);
			match(COMMA);
			setState(190);
			fileFullName();
			setState(191);
			match(COMMA);
			setState(192);
			documentName();
			setState(193);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StoreDocumentWithoutNameContext extends ParserRuleContext {
		public TerminalNode TXML_COLON_STORE() { return getToken(XQueryParser.TXML_COLON_STORE, 0); }
		public TerminalNode LPAR() { return getToken(XQueryParser.LPAR, 0); }
		public SchemaNameContext schemaName() {
			return getRuleContext(SchemaNameContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParser.COMMA, 0); }
		public FileFullNameContext fileFullName() {
			return getRuleContext(FileFullNameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(XQueryParser.RPAR, 0); }
		public StoreDocumentWithoutNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_storeDocumentWithoutName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterStoreDocumentWithoutName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitStoreDocumentWithoutName(this);
		}
	}

	public final StoreDocumentWithoutNameContext storeDocumentWithoutName() throws RecognitionException {
		StoreDocumentWithoutNameContext _localctx = new StoreDocumentWithoutNameContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_storeDocumentWithoutName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(TXML_COLON_STORE);
			setState(196);
			match(LPAR);
			setState(197);
			schemaName();
			setState(198);
			match(COMMA);
			setState(199);
			fileFullName();
			setState(200);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DocContext extends ParserRuleContext {
		public TerminalNode TXML_COLON_DOC() { return getToken(XQueryParser.TXML_COLON_DOC, 0); }
		public TerminalNode LPAR() { return getToken(XQueryParser.LPAR, 0); }
		public SchemaNameContext schemaName() {
			return getRuleContext(SchemaNameContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParser.COMMA, 0); }
		public DocumentNameContext documentName() {
			return getRuleContext(DocumentNameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(XQueryParser.RPAR, 0); }
		public DocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDoc(this);
		}
	}

	public final DocContext doc() throws RecognitionException {
		DocContext _localctx = new DocContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_doc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			match(TXML_COLON_DOC);
			setState(203);
			match(LPAR);
			setState(204);
			schemaName();
			setState(205);
			match(COMMA);
			setState(206);
			documentName();
			setState(207);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SnapshotQueryContext extends ParserRuleContext {
		public TerminalNode TXML_COLON_DOCSNAPSHOT() { return getToken(XQueryParser.TXML_COLON_DOCSNAPSHOT, 0); }
		public TerminalNode LPAR() { return getToken(XQueryParser.LPAR, 0); }
		public SchemaNameContext schemaName() {
			return getRuleContext(SchemaNameContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public DocumentNameContext documentName() {
			return getRuleContext(DocumentNameContext.class,0);
		}
		public TimeContext time() {
			return getRuleContext(TimeContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(XQueryParser.RPAR, 0); }
		public SnapshotQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_snapshotQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterSnapshotQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitSnapshotQuery(this);
		}
	}

	public final SnapshotQueryContext snapshotQuery() throws RecognitionException {
		SnapshotQueryContext _localctx = new SnapshotQueryContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_snapshotQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			match(TXML_COLON_DOCSNAPSHOT);
			setState(210);
			match(LPAR);
			setState(211);
			schemaName();
			setState(212);
			match(COMMA);
			setState(213);
			documentName();
			setState(214);
			match(COMMA);
			setState(215);
			time();
			setState(216);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbsPathExprWithDocContext extends ParserRuleContext {
		public DocContext doc() {
			return getRuleContext(DocContext.class,0);
		}
		public AbsPathExprContext absPathExpr() {
			return getRuleContext(AbsPathExprContext.class,0);
		}
		public AbsPathExprWithDocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absPathExprWithDoc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterAbsPathExprWithDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitAbsPathExprWithDoc(this);
		}
	}

	public final AbsPathExprWithDocContext absPathExprWithDoc() throws RecognitionException {
		AbsPathExprWithDocContext _localctx = new AbsPathExprWithDocContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_absPathExprWithDoc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			doc();
			setState(219);
			absPathExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XPathQueryContext extends ParserRuleContext {
		public AbsPathExprWithDocContext absPathExprWithDoc() {
			return getRuleContext(AbsPathExprWithDocContext.class,0);
		}
		public XPathQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xPathQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterXPathQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitXPathQuery(this);
		}
	}

	public final XPathQueryContext xPathQuery() throws RecognitionException {
		XPathQueryContext _localctx = new XPathQueryContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_xPathQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			absPathExprWithDoc();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UpdateContext extends ParserRuleContext {
		public ForClauseContext forClause() {
			return getRuleContext(ForClauseContext.class,0);
		}
		public List<UpdateExprContext> updateExpr() {
			return getRuleContexts(UpdateExprContext.class);
		}
		public UpdateExprContext updateExpr(int i) {
			return getRuleContext(UpdateExprContext.class,i);
		}
		public TerminalNode LCURBRAC() { return getToken(XQueryParser.LCURBRAC, 0); }
		public TerminalNode RCURBRAC() { return getToken(XQueryParser.RCURBRAC, 0); }
		public UpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitUpdate(this);
		}
	}

	public final UpdateContext update() throws RecognitionException {
		UpdateContext _localctx = new UpdateContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_update);
		int _la;
		try {
			setState(235);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(223);
				forClause();
				setState(224);
				updateExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(226);
				forClause();
				setState(227);
				match(LCURBRAC);
				setState(229); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(228);
					updateExpr();
					}
					}
					setState(231); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INSERT) | (1L << DELETE) | (1L << SET))) != 0) );
				setState(233);
				match(RCURBRAC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForClauseContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(XQueryParser.FOR, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode IN() { return getToken(XQueryParser.IN, 0); }
		public AbsPathExprWithDocContext absPathExprWithDoc() {
			return getRuleContext(AbsPathExprWithDocContext.class,0);
		}
		public ForClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterForClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitForClause(this);
		}
	}

	public final ForClauseContext forClause() throws RecognitionException {
		ForClauseContext _localctx = new ForClauseContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_forClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			match(FOR);
			setState(238);
			variable();
			setState(239);
			match(IN);
			setState(240);
			absPathExprWithDoc();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UpdateExprContext extends ParserRuleContext {
		public InsertExprContext insertExpr() {
			return getRuleContext(InsertExprContext.class,0);
		}
		public DeleteExprContext deleteExpr() {
			return getRuleContext(DeleteExprContext.class,0);
		}
		public ParentExprContext parentExpr() {
			return getRuleContext(ParentExprContext.class,0);
		}
		public UpdateExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_updateExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterUpdateExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitUpdateExpr(this);
		}
	}

	public final UpdateExprContext updateExpr() throws RecognitionException {
		UpdateExprContext _localctx = new UpdateExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_updateExpr);
		try {
			setState(245);
			switch (_input.LA(1)) {
			case INSERT:
				enterOuterAlt(_localctx, 1);
				{
				setState(242);
				insertExpr();
				}
				break;
			case DELETE:
				enterOuterAlt(_localctx, 2);
				{
				setState(243);
				deleteExpr();
				}
				break;
			case SET:
				enterOuterAlt(_localctx, 3);
				{
				setState(244);
				parentExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InsertExprContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(XQueryParser.INSERT, 0); }
		public InsertXPathExpressionContext insertXPathExpression() {
			return getRuleContext(InsertXPathExpressionContext.class,0);
		}
		public TerminalNode VALUE() { return getToken(XQueryParser.VALUE, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode POSITION() { return getToken(XQueryParser.POSITION, 0); }
		public Insert_posContext insert_pos() {
			return getRuleContext(Insert_posContext.class,0);
		}
		public InsertExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterInsertExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitInsertExpr(this);
		}
	}

	public final InsertExprContext insertExpr() throws RecognitionException {
		InsertExprContext _localctx = new InsertExprContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_insertExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			match(INSERT);
			setState(248);
			insertXPathExpression();
			setState(251);
			_la = _input.LA(1);
			if (_la==VALUE) {
				{
				setState(249);
				match(VALUE);
				setState(250);
				value();
				}
			}

			setState(255);
			_la = _input.LA(1);
			if (_la==POSITION) {
				{
				setState(253);
				match(POSITION);
				setState(254);
				insert_pos();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeleteExprContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(XQueryParser.DELETE, 0); }
		public TerminalNode NODE() { return getToken(XQueryParser.NODE, 0); }
		public ChildXPathExpressionContext childXPathExpression() {
			return getRuleContext(ChildXPathExpressionContext.class,0);
		}
		public DeleteExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDeleteExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDeleteExpr(this);
		}
	}

	public final DeleteExprContext deleteExpr() throws RecognitionException {
		DeleteExprContext _localctx = new DeleteExprContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_deleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(DELETE);
			setState(258);
			match(NODE);
			setState(259);
			childXPathExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParentExprContext extends ParserRuleContext {
		public TerminalNode SET() { return getToken(XQueryParser.SET, 0); }
		public TerminalNode PARENT() { return getToken(XQueryParser.PARENT, 0); }
		public ParentXPathExpressionContext parentXPathExpression() {
			return getRuleContext(ParentXPathExpressionContext.class,0);
		}
		public TerminalNode POSITION() { return getToken(XQueryParser.POSITION, 0); }
		public Insert_posContext insert_pos() {
			return getRuleContext(Insert_posContext.class,0);
		}
		public ParentExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parentExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterParentExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitParentExpr(this);
		}
	}

	public final ParentExprContext parentExpr() throws RecognitionException {
		ParentExprContext _localctx = new ParentExprContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_parentExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			match(SET);
			setState(262);
			match(PARENT);
			setState(263);
			parentXPathExpression();
			setState(266);
			_la = _input.LA(1);
			if (_la==POSITION) {
				{
				setState(264);
				match(POSITION);
				setState(265);
				insert_pos();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParentXPathExpressionContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode AS() { return getToken(XQueryParser.AS, 0); }
		public StepsContext steps() {
			return getRuleContext(StepsContext.class,0);
		}
		public ParentXPathExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parentXPathExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterParentXPathExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitParentXPathExpression(this);
		}
	}

	public final ParentXPathExpressionContext parentXPathExpression() throws RecognitionException {
		ParentXPathExpressionContext _localctx = new ParentXPathExpressionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_parentXPathExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			variable();
			setState(269);
			match(AS);
			setState(270);
			steps();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChildXPathExpressionContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public StepsContext steps() {
			return getRuleContext(StepsContext.class,0);
		}
		public ChildXPathExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_childXPathExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterChildXPathExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitChildXPathExpression(this);
		}
	}

	public final ChildXPathExpressionContext childXPathExpression() throws RecognitionException {
		ChildXPathExpressionContext _localctx = new ChildXPathExpressionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_childXPathExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(272);
			variable();
			setState(273);
			steps();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleXPathExpressionContext extends ParserRuleContext {
		public List<ElementNameContext> elementName() {
			return getRuleContexts(ElementNameContext.class);
		}
		public ElementNameContext elementName(int i) {
			return getRuleContext(ElementNameContext.class,i);
		}
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public SimpleXPathExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleXPathExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterSimpleXPathExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitSimpleXPathExpression(this);
		}
	}

	public final SimpleXPathExpressionContext simpleXPathExpression() throws RecognitionException {
		SimpleXPathExpressionContext _localctx = new SimpleXPathExpressionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_simpleXPathExpression);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(275);
					match(PATHSEP);
					setState(276);
					elementName();
					}
					} 
				}
				setState(281);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(284);
			_la = _input.LA(1);
			if (_la==PATHSEP) {
				{
				setState(282);
				match(PATHSEP);
				setState(283);
				attributeName();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InsertXPathExpressionContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public SimpleXPathExpressionContext simpleXPathExpression() {
			return getRuleContext(SimpleXPathExpressionContext.class,0);
		}
		public InsertXPathExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertXPathExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterInsertXPathExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitInsertXPathExpression(this);
		}
	}

	public final InsertXPathExpressionContext insertXPathExpression() throws RecognitionException {
		InsertXPathExpressionContext _localctx = new InsertXPathExpressionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_insertXPathExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			variable();
			setState(287);
			simpleXPathExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbsPathExprContext extends ParserRuleContext {
		public StepsContext steps() {
			return getRuleContext(StepsContext.class,0);
		}
		public AbsPathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absPathExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterAbsPathExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitAbsPathExpr(this);
		}
	}

	public final AbsPathExprContext absPathExpr() throws RecognitionException {
		AbsPathExprContext _localctx = new AbsPathExprContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_absPathExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			steps();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StepsContext extends ParserRuleContext {
		public DirectStepContext directStep() {
			return getRuleContext(DirectStepContext.class,0);
		}
		public UndirectStepContext undirectStep() {
			return getRuleContext(UndirectStepContext.class,0);
		}
		public List<StepsContext> steps() {
			return getRuleContexts(StepsContext.class);
		}
		public StepsContext steps(int i) {
			return getRuleContext(StepsContext.class,i);
		}
		public StepsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_steps; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterSteps(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitSteps(this);
		}
	}

	public final StepsContext steps() throws RecognitionException {
		StepsContext _localctx = new StepsContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_steps);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			switch (_input.LA(1)) {
			case PATHSEP:
				{
				setState(291);
				directStep();
				}
				break;
			case ABRPATH:
				{
				setState(292);
				undirectStep();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(298);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(295);
					steps();
					}
					} 
				}
				setState(300);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UndirectStepContext extends ParserRuleContext {
		public NodeGeneratorContext nodeGenerator() {
			return getRuleContext(NodeGeneratorContext.class,0);
		}
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public UndirectStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_undirectStep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterUndirectStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitUndirectStep(this);
		}
	}

	public final UndirectStepContext undirectStep() throws RecognitionException {
		UndirectStepContext _localctx = new UndirectStepContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_undirectStep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			match(ABRPATH);
			setState(302);
			nodeGenerator();
			setState(306);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRAC) {
				{
				{
				setState(303);
				predicate();
				}
				}
				setState(308);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DirectStepContext extends ParserRuleContext {
		public NodeGeneratorContext nodeGenerator() {
			return getRuleContext(NodeGeneratorContext.class,0);
		}
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public DirectStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directStep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterDirectStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitDirectStep(this);
		}
	}

	public final DirectStepContext directStep() throws RecognitionException {
		DirectStepContext _localctx = new DirectStepContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_directStep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			match(PATHSEP);
			setState(310);
			nodeGenerator();
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRAC) {
				{
				{
				setState(311);
				predicate();
				}
				}
				setState(316);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitPredicate(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			match(LBRAC);
			setState(318);
			expr();
			setState(319);
			match(RBRAC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XPathInExprContext extends ParserRuleContext {
		public NodeGeneratorContext nodeGenerator() {
			return getRuleContext(NodeGeneratorContext.class,0);
		}
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public StepsContext steps() {
			return getRuleContext(StepsContext.class,0);
		}
		public XPathInExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xPathInExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterXPathInExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitXPathInExpr(this);
		}
	}

	public final XPathInExprContext xPathInExpr() throws RecognitionException {
		XPathInExprContext _localctx = new XPathInExprContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_xPathInExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(321);
			nodeGenerator();
			setState(325);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRAC) {
				{
				{
				setState(322);
				predicate();
				}
				}
				setState(327);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(329);
			_la = _input.LA(1);
			if (_la==PATHSEP || _la==ABRPATH) {
				{
				setState(328);
				steps();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public XPathInExprContext xPathInExpr() {
			return getRuleContext(XPathInExprContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PositionContext position() {
			return getRuleContext(PositionContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_expr);
		try {
			setState(340);
			switch (_input.LA(1)) {
			case AT:
			case TEXT:
			case DOT:
			case STAR:
			case TWO_DOTS:
			case ATTRIBUTE:
			case Name:
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				xPathInExpr();
				setState(332);
				operator();
				setState(333);
				value();
				}
				break;
			case StringLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(335);
				value();
				setState(336);
				operator();
				setState(337);
				xPathInExpr();
				}
				break;
			case DIGIT:
			case LAST:
				enterOuterAlt(_localctx, 3);
				{
				setState(339);
				position();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PositionContext extends ParserRuleContext {
		public IntegerNumberContext integerNumber() {
			return getRuleContext(IntegerNumberContext.class,0);
		}
		public LastContext last() {
			return getRuleContext(LastContext.class,0);
		}
		public PositionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_position; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterPosition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitPosition(this);
		}
	}

	public final PositionContext position() throws RecognitionException {
		PositionContext _localctx = new PositionContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_position);
		try {
			setState(344);
			switch (_input.LA(1)) {
			case DIGIT:
				enterOuterAlt(_localctx, 1);
				{
				setState(342);
				integerNumber();
				}
				break;
			case LAST:
				enterOuterAlt(_localctx, 2);
				{
				setState(343);
				last();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LastContext extends ParserRuleContext {
		public TerminalNode LAST() { return getToken(XQueryParser.LAST, 0); }
		public LastContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_last; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterLast(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitLast(this);
		}
	}

	public final LastContext last() throws RecognitionException {
		LastContext _localctx = new LastContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_last);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(346);
			match(LAST);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerNumberContext extends ParserRuleContext {
		public List<TerminalNode> DIGIT() { return getTokens(XQueryParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(XQueryParser.DIGIT, i);
		}
		public IntegerNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterIntegerNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitIntegerNumber(this);
		}
	}

	public final IntegerNumberContext integerNumber() throws RecognitionException {
		IntegerNumberContext _localctx = new IntegerNumberContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_integerNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(349); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(348);
				match(DIGIT);
				}
				}
				setState(351); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DIGIT );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode OpEq() { return getToken(XQueryParser.OpEq, 0); }
		public TerminalNode OpNEq() { return getToken(XQueryParser.OpNEq, 0); }
		public TerminalNode OpLess() { return getToken(XQueryParser.OpLess, 0); }
		public TerminalNode OpMore() { return getToken(XQueryParser.OpMore, 0); }
		public TerminalNode OpLe() { return getToken(XQueryParser.OpLe, 0); }
		public TerminalNode OpGe() { return getToken(XQueryParser.OpGe, 0); }
		public TerminalNode OpPrecedes() { return getToken(XQueryParser.OpPrecedes, 0); }
		public TerminalNode OpFollows() { return getToken(XQueryParser.OpFollows, 0); }
		public TerminalNode OpLMeets() { return getToken(XQueryParser.OpLMeets, 0); }
		public TerminalNode OpRMeets() { return getToken(XQueryParser.OpRMeets, 0); }
		public TerminalNode OpOverlaps() { return getToken(XQueryParser.OpOverlaps, 0); }
		public TerminalNode OpContains() { return getToken(XQueryParser.OpContains, 0); }
		public TerminalNode OpIn() { return getToken(XQueryParser.OpIn, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitOperator(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OpEq) | (1L << OpNEq) | (1L << OpLess) | (1L << OpMore) | (1L << OpLe) | (1L << OpGe) | (1L << OpPrecedes) | (1L << OpFollows) | (1L << OpLMeets) | (1L << OpRMeets) | (1L << OpOverlaps) | (1L << OpContains) | (1L << OpIn))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public TerminalNode Name() { return getToken(XQueryParser.Name, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitVariable(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			match(VariableStart);
			setState(356);
			match(Name);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NodeGeneratorContext extends ParserRuleContext {
		public ElementNameContext elementName() {
			return getRuleContext(ElementNameContext.class,0);
		}
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public NodesByTypeContext nodesByType() {
			return getRuleContext(NodesByTypeContext.class,0);
		}
		public CurrentNodesContext currentNodes() {
			return getRuleContext(CurrentNodesContext.class,0);
		}
		public ParentNodesContext parentNodes() {
			return getRuleContext(ParentNodesContext.class,0);
		}
		public ChildsElementsContext childsElements() {
			return getRuleContext(ChildsElementsContext.class,0);
		}
		public NodeGeneratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeGenerator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterNodeGenerator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitNodeGenerator(this);
		}
	}

	public final NodeGeneratorContext nodeGenerator() throws RecognitionException {
		NodeGeneratorContext _localctx = new NodeGeneratorContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_nodeGenerator);
		try {
			setState(364);
			switch (_input.LA(1)) {
			case Name:
				enterOuterAlt(_localctx, 1);
				{
				setState(358);
				elementName();
				}
				break;
			case AT:
			case ATTRIBUTE:
				enterOuterAlt(_localctx, 2);
				{
				setState(359);
				attributeName();
				}
				break;
			case TEXT:
				enterOuterAlt(_localctx, 3);
				{
				setState(360);
				nodesByType();
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 4);
				{
				setState(361);
				currentNodes();
				}
				break;
			case TWO_DOTS:
				enterOuterAlt(_localctx, 5);
				{
				setState(362);
				parentNodes();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 6);
				{
				setState(363);
				childsElements();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CurrentNodesContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(XQueryParser.DOT, 0); }
		public CurrentNodesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_currentNodes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterCurrentNodes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitCurrentNodes(this);
		}
	}

	public final CurrentNodesContext currentNodes() throws RecognitionException {
		CurrentNodesContext _localctx = new CurrentNodesContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_currentNodes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			match(DOT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParentNodesContext extends ParserRuleContext {
		public TerminalNode TWO_DOTS() { return getToken(XQueryParser.TWO_DOTS, 0); }
		public ParentNodesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parentNodes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterParentNodes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitParentNodes(this);
		}
	}

	public final ParentNodesContext parentNodes() throws RecognitionException {
		ParentNodesContext _localctx = new ParentNodesContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_parentNodes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368);
			match(TWO_DOTS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChildsElementsContext extends ParserRuleContext {
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public ChildsElementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_childsElements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterChildsElements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitChildsElements(this);
		}
	}

	public final ChildsElementsContext childsElements() throws RecognitionException {
		ChildsElementsContext _localctx = new ChildsElementsContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_childsElements);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			match(STAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NodesByTypeContext extends ParserRuleContext {
		public TextNodesContext textNodes() {
			return getRuleContext(TextNodesContext.class,0);
		}
		public NodesByTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodesByType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterNodesByType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitNodesByType(this);
		}
	}

	public final NodesByTypeContext nodesByType() throws RecognitionException {
		NodesByTypeContext _localctx = new NodesByTypeContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_nodesByType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			textNodes();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TextNodesContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(XQueryParser.TEXT, 0); }
		public TerminalNode LPAR() { return getToken(XQueryParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(XQueryParser.RPAR, 0); }
		public TextNodesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textNodes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterTextNodes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitTextNodes(this);
		}
	}

	public final TextNodesContext textNodes() throws RecognitionException {
		TextNodesContext _localctx = new TextNodesContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_textNodes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			match(TEXT);
			setState(375);
			match(LPAR);
			setState(376);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementNameContext extends ParserRuleContext {
		public List<TerminalNode> Name() { return getTokens(XQueryParser.Name); }
		public TerminalNode Name(int i) {
			return getToken(XQueryParser.Name, i);
		}
		public TerminalNode COLON() { return getToken(XQueryParser.COLON, 0); }
		public ElementNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterElementName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitElementName(this);
		}
	}

	public final ElementNameContext elementName() throws RecognitionException {
		ElementNameContext _localctx = new ElementNameContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_elementName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(378);
				match(Name);
				setState(379);
				match(COLON);
				}
				break;
			}
			setState(382);
			match(Name);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeNameContext extends ParserRuleContext {
		public List<TerminalNode> Name() { return getTokens(XQueryParser.Name); }
		public TerminalNode Name(int i) {
			return getToken(XQueryParser.Name, i);
		}
		public TerminalNode ATTRIBUTE() { return getToken(XQueryParser.ATTRIBUTE, 0); }
		public TerminalNode TWO_COLON() { return getToken(XQueryParser.TWO_COLON, 0); }
		public TerminalNode COLON() { return getToken(XQueryParser.COLON, 0); }
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterAttributeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitAttributeName(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_attributeName);
		try {
			setState(398);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(387);
				switch (_input.LA(1)) {
				case AT:
					{
					setState(384);
					match(AT);
					}
					break;
				case ATTRIBUTE:
					{
					setState(385);
					match(ATTRIBUTE);
					setState(386);
					match(TWO_COLON);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(389);
				match(Name);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(393);
				switch (_input.LA(1)) {
				case AT:
					{
					setState(390);
					match(AT);
					}
					break;
				case ATTRIBUTE:
					{
					setState(391);
					match(ATTRIBUTE);
					setState(392);
					match(TWO_COLON);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(395);
				match(Name);
				setState(396);
				match(COLON);
				setState(397);
				match(Name);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(400);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Insert_posContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public Insert_posContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert_pos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterInsert_pos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitInsert_pos(this);
		}
	}

	public final Insert_posContext insert_pos() throws RecognitionException {
		Insert_posContext _localctx = new Insert_posContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_insert_pos);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimeContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public TimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_time; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitTime(this);
		}
	}

	public final TimeContext time() throws RecognitionException {
		TimeContext _localctx = new TimeContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_time);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormatContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(XQueryParser.StringLiteral, 0); }
		public FormatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_format; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).enterFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryListener ) ((XQueryListener)listener).exitFormat(this);
		}
	}

	public final FormatContext format() throws RecognitionException {
		FormatContext _localctx = new FormatContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_format);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(406);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3A\u019b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\3\2\3\2\3\2\3\2\3"+
		"\3\7\3z\n\3\f\3\16\3}\13\3\3\4\3\4\3\4\5\4\u0082\n\4\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t"+
		"\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\6\16\u00a3\n\16\r\16\16\16\u00a4"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00ad\n\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\22\3\22\5\22\u00bb\n\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\6\31\u00e8\n\31"+
		"\r\31\16\31\u00e9\3\31\3\31\5\31\u00ee\n\31\3\32\3\32\3\32\3\32\3\32\3"+
		"\33\3\33\3\33\5\33\u00f8\n\33\3\34\3\34\3\34\3\34\5\34\u00fe\n\34\3\34"+
		"\3\34\5\34\u0102\n\34\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\5\36"+
		"\u010d\n\36\3\37\3\37\3\37\3\37\3 \3 \3 \3!\3!\7!\u0118\n!\f!\16!\u011b"+
		"\13!\3!\3!\5!\u011f\n!\3\"\3\"\3\"\3#\3#\3$\3$\5$\u0128\n$\3$\7$\u012b"+
		"\n$\f$\16$\u012e\13$\3%\3%\3%\7%\u0133\n%\f%\16%\u0136\13%\3&\3&\3&\7"+
		"&\u013b\n&\f&\16&\u013e\13&\3\'\3\'\3\'\3\'\3(\3(\7(\u0146\n(\f(\16(\u0149"+
		"\13(\3(\5(\u014c\n(\3)\3)\3)\3)\3)\3)\3)\3)\3)\5)\u0157\n)\3*\3*\5*\u015b"+
		"\n*\3+\3+\3,\6,\u0160\n,\r,\16,\u0161\3-\3-\3.\3.\3.\3/\3/\3/\3/\3/\3"+
		"/\5/\u016f\n/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\64\3"+
		"\64\3\65\3\65\5\65\u017f\n\65\3\65\3\65\3\66\3\66\3\66\5\66\u0186\n\66"+
		"\3\66\3\66\3\66\3\66\5\66\u018c\n\66\3\66\3\66\3\66\5\66\u0191\n\66\3"+
		"\67\3\67\38\38\39\39\3:\3:\3:\2\2;\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnpr\2\3\3\2\3\17\u0187"+
		"\2t\3\2\2\2\4{\3\2\2\2\6\u0081\3\2\2\2\b\u0083\3\2\2\2\n\u0089\3\2\2\2"+
		"\f\u008f\3\2\2\2\16\u0095\3\2\2\2\20\u0097\3\2\2\2\22\u0099\3\2\2\2\24"+
		"\u009b\3\2\2\2\26\u009d\3\2\2\2\30\u009f\3\2\2\2\32\u00a2\3\2\2\2\34\u00ac"+
		"\3\2\2\2\36\u00ae\3\2\2\2 \u00b3\3\2\2\2\"\u00ba\3\2\2\2$\u00bc\3\2\2"+
		"\2&\u00c5\3\2\2\2(\u00cc\3\2\2\2*\u00d3\3\2\2\2,\u00dc\3\2\2\2.\u00df"+
		"\3\2\2\2\60\u00ed\3\2\2\2\62\u00ef\3\2\2\2\64\u00f7\3\2\2\2\66\u00f9\3"+
		"\2\2\28\u0103\3\2\2\2:\u0107\3\2\2\2<\u010e\3\2\2\2>\u0112\3\2\2\2@\u0119"+
		"\3\2\2\2B\u0120\3\2\2\2D\u0123\3\2\2\2F\u0127\3\2\2\2H\u012f\3\2\2\2J"+
		"\u0137\3\2\2\2L\u013f\3\2\2\2N\u0143\3\2\2\2P\u0156\3\2\2\2R\u015a\3\2"+
		"\2\2T\u015c\3\2\2\2V\u015f\3\2\2\2X\u0163\3\2\2\2Z\u0165\3\2\2\2\\\u016e"+
		"\3\2\2\2^\u0170\3\2\2\2`\u0172\3\2\2\2b\u0174\3\2\2\2d\u0176\3\2\2\2f"+
		"\u0178\3\2\2\2h\u017e\3\2\2\2j\u0190\3\2\2\2l\u0192\3\2\2\2n\u0194\3\2"+
		"\2\2p\u0196\3\2\2\2r\u0198\3\2\2\2tu\5\4\3\2uv\5\32\16\2vw\7\2\2\3w\3"+
		"\3\2\2\2xz\5\6\4\2yx\3\2\2\2z}\3\2\2\2{y\3\2\2\2{|\3\2\2\2|\5\3\2\2\2"+
		"}{\3\2\2\2~\u0082\5\b\5\2\177\u0082\5\n\6\2\u0080\u0082\5\f\7\2\u0081"+
		"~\3\2\2\2\u0081\177\3\2\2\2\u0081\u0080\3\2\2\2\u0082\7\3\2\2\2\u0083"+
		"\u0084\7*\2\2\u0084\u0085\7+\2\2\u0085\u0086\7.\2\2\u0086\u0087\5\26\f"+
		"\2\u0087\u0088\7\35\2\2\u0088\t\3\2\2\2\u0089\u008a\7*\2\2\u008a\u008b"+
		"\7+\2\2\u008b\u008c\7/\2\2\u008c\u008d\5\22\n\2\u008d\u008e\7\35\2\2\u008e"+
		"\13\3\2\2\2\u008f\u0090\7*\2\2\u0090\u0091\7+\2\2\u0091\u0092\7\60\2\2"+
		"\u0092\u0093\5\24\13\2\u0093\u0094\7\35\2\2\u0094\r\3\2\2\2\u0095\u0096"+
		"\7\20\2\2\u0096\17\3\2\2\2\u0097\u0098\7\20\2\2\u0098\21\3\2\2\2\u0099"+
		"\u009a\7\20\2\2\u009a\23\3\2\2\2\u009b\u009c\7\20\2\2\u009c\25\3\2\2\2"+
		"\u009d\u009e\7\20\2\2\u009e\27\3\2\2\2\u009f\u00a0\7\20\2\2\u00a0\31\3"+
		"\2\2\2\u00a1\u00a3\5\34\17\2\u00a2\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4"+
		"\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\33\3\2\2\2\u00a6\u00ad\5\60\31"+
		"\2\u00a7\u00ad\5.\30\2\u00a8\u00ad\5*\26\2\u00a9\u00ad\5\36\20\2\u00aa"+
		"\u00ad\5 \21\2\u00ab\u00ad\5\"\22\2\u00ac\u00a6\3\2\2\2\u00ac\u00a7\3"+
		"\2\2\2\u00ac\u00a8\3\2\2\2\u00ac\u00a9\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac"+
		"\u00ab\3\2\2\2\u00ad\35\3\2\2\2\u00ae\u00af\7\61\2\2\u00af\u00b0\7\31"+
		"\2\2\u00b0\u00b1\5\20\t\2\u00b1\u00b2\7\32\2\2\u00b2\37\3\2\2\2\u00b3"+
		"\u00b4\7\62\2\2\u00b4\u00b5\7\31\2\2\u00b5\u00b6\5\20\t\2\u00b6\u00b7"+
		"\7\32\2\2\u00b7!\3\2\2\2\u00b8\u00bb\5$\23\2\u00b9\u00bb\5&\24\2\u00ba"+
		"\u00b8\3\2\2\2\u00ba\u00b9\3\2\2\2\u00bb#\3\2\2\2\u00bc\u00bd\7\63\2\2"+
		"\u00bd\u00be\7\31\2\2\u00be\u00bf\5\20\t\2\u00bf\u00c0\7\36\2\2\u00c0"+
		"\u00c1\5\30\r\2\u00c1\u00c2\7\36\2\2\u00c2\u00c3\5\16\b\2\u00c3\u00c4"+
		"\7\32\2\2\u00c4%\3\2\2\2\u00c5\u00c6\7\63\2\2\u00c6\u00c7\7\31\2\2\u00c7"+
		"\u00c8\5\20\t\2\u00c8\u00c9\7\36\2\2\u00c9\u00ca\5\30\r\2\u00ca\u00cb"+
		"\7\32\2\2\u00cb\'\3\2\2\2\u00cc\u00cd\7\64\2\2\u00cd\u00ce\7\31\2\2\u00ce"+
		"\u00cf\5\20\t\2\u00cf\u00d0\7\36\2\2\u00d0\u00d1\5\16\b\2\u00d1\u00d2"+
		"\7\32\2\2\u00d2)\3\2\2\2\u00d3\u00d4\7\37\2\2\u00d4\u00d5\7\31\2\2\u00d5"+
		"\u00d6\5\20\t\2\u00d6\u00d7\7\36\2\2\u00d7\u00d8\5\16\b\2\u00d8\u00d9"+
		"\7\36\2\2\u00d9\u00da\5p9\2\u00da\u00db\7\32\2\2\u00db+\3\2\2\2\u00dc"+
		"\u00dd\5(\25\2\u00dd\u00de\5D#\2\u00de-\3\2\2\2\u00df\u00e0\5,\27\2\u00e0"+
		"/\3\2\2\2\u00e1\u00e2\5\62\32\2\u00e2\u00e3\5\64\33\2\u00e3\u00ee\3\2"+
		"\2\2\u00e4\u00e5\5\62\32\2\u00e5\u00e7\7\33\2\2\u00e6\u00e8\5\64\33\2"+
		"\u00e7\u00e6\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea"+
		"\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ec\7\34\2\2\u00ec\u00ee\3\2\2\2"+
		"\u00ed\u00e1\3\2\2\2\u00ed\u00e4\3\2\2\2\u00ee\61\3\2\2\2\u00ef\u00f0"+
		"\7 \2\2\u00f0\u00f1\5Z.\2\u00f1\u00f2\7!\2\2\u00f2\u00f3\5,\27\2\u00f3"+
		"\63\3\2\2\2\u00f4\u00f8\5\66\34\2\u00f5\u00f8\58\35\2\u00f6\u00f8\5:\36"+
		"\2\u00f7\u00f4\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f7\u00f6\3\2\2\2\u00f8\65"+
		"\3\2\2\2\u00f9\u00fa\7\"\2\2\u00fa\u00fd\5B\"\2\u00fb\u00fc\7#\2\2\u00fc"+
		"\u00fe\5l\67\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u0101\3\2"+
		"\2\2\u00ff\u0100\7$\2\2\u0100\u0102\5n8\2\u0101\u00ff\3\2\2\2\u0101\u0102"+
		"\3\2\2\2\u0102\67\3\2\2\2\u0103\u0104\7%\2\2\u0104\u0105\7&\2\2\u0105"+
		"\u0106\5> \2\u01069\3\2\2\2\u0107\u0108\7\'\2\2\u0108\u0109\7(\2\2\u0109"+
		"\u010c\5<\37\2\u010a\u010b\7$\2\2\u010b\u010d\5n8\2\u010c\u010a\3\2\2"+
		"\2\u010c\u010d\3\2\2\2\u010d;\3\2\2\2\u010e\u010f\5Z.\2\u010f\u0110\7"+
		")\2\2\u0110\u0111\5F$\2\u0111=\3\2\2\2\u0112\u0113\5Z.\2\u0113\u0114\5"+
		"F$\2\u0114?\3\2\2\2\u0115\u0116\7\24\2\2\u0116\u0118\5h\65\2\u0117\u0115"+
		"\3\2\2\2\u0118\u011b\3\2\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a"+
		"\u011e\3\2\2\2\u011b\u0119\3\2\2\2\u011c\u011d\7\24\2\2\u011d\u011f\5"+
		"j\66\2\u011e\u011c\3\2\2\2\u011e\u011f\3\2\2\2\u011fA\3\2\2\2\u0120\u0121"+
		"\5Z.\2\u0121\u0122\5@!\2\u0122C\3\2\2\2\u0123\u0124\5F$\2\u0124E\3\2\2"+
		"\2\u0125\u0128\5J&\2\u0126\u0128\5H%\2\u0127\u0125\3\2\2\2\u0127\u0126"+
		"\3\2\2\2\u0128\u012c\3\2\2\2\u0129\u012b\5F$\2\u012a\u0129\3\2\2\2\u012b"+
		"\u012e\3\2\2\2\u012c\u012a\3\2\2\2\u012c\u012d\3\2\2\2\u012dG\3\2\2\2"+
		"\u012e\u012c\3\2\2\2\u012f\u0130\7\25\2\2\u0130\u0134\5\\/\2\u0131\u0133"+
		"\5L\'\2\u0132\u0131\3\2\2\2\u0133\u0136\3\2\2\2\u0134\u0132\3\2\2\2\u0134"+
		"\u0135\3\2\2\2\u0135I\3\2\2\2\u0136\u0134\3\2\2\2\u0137\u0138\7\24\2\2"+
		"\u0138\u013c\5\\/\2\u0139\u013b\5L\'\2\u013a\u0139\3\2\2\2\u013b\u013e"+
		"\3\2\2\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013dK\3\2\2\2\u013e"+
		"\u013c\3\2\2\2\u013f\u0140\7\27\2\2\u0140\u0141\5P)\2\u0141\u0142\7\30"+
		"\2\2\u0142M\3\2\2\2\u0143\u0147\5\\/\2\u0144\u0146\5L\'\2\u0145\u0144"+
		"\3\2\2\2\u0146\u0149\3\2\2\2\u0147\u0145\3\2\2\2\u0147\u0148\3\2\2\2\u0148"+
		"\u014b\3\2\2\2\u0149\u0147\3\2\2\2\u014a\u014c\5F$\2\u014b\u014a\3\2\2"+
		"\2\u014b\u014c\3\2\2\2\u014cO\3\2\2\2\u014d\u014e\5N(\2\u014e\u014f\5"+
		"X-\2\u014f\u0150\5l\67\2\u0150\u0157\3\2\2\2\u0151\u0152\5l\67\2\u0152"+
		"\u0153\5X-\2\u0153\u0154\5N(\2\u0154\u0157\3\2\2\2\u0155\u0157\5R*\2\u0156"+
		"\u014d\3\2\2\2\u0156\u0151\3\2\2\2\u0156\u0155\3\2\2\2\u0157Q\3\2\2\2"+
		"\u0158\u015b\5V,\2\u0159\u015b\5T+\2\u015a\u0158\3\2\2\2\u015a\u0159\3"+
		"\2\2\2\u015bS\3\2\2\2\u015c\u015d\7:\2\2\u015dU\3\2\2\2\u015e\u0160\7"+
		"9\2\2\u015f\u015e\3\2\2\2\u0160\u0161\3\2\2\2\u0161\u015f\3\2\2\2\u0161"+
		"\u0162\3\2\2\2\u0162W\3\2\2\2\u0163\u0164\t\2\2\2\u0164Y\3\2\2\2\u0165"+
		"\u0166\7>\2\2\u0166\u0167\7?\2\2\u0167[\3\2\2\2\u0168\u016f\5h\65\2\u0169"+
		"\u016f\5j\66\2\u016a\u016f\5d\63\2\u016b\u016f\5^\60\2\u016c\u016f\5`"+
		"\61\2\u016d\u016f\5b\62\2\u016e\u0168\3\2\2\2\u016e\u0169\3\2\2\2\u016e"+
		"\u016a\3\2\2\2\u016e\u016b\3\2\2\2\u016e\u016c\3\2\2\2\u016e\u016d\3\2"+
		"\2\2\u016f]\3\2\2\2\u0170\u0171\7\66\2\2\u0171_\3\2\2\2\u0172\u0173\7"+
		"8\2\2\u0173a\3\2\2\2\u0174\u0175\7\67\2\2\u0175c\3\2\2\2\u0176\u0177\5"+
		"f\64\2\u0177e\3\2\2\2\u0178\u0179\7\65\2\2\u0179\u017a\7\31\2\2\u017a"+
		"\u017b\7\32\2\2\u017bg\3\2\2\2\u017c\u017d\7?\2\2\u017d\u017f\7\22\2\2"+
		"\u017e\u017c\3\2\2\2\u017e\u017f\3\2\2\2\u017f\u0180\3\2\2\2\u0180\u0181"+
		"\7?\2\2\u0181i\3\2\2\2\u0182\u0186\7\23\2\2\u0183\u0184\7<\2\2\u0184\u0186"+
		"\7;\2\2\u0185\u0182\3\2\2\2\u0185\u0183\3\2\2\2\u0186\u0187\3\2\2\2\u0187"+
		"\u0191\7?\2\2\u0188\u018c\7\23\2\2\u0189\u018a\7<\2\2\u018a\u018c\7;\2"+
		"\2\u018b\u0188\3\2\2\2\u018b\u0189\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e"+
		"\7?\2\2\u018e\u018f\7\22\2\2\u018f\u0191\7?\2\2\u0190\u0185\3\2\2\2\u0190"+
		"\u018b\3\2\2\2\u0191k\3\2\2\2\u0192\u0193\7\20\2\2\u0193m\3\2\2\2\u0194"+
		"\u0195\7\20\2\2\u0195o\3\2\2\2\u0196\u0197\7\20\2\2\u0197q\3\2\2\2\u0198"+
		"\u0199\7\20\2\2\u0199s\3\2\2\2\35{\u0081\u00a4\u00ac\u00ba\u00e9\u00ed"+
		"\u00f7\u00fd\u0101\u010c\u0119\u011e\u0127\u012c\u0134\u013c\u0147\u014b"+
		"\u0156\u015a\u0161\u016e\u017e\u0185\u018b\u0190";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}