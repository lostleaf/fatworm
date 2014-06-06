package fatworm.plan;

import org.antlr.runtime.tree.CommonTree;

/**
 * Created by lostleaf on 14-4-17.
 */
public class RenamePlan extends Plan {
    CommonTree to;
    Plan from;

    public RenamePlan(Plan from, CommonTree to) {
        this.to = to;
        this.from = from;
    }

    @Override
    public String toString() {
        return "Rename from {" + from.toString() + "} to {" + to.toStringTree() + "}";
    }
}
