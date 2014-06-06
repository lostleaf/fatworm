package fatworm.plan;

import org.antlr.runtime.tree.CommonTree;

/**
 * Created by lostleaf on 14-4-17.
 */
public class ExprPlan extends Plan {
    CommonTree ct;

    public ExprPlan(CommonTree ct) {
        this.ct = ct;
    }

    @Override
    public String toString() {
        return ct == null ? "" : ct.toStringTree();
    }
}
