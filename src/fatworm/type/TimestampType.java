package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.TimestampConst;
import fatworm.util.Lib;

import java.sql.Types;

public class TimestampType implements Type {

	@Override
	public int getLength() {
		return Lib.TIMESTAMP_SIZE + Lib.TIMESTAMP_SIZE;
	}

	@Override
	public int getType() {
		return Types.TIMESTAMP;
	}

	@Override
	public Const toConst(String s) {
		return new TimestampConst(s);
	}

}
