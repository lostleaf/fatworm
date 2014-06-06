package fatworm.constant;

import fatworm.expr.Function;
import fatworm.expr.Operator;

import java.sql.Types;

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
		if (!(c instanceof IntegerConst)) return false;
		return obj.equals(c.getObj());
	}

	@Override
	public int compareTo(Const c) {
		return obj.compareTo((Integer)c.getObj());
	}

	@Override
	public BooleanConst toBooleanConst() {
		if (obj.intValue() == 0) return new BooleanConst(false);
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
			return new IntegerConst(obj.intValue() + 1);
		}
		return this;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer("Int (");
		s.append(obj.toString());
		s.append(")");
		return s.toString();
	}

	@Override
	public DecimalConst toDecimalConst() {
		return new DecimalConst(obj.doubleValue());
	}

	@Override
	public Const binaryOp(Const c, int op) {
		int l = obj;
		int r = (Integer)c.getObj();
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
