package fatworm.expr;

import fatworm.constant.Const;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public class ColNameExpr implements Expression {

    private String tblName, fldName;

    public ColNameExpr(String tblName, String fldName) {
        this.tblName = tblName == null ? null : tblName.toLowerCase();
        this.fldName = fldName.toLowerCase();
    }

    public String getTblName() {
        return tblName;
    }

    public String getFldName() {
        return fldName;
    }

    @Override
    public Const getResult(Scan s) {
        return s.get(this, true);
    }

    @Override
    public int getType(Scan s) {
        return s.getColumnType(this, true);
    }

    public String toString() {
        return "ColName ( " + "tblName: " + tblName + ", " + "fldName: " + fldName + " )";
    }

    @Override
    public void renameTable(String from, String to) {
        if (from.equals(tblName)) tblName = to;
    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        HashSet<String> s = new HashSet<String>();
        if (tblName != null) s.add(tblName);
        else s.add(p.getTblName(fldName, true));
        return s;
    }

    @Override
    public String toHashString() {
        return (tblName == null ? "null" : tblName) + fldName;
    }

}
