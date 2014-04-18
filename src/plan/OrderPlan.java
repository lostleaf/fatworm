package plan;

import org.antlr.runtime.tree.CommonTree;

/**
 * Created by lostleaf on 14-4-18.
 */
public class OrderPlan extends Plan{
    Plan from;
    CommonTree by;

    public OrderPlan(Plan from, CommonTree by) {
        this.from = from;
        this.by = by;
    }

    @Override
    public String toString() {
        return "Order {" + from.toString() + "} by {(" + by.toStringTree().substring(6) + "}";
    }
}
