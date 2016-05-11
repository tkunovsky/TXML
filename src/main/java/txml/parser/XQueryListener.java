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
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XQueryParser}.
 */
public interface XQueryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XQueryParser#main}.
	 * @param ctx the parse tree
	 */
	void enterMain(XQueryParser.MainContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#main}.
	 * @param ctx the parse tree
	 */
	void exitMain(XQueryParser.MainContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#prolog}.
	 * @param ctx the parse tree
	 */
	void enterProlog(XQueryParser.PrologContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#prolog}.
	 * @param ctx the parse tree
	 */
	void exitProlog(XQueryParser.PrologContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#declareOption}.
	 * @param ctx the parse tree
	 */
	void enterDeclareOption(XQueryParser.DeclareOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#declareOption}.
	 * @param ctx the parse tree
	 */
	void exitDeclareOption(XQueryParser.DeclareOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#declareOptionConnection}.
	 * @param ctx the parse tree
	 */
	void enterDeclareOptionConnection(XQueryParser.DeclareOptionConnectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#declareOptionConnection}.
	 * @param ctx the parse tree
	 */
	void exitDeclareOptionConnection(XQueryParser.DeclareOptionConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#declareOptionTimeFormat}.
	 * @param ctx the parse tree
	 */
	void enterDeclareOptionTimeFormat(XQueryParser.DeclareOptionTimeFormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#declareOptionTimeFormat}.
	 * @param ctx the parse tree
	 */
	void exitDeclareOptionTimeFormat(XQueryParser.DeclareOptionTimeFormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#declareOptionSort}.
	 * @param ctx the parse tree
	 */
	void enterDeclareOptionSort(XQueryParser.DeclareOptionSortContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#declareOptionSort}.
	 * @param ctx the parse tree
	 */
	void exitDeclareOptionSort(XQueryParser.DeclareOptionSortContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#documentName}.
	 * @param ctx the parse tree
	 */
	void enterDocumentName(XQueryParser.DocumentNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#documentName}.
	 * @param ctx the parse tree
	 */
	void exitDocumentName(XQueryParser.DocumentNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#schemaName}.
	 * @param ctx the parse tree
	 */
	void enterSchemaName(XQueryParser.SchemaNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#schemaName}.
	 * @param ctx the parse tree
	 */
	void exitSchemaName(XQueryParser.SchemaNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#timeFormat}.
	 * @param ctx the parse tree
	 */
	void enterTimeFormat(XQueryParser.TimeFormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#timeFormat}.
	 * @param ctx the parse tree
	 */
	void exitTimeFormat(XQueryParser.TimeFormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#sort}.
	 * @param ctx the parse tree
	 */
	void enterSort(XQueryParser.SortContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#sort}.
	 * @param ctx the parse tree
	 */
	void exitSort(XQueryParser.SortContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#connectionUrl}.
	 * @param ctx the parse tree
	 */
	void enterConnectionUrl(XQueryParser.ConnectionUrlContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#connectionUrl}.
	 * @param ctx the parse tree
	 */
	void exitConnectionUrl(XQueryParser.ConnectionUrlContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#fileFullName}.
	 * @param ctx the parse tree
	 */
	void enterFileFullName(XQueryParser.FileFullNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#fileFullName}.
	 * @param ctx the parse tree
	 */
	void exitFileFullName(XQueryParser.FileFullNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(XQueryParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(XQueryParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(XQueryParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(XQueryParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#initSchema}.
	 * @param ctx the parse tree
	 */
	void enterInitSchema(XQueryParser.InitSchemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#initSchema}.
	 * @param ctx the parse tree
	 */
	void exitInitSchema(XQueryParser.InitSchemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#deinitSchema}.
	 * @param ctx the parse tree
	 */
	void enterDeinitSchema(XQueryParser.DeinitSchemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#deinitSchema}.
	 * @param ctx the parse tree
	 */
	void exitDeinitSchema(XQueryParser.DeinitSchemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#storeDocument}.
	 * @param ctx the parse tree
	 */
	void enterStoreDocument(XQueryParser.StoreDocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#storeDocument}.
	 * @param ctx the parse tree
	 */
	void exitStoreDocument(XQueryParser.StoreDocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#storeDocumentWithName}.
	 * @param ctx the parse tree
	 */
	void enterStoreDocumentWithName(XQueryParser.StoreDocumentWithNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#storeDocumentWithName}.
	 * @param ctx the parse tree
	 */
	void exitStoreDocumentWithName(XQueryParser.StoreDocumentWithNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#storeDocumentWithoutName}.
	 * @param ctx the parse tree
	 */
	void enterStoreDocumentWithoutName(XQueryParser.StoreDocumentWithoutNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#storeDocumentWithoutName}.
	 * @param ctx the parse tree
	 */
	void exitStoreDocumentWithoutName(XQueryParser.StoreDocumentWithoutNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#doc}.
	 * @param ctx the parse tree
	 */
	void enterDoc(XQueryParser.DocContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#doc}.
	 * @param ctx the parse tree
	 */
	void exitDoc(XQueryParser.DocContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#snapshotQuery}.
	 * @param ctx the parse tree
	 */
	void enterSnapshotQuery(XQueryParser.SnapshotQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#snapshotQuery}.
	 * @param ctx the parse tree
	 */
	void exitSnapshotQuery(XQueryParser.SnapshotQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#absPathExprWithDoc}.
	 * @param ctx the parse tree
	 */
	void enterAbsPathExprWithDoc(XQueryParser.AbsPathExprWithDocContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#absPathExprWithDoc}.
	 * @param ctx the parse tree
	 */
	void exitAbsPathExprWithDoc(XQueryParser.AbsPathExprWithDocContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#xPathQuery}.
	 * @param ctx the parse tree
	 */
	void enterXPathQuery(XQueryParser.XPathQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#xPathQuery}.
	 * @param ctx the parse tree
	 */
	void exitXPathQuery(XQueryParser.XPathQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(XQueryParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(XQueryParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#forClause}.
	 * @param ctx the parse tree
	 */
	void enterForClause(XQueryParser.ForClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#forClause}.
	 * @param ctx the parse tree
	 */
	void exitForClause(XQueryParser.ForClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#updateExpr}.
	 * @param ctx the parse tree
	 */
	void enterUpdateExpr(XQueryParser.UpdateExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#updateExpr}.
	 * @param ctx the parse tree
	 */
	void exitUpdateExpr(XQueryParser.UpdateExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#insertExpr}.
	 * @param ctx the parse tree
	 */
	void enterInsertExpr(XQueryParser.InsertExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#insertExpr}.
	 * @param ctx the parse tree
	 */
	void exitInsertExpr(XQueryParser.InsertExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#deleteExpr}.
	 * @param ctx the parse tree
	 */
	void enterDeleteExpr(XQueryParser.DeleteExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#deleteExpr}.
	 * @param ctx the parse tree
	 */
	void exitDeleteExpr(XQueryParser.DeleteExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#parentExpr}.
	 * @param ctx the parse tree
	 */
	void enterParentExpr(XQueryParser.ParentExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#parentExpr}.
	 * @param ctx the parse tree
	 */
	void exitParentExpr(XQueryParser.ParentExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#parentXPathExpression}.
	 * @param ctx the parse tree
	 */
	void enterParentXPathExpression(XQueryParser.ParentXPathExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#parentXPathExpression}.
	 * @param ctx the parse tree
	 */
	void exitParentXPathExpression(XQueryParser.ParentXPathExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#childXPathExpression}.
	 * @param ctx the parse tree
	 */
	void enterChildXPathExpression(XQueryParser.ChildXPathExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#childXPathExpression}.
	 * @param ctx the parse tree
	 */
	void exitChildXPathExpression(XQueryParser.ChildXPathExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#simpleXPathExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleXPathExpression(XQueryParser.SimpleXPathExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#simpleXPathExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleXPathExpression(XQueryParser.SimpleXPathExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#insertXPathExpression}.
	 * @param ctx the parse tree
	 */
	void enterInsertXPathExpression(XQueryParser.InsertXPathExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#insertXPathExpression}.
	 * @param ctx the parse tree
	 */
	void exitInsertXPathExpression(XQueryParser.InsertXPathExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#absPathExpr}.
	 * @param ctx the parse tree
	 */
	void enterAbsPathExpr(XQueryParser.AbsPathExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#absPathExpr}.
	 * @param ctx the parse tree
	 */
	void exitAbsPathExpr(XQueryParser.AbsPathExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#steps}.
	 * @param ctx the parse tree
	 */
	void enterSteps(XQueryParser.StepsContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#steps}.
	 * @param ctx the parse tree
	 */
	void exitSteps(XQueryParser.StepsContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#undirectStep}.
	 * @param ctx the parse tree
	 */
	void enterUndirectStep(XQueryParser.UndirectStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#undirectStep}.
	 * @param ctx the parse tree
	 */
	void exitUndirectStep(XQueryParser.UndirectStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#directStep}.
	 * @param ctx the parse tree
	 */
	void enterDirectStep(XQueryParser.DirectStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#directStep}.
	 * @param ctx the parse tree
	 */
	void exitDirectStep(XQueryParser.DirectStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(XQueryParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(XQueryParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#xPathInExpr}.
	 * @param ctx the parse tree
	 */
	void enterXPathInExpr(XQueryParser.XPathInExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#xPathInExpr}.
	 * @param ctx the parse tree
	 */
	void exitXPathInExpr(XQueryParser.XPathInExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(XQueryParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(XQueryParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#position}.
	 * @param ctx the parse tree
	 */
	void enterPosition(XQueryParser.PositionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#position}.
	 * @param ctx the parse tree
	 */
	void exitPosition(XQueryParser.PositionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#last}.
	 * @param ctx the parse tree
	 */
	void enterLast(XQueryParser.LastContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#last}.
	 * @param ctx the parse tree
	 */
	void exitLast(XQueryParser.LastContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#integerNumber}.
	 * @param ctx the parse tree
	 */
	void enterIntegerNumber(XQueryParser.IntegerNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#integerNumber}.
	 * @param ctx the parse tree
	 */
	void exitIntegerNumber(XQueryParser.IntegerNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(XQueryParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(XQueryParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(XQueryParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(XQueryParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#nodeGenerator}.
	 * @param ctx the parse tree
	 */
	void enterNodeGenerator(XQueryParser.NodeGeneratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#nodeGenerator}.
	 * @param ctx the parse tree
	 */
	void exitNodeGenerator(XQueryParser.NodeGeneratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#currentNodes}.
	 * @param ctx the parse tree
	 */
	void enterCurrentNodes(XQueryParser.CurrentNodesContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#currentNodes}.
	 * @param ctx the parse tree
	 */
	void exitCurrentNodes(XQueryParser.CurrentNodesContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#parentNodes}.
	 * @param ctx the parse tree
	 */
	void enterParentNodes(XQueryParser.ParentNodesContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#parentNodes}.
	 * @param ctx the parse tree
	 */
	void exitParentNodes(XQueryParser.ParentNodesContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#childsElements}.
	 * @param ctx the parse tree
	 */
	void enterChildsElements(XQueryParser.ChildsElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#childsElements}.
	 * @param ctx the parse tree
	 */
	void exitChildsElements(XQueryParser.ChildsElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#nodesByType}.
	 * @param ctx the parse tree
	 */
	void enterNodesByType(XQueryParser.NodesByTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#nodesByType}.
	 * @param ctx the parse tree
	 */
	void exitNodesByType(XQueryParser.NodesByTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#textNodes}.
	 * @param ctx the parse tree
	 */
	void enterTextNodes(XQueryParser.TextNodesContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#textNodes}.
	 * @param ctx the parse tree
	 */
	void exitTextNodes(XQueryParser.TextNodesContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#elementName}.
	 * @param ctx the parse tree
	 */
	void enterElementName(XQueryParser.ElementNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#elementName}.
	 * @param ctx the parse tree
	 */
	void exitElementName(XQueryParser.ElementNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(XQueryParser.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(XQueryParser.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(XQueryParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(XQueryParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#insert_pos}.
	 * @param ctx the parse tree
	 */
	void enterInsert_pos(XQueryParser.Insert_posContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#insert_pos}.
	 * @param ctx the parse tree
	 */
	void exitInsert_pos(XQueryParser.Insert_posContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#time}.
	 * @param ctx the parse tree
	 */
	void enterTime(XQueryParser.TimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#time}.
	 * @param ctx the parse tree
	 */
	void exitTime(XQueryParser.TimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#format}.
	 * @param ctx the parse tree
	 */
	void enterFormat(XQueryParser.FormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#format}.
	 * @param ctx the parse tree
	 */
	void exitFormat(XQueryParser.FormatContext ctx);
}