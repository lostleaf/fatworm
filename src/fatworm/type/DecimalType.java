package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.DecimalConst;
import fatworm.util.Lib;

import java.sql.Types;

public class DecimalType implements Type {
	
	private int m, n;
	
	public DecimalType(int m, int n) {
		this.m = m;
		this.n = n;
	}
	
	public int getScale() {
		return n;
	}

	@Override
	public int getLength() {
		return Lib.getDecimalSize(m);
	}

	@Override
	public int getType() {
		return Types.DECIMAL;
	}

	@Override
	public Const toConst(String s) {
		DecimalConst c = new DecimalConst(s);
		c.setScale(n);
		return c;
	}

}
