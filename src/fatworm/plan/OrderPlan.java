package fatworm.plan;

import fatworm.expr.ColNameExpr;
import fatworm.scan.OrderScan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-7.
 */
public class OrderPlan implements Plan {

    private Plan p;
    private ColNameExpr colName;
    private boolean asc;
    private Plan father;

    public OrderPlan(Plan p, ColNameExpr colName, boolean asc, Plan father) {
        this.p = p;
        this.colName = colName;
        this.asc = asc;
        this.father = father;
    }

    @Override
    public Scan open(Scan father) {
        Scan s = p.open(father);
        s = new OrderScan(s, colName, asc, father);
        return s;
    }

    public String toString() {
        return "Order plan ( " + "col name: ( " + colName.toString() + " ), "
                + "asc: " + asc + ", " + "plan: ( " + p.toString() + ") )";
    }

    @Override
    public Plan getPlan() {
        return p;
    }

    @Override
    public void setPlan(Plan p) {
        this.p = p;
    }

    @Override
    public void renameTable(String from, String to) {
        p.renameTable(from, to);
        colName.renameTable(from, to);
    }

    @Override
    public HashSet<String> getAllTblNames() {
        return p.getAllTblNames();
    }

    @Override
    public String getTblName(String fldName, boolean findFather) {
        return p.getTblName(fldName, findFather);
//		if (tblName != null) return tblName;
//		if (father == null || (!(p instanceof TablePlan))) return null;
//		return father.getTblName(fldName);
    }

    @Override
    public HashSet<String> getAllUsedTblNames() {
        HashSet<String> s = p.getAllUsedTblNames();
        s.addAll(colName.getTblNames(this));
        return s;
    }

    @Override
    public Plan getParentPlan() {
        return father;
    }
}
