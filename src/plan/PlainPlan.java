package plan;

import org.antlr.runtime.tree.CommonTree;

/**
 * Created by lostleaf on 14-4-18.
 */
public class PlainPlan extends Plan {
    CommonTree ct;

    public PlainPlan(CommonTree ct) {
        this.ct = ct;
    }

    @Override
    public String toString() {
        return ct.toStringTree();
    }
}
