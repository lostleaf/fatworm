package fatworm.expr;

import fatworm.constant.Const;
import fatworm.scan.Scan;

/**
 * Created by lostleaf on 14-6-5.
 */
public class ConstExpr implements Expr {

    private Const obj;

    public ConstExpr(Const obj) {
        this.obj = obj;
    }

    @Override
    public Const getResult(Scan s) {
        return obj;
    }

    @Override
    public int getType(Scan s) {
        return obj.getType();
    }

    public String toString() {
        return "Const (" + obj.toString() + ")";
    }

    @Override
    public void renameTable(String from, String to) {
    }

    @Override
    public String toHashString() {
        return null;
    }
}
