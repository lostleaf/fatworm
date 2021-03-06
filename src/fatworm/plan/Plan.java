package fatworm.plan;

import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public interface Plan {

    public Scan open(Scan parentScan);

    public Plan getPlan();

    public void setPlan(Plan p);

    public void renameTable(String from, String to);

    public HashSet<String> getAllTblNames();

    public Plan getParentPlan();

}
