package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.IntegerConst;
import fatworm.util.Lib;

import java.sql.Types;

public class IntegerType implements Type {

	@Override
	public int getLength() {
//		return Lib.INT_SIZE + Lib.INT_SIZE;
		return Lib.INT_SIZE;
	}

	@Override
	public int getType() {
		return Types.INTEGER;
	}

	@Override
	public Const toConst(String s) {
		return new IntegerConst(s);
	}
	

}
