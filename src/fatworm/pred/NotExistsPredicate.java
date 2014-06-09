package fatworm.pred;

import fatworm.plan.Plan;
import fatworm.scan.Scan;

/**
 * Created by lostleaf on 14-6-7.
 */
public class NotExistsPredicate implements Predicate {

    private Plan p;

    public NotExistsPredicate(Plan p) {
        this.p = p;
    }

    @Override
    public boolean isSatisfied(Scan s) {
        Scan s1 = p.open(s);
        if (s1.next()) {
            s1.close();
            return false;
        }
        s1.close();
        return true;
    }

    public String toString() {
        return "Predicate not exists: ( " + "plan: ( " + p.toString() + " ) )";
    }

    @Override
    public void renameTable(String from, String to) {
        p.renameTable(from, to);
    }

}
