package fatworm.constant;

import fatworm.expr.Function;
import fatworm.expr.Operator;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class StringConst implements Const {

    private String obj;

    public StringConst(String o) {
        if (o.startsWith("'")) o = o.substring(1, o.length() - 1);
        obj = o;
    }

    @Override
    public Object getObj() {
        return obj;
    }

    @Override
    public boolean equals(Const c) {
        return c instanceof StringConst && obj.toLowerCase().equals(((String) c.getObj()).toLowerCase());
    }

    @Override
    public int compareTo(Const c) {
        return obj.toLowerCase().compareTo(((String) c.getObj()).toLowerCase());
    }

    @Override
    public BooleanConst toBooleanConst() {
        return new BooleanConst(obj);
    }

    @Override
    public TimestampConst toTimestampConst() {
        return new TimestampConst(obj);
    }

    @Override
    public DoubleConst toDoubleConst() {
        return new DoubleConst(obj);
    }

    @Override
    public IntegerConst toIntegerConst() {
        return new IntegerConst(obj);
    }

    @Override
    public StringConst toStringConst() {
        return this;
    }

    @Override
    public int getType() {
        return Types.VARCHAR;
    }

    @Override
    public Const operate(Const c, int func) {
        if (c instanceof NullConst) return this;
        switch (func) {
            case Function.MAX:
                if (this.compareTo(c) < 0) return c;
                return this;
            case Function.MIN:
                if (this.compareTo(c) > 0) return c;
                return this;
            case Function.AVG:
            case Function.SUM:
            case Function.COUNT:
        }
        return this;
    }

    public String toString() {
        return "String (" + obj + ")";
    }

    @Override
    public DecimalConst toDecimalConst() {
        return new DecimalConst(obj);
    }

    @Override
    public Const binaryOp(Const c, int op) {
        String l = obj;
        String r = (String) c.getObj();
        switch (op) {
            case Operator.PLUS:
                return new StringConst(l + r);
            case Operator.MINUS:
            case Operator.DIVIDE:
            case Operator.MOD:
            case Operator.MULTIPLY:
        }
        return null;
    }

    @Override
    public Const copy() {
        return new StringConst(obj);
    }

    @Override
    public FloatConst toFloatConst() {
        return new FloatConst(obj);
    }

}
