package fatworm.type;

import fatworm.constant.BooleanConst;
import fatworm.constant.Const;
import fatworm.util.TypeUtil;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class BooleanType implements Type {

    @Override
    public int getLength() {
        return TypeUtil.BOOLEAN_SIZE + TypeUtil.INT_SIZE;
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
