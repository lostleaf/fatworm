package fatworm.pred;

import fatworm.scan.Scan;

/**
 * Created by lostleaf on 14-6-5.
 */
public interface Predicate {

    public boolean isSatisfied(Scan s);

    public void renameTable(String from, String to);
}
