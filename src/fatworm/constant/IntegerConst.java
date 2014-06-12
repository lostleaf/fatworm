package fatworm.constant;

import fatworm.util.Function;
import fatworm.util.Operator;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class IntegerConst implements Const {

    private Integer obj;

    public IntegerConst(int o) {
        obj = o;
    }

    public IntegerConst(String val) {
        obj = Integer.valueOf(val);
    }

    @Override
    public Object getObj() {
        return obj;
    }

    @Override
    public boolean equals(Const c) {
        return c instanceof IntegerConst && obj.equals(c.getObj());
    }

    @Override
    public int compareTo(Const c) {
        return obj.compareTo((Integer) c.getObj());
    }

    @Override
    public BooleanConst toBooleanConst() {
        if (obj == 0) return new BooleanConst(false);
        return new BooleanConst(true);
    }

    @Override
    public TimestampConst toTimestampConst() {
        return null;
    }

    @Override
    public DoubleConst toDoubleConst() {
        return new DoubleConst(obj.doubleValue());
    }

    @Override
    public IntegerConst toIntegerConst() {
        return this;
    }

    @Override
    public StringConst toStringConst() {
        return new StringConst(obj.toString());
    }

    @Override
    public int getType() {
        return Types.INTEGER;
    }

    @Override
    public Const operate(Const c, int func) {
        if (c instanceof NullConst) return this;
        switch (func) {
            case Function.AVG:
            case Function.SUM:
                return binaryOp(c, Operator.PLUS);
            case Function.MAX:
                if (this.compareTo(c) < 0) return c;
                return this;
            case Function.MIN:
                if (this.compareTo(c) > 0) return c;
                return this;
            case Function.COUNT:
                return new IntegerConst(obj + 1);
        }
        return this;
    }

    public String toString() {
        return "Int (" + obj.toString() + ")";
    }

    @Override
    public DecimalConst toDecimalConst() {
        return new DecimalConst(obj.doubleValue());
    }

    @Override
    public Const binaryOp(Const c, int op) {
        int l = obj;
        int r = (Integer) c.getObj();
        switch (op) {
            case Operator.PLUS:
                return new IntegerConst(l + r);
            case Operator.MINUS:
                return new IntegerConst(l - r);
            case Operator.DIVIDE:
                return new IntegerConst(l / r);
            case Operator.MOD:
                return new IntegerConst(l % r);
            case Operator.MULTIPLY:
                return new IntegerConst(l * r);
        }
        return null;
    }

    @Override
    public Const copy() {
        return new IntegerConst(obj);
    }

    @Override
    public FloatConst toFloatConst() {
        return new FloatConst(obj);
    }

}
