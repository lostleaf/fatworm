package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.TimestampConst;
import fatworm.util.TypeUtil;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class TimestampType implements Type {

    @Override
    public int getLength() {
        return TypeUtil.TIMESTAMP_SIZE + TypeUtil.TIMESTAMP_SIZE;
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
