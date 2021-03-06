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

    @Override
	public Scan open(Scan parentScan) {
		Plan p = plans.get(0);
		for (int i = 1; i < plans.size(); ++i) p = new ProductPlan(p, plans.get(i), parentPlan);
		return p.open(parentScan);
	}

	@Override
	public Plan getPlan() {
		return null;
	}

	@Override
	public void setPlan(Plan p) {
		
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
	
	public String toString() {
        return "Original ProductPlan ( " + plans.toString() + ")";
	}

	@Override
	public Plan getParentPlan() {
		return parentPlan;
	}
}
