package fatworm.plan;

import org.antlr.runtime.tree.CommonTree;

/**
 * Created by lostleaf on 14-4-18.
 */
public class GroupPlan extends Plan {
    CommonTree expr, cond;
    Plan from;

    public GroupPlan(CommonTree cond, CommonTree expr, Plan from) {
        this.cond = cond;
        this.expr = expr;
        this.from = from;
    }

    @Override
    public String toString() {
        return "Group {" + expr.toStringTree() + "} having {" + (cond == null ? "":cond.toStringTree())
                + "} from {" + from.toString() + "}";
    }
}
