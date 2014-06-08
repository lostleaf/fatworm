package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.IntegerConst;
import fatworm.util.TypeUtil;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class IntegerType implements Type {

    @Override
    public int getLength() {
//		return TypeUtil.INT_SIZE + TypeUtil.INT_SIZE;
        return TypeUtil.INT_SIZE;
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
