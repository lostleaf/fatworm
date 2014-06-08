package fatworm.pred;

import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

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
        StringBuffer s = new StringBuffer("Predicate not exists: ( ");
        s.append("plan: ( ");
        s.append(p.toString());
        s.append(" ) )");
        return s.toString();
    }

    @Override
    public void renameTable(String from, String to) {
        p.renameTable(from, to);
    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        return new HashSet<String>();
    }

    @Override
    public HashSet<String> getAllUsedTblNames(Plan p) {
        return p.getAllUsedTblNames();
    }

}
