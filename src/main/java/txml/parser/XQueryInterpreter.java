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

import java.nio.file.Paths;
import txml.interpreter.model.Instruction;
import txml.interpreter.InstructionsInterpreter;

public class XQueryInterpreter extends XQueryBaseListener {
    InstructionsInterpreter instructionsInterpreter;
    private Integer lastVariable = null;
    private Integer lastLabel = null;
    
    public XQueryInterpreter(InstructionsInterpreter interpreter) {
        this.instructionsInterpreter = interpreter;
    }
    
    private String getLastGenVariable() {        
        return "$" + lastVariable.toString();
    }
    
    private String genVariable() {
        if (lastVariable == null) {
            lastVariable = 1;
            return "$" + lastVariable.toString() ;
        }
        
        return "$" + (++lastVariable).toString();
    }
    
    private String getLastGenLabel() {        
        return "L" + lastLabel.toString();
    }
    
    private String getPreviousGenLabel() {        
        return "L" + (new Integer(lastLabel - 1)).toString();
    }
    
    private String genLabel() {
        if (lastLabel == null) {
            lastLabel = 1;
            return "L" + lastLabel.toString() ;
        }
        
        return "L" + (++lastLabel).toString();
    }
    
    public String literalToString(String literal) {
        return literal.substring(1, literal.length() - 1);
    }
    
