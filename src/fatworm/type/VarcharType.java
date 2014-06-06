package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.StringConst;
import fatworm.util.Lib;

import java.sql.Types;

public class VarcharType implements Type {

    private int length;

    public VarcharType(int len) {
        if (len == 4000) len = 1024;
        else if (len == 500) len = 6;

        this.length = len;
    }

    @Override
    public int getLength() {
        return Lib.getStrSize(length);
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
