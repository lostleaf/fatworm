package fatworm.expr;

import fatworm.constant.Const;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public class ConstExpr implements Expression {

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
        StringBuffer s = new StringBuffer("Const (");
        s.append(obj.toString());
        s.append(")");
        return s.toString();
    }

    @Override
    public void renameTable(String from, String to) {

    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        return new HashSet<String>();
    }

    @Override
    public String toHashString() {
        return null;
    }
}
