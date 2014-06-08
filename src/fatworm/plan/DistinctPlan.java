package fatworm.plan;

import fatworm.scan.DistinctScan;
import fatworm.scan.Scan;

import java.util.HashSet;

public class DistinctPlan implements Plan {
	
	private Plan p;
	private Plan parentPlan;
	
	public DistinctPlan(Plan p, Plan parentPlan) {
		this.p = p;
		this.parentPlan = parentPlan;
	}
	
	@Override
	public Scan open(Scan father) {
		Scan s = p.open(father);
		s = new DistinctScan(s, father);
		return s;
	}
	
	public String toString() {
        return "Distinct plan ( " + p.toString() + " )";
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
		return p.getAllUsedTblNames();
	}

	@Override
	public Plan getParentPlan() {
		return parentPlan;
	}

}
