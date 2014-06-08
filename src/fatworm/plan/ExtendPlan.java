package fatworm.plan;

import fatworm.expr.Expression;
import fatworm.scan.ExtendScan;
import fatworm.scan.Scan;

import java.util.HashSet;

public class ExtendPlan implements Plan {
	
	private Plan p;
	private String newName;
	private Expression expr;
	private Plan father;
	
	public ExtendPlan(Plan p, Expression expr, String newName, Plan father) {
		this.p = p;
		this.expr = expr;
		if (newName != null) {
			this.newName = newName.toLowerCase();
		} else {
			this.newName = null;
		}
		this.father = father;
	}
	
	public Expression getExpr() {
		return expr;
	}
	
	public String getNewName() {
		return newName;
	}

	@Override
	public Scan open(Scan father) {
		Scan s = p.open(father);
		s = new ExtendScan(s, expr, newName, father);
		return s;
	}
	
	public String toString() {
        return "Extend plan ( " + "expr: ( " + expr.toString() + " ), " +
                "new name: " + newName + ", " + "plan: ( " + p.toString() + ") )";
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
		expr.renameTable(from, to);
	}

	@Override
	public HashSet<String> getAllTblNames() {
		return p.getAllTblNames();
	}

	@Override
	public String getTblName(String fldName, boolean findFather) {
		if (newName.equals(fldName)) return "new_name";
        return p.getTblName(fldName, findFather);
//		if (tblName != null) return tblName;
//		if (father == null || (!(p instanceof TablePlan))) return null;
//		return father.getTblName(fldName);
	}

	@Override
	public HashSet<String> getAllUsedTblNames() {
		HashSet<String> s = p.getAllUsedTblNames();
		s.addAll(expr.getTblNames(this));
		return s;
	}

	@Override
	public Plan getParentPlan() {
		return father;
	}
}
