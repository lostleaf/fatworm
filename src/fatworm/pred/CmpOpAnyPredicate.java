package fatworm.pred;

import fatworm.constant.Const;
import fatworm.expr.Expr;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

public class CmpOpAnyPredicate implements Predicate {

    private Expr expr;
    private Plan plan;
    private int cmpOp;

    public CmpOpAnyPredicate(Expr expr, Plan plan, int op) {
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

}
