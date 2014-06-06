package fatworm.plan;

import org.antlr.runtime.tree.CommonTree;

/**
 * Created by lostleaf on 14-4-18.
 */
public class SelectPlan extends Plan{
    Plan from;
    Plan cond;

    public SelectPlan(Plan from, CommonTree cond) {
        this.from = from;
        this.cond = new ExprPlan(cond);
    }

    public SelectPlan(Plan from, Plan cond){
        this.from = from;
        this.cond = cond;
    }

    @Override
    public String toString() {
        return "Select from {" + from.toString() + "} Condition {" + cond.toString() + "}";
    }
}
