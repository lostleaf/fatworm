package fatworm.plan;

import fatworm.expr.ColNameExpr;
import fatworm.scan.ProjectScan;
import fatworm.scan.Scan;

import java.util.HashSet;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class ProjectPlan implements Plan {

    private Plan p;
    private List<ColNameExpr> projs;
    private Plan parentPlan;

    public ProjectPlan(Plan p, List<ColNameExpr> projs, Plan parentPlan) {
        this.p = p;
        this.projs = projs;
        this.parentPlan = parentPlan;
    }

    public List<ColNameExpr> getProjs() {
        return projs;
    }

    @Override
    public Scan open(Scan parentScan) {
        Scan s = p.open(parentScan);
        s = new ProjectScan(s, projs, parentScan);
        return s;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Project plan ( projs: ( ");
        for (ColNameExpr proj : projs) s.append(proj.toString()).append(", ");
        s.append(" ), plan: ( ").append(p.toString()).append(") )");
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
        for (ColNameExpr cne : projs) cne.renameTable(from, to);
    }

    @Override
    public HashSet<String> getAllTblNames() {
        return p.getAllTblNames();
    }

    @Override
    public Plan getParentPlan() {
        return parentPlan;
    }
}
