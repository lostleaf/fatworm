package fatworm.pred;

import fatworm.constant.*;
import fatworm.expr.Expr;
import fatworm.scan.Scan;

/**
 * Created by lostleaf on 14-6-5.
 */
public class CmpOpPredicate implements Predicate {
    private Expr left, right;
    private int cmpOp;

    public CmpOpPredicate(Expr left, Expr right, int op) {
        this.left = left;
        this.right = right;
        cmpOp = op;
    }

    @Override
    public boolean isSatisfied(Scan s) {
        Const l = left.getResult(s);
        Const r = right.getResult(s);
        if (l instanceof NullConst || r instanceof NullConst)
            return false;
        else if (l instanceof TimestampConst || r instanceof TimestampConst) {
            l = l.toTimestampConst();
            r = r.toTimestampConst();
        } else if (l instanceof StringConst || r instanceof StringConst) {
            l = l.toStringConst();
            r = r.toStringConst();
        } else if (l instanceof DecimalConst || r instanceof DecimalConst) {
            l = l.toDecimalConst();
            r = r.toDecimalConst();
        } else if (l instanceof DoubleConst || r instanceof DoubleConst) {
            l = l.toDoubleConst();
            r = r.toDoubleConst();
        } else if (l instanceof FloatConst || r instanceof FloatConst) {
            l = l.toFloatConst();
            r = r.toFloatConst();
        } else if (l instanceof IntegerConst || r instanceof IntegerConst) {
            l = l.toIntegerConst();
            r = r.toIntegerConst();
        }

        int compare = l.compareTo(r);
        return CmpOp.doCompare(compare, cmpOp);
    }

    @Override
    public void renameTable(String from, String to) {
        left.renameTable(from, to);
        right.renameTable(from, to);
    }

}