    @Override 
    public void enterPredicate(XQueryParser.PredicateContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("SAVE_START_PATHS", null, null, null, getLastGenVariable()));  
    }

    @Override 
    public void exitPredicate(XQueryParser.PredicateContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("GO_BACK_TO_START_PATHS", null, null, null, getLastGenVariable()));
    }

    @Override 
    public void enterDirectStep(XQueryParser.DirectStepContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("GET_DIRECT_STEP", null, ctx.nodeGenerator().getText(), getLastGenVariable(), getLastGenVariable()));
    }
    
    @Override 
    public void enterUndirectStep(XQueryParser.UndirectStepContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("GET_UNDIRECT_STEP", null, ctx.nodeGenerator().getText(), getLastGenVariable(), getLastGenVariable()));
    }
    
    @Override
    public void enterXPathInExpr(XQueryParser.XPathInExprContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("GET_DIRECT_STEP", null, ctx.nodeGenerator().getText(), getLastGenVariable(), getLastGenVariable()));
    }
    
    @Override 
    public void exitPosition(XQueryParser.PositionContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("FILTER_POSITION", null, ctx.getText().toLowerCase(), getLastGenVariable(), getLastGenVariable()));
    }
    
    @Override 
    public void exitXPathInExpr(XQueryParser.XPathInExprContext ctx) { 
        String valueLiteral = ctx.getParent().getRuleContext(XQueryParser.ValueContext.class,0).getText();
        String operator = ctx.getParent().getRuleContext(XQueryParser.OperatorContext.class,0).getText();
        String value = valueLiteral.substring(1, valueLiteral.length()-1);
        String filter;
        String invertFilter;
        boolean valueFilter = true;
        
        switch (operator) {
            case "=":
                filter = "FILTER_EQ";
                invertFilter = "FILTER_EQ";
                break;
            case "!=":
                filter = "FILTER_NEQ";
                invertFilter = "FILTER_NEQ";
                break;
            case "<":
                filter = "FILTER_LESS";
                invertFilter = "FILTER_MORE";
                break;
            case ">":
                filter = "FILTER_MORE"; 
                invertFilter = "FILTER_LESS";
                break;
            case "<=":
                filter = "FILTER_LE";
                invertFilter = "FILTER_GE";
                break;
            case ">=":
                filter = "FILTER_GE";
                invertFilter = "FILTER_LE";
                break;
            case "PRECEDES":
                valueFilter = false;
                filter = "FILTER_PRECEDES";
                invertFilter = "FILTER_FOLLOWS";
                break;
            case "FOLLOWS":
                valueFilter = false;
                filter = "FILTER_FOLLOWS";
                invertFilter = "FILTER_PRECEDES";
                break;
            case "MEETS":
                valueFilter = false;
                filter = "FILTER_LMEETS";
                invertFilter = "FILTER_RMEETS";
                break;
            case "LMEETS":
                valueFilter = false;
                filter = "FILTER_LMEETS";
                invertFilter = "FILTER_RMEETS";
                break;
            case "RMEETS":
                valueFilter = false;
                filter = "FILTER_RMEETS";
                invertFilter = "FILTER_LMEETS";
                break;
            case "OVERLAPS":
                valueFilter = false;
                filter = "FILTER_OVERLAPS";
                invertFilter = "FILTER_OVERLAPS";
                break;    
            case "CONTAINS":
                valueFilter = false;
                filter = "FILTER_CONTAINS";
                invertFilter = "FILTER_IN";
                break; 
            case "IN":
                valueFilter = false;
                filter = "FILTER_IN";
                invertFilter = "FILTER_CONTAINS";
                break; 
            default:
                filter = "UNKNOWN_OPERATOR";
                invertFilter = "UNKNOWN_OPERATOR";
                break;   
        }
        
        if (valueFilter) {
            this.instructionsInterpreter.add(new Instruction("GET_VALUES", null, getLastGenVariable(), null, getLastGenVariable()));
        }
        
        if (ctx.getParent().getChild(0).getText().equals(valueLiteral)) {
            this.instructionsInterpreter.add(new Instruction(invertFilter, null, getLastGenVariable(), value, getLastGenVariable()));
        } else {
            this.instructionsInterpreter.add(new Instruction(filter, null, getLastGenVariable(), value, getLastGenVariable())); 
        }
                
    }
    
    @Override 
    public void enterAbsPathExprWithDoc(XQueryParser.AbsPathExprWithDocContext ctx) { 
        String schemaName = literalToString(ctx.doc().schemaName().getText());
        String documentName = literalToString(ctx.doc().documentName().getText());
        this.instructionsInterpreter.add(new Instruction("SET_EMPTY_TNODE_LIST", null, schemaName, documentName, genVariable()));
    }
    
    @Override 
    public void exitAbsPathExprWithDoc(XQueryParser.AbsPathExprWithDocContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("REMOVE_DUPLICATES_IN_LIST", null, getLastGenVariable(), null, getLastGenVariable()));
    }
    
    @Override
    public void exitXPathQuery(XQueryParser.XPathQueryContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("RETURN_LIST", null, getLastGenVariable(), null, null));
    }
    
    @Override 
    public void exitForClause(XQueryParser.ForClauseContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("ERASE_TXML_NODES", null, null, null, getLastGenVariable()));
        this.instructionsInterpreter.add(new Instruction("CHECK_SORT", null, null, null, getLastGenVariable()));
        this.instructionsInterpreter.add(new Instruction("ITERATOR_INIT", null, null, null, getLastGenVariable()));
        this.instructionsInterpreter.add(new Instruction("LABEL", null, null, null, genLabel()));
        this.instructionsInterpreter.add(new Instruction("GOTO_IF_NO_NEXT", null, getLastGenVariable(), null, genLabel()));
        this.instructionsInterpreter.add(new Instruction("ITERATOR_NEXT", null, getLastGenVariable(), null, ctx.variable().getText()));
    }
    
    @Override 
    public void enterChildXPathExpression(XQueryParser.ChildXPathExpressionContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("SET_TNODE_LIST_IN_CYCLE", null, ctx.variable().getText(), null, genVariable()));
    }
    
    @Override 
    public void enterParentXPathExpression(XQueryParser.ParentXPathExpressionContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("SET_LIST_FOR_PARENT", null, ctx.variable().getText(), null, genVariable()));
    
    }
    
    @Override 
    public void exitDeleteExpr(XQueryParser.DeleteExprContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("DELETE_IN_DOCUMENT", null, getLastGenVariable(), null, null));
    }

    @Override 
    public void exitUpdate(XQueryParser.UpdateContext ctx) { 
        this.instructionsInterpreter.add(new Instruction("GOTO",  null, null, null, getPreviousGenLabel()));
        this.instructionsInterpreter.add(new Instruction("LABEL", null, null , null, getLastGenLabel()));
    }
    
    @Override 
    public void exitParentExpr(XQueryParser.ParentExprContext ctx) { 
        String position = ctx.insert_pos() != null ? literalToString(ctx.insert_pos().getText()) : null;
        
        this.instructionsInterpreter.add(new Instruction("SET_PARENT_IN_DOCUMENT", position, ctx.parentXPathExpression().variable().getText(), getLastGenVariable(), null));
    
    }
    
    @Override 
    public void exitInsertExpr(XQueryParser.InsertExprContext ctx) { 
        String value = ctx.value() != null ? literalToString(ctx.value().getText()) : null;
        String position = ctx.insert_pos() != null ? literalToString(ctx.insert_pos().getText()) : null;

        this.instructionsInterpreter.add(new Instruction("INSERT_INTO_DOCUMENT", position, ctx.insertXPathExpression().variable().getText(), value, ctx.insertXPathExpression().simpleXPathExpression().getText()));

    }
        
    @Override 
    public void exitSnapshotQuery(XQueryParser.SnapshotQueryContext ctx) { 
        String timeLiteral = ctx.time().getText();
        String time = timeLiteral.substring(1, timeLiteral.length() - 1);
        
        String documentNameLiteral = ctx.documentName().getText();
        String documentName = documentNameLiteral.substring(1, documentNameLiteral.length() - 1);
        
        String schemaNameLiteral = ctx.schemaName().getText();
        String schemaName = schemaNameLiteral.substring(1, schemaNameLiteral.length() - 1);

        this.instructionsInterpreter.add(new Instruction("RETURN_SNAPSHOT", schemaName, documentName, null, time));
    
    }
    
    @Override 
    public void enterDeclareOptionTimeFormat(XQueryParser.DeclareOptionTimeFormatContext ctx) { 
        String timeFormatLiteral = ctx.timeFormat().getText();
        String timeFormat = timeFormatLiteral.substring(1, timeFormatLiteral.length() - 1);
        this.instructionsInterpreter.add(new Instruction("DECLARE_OPTION_TIME_FORMAT", null, timeFormat, null, null));
    }
    
    @Override 
    public void exitDeclareOptionConnection(XQueryParser.DeclareOptionConnectionContext ctx) {
        String connectionUrlLiteral = ctx.connectionUrl().getText();
        String connectionUrl = connectionUrlLiteral.substring(1, connectionUrlLiteral.length() - 1);
        this.instructionsInterpreter.add(new Instruction("DECLARE_OPTION_CONNECTION", null, connectionUrl, null, null));
    }
    
    @Override public void exitDeclareOptionSort(XQueryParser.DeclareOptionSortContext ctx) { 
        String sortLiteral = ctx.sort().getText();
        String sort = sortLiteral.substring(1, sortLiteral.length() - 1);
        this.instructionsInterpreter.add(new Instruction("DECLARE_OPTION_SORT", null, sort, null, null));
    }
    
    @Override 
    public void exitInitSchema(XQueryParser.InitSchemaContext ctx) { 
        String schemaNameLiteral = ctx.schemaName().getText();
        String schemaName = schemaNameLiteral.substring(1, schemaNameLiteral.length() - 1);
        this.instructionsInterpreter.add(new Instruction("INIT_SCHEMA", null, schemaName, null, null));  
    }
    
    @Override 
    public void enterDeinitSchema(XQueryParser.DeinitSchemaContext ctx) { 
        String schemaNameLiteral = ctx.schemaName().getText();
        String schemaName = schemaNameLiteral.substring(1, schemaNameLiteral.length() - 1);
        this.instructionsInterpreter.add(new Instruction("DEINIT_SCHEMA", null, schemaName, null, null));   
    }
    
    @Override 
    public void exitStoreDocumentWithName(XQueryParser.StoreDocumentWithNameContext ctx) { 
        String fileFullNameLiteral = ctx.fileFullName().getText();
        String fileFullName = fileFullNameLiteral.substring(1, fileFullNameLiteral.length() - 1);
        String documentNameLiteral = ctx.documentName().getText();
        String documentName = documentNameLiteral.substring(1, documentNameLiteral.length() - 1);
        String schemaNameLiteral = ctx.schemaName().getText();
        String schemaName = schemaNameLiteral.substring(1, schemaNameLiteral.length() - 1);
        this.instructionsInterpreter.add(new Instruction("STORE_DOCUMENT", null, fileFullName, documentName, schemaName));    
    }
    
    @Override 
    public void exitStoreDocumentWithoutName(XQueryParser.StoreDocumentWithoutNameContext ctx) { 
        String fileFullNameLiteral = ctx.fileFullName().getText();
        String fileFullName = fileFullNameLiteral.substring(1, fileFullNameLiteral.length() - 1);
        String documentName = Paths.get(fileFullName).getFileName().toString();
        String schemaNameLiteral = ctx.schemaName().getText();
        String schemaName = schemaNameLiteral.substring(1, schemaNameLiteral.length() - 1);
        this.instructionsInterpreter.add(new Instruction("STORE_DOCUMENT", null, fileFullName, documentName, schemaName));    
    }
    
    
}
