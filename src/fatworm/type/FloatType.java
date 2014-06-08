package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.FloatConst;
import fatworm.util.Lib;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class FloatType implements Type {

    @Override
    public int getLength() {
        return Lib.FLOAT_SIZE + Lib.INT_SIZE;
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
