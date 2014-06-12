package fatworm.plan;

import fatworm.scan.RenameScan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-7.
 */
public class RenamePlan implements Plan {
    private String newName;
    private Plan p;
    private Plan parentPlan;

    public RenamePlan(Plan p, String name, Plan parentPlan) {
        this.p = p;
        this.newName = name.toLowerCase();
        this.parentPlan = parentPlan;
    }

    @Override
    public Scan open(Scan parentScan) {
        Scan s = p.open(parentScan);
        s = new RenameScan(s, newName, parentScan);
        return s;
    }

    public String toString() {
        return "Rename plan ( " + "new name: ( " + newName + " ), " + "plan: ( " + p.toString() + ") )";
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
    }

    @Override
    public HashSet<String> getAllTblNames() {
        HashSet<String> s = new HashSet<String>();
        s.add(newName);
        return s;
    }

    @Override
    public Plan getParentPlan() {
        return parentPlan;
    }
}
