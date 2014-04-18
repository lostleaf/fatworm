package plan;

import org.antlr.runtime.tree.CommonTree;

/**
 * Created by lostleaf on 14-4-17.
 */
public class TablePlan extends Plan {
    CommonTree ct;

    public TablePlan(CommonTree ct) {
        this.ct = ct;
    }

    @Override
    public String toString() {
        return ct.toStringTree();
    }
}
