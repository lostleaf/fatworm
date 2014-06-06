package fatworm.expr;

import fatworm.constant.Const;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

public class ColNameExpr implements Expression {

    private String tblName, fldName;

    public ColNameExpr(String tblName, String fldName) {
        if (tblName == null)
            this.tblName = null;
        else
            this.tblName = tblName.toLowerCase();
        
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
        StringBuffer s = new StringBuffer("ColName ( ");
        s.append("tblName: ");
        s.append(tblName);
        s.append(", ");
        s.append("fldName: ");
        s.append(fldName);
        s.append(" )");
        return s.toString();
    }

    @Override
    public void renameTable(String from, String to) {
        if (from.equals(tblName)) {
            tblName = to;
        }
    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        HashSet<String> s = new HashSet<String>();
        if (tblName != null) {
            s.add(tblName);
        } else {
            s.add(p.getTblName(fldName, true));
        }
        return s;
    }

    @Override
    public String toHashString() {
        StringBuffer s;
        if (tblName == null)
            s = new StringBuffer("null");
        else
            s = new StringBuffer(tblName);
        s.append(fldName);
        return s.toString();
    }

}
