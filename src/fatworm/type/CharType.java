package fatworm.type;

import fatworm.constant.Const;
import fatworm.constant.StringConst;
import fatworm.util.Lib;

import java.sql.Types;

/**
 * Created by lostleaf on 14-6-5.
 */
public class CharType implements Type {

    private int length;

    public CharType(int len) {
        this.length = len;
    }

    @Override
    public int getLength() {
        return Lib.getStrSize(length);
    }

    @Override
    public int getType() {
        return Types.CHAR;
    }

    @Override
    public Const toConst(String s) {
        return new StringConst(s);
    }

}
