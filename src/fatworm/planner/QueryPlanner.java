package fatworm.planner;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expr;
import fatworm.expr.FuncExpr;
import fatworm.handler.Manager;
import fatworm.meta.Schema;
import fatworm.parser.FatwormParser;
import fatworm.plan.*;
import fatworm.pred.Predicate;
import fatworm.scan.TableScan;
import org.antlr.runtime.tree.CommonTree;

import java.util.*;

/**
 * Created by lostleaf on 14-6-5.
 */
public class QueryPlanner {
    public Plan getPlan(CommonTree tree, List<FuncExpr> funcs, Plan parentPlan) {
        return parseSelect(tree, funcs, parentPlan);
    }

    public Plan parseSelect(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        last = tree.getChildCount() - 1;
        int[] idxClauses = extractClauses(tree);
//        System.out.println(last);
        Plan p = parseFrom(tree, idxClauses[0], upFuncs, parentPlan);
        p = parseWhere(p, idxClauses[1], tree, upFuncs, parentPlan);
        
        int groupClause = idxClauses[2], havingClause = idxClauses[3], orderClause = idxClauses[4];
        Plan insertGroup = null;
        GroupPlan groupP = null;

        if (groupClause >= 0) {
            groupP = new GroupPlan(p, (ColNameExpr)ExprPlanner.getExpression(
                    (CommonTree)tree.getChild(groupClause).getChild(0), upFuncs, p), parentPlan);
            p = groupP;
            insertGroup = p;
        }

        Plan insertHaving = null;
        List<FuncExpr> funcs = new ArrayList<FuncExpr>();

        if (havingClause >= 0) {
            p = new SelectPlan(p,
                    PredParser.getPredicate((CommonTree)tree.getChild(havingClause).getChild(0),
                            funcs, p), parentPlan);
            if (insertGroup == null) insertGroup = p;
            insertHaving = p;
        }

        Plan projectP = null;

        int numProjs = last;
        Map<CommonTree, String> renaming = new HashMap<CommonTree, String>();
        while (last >= 0) {
            p = getRenamePlan((CommonTree)tree.getChild(last--), p, insertGroup, insertHaving,
                    renaming, funcs, parentPlan);
            if (projectP == null) projectP = p;
        }

        if (orderClause >= 0) {
            CommonTree subTree = (CommonTree)tree.getChild(orderClause);
            int son = subTree.getChildCount();
            for (int i = son - 1; i >= 0; --i) {
                CommonTree t = (CommonTree)subTree.getChild(i);
                boolean asc = true;
                if (t.getType() == FatwormParser.DESC) {
                    t = (CommonTree)t.getChild(0);
                    asc = false;
                } else if (t.getType() == FatwormParser.ASC) {
                    t = (CommonTree)t.getChild(0);
                }
                p = new OrderPlan(p, (ColNameExpr)ExprPlanner.getExpression(t, upFuncs, p),
                        asc, parentPlan);
            }
        }

        List<ColNameExpr> projs = new ArrayList<ColNameExpr>();
        for (int i = 0; i <= numProjs; ++i) {
            CommonTree subTree = (CommonTree)tree.getChild(i);
            if (subTree.getType() == FatwormParser.AS) {
                projs.add(new ColNameExpr(null, subTree.getChild(1).getText()));
            } else if (renaming.containsKey(subTree)) {
                projs.add(new ColNameExpr(null, renaming.get(subTree)));
            } else {
                projs.add((ColNameExpr)ExprPlanner.getExpression(subTree, funcs, p));
            }
        }
        p = new ProjectPlan(p, projs, parentPlan);
        if (projectP == null) projectP = p;

        if (tree.getType() == FatwormParser.SELECT_DISTINCT) {
            p = new DistinctPlan(p, parentPlan);
        }

        upFuncs.addAll(funcs);
        if (groupP != null) {
            groupP.addFuncs(funcs);
        } else if (funcs.size() > 0) {
            GroupPlan gp = new GroupPlan(projectP.getPlan(), null, parentPlan);
            gp.addFuncs(funcs);
            projectP.setPlan(gp);
        }

        return p;
    }

    int last = -1;

