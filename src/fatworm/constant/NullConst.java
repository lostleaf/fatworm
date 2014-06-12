package fatworm.constant;

import fatworm.util.Function;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class NullConst implements Const {

    public NullConst() {

    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public boolean equals(Const c) {
        return false;
    }

    @Override
    public int compareTo(Const c) {
        return 0;
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
        return null;
    }

    @Override
    public IntegerConst toIntegerConst() {
        return null;
    }

    @Override
    public StringConst toStringConst() {
        return null;
    }

    @Override
    public int getType() {
        return Types.NULL;
    }

    @Override
    public Const operate(Const c, int func) {
        if (c instanceof NullConst) return this;
        switch (func) {
            case Function.AVG:
            case Function.SUM:
            case Function.MAX:
            case Function.MIN:
                return c;
            case Function.CALAVG:
                return this;
            case Function.COUNT:
                return new IntegerConst(1);
        }
        return this;
    }

    public String toString() {
        return "null";
    }

    @Override
    public DecimalConst toDecimalConst() {
        return null;
    }

    @Override
    public Const binaryOp(Const c, int op) {
        return null;
    }

    @Override
    public Const copy() {
        return new NullConst();
    }

    @Override
    public FloatConst toFloatConst() {
        return null;
    }

}
