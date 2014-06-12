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
    private Plan parentPlan;

    public OrderPlan(Plan p, ColNameExpr colName, boolean asc, Plan parentPlan) {
        this.p = p;
        this.colName = colName;
        this.asc = asc;
        this.parentPlan = parentPlan;
    }

    @Override
    public Scan open(Scan parentScan) {
        Scan s = p.open(parentScan);
        s = new OrderScan(s, colName, asc, parentScan);
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
    public Plan getParentPlan() {
        return parentPlan;
    }
}
