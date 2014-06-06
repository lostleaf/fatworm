package fatworm.planner;

import fatworm.expr.ColNameExpr;
import fatworm.expr.FuncExpr;
import fatworm.handler.Manager;
import fatworm.parser.FatwormParser;
import fatworm.plan.Plan;
import fatworm.plan.ProjectPlan;
import fatworm.plan.SelectPlan;
import fatworm.plan.TablePlan;
import fatworm.pred.Predicate;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class QueryPlanner {
    public Plan getPlan(CommonTree tree, List<FuncExpr> funcs, Plan parentPlan) {
        return parseSelect(tree, funcs, parentPlan);
    }
    int last = -1;
    public int[] extractClauses(CommonTree tree) {
        int[] result = new int[5];
        Arrays.fill(result, -1);

        if (tree.getChild(last).getType() == FatwormParser.ORDER) result[4] = last--;
        if (tree.getChild(last).getType() == FatwormParser.HAVING) result[3] = last--;
        if (tree.getChild(last).getType() == FatwormParser.GROUP) result[2] = last--;
        if (tree.getChild(last).getType() == FatwormParser.WHERE) result[1] = last--;
        if (tree.getChild(last).getType() == FatwormParser.FROM) result[0] = last;
        return result;
    }

    private Plan parseFrom(CommonTree tree, int fromClause, List<FuncExpr> upFuncs, Plan parentPlan) {
        Plan p;
        if (fromClause >= 0) {
            CommonTree fromTree = (CommonTree) tree.getChild(fromClause);
            int fromNum = fromTree.getChildCount();
            p = getTablePlan((CommonTree) fromTree.getChild(0), upFuncs, parentPlan);
            if (fromNum > 1) {
                List<Plan> plans = new ArrayList<Plan>();
                plans.add(p);
                for (int i = 1; i < fromNum; ++i) {
                    p = getTablePlan((CommonTree) fromTree.getChild(i), upFuncs, parentPlan);
                    plans.add(p);
                }
                //TODO support product
                //p = new OriginProductPlan(plans, parentPlan);
            }
        } else {
            if (Manager.getDBManager().getCurrentDB().getTable(emptyTableName) == null)
                dealEmptyTableNull();
            p = new TablePlan(emptyTableName, parentPlan);
        }
        return p;
    }

    private void dealEmptyTableNull() {

    }

    public Plan parseWhere(Plan plan, int where, CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        if (where >= 0) {
            Predicate pred = PredPlanner.getPredicate(
                    (CommonTree) tree.getChild(where).getChild(0), upFuncs, parentPlan);
            plan = new SelectPlan(plan, pred, parentPlan);
        }

        return plan;
    }
    public Plan parseProject(Plan plan, CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        List<ColNameExpr> projects = new ArrayList<ColNameExpr>();

        for (int i = 0; i <= last; i++) {
            CommonTree t = (CommonTree) tree.getChild(i);
            projects.add((ColNameExpr) ExprPlanner.getExpression(t, new ArrayList<FuncExpr>(), parentPlan));
        }

        return new ProjectPlan(plan, projects, parentPlan);
    }
    public Plan parseSelect(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        int[] idxClauses = extractClauses(tree);
        last = tree.getChildCount() - 1;
        Plan plan = parseFrom(tree, idxClauses[0], upFuncs, parentPlan);
        plan = parseWhere(plan, idxClauses[1], tree, upFuncs, parentPlan);
        plan = parseProject(plan, tree, upFuncs, parentPlan);
        return plan;
    }

    protected Plan getTablePlan(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        //TODO deal with more complicated case (renaming etc.)
        return new TablePlan(tree.getText(), parentPlan);
    }

    protected static String emptyTableName = "empty_shit";
}
