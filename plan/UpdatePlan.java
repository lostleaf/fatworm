package fatworm.plan;

import java.util.LinkedList;

/**
 * Created by lostleaf on 14-4-18.
 */
public class UpdatePlan extends Plan {
    ExprPlan to;
    Plan cond;
    LinkedList<ExprPlan> cols;
    LinkedList<Plan> values;

    public UpdatePlan(ExprPlan to, LinkedList<ExprPlan> cols, LinkedList<Plan> values, Plan cond) {
        this.to = to;
        this.cols = cols;
        this.values = values;
        this.cond = cond;
    }

    @Override
    public String toString() {
        String ret = "Update " + to + " {";
        for (int i = 0; i < cols.size(); i++)
            ret += "(" + cols.get(i) + " ," + values.get(i) + ")";
        return ret + "} where {" + cond + "}";
    }
}
