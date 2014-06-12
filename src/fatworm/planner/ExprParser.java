package fatworm.planner;

import fatworm.constant.BooleanConst;
import fatworm.constant.DecimalConst;
import fatworm.constant.IntegerConst;
import fatworm.constant.StringConst;
import fatworm.expr.*;
import fatworm.handler.Fucker;
import fatworm.parser.FatwormParser;
import fatworm.plan.Plan;
import fatworm.util.Function;
import fatworm.util.Operator;
import org.antlr.runtime.tree.CommonTree;

import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class ExprParser {
    public static Expr getExpression(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        switch (tree.getType()) {
            case FatwormParser.T__105: // %
            case FatwormParser.T__108: // *
            case FatwormParser.T__109: // +
            case FatwormParser.T__111: // -
            case FatwormParser.T__113: // /
                if (tree.getChildCount() == 2) {
                    int op = Operator.getOpFromType(tree.getType());
                    Expr left = getExpression((CommonTree)tree.getChild(0), upFuncs, parentPlan);
                    Expr right = getExpression((CommonTree)tree.getChild(1), upFuncs, parentPlan);
                    return new BinaryExpr(left, right, op);
                } else if (tree.getChildCount() == 1) {
                    int op = Operator.getOpFromType(tree.getType());
                    Expr left = new ConstExpr(new IntegerConst(0));
                    Expr right = getExpression((CommonTree)tree.getChild(0), upFuncs, parentPlan);
                    Expr newExpr = new BinaryExpr(left, right, op);
                    if (right instanceof ConstExpr) {
                        return new ConstExpr(newExpr.getResult(null));
                    }
                    return newExpr;
                } else {
                    return new ColNameExpr(null, "*");
                }
            case FatwormParser.T__112: // .
                return new ColNameExpr(tree.getChild(0).getText(), tree.getChild(1).getText());
            case FatwormParser.INTEGER_LITERAL:
                if (tree.getText().length() < 10)
                    return new ConstExpr(new IntegerConst(tree.getText()));
                else
                    return new ConstExpr(new DecimalConst(tree.getText()));
            case FatwormParser.STRING_LITERAL:
                return new ConstExpr(new StringConst(tree.getText()));
            case FatwormParser.FLOAT_LITERAL:
                return new ConstExpr(new DecimalConst(tree.getText()));
            case FatwormParser.TRUE:
                return new ConstExpr(new BooleanConst(true));
            case FatwormParser.FALSE:
                return new ConstExpr(new BooleanConst(false));
            case FatwormParser.NULL:
                return new ConstExpr(null);
            case FatwormParser.DEFAULT:
                return new DefaultExpr();
            case FatwormParser.SELECT:
            case FatwormParser.SELECT_DISTINCT:
                Sucker planner = Fucker.createPlanner();
                planner.doExecute(tree, upFuncs, parentPlan);
                Plan p = planner.getQueryPlan();
                return new QueryExpr(p);
            case FatwormParser.AVG:
            case FatwormParser.COUNT:
            case FatwormParser.MIN:
            case FatwormParser.MAX:
            case FatwormParser.SUM:
                int func = Function.getFuncFromType(tree.getType());
                Expr colName = getExpression((CommonTree)tree.getChild(0), upFuncs, parentPlan);
                FuncExpr funcExpr = new FuncExpr((ColNameExpr)colName, func);
                upFuncs.add(funcExpr);
                return funcExpr;
            default:
                return new ColNameExpr(null, tree.getText());
        }
    }
}
