package fatworm.type;

import fatworm.constant.Const;

import java.io.Serializable;

/**
 * Created by lostleaf on 14-6-5.
 */
public interface Type extends Serializable {

    public int getLength();

    public int getType();

    public Const toConst(String s);

}
