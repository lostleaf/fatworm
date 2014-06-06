package fatworm.constant;

import java.io.Serializable;

public interface Const extends Serializable {
	
	public Object getObj();
	
	public boolean equals(Const c);
	
	public int compareTo(Const c);
	
	public BooleanConst toBooleanConst();
	public TimestampConst toTimestampConst();
	public DoubleConst toDoubleConst();
	public IntegerConst toIntegerConst();
	public StringConst toStringConst();
	public DecimalConst toDecimalConst();
	public FloatConst toFloatConst();
	
	public int getType();
	
	public Const operate(Const c, int func);
	
	public Const binaryOp(Const c, int op);
	
	public Const copy();

}
