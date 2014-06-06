package fatworm.constant;

import fatworm.expr.Function;

import java.sql.Timestamp;
import java.sql.Types;

public class TimestampConst implements Const {
	
	private Timestamp obj;
	
	public TimestampConst(String s) {
		if (s.startsWith("'")) s = s.substring(1, s.length() - 1);
		obj = Timestamp.valueOf(s);
	}
	
	public TimestampConst(Timestamp t) {
		this.obj = t;
	}
	
	@Override
	public Object getObj() {
		return obj;
	}

	@Override
	public boolean equals(Const c) {
		if (!(c instanceof TimestampConst)) return false;
		return obj.equals(c.getObj());
	}

	@Override
	public int compareTo(Const c) {
		return obj.compareTo((Timestamp)c.getObj());
	}

	@Override
	public BooleanConst toBooleanConst() {
		return null;
	}

	@Override
	public TimestampConst toTimestampConst() {
		return this;
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
		return new StringConst(obj.toString());
	}

	@Override
	public int getType() {
		return Types.TIMESTAMP;
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
		StringBuffer s = new StringBuffer("Datetime (");
		s.append(obj.toString());
		s.append(")");
		return s.toString();
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
		return new TimestampConst(obj.toString());
	}

	@Override
	public FloatConst toFloatConst() {
		return null;
	}

}
