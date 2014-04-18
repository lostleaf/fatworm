package main;

import org.antlr.runtime.tree.CommonTree;
import plan.*;

import java.util.LinkedList;

import static parser.FatwormParser.*;

/**
 * Created by lostleaf on 14-4-17.
 */
public class planParser {
    public static Plan parse(CommonTree ct) {
        switch (ct.getType()) {
            case SELECT:
            case SELECT_DISTINCT:
                return parseSelect(ct);
            case INSERT_SUBQUERY:
                return new InsertSubqueryPlan(new ExprPlan((CommonTree) ct.getChild(0)),
                        parseSelect((CommonTree) ct.getChild(1)));
            case UPDATE:
                return parseUpdate(ct);
            default:
                return new PlainPlan(ct);
        }
    }

    private static UpdatePlan parseUpdate(CommonTree ct) {
        LinkedList<ExprPlan> cols = new LinkedList<ExprPlan>();
        LinkedList<Plan> values = new LinkedList<Plan>();
        ExprPlan wherePlan = null, toPlan = null;
        for (Object o : ct.getChildren()) {
            CommonTree t = (CommonTree) o;
            switch (t.getType()) {
                case UPDATE_PAIR:
                    cols.add(new ExprPlan((CommonTree) t.getChild(0)));
                    CommonTree ch = (CommonTree) t.getChild(1);
                    values.add(ch.getType() == SELECT ? parseSelect(ch) : new ExprPlan(ch));
                    break;
                case WHERE:
                    wherePlan = new ExprPlan((CommonTree)t.getChild(0));
                    break;
                default:
                    toPlan = new ExprPlan(t);
            }
        }
        return new UpdatePlan(toPlan, cols, values, wherePlan);
    }

    private static Plan parseSelect(CommonTree ct) {
        boolean distinct = ct.getType() == SELECT_DISTINCT;
        LinkedList<ExprPlan> to = new LinkedList<ExprPlan>();
        Plan from = null, wherePlan = null;
        CommonTree whereCond = null, groupExpr = null, groupCond = null, order = null;
        for (Object o : ct.getChildren()) {
            CommonTree tree = (CommonTree) o;
            switch (tree.getType()) {
                case FROM:
                    from = parseFrom(tree);
                    break;
                case WHERE:
                    whereCond = (CommonTree) tree.getChild(0);
                    if (whereCond.getType() == IN)
                        wherePlan = parseSelect((CommonTree) whereCond.getChild(1));
                    else
                        wherePlan = new ExprPlan(whereCond);
                    break;
                case GROUP:
                    groupExpr = (CommonTree) tree.getChild(0);
                    break;
                case HAVING:
                    groupCond = (CommonTree) tree.getChild(0);
                    break;
                case ORDER:
                    order = tree;
                    break;
                default:
                    to.add(new ExprPlan(tree));
            }
        }
        Plan p = (whereCond != null) ? new SelectPlan(from, wherePlan) : null;
        if (groupExpr != null)
            p = new GroupPlan(groupCond, groupExpr, p == null ? from : p);
        p = new ProjectPlan(p == null ? from : p, to, distinct);
        return (order != null) ? new OrderPlan(p, order) : p;
    }

    private static Plan parseFrom(CommonTree ct) {
        LinkedList<Plan> l = new LinkedList<Plan>();
        for (Object o : ct.getChildren()) {
            CommonTree tree = (CommonTree) o;
            if (tree.getType() == AS) {
                CommonTree ch = (CommonTree) tree.getChild(0);
                Plan p;
                if (ch.getType() == SELECT || ch.getType() == SELECT_DISTINCT)
                    p = parseSelect(ch);
                else
                    p = new TablePlan(ch);
                l.add(new RenamePlan(p, (CommonTree) tree.getChild(1)));
            } else
                l.add(new TablePlan(tree));
        }
        return new JoinPlan(l);
    }
}
