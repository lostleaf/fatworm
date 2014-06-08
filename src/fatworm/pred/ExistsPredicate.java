package fatworm.pred;

import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

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
        StringBuffer s = new StringBuffer("Predicate exists: ( ");
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
//		return p.getAllUsedTblNames();
    }

    @Override
    public HashSet<String> getAllUsedTblNames(Plan p) {
        return p.getAllUsedTblNames();
    }

}
