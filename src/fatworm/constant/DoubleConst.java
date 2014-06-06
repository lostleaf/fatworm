package fatworm.constant;

import fatworm.expr.Function;
import fatworm.expr.Operator;

import java.sql.Types;

public class DoubleConst implements Const {
	
	private Double obj;
	
	public DoubleConst(double o) {
		obj = new Double(o);
	}
	
	public DoubleConst(String val) {
		obj = Double.valueOf(val);
	}

	@Override
	public Object getObj() {
		return obj;
	}

	@Override
	public boolean equals(Const c) {
		if (!(c instanceof DoubleConst)) return false;
		return obj.equals(c.getObj());
	}

	@Override
	public int compareTo(Const c) {
		return obj.compareTo((Double)c.getObj());
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
		return this;
	}

	@Override
	public IntegerConst toIntegerConst() {
		return new IntegerConst(obj.intValue());
	}

	@Override
	public StringConst toStringConst() {
		return new StringConst(obj.toString());
	}

	@Override
	public int getType() {
		return Types.DOUBLE;
	}

	@Override
	public Const operate(Const c, int func) {
		if (c instanceof NullConst) return this;
		switch (func) {
		case Function.AVG:
		case Function.SUM:
			return binaryOp(c.toDoubleConst(), Operator.PLUS);
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
	
	public String toString() {
		StringBuffer s = new StringBuffer("Double (");
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
		double l = obj;
		double r = (Double)c.getObj();
		switch (op) {
		case Operator.PLUS:
			return new DoubleConst(l + r);
		case Operator.MINUS:
			return new DoubleConst(l - r);
		case Operator.DIVIDE:
			return new DoubleConst(l / r);
		case Operator.MOD:
			return new DoubleConst(l - ((int)(l / r)) * r);
		case Operator.MULTIPLY:
			return new DoubleConst(l * r);
		}
		return null;
	}

	@Override
	public Const copy() {
		return new DoubleConst(obj);
	}

	@Override
	public FloatConst toFloatConst() {
		return new FloatConst(obj.floatValue());
	}

}
