package fatworm.planner;

import fatworm.expr.Expression;
import fatworm.expr.FuncExpr;
import fatworm.handler.Manager;
import fatworm.parser.FatwormParser;
import fatworm.plan.Plan;
import fatworm.pred.*;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class PredParser {
    public static AllAndPredicate getPredicate(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        Predicate pred = parsePredicate(tree, upFuncs, parentPlan);
        List<Predicate> predicateList = toCNF(pred);
        return new AllAndPredicate(predicateList);
    }

    public static Predicate parsePredicate(CommonTree tree, List<FuncExpr> upFuncs, Plan parentPlan) {
        switch (tree.getType()) {
            case FatwormParser.OR: {
                Predicate left = parsePredicate((CommonTree) tree.getChild(0), upFuncs, parentPlan);
                Predicate right = parsePredicate((CommonTree) tree.getChild(1), upFuncs, parentPlan);
                return new OrPredicate(right, left);
            }
            case FatwormParser.AND: {
                Predicate left = parsePredicate((CommonTree) tree.getChild(0), upFuncs, parentPlan);
                Predicate right = parsePredicate((CommonTree) tree.getChild(1), upFuncs, parentPlan);
                return new AndPredicate(right, left);
            }
            case FatwormParser.EXISTS: {
                Planner planner = Manager.createPlanner();
                planner.execute((CommonTree) tree.getChild(0), upFuncs, parentPlan);
                Plan p = planner.getQueryPlan();
                return new ExistsPredicate(p);
            }
            case FatwormParser.NOT_EXISTS: {
                Planner planner = Manager.createPlanner();
                planner.execute((CommonTree) tree.getChild(0), upFuncs, parentPlan);
                Plan p = planner.getQueryPlan();
                return new NotExistsPredicate(p);
            }
            case FatwormParser.IN: {
                Expression e = ExprPlanner.getExpression((CommonTree) tree.getChild(0), upFuncs,
                        parentPlan);
                Planner planner = Manager.createPlanner();
                planner.execute((CommonTree) tree.getChild(1), upFuncs, parentPlan);
                Plan p = planner.getQueryPlan();
                return new InPredicate(e, p);
            }
            case FatwormParser.ALL: {
                Expression e = ExprPlanner.getExpression((CommonTree) tree.getChild(0), upFuncs,
                        parentPlan);
                Planner planner = Manager.createPlanner();
                planner.execute((CommonTree) tree.getChild(2), upFuncs, parentPlan);
                Plan p = planner.getQueryPlan();
                int op = CmpOp.getCopFromType(tree.getChild(1).getType());
                return new CmpOpAllPredicate(e, p, op);
            }
            case FatwormParser.ANY: {
                Expression e = ExprPlanner.getExpression((CommonTree) tree.getChild(0), upFuncs,
                        parentPlan);
                Planner planner = Manager.createPlanner();
                planner.execute((CommonTree) tree.getChild(2), upFuncs, parentPlan);
                Plan p = planner.getQueryPlan();
                int op = CmpOp.getCopFromType(tree.getChild(1).getType());
                return new CmpOpAnyPredicate(e, p, op);
            }

            //less, greater, equal etc.
            case FatwormParser.T__114:
            case FatwormParser.T__115:
            case FatwormParser.T__116:
            case FatwormParser.T__117:
            case FatwormParser.T__118:
            case FatwormParser.T__119: {
                int op = CmpOp.getCopFromType(tree.getType());
                Expression left = ExprPlanner.getExpression((CommonTree) tree.getChild(0),
                        upFuncs, parentPlan);
                Expression right = ExprPlanner.getExpression((CommonTree) tree.getChild(1),
                        upFuncs, parentPlan);
                return new CmpOpPredicate(left, right, op);
            }
        }
        return null;
    }

    private static List<Predicate> toCNF(Predicate pred) {
        if (pred instanceof AndPredicate) {
            AndPredicate andPredicate = (AndPredicate) pred;
            List<Predicate> p1 = toCNF(andPredicate.getLeft());
            List<Predicate> p2 = toCNF(andPredicate.getRight());
            p1.addAll(p2);
            return p1;
        }

        if (pred instanceof OrPredicate) {
            OrPredicate orPredicate = (OrPredicate) pred;
            List<Predicate> p1 = toCNF(orPredicate.getLeft());
            List<Predicate> p2 = toCNF(orPredicate.getRight());
            List<Predicate> p = new ArrayList<Predicate>();
            for (Predicate pred1 : p1)
                for (Predicate pred2 : p2)
                    p.add(new OrPredicate(pred1, pred2));
            return p;
        }

        List<Predicate> p = new ArrayList<Predicate>();
        p.add(pred);
        return p;
    }
}
