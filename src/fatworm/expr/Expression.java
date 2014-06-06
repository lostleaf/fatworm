package fatworm.expr;

import fatworm.constant.Const;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */

public interface Expression {

    public Const getResult(Scan s);
    public int getType(Scan s);

    public void renameTable(String from, String to);

    public HashSet<String> getTblNames(Plan p);

    public String toHashString();

}

