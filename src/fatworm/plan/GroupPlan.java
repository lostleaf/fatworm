package fatworm.plan;

import fatworm.expr.ColNameExpr;
import fatworm.expr.FuncExpr;
import fatworm.scan.GroupScan;
import fatworm.scan.Scan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lostleaf on 14-6-7.
 */
public class GroupPlan implements Plan {
    private Plan p;
    private ColNameExpr colName;
    private List<FuncExpr> funcs;
    private Plan parentPlan;

    public GroupPlan(Plan p, ColNameExpr colName, Plan parentPlan) {
        this.p = p;
        this.colName = colName;
        funcs = new ArrayList<FuncExpr>();
        this.parentPlan = parentPlan;
    }

    public void addFuncs(List<FuncExpr> f) {
        funcs.addAll(f);
    }

    @Override
    public Scan open(Scan father) {
        Scan s = p.open(father);
        s = new GroupScan(s, colName, funcs, father);
        return s;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Group plan ( col name: ( ");
        s.append(colName == null ? "null" : colName.toString()).append(" ), ").append("funcs: ( ");

        for (FuncExpr func : funcs) s.append(func.toString()).append(", ");

        s.append(" ), ").append("plan: ( ").append(p.toString()).append(") )");
        return s.toString();
    }

    @Override
    public Plan getPlan() {
        return p;
    }

    @Override
    public void setPlan(Plan p) {
        this.p = p;
    }


    @Override
    public void renameTable(String from, String to) {
        p.renameTable(from, to);
        colName.renameTable(from, to);
        for (FuncExpr expr : funcs) expr.renameTable(from, to);
    }

    @Override
    public HashSet<String> getAllTblNames() {
        return p.getAllTblNames();
    }

    @Override
    public String getTblName(String fldName, boolean findFather) {
        return p.getTblName(fldName, findFather);
//		if (tblName != null) return tblName;
//		if (parentPlan == null || (!(p instanceof TablePlan))) return null;
//		return parentPlan.getTblName(fldName);
    }

    @Override
    public HashSet<String> getAllUsedTblNames() {
        HashSet<String> s = p.getAllUsedTblNames();
        if (colName != null) s.addAll(colName.getTblNames(this));
        for (FuncExpr fe : funcs) s.addAll(fe.getTblNames(this));
        return s;
    }

    @Override
    public Plan getParentPlan() {
        return parentPlan;
    }
}
