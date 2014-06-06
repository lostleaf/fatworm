package fatworm.util;

import fatworm.constant.Const;
import fatworm.constant.NullConst;
import fatworm.type.Type;

import java.sql.Types;

public class ConstUtil {
	
	public static Const changeToType(Const c, Type p) {
		switch (p.getType()) {
		case Types.BOOLEAN:
			return c.toBooleanConst();
		case Types.INTEGER:
			return c.toIntegerConst();
		case Types.DOUBLE:
			return c.toDoubleConst();
		case Types.FLOAT:
			return c.toFloatConst();
		case Types.DECIMAL:
			return c.toDecimalConst();
		case Types.CHAR:
		case Types.VARCHAR:
			return c.toStringConst();
		case Types.TIMESTAMP:
			return c.toTimestampConst();
		default:
			return new NullConst();
		}
	}
	
	public static Const changeToType(Const c, int p) {
		switch (p) {
		case Types.BOOLEAN:
			return c.toBooleanConst();
		case Types.INTEGER:
			return c.toIntegerConst();
		case Types.DOUBLE:
			return c.toDoubleConst();
		case Types.FLOAT:
			return c.toFloatConst();
		case Types.DECIMAL:
			return c.toDecimalConst();
		case Types.CHAR:
		case Types.VARCHAR:
			return c.toStringConst();
		case Types.TIMESTAMP:
			return c.toTimestampConst();
		default:
			return new NullConst();
		}
	}

}
