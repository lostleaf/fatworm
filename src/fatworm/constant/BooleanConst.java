package fatworm.constant;


import fatworm.expr.Function;

import java.sql.Types;

public class BooleanConst implements Const {
	
	private Boolean obj;
	
	public BooleanConst(boolean o) {
		obj = o;
	}
	
	public BooleanConst(String val) {
		obj = Boolean.valueOf(val);
	}

	@Override
	public Object getObj() {
		return obj;
	}

	@Override
	public boolean equals(Const c) {
		if (!(c instanceof BooleanConst)) return false;
		return obj.equals(c.getObj());
	}

	@Override
	public int compareTo(Const c) {
		return obj.compareTo((Boolean)c.getObj());
	}

	@Override
	public BooleanConst toBooleanConst() {
		return this;
	}

	@Override
	public TimestampConst toTimestampConst() {
		return null;
	}

	@Override
	public DoubleConst toDoubleConst() {
		if (obj.booleanValue()) {
			return new DoubleConst(1);
		} else {
			return new DoubleConst(0);
		}
	}

	@Override
	public IntegerConst toIntegerConst() {
		if (obj.booleanValue()) {
			return new IntegerConst(1);
		} else {
			return new IntegerConst(0);
		}
	}

	@Override
	public StringConst toStringConst() {
		return new StringConst(obj.toString());
	}

	@Override
	public int getType() {
		return Types.BOOLEAN;
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
		StringBuffer s = new StringBuffer("boolean(");
		s.append(obj.toString());
		s.append(")");
		return s.toString();
	}

	@Override
	public DecimalConst toDecimalConst() {
		if (obj.booleanValue()) {
			return new DecimalConst("1");
		} else {
			return new DecimalConst("0");
		}
	}

	@Override
	public Const binaryOp(Const c, int op) {
		return null;
	}

	@Override
	public Const copy() {
		return new BooleanConst(obj);
	}

	@Override
	public FloatConst toFloatConst() {
		if (obj.booleanValue()) {
			return new FloatConst(1);
		} else {
			return new FloatConst(0);
		}
	}

}
