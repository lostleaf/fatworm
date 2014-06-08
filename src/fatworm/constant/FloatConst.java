package fatworm.constant;

import fatworm.expr.Function;
import fatworm.expr.Operator;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class FloatConst implements Const {

    private Float obj;

    public FloatConst(float o) {
        obj = o;
    }

    public FloatConst(String val) {
        obj = Float.valueOf(val);
    }

    @Override
    public Object getObj() {
        return obj;
    }

    @Override
    public boolean equals(Const c) {
        return c instanceof FloatConst && obj.equals(c.getObj());
    }

    @Override
    public int compareTo(Const c) {
        return obj.compareTo((Float) c.getObj());
    }

    @Override
    public BooleanConst toBooleanConst() {
        return null;
    }

    @Override
    public TimestampConst toTimestampConst() {
        return null;
    }

    @Override
    public DoubleConst toDoubleConst() {
        return new DoubleConst(obj);
    }

    @Override
    public IntegerConst toIntegerConst() {
        return new IntegerConst(obj.intValue());
    }

    @Override
    public StringConst toStringConst() {
        return new StringConst(this.toString());
    }

    @Override
    public DecimalConst toDecimalConst() {
        return new DecimalConst(obj);
    }

    @Override
    public FloatConst toFloatConst() {
        return this;
    }

    @Override
    public int getType() {
        return Types.FLOAT;
    }

    @Override
    public Const operate(Const c, int func) {
        if (c instanceof NullConst) return this;
        switch (func) {
            case Function.AVG:
            case Function.SUM:
                return binaryOp(c.toFloatConst(), Operator.PLUS);
            case Function.MAX:
                if (this.compareTo(c) < 0) return c;
                return this;
            case Function.MIN:
                if (this.compareTo(c) > 0) return c;
                return this;
            case Function.CALAVG:
                return binaryOp(c, Operator.DIVIDE);
            case Function.COUNT:

        }
        return this;
    }

    @Override
    public Const binaryOp(Const c, int op) {
        float l = obj;
        float r = (Float) c.getObj();
        switch (op) {
            case Operator.PLUS:
                return new FloatConst(l + r);
            case Operator.MINUS:
                return new FloatConst(l - r);
            case Operator.DIVIDE:
                return new FloatConst(l / r);
            case Operator.MOD:
                return new FloatConst(l - ((int) (l / r)) * r);
            case Operator.MULTIPLY:
                return new FloatConst(l * r);
        }
        return null;
    }

    @Override
    public Const copy() {
        return new FloatConst(obj);
    }

    public String toString() {
        return "Float (" + obj.toString() + ")";
    }

}
