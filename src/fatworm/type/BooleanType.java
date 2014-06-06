package fatworm.type;

import fatworm.constant.BooleanConst;
import fatworm.constant.Const;
import fatworm.util.Lib;

import java.sql.Types;

public class BooleanType implements Type {

	@Override
	public int getLength() {
		return Lib.BOOLEAN_SIZE + Lib.INT_SIZE;
	}

	@Override
	public int getType() {
		return Types.BOOLEAN;
	}

	@Override
	public Const toConst(String s) {
		return new BooleanConst(s);
	}

}
