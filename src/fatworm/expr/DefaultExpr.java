package fatworm.expr;

import fatworm.constant.Const;
import fatworm.constant.DefaultConst;
import fatworm.scan.Scan;

/**
 * Created by lostleaf on 14-6-5.
 */
public class DefaultExpr implements Expr {

    public DefaultExpr() {

    }

    @Override
    public Const getResult(Scan s) {
        return new DefaultConst();
    }

    @Override
    public int getType(Scan s) {
        return 0;
    }

    public String toString() {
        return "default";
    }

    @Override
    public void renameTable(String from, String to) {
    }

    @Override
    public String toHashString() {
        return null;
    }

}
