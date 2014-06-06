package fatworm.constant;

import fatworm.expr.Function;
import fatworm.expr.Operator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalConst implements Const {
	
	private BigDecimal obj;
	
	public DecimalConst(String val) {
		obj = new BigDecimal(val);
	}
	
	public DecimalConst(double val) {
		obj = new BigDecimal(val);
	}
	
	public DecimalConst(BigDecimal val) {
		obj = val;
	}
	
	public void setScale(int scale) {
		obj = obj.setScale(scale, RoundingMode.HALF_UP);
	}

	@Override
	public Object getObj() {
		return obj;
	}

	@Override
	public boolean equals(Const c) {
		if (!(c instanceof DecimalConst)) return false;
		return obj.equals(c.getObj());
	}

	@Override
	public int compareTo(Const c) {
		return obj.compareTo((BigDecimal)c.getObj());
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
		return new DoubleConst(obj.doubleValue());
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
		return java.sql.Types.DECIMAL;
	}

	@Override
	public Const operate(Const c, int func) {
		if (c instanceof NullConst) return this;
		switch (func) {
		case Function.AVG:
		case Function.SUM:
			return binaryOp(c.toDecimalConst(), Operator.PLUS);
		case Function.MAX:
			if (this.compareTo(c) < 0) return c;
			return this;
		case Function.MIN:
			if (this.compareTo(c) > 0) return c;
			return this;
		case Function.CALAVG:
			return binaryOp(c.toDecimalConst(), Operator.DIVIDE);
//			return new DecimalConst(obj.divide(new BigDecimal((Double)c.getObj())));
		case Function.COUNT:
			
		}
		return this;
	}

	@Override
	public DecimalConst toDecimalConst() {
		return this;
	}

	@Override
	public Const binaryOp(Const c, int op) {
		BigDecimal l = obj;
		BigDecimal r = (BigDecimal)c.getObj();
		switch (op) {
		case Operator.PLUS:
			return new DecimalConst(l.add(r));
		case Operator.MINUS:
			return new DecimalConst(l.subtract(r));
		case Operator.DIVIDE:
			return new DecimalConst(l.divide(r, remainDigit, RoundingMode.HALF_UP));
		case Operator.MOD:
			return new DecimalConst(l.subtract(l.divideToIntegralValue(r).multiply(r)));
		case Operator.MULTIPLY:
			return new DecimalConst(l.multiply(r));
		}
		return null;
	}

	@Override
	public Const copy() {
		return new DecimalConst(obj);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer("decimal(");
		s.append(obj.toString());
		s.append(")");
		return s.toString();
	}
	
	private static int remainDigit = 4;

	@Override
	public FloatConst toFloatConst() {
		return new FloatConst(obj.floatValue());
	}

}