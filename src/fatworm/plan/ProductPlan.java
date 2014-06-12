package fatworm.plan;

import fatworm.scan.*;

import java.util.HashSet;

public class ProductPlan implements Plan {
	
	private Plan p1, p2;
	private Plan parentPlan;
	
	public ProductPlan(Plan p1, Plan p2, Plan parentPlan) {
		this.p1 = p1;
		this.p2 = p2;
		this.parentPlan = parentPlan;
	}

	@Override
	public Scan open(Scan parentScan) {
		Scan s1 = p1.open(parentScan);
		Scan s2 = p2.open(parentScan);
        return new ProductScan(s1, s2, parentScan);
	}
	
	public String toString() {
        return "Product plan ( " + "plan a: ( " + p1.toString() + "), "
                + "plan b: ( " + p2.toString() + ") )";
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
		p1.renameTable(from, to);
		p2.renameTable(from, to);
	}

	@Override
	public HashSet<String> getAllTblNames() {
		HashSet<String> s1 = p1.getAllTblNames();
		HashSet<String> s2 = p2.getAllTblNames();
		s1.addAll(s2);
		return s1;
	}

	@Override
	public Plan getParentPlan() {
		return parentPlan;
	}
}