    public int[] extractClauses(CommonTree tree) {
        int[] result = new int[5];
        Arrays.fill(result, -1);

        if (tree.getChild(last).getType() == FatwormParser.ORDER) result[4] = last--;
        if (tree.getChild(last).getType() == FatwormParser.HAVING) result[3] = last--;
        if (tree.getChild(last).getType() == FatwormParser.GROUP) result[2] = last--;
        if (tree.getChild(last).getType() == FatwormParser.WHERE) result[1] = last--;
        if (tree.getChild(last).getType() == FatwormParser.FROM) result[0] = last--;
        return result;
    }

    private Plan parseFrom(CommonTree tree, int fromClause, List<FuncExpr> upFuncs, Plan parentPlan) {
        Plan p;
        if (fromClause >= 0) {
            CommonTree fromTree = (CommonTree) tree.getChild(fromClause);
            int fromNum = fromTree.getChildCount();
            p = parseTable((CommonTree) fromTree.getChild(0), upFuncs, parentPlan);
            if (fromNum > 1) {
                List<Plan> plans = new ArrayList<Plan>();
                plans.add(p);
                for (int i = 1; i < fromNum; ++i) {
                    p = parseTable((CommonTree) fromTree.getChild(i), upFuncs, parentPlan);
                    plans.add(p);
                }
                p = new OriginProductPlan(plans, parentPlan);
            }
        } else {
            if (Manager.getDBManager().getCurrentDB().getTable(EmptyTableName) == null)
                dealEmptyTableNull(parentPlan);
            p = new TablePlan(EmptyTableName, parentPlan);
        }
        return p;
    }

    private void dealEmptyTableNull(Plan parentPlan) {
        Schema sch = new Schema();
        Manager.getDBManager().getCurrentDB().addTable(EmptyTableName, sch);
        TablePlan tp = new TablePlan(EmptyTableName, parentPlan);
        TableScan ts = (TableScan) tp.open(null);
        ts.insert(new ArrayList<Const>());
    }

    public Plan parseWhere(Plan plan, int where, CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        if (where >= 0) {
            Predicate pred = PredParser.getPredicate(
                    (CommonTree) tree.getChild(where).getChild(0), upFuncs, parentPlan);
            plan = new SelectPlan(plan, pred, parentPlan);
        }

        return plan;
    }

    protected Plan parseTable(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        if (tree.getType() == FatwormParser.AS) {
            CommonTree left = (CommonTree) tree.getChild(0), right = (CommonTree) tree.getChild(1);
            if (left.getType() == FatwormParser.ID)
                return new RenamePlan(new TablePlan(left.getText(), parentPlan),
                        right.getText(), parentPlan);
            else
                return new RenamePlan(parseSelect(left, upFuncs, parentPlan), right.getText(), parentPlan);
        }
        return new TablePlan(tree.getText(), parentPlan);
    }
    protected Plan getRenamePlan(CommonTree tree, Plan p, Plan insertGroup, Plan insertHaving,
                                 Map<CommonTree, String> renaming, List<FuncExpr> upFuncs, Plan parentPlan) {
        if (tree.getType() == FatwormParser.AS) {
            Expr from = ExprPlanner.getExpression((CommonTree)tree.getChild(0), upFuncs, p);
            String to = tree.getChild(1).getText();
            if (from instanceof ColNameExpr && insertGroup != null) {
                Plan subP = insertGroup.getPlan();
                subP = new ExtendPlan(subP, from, to, parentPlan);
                insertGroup.setPlan(subP);
                return p;
            } else if (insertHaving != null) {
                Plan subP = insertHaving.getPlan();
                subP = new ExtendPlan(subP, from, to, parentPlan);
                insertHaving.setPlan(subP);
                return p;
            } else {
                return new ExtendPlan(p, from, to, parentPlan);
            }

        }
        Expr expr = ExprPlanner.getExpression(tree, upFuncs, p);
        if (expr instanceof ColNameExpr) {
            return p;
        }
        String newName = getNewName();
        renaming.put(tree, newName);
        return new ExtendPlan(p, expr, newName, parentPlan);
    }
    protected static String EmptyTableName = "emptyTable_shit";
    protected String getNewName() {
        return "temp_shit_" + Integer.toString(num++);
    }

    protected static int num = 0;
}
