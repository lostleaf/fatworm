package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.DoubleConst;
import fatworm.util.TypeUtil;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class DoubleType implements Type {

    @Override
    public int getLength() {
        return TypeUtil.DOUBLE_SIZE + TypeUtil.INT_SIZE;
    }

    @Override
    public int getType() {
        return Types.DOUBLE;
    }

    @Override
    public Const toConst(String s) {
        return new DoubleConst(s);
    }

}
