package fatworm.planner;

import fatworm.expr.Expression;
import fatworm.expr.FuncExpr;
import fatworm.plan.Plan;
import fatworm.pred.CmpOp;
import fatworm.pred.CmpOpPredicate;
import fatworm.pred.Predicate;
import org.antlr.runtime.tree.CommonTree;

import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class PredPlanner {
    public static Predicate getPredicate(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan){
        int cmpOp = CmpOp.getCopFromType(tree.getType());
        Expression left = ExprPlanner.getExpression((CommonTree) tree.getChild(0),
                upFuncs, parentPlan);
        Expression right = ExprPlanner.getExpression((CommonTree) tree.getChild(1),
                upFuncs, parentPlan);
        return new CmpOpPredicate(left, right, cmpOp);
    }
}
