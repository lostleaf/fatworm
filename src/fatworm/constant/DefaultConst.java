package fatworm.constant;

/**
 * Created by lostleaf on 14-6-5.
 */
public class DefaultConst implements Const {

    public DefaultConst() {

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
        return 0;
    }

    @Override
    public Const operate(Const c, int func) {
        return this;
    }

    public String toString() {
        return "default";
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
        return null;
    }

    @Override
    public FloatConst toFloatConst() {
        return null;
    }

}
