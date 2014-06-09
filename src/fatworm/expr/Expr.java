package fatworm.expr;

import fatworm.constant.Const;
import fatworm.scan.Scan;

/**
 * Created by lostleaf on 14-6-5.
 */

public interface Expr {

    public Const getResult(Scan s);

    public int getType(Scan s);

    public void renameTable(String from, String to);

    public String toHashString();

}

