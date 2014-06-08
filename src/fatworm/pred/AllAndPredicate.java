package fatworm.pred;

import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;
import java.util.List;

/**
 * Created by lostleaf on 14-6-7.
 */
public class AllAndPredicate implements Predicate {

    private List<Predicate> preds;

    public AllAndPredicate(List<Predicate> preds) {
        this.preds = preds;
    }

    public List<Predicate> getPreds() {
        return preds;
    }

    public void setPreds(List<Predicate> preds) {
        this.preds = preds;
    }

    public boolean isSatisfied(Scan s) {
        for (Predicate pred : preds)
            if (!pred.isSatisfied(s))
                return false;
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("All And Predicate: ( ");
        for (Predicate pred : preds)
            s.append(pred.toString()).append(", ");
        s.append(" ) ");
        return s.toString();
    }

    public void renameTable(String from, String to) {
        for (Predicate pred : preds)
            pred.renameTable(from, to);
    }

    public HashSet<String> getTblNames(Plan p) {
        HashSet<String> s = new HashSet<String>();
        for (Predicate pred : preds)
            s.addAll(pred.getTblNames(p));
        return s;
    }

    @Override
    public HashSet<String> getAllUsedTblNames(Plan p) {
        HashSet<String> s = new HashSet<String>();
        for (Predicate pred : preds)
            s.addAll(pred.getAllUsedTblNames(p));
        return s;
    }

}
