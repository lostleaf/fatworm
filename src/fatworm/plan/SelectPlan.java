package fatworm.plan;

import fatworm.pred.Predicate;
import fatworm.scan.Scan;
import fatworm.scan.SelectScan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public class SelectPlan implements Plan {
    protected Predicate pred;
    protected Plan plan, parentPlan;

    public SelectPlan(Plan plan, Predicate pred, Plan parentPlan) {
        this.pred = pred;
        this.plan = plan;
        this.parentPlan = parentPlan;
    }

    public String toString() {
        return "Select plan ( " + "pred: ( " + pred.toString()
                + " ), " + "plan: ( " + plan.toString() + ") )";
    }

    @Override
    public Scan open(Scan father) {
        Scan s = plan.open(father);
        s = new SelectScan(s, pred, father);
        return s;
    }
    @Override
    public Plan getPlan() {
        return plan;
    }

    @Override
    public void setPlan(Plan p) {
        plan = p;
    }

    @Override
    public void renameTable(String from, String to) {
        plan.renameTable(from, to);
        pred.renameTable(from, to);
    }

    @Override
    public HashSet<String> getAllTblNames() {
        return plan.getAllTblNames();
    }

    @Override
    public Plan getParentPlan() {
        return parentPlan;
    }
}
