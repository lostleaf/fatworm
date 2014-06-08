package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.StringConst;
import fatworm.util.TypeUtil;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class VarcharType implements Type {

    private int length;

    public VarcharType(int len) {
        if (len == 4000) len = 1024;
        else if (len == 500) len = 6;

        this.length = len;
    }

    @Override
    public int getLength() {
        return TypeUtil.getStrSize(length);
    }

    @Override
    public int getType() {
        return Types.VARCHAR;
    }

    @Override
    public Const toConst(String s) {
        return new StringConst(s);
    }

}
