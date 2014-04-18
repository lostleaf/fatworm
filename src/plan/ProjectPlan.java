package plan;

import java.util.LinkedList;

/**
 * Created by lostleaf on 14-4-17.
 */
public class ProjectPlan extends Plan {
    Plan from;
    LinkedList<ExprPlan> to;
    boolean distinct;

    public ProjectPlan(Plan from, LinkedList<ExprPlan> to, boolean distinct) {
        this.from = from;
        this.to = to;
        this.distinct = distinct;
    }

    @Override
    public String toString() {
        String ret = "Project " + (distinct ? "distinct " : "") + "from{" + from.toString() + "} to{";
        for (ExprPlan exprPlan : to)
            ret += exprPlan.toString() + " ";
        return ret + "}";
    }
}
