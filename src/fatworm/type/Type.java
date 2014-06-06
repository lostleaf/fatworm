package fatworm.type;

import fatworm.constant.Const;

import java.io.Serializable;


public interface Type extends Serializable {
	
	public int getLength();
	public int getType();
	public Const toConst(String s);

}
