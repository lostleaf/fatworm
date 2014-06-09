package fatworm.pred;

import fatworm.constant.Const;
import fatworm.expr.Expr;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

public class CmpOpAllPredicate implements Predicate {

    private Expr e;
    private Plan p;
    private int cop;

    public CmpOpAllPredicate(Expr e, Plan p, int cop) {
        this.e = e;
        this.p = p;
        this.cop = cop;
    }

    @Override
    public boolean isSatisfied(Scan s) {
        Const c = e.getResult(s);
        Scan s1 = p.open(s);
        while (s1.next()) {
            if (s1.getColumnCount() != 1) return false;
            Const in = s1.get(s1.getFieldName(0), true);
            int compare = c.compareTo(in);
            if (!CmpOp.doCompare(compare, cop)) return false;
        }
        return true;
    }

    public String toString() {
        return "Predicate cop all: ( " + "expr: ( " + e.toString() + " ), " +
                "cop: ( " + cop + " ), " + "plan: ( " + p.toString() + " ) )";
    }

    @Override
    public void renameTable(String from, String to) {
        e.renameTable(from, to);
        p.renameTable(from, to);
    }

}
