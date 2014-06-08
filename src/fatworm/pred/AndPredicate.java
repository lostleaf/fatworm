package fatworm.pred;

import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-7.
 */
public class AndPredicate implements Predicate {

    private Predicate left, right;

    public AndPredicate(Predicate left, Predicate right) {
        this.left = left;
        this.right = right;
    }

    public Predicate getLeft() {
        return left;
    }

    public Predicate getRight() {
        return right;
    }

    @Override
    public boolean isSatisfied(Scan s) {
        return left.isSatisfied(s) && right.isSatisfied(s);
    }

    public String toString() {
        return "Predicate and: ( " + left.toString() + ", " + right.toString() + " ) ";
    }

    @Override
    public void renameTable(String from, String to) {
        left.renameTable(from, to);
        right.renameTable(from, to);
    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        HashSet<String> sleft = left.getTblNames(p);
        HashSet<String> sright = right.getTblNames(p);
        sleft.addAll(sright);
        return sleft;
    }

    @Override
    public HashSet<String> getAllUsedTblNames(Plan p) {
        HashSet<String> sleft = left.getAllUsedTblNames(p);
        HashSet<String> sright = right.getAllUsedTblNames(p);
        sleft.addAll(sright);
        return sleft;
    }

}
