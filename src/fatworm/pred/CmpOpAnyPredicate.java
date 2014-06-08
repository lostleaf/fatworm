package fatworm.pred;

import fatworm.constant.Const;
import fatworm.expr.Expression;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

public class CmpOpAnyPredicate implements Predicate {

    private Expression expr;
    private Plan plan;
    private int cmpOp;

    public CmpOpAnyPredicate(Expression expr, Plan plan, int op) {
        this.expr = expr;
        this.plan = plan;
        this.cmpOp = op;
    }

    @Override
    public boolean isSatisfied(Scan s) {
        Const c = expr.getResult(s);
        Scan s1 = plan.open(s);
        while (s1.next()) {
            if (s1.getColumnCount() != 1) return false;
            Const in = s1.get(0);
            int compare = c.compareTo(in);
            if (CmpOp.doCompare(compare, cmpOp)) return true;
        }
        return false;
    }

    public String toString() {
        return "Predicate cmpOp any: ( " + "expr: ( " + expr.toString() + " ), " +
                "cmpOp: ( " + cmpOp + " ), " + "plan: ( " + plan.toString() + " ) )";
    }

    @Override
    public void renameTable(String from, String to) {
        expr.renameTable(from, to);
        plan.renameTable(from, to);
    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        return new HashSet<String>();
//		HashSet<String> s = expr.getTblNames(plan);
//		s.addAll(plan.getAllUsedTblNames());
//		return s;
    }

    @Override
    public HashSet<String> getAllUsedTblNames(Plan p) {
        HashSet<String> s = expr.getTblNames(p);
        s.addAll(p.getAllUsedTblNames());
        return s;
    }

}
