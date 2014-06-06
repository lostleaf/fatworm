package fatworm.pred;

import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public interface Predicate {

    public boolean isSatisfied(Scan s);

    public void renameTable(String from, String to);

    public HashSet<String> getTblNames(Plan p);
    public HashSet<String> getAllUsedTblNames(Plan p);
}
