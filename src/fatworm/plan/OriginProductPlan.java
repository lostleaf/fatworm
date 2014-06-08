package fatworm.plan;

import fatworm.scan.Scan;

import java.util.HashSet;
import java.util.List;

/**
 * Created by lostleaf on 14-6-7.
 */
public class OriginProductPlan implements Plan {
	
	private List<Plan> plans;
	private Plan parentPlan;
	
	public OriginProductPlan(List<Plan> plans, Plan parentPlan) {
		this.plans = plans;
		this.parentPlan = parentPlan;
	}
	
	public List<Plan> getPlans() {
		return plans;
	}

	@Override
	public Scan open(Scan father) {
		Plan p = plans.get(0);
		for (int i = 1; i < plans.size(); ++i) p = new ProductPlan(p, plans.get(i), parentPlan);
		return p.open(father);
	}

	@Override
	public Plan getPlan() {
		return null;
	}

	@Override
	public void setPlan(Plan p) {
		
	}

	@Override
	public Plan down() {
		for (int i = 0; i < plans.size(); ++i) plans.set(i, plans.get(i).down());
		return this;
	}

	@Override
	public void renameTable(String from, String to) {
        for (Plan plan : plans) plan.renameTable(from, to);
	}

	@Override
	public HashSet<String> getAllTblNames() {
		HashSet<String> hs = new HashSet<String>();
        for (Plan plan : plans) hs.addAll(plan.getAllTblNames());
		return hs;
	}

	@Override
	public HashSet<String> getAllUsedTblNames() {
		HashSet<String> hs = new HashSet<String>();
        for (Plan plan : plans) hs.addAll(plan.getAllUsedTblNames());
		return hs;
	}

	@Override
	public String getTblName(String fldName, boolean findFather) {
        for (Plan plan : plans) {
            String tblName = plan.getTblName(fldName, false);
            if (tblName != null) return tblName;
        }
		if (findFather) {
            for (Plan plan : plans) {
                String tblName = plan.getTblName(fldName, true);
                if (tblName != null) return tblName;
            }
		}
		return null;
	}
	
	public String toString() {
        return "Original ProductPlan ( " + plans.toString() + ")";
	}

	@Override
	public Plan getParentPlan() {
		return parentPlan;
	}
}
