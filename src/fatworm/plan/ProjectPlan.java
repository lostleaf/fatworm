package fatworm.plan;

import fatworm.expr.ColNameExpr;
import fatworm.scan.ProjectScan;
import fatworm.scan.Scan;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ProjectPlan implements Plan {
	
	private Plan p;
	private List<ColNameExpr> projs;
	private Plan father;
	
	public ProjectPlan(Plan p, List<ColNameExpr> projs, Plan father) {
		this.p = p;
		this.projs = projs;
		this.father = father;
	}
	
	public List<ColNameExpr> getProjs() {
		return projs;
	}

	@Override
	public Scan open(Scan father) {
		Scan s = p.open(father);
		s = new ProjectScan(s, projs, father);
		return s;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder("Project plan ( ");
		s.append("projs: ( ");
		for (int i = 0; i < projs.size(); ++i) {
			s.append(projs.get(i).toString());
			s.append(", ");
		}
		s.append(" ), ");
		s.append("plan: ( "); s.append(p.toString()); s.append(") )");
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
	public Plan down() {
		p = p.down();
		return this;
	}

	@Override
	public void renameTable(String from, String to) {
		p.renameTable(from, to);
		for (Iterator<ColNameExpr> iter = projs.iterator(); iter.hasNext(); ) {
			ColNameExpr cne = iter.next();
			cne.renameTable(from, to);
		}
	}

	@Override
	public HashSet<String> getAllTblNames() {
		return p.getAllTblNames();
	}

	@Override
	public String getTblName(String fldName, boolean findFather) {
		String tblName = p.getTblName(fldName, findFather);
		return tblName;
//		if (tblName != null) return tblName;
//		if (father == null || (!(p instanceof TablePlan))) return null;
//		return father.getTblName(fldName);
	}

	@Override
	public HashSet<String> getAllUsedTblNames() {
		return p.getAllUsedTblNames();
	}

	@Override
	public Plan getParentPlan() {
		return father;
	}
}
