package fatworm.pred;

import fatworm.scan.Scan;

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
        return "Predicate and: ( " +
                left.toString() + ", " + right.toString() + " ) ";
    }

    @Override
    public void renameTable(String from, String to) {
        left.renameTable(from, to);
        right.renameTable(from, to);
    }

}
