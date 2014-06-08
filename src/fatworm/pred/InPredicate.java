package fatworm.pred;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.ConstExpr;
import fatworm.expr.Expression;
import fatworm.plan.Plan;
import fatworm.plan.ProjectPlan;
import fatworm.plan.SelectPlan;
import fatworm.scan.Scan;
import fatworm.util.ConstUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lostleaf on 14-6-7.
 */
public class InPredicate implements Predicate {

    private Expression e;
    private Plan p;

    public InPredicate(Expression e, Plan p) {
        this.e = e;
        this.p = p;
    }

    @Override
    public boolean isSatisfied(Scan s) {
        Const c = e.getResult(s);
//		boolean flag = false;
        Scan s1 = p.open(s);
        if (p instanceof ProjectPlan) {
            ProjectPlan pp = (ProjectPlan) p;
            List<ColNameExpr> projs = pp.getProjs();
            if (projs.size() == 1) {
                ColNameExpr cne = projs.get(0);
                if (!cne.getFldName().equals("*")) {
                    Plan subPlan = pp.getPlan();
                    Predicate pred = new CmpOpPredicate(cne, new ConstExpr(c), CmpOp.EQUAL);
                    List<Predicate> preds = new ArrayList<Predicate>();
                    preds.add(pred);
                    subPlan = new SelectPlan(subPlan, new AllAndPredicate(preds), subPlan.getParentPlan());
                    pp = new ProjectPlan(subPlan, projs, pp.getParentPlan());
                    s1.close();
                    s1 = pp.open(s);
                }
            }
        }
        while (s1.next()) {
            if (s1.getColumnCount() != 1) {
                s1.close();
                return false;
            }
            Const in = s1.get(0);
            in = ConstUtil.changeToType(in, c.getType());
            int compare = c.compareTo(in);
            if (compare == 0) {
                s1.close();
                return true;
            }
        }
        s1.close();
        return false;
    }

    public String toString() {
        return "Predicate in: ( " + "expr: ( " + e.toString() + " ), " + "plan: ( " + p.toString() + " ) )";
    }

    @Override
    public void renameTable(String from, String to) {
        e.renameTable(from, to);
        p.renameTable(from, to);
    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        return new HashSet<String>();
    }

    @Override
    public HashSet<String> getAllUsedTblNames(Plan p) {
        HashSet<String> s = e.getTblNames(p);
        s.addAll(p.getAllUsedTblNames());
        return s;
    }

}
