package fatworm.plan;

import fatworm.scan.Scan;
import fatworm.scan.TableScan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public class TablePlan implements Plan {

    protected String tblName;
    protected Plan parentPlan;

    public TablePlan(String tblName, Plan parentPlan) {
        this.tblName = tblName.toLowerCase();
        this.parentPlan = parentPlan;
    }

    @Override
    public Scan open(Scan parentScan) {
        return new TableScan(tblName, parentScan);
    }

    public String toString() {
        return "Table plan ( " + "table name: ( " + tblName + " ) )";
    }

    @Override
    public Plan getPlan() {
        return null;
    }

    @Override
    public void setPlan(Plan p) {}

    @Override
    public void renameTable(String from, String to) {}

    @Override
    public HashSet<String> getAllTblNames() {
        HashSet<String> s = new HashSet<String>();
        s.add(tblName);
        return s;
    }

    @Override
    public Plan getParentPlan() {
        return parentPlan;
    }
}
