package fatworm.pred;

import fatworm.plan.Plan;
import fatworm.scan.Scan;

/**
 * Created by lostleaf on 14-6-7.
 */
public class ExistsPredicate implements Predicate {

    private Plan p;

    public ExistsPredicate(Plan p) {
        this.p = p;
    }

    @Override
    public boolean isSatisfied(Scan s) {
        Scan s1 = p.open(s);
        if (s1.next()) {
            s1.close();
            return true;
        }
        s1.close();
        return false;
    }

    public String toString() {
        return "Predicate exists: ( " + "plan: ( " + p.toString() + " ) )";
    }

    @Override
    public void renameTable(String from, String to) {
        p.renameTable(from, to);
    }

}
