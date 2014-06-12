package fatworm.expr;

import fatworm.constant.*;
import fatworm.scan.Scan;
import fatworm.util.Operator;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-7.
 */
public class BinaryExpr implements Expr {

    private Expr a, b;
    private int op;

    public BinaryExpr(Expr a, Expr b, int op) {
        this.a = a;
        this.b = b;
        this.op = op;
    }

    @Override
    public Const getResult(Scan s) {
        Const left = a.getResult(s), right = b.getResult(s);
        if (left instanceof StringConst || right instanceof StringConst ||
                left instanceof TimestampConst || right instanceof TimestampConst) {
            left = left.toStringConst();
            right = right.toStringConst();
        }
        if (left instanceof DecimalConst || right instanceof DecimalConst
                || op == Operator.DIVIDE) {
            left = left.toDecimalConst();
            right = right.toDecimalConst();
        } else if (left instanceof DoubleConst || right instanceof DoubleConst) {
            left = left.toDoubleConst();
            right = right.toDoubleConst();
        } else if (left instanceof IntegerConst || right instanceof IntegerConst) {
            left = left.toIntegerConst();
            right = right.toIntegerConst();
        } else {
            left = left.toIntegerConst();
            right = right.toIntegerConst();
        }

        return left.binaryOp(right, op);
    }

    @Override
    public int getType(Scan s) {
        int ta = a.getType(s), tb = b.getType(s);
        if (ta == Types.VARCHAR || tb == Types.VARCHAR ||
                ta == Types.TIMESTAMP || tb == Types.TIMESTAMP)
            return Types.VARCHAR;
        if (ta == Types.DECIMAL || tb == Types.DECIMAL || op == Operator.DIVIDE)
            return Types.DECIMAL;
        if (ta == Types.DOUBLE || tb == Types.DOUBLE)
            return Types.DOUBLE;
        if (ta == Types.FLOAT || tb == Types.FLOAT)
            return Types.FLOAT;

        return Types.INTEGER;
    }

    public String toString() {
        return "Calc ( " + "op = " + op + ", " + "a = ( " + a.toString() + " ), "
                + "b = ( " + b.toString() + " ) )";
    }

    @Override
    public void renameTable(String from, String to) {
        a.renameTable(from, to);
        b.renameTable(from, to);
    }

    @Override
    public String toHashString() {
        return null;
    }

}
