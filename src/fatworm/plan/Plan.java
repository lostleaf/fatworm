package fatworm.plan;

import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public interface Plan {

    public Scan open(Scan father);

    public Plan getPlan();

    public void setPlan(Plan p);

    public Plan down();

    public void renameTable(String from, String to);

    public HashSet<String> getAllTblNames();

    public HashSet<String> getAllUsedTblNames();

    public String getTblName(String fldName, boolean findFather);

    public Plan getParentPlan();

}
