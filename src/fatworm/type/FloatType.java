package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.FloatConst;
import fatworm.util.TypeUtil;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class FloatType implements Type {

    @Override
    public int getLength() {
        return TypeUtil.FLOAT_SIZE + TypeUtil.INT_SIZE;
    }

    @Override
    public int getType() {
        return Types.FLOAT;
    }

    @Override
    public Const toConst(String s) {
        return new FloatConst(s);
    }

}
