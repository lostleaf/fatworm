package fatworm.planner;

import fatworm.constant.DecimalConst;
import fatworm.constant.IntegerConst;
import fatworm.expr.ColNameExpr;
import fatworm.expr.ConstExpr;
import fatworm.expr.Expression;
import fatworm.expr.FuncExpr;
import fatworm.parser.FatwormParser;
import fatworm.plan.Plan;
import org.antlr.runtime.tree.CommonTree;

import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class ExprPlanner {
    public static Expression getExpression(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        if (tree.getType() == FatwormParser.INTEGER_LITERAL)
            if (tree.getText().length() < 10)
                return new ConstExpr(new IntegerConst(tree.getText()));
            else
                return new ConstExpr(new DecimalConst(tree.getText()));
        return new ColNameExpr(null, tree.getText());
    }
}
