package fatworm.plan;

import fatworm.expr.Expr;
import fatworm.scan.ExtendScan;
import fatworm.scan.Scan;

import java.util.HashSet;

public class ExtendPlan implements Plan {
	
	private Plan p;
	private String newName;
	private Expr expr;
	private Plan father;
	
	public ExtendPlan(Plan p, Expr expr, String newName, Plan father) {
		this.p = p;
		this.expr = expr;
		if (newName != null) {
			this.newName = newName.toLowerCase();
		} else {
			this.newName = null;
		}
		this.father = father;
	}
	
	public Expr getExpr() {
		return expr;
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
	public void renameTable(String from, String to) {
		p.renameTable(from, to);
		expr.renameTable(from, to);
	}

	@Override
	public HashSet<String> getAllTblNames() {
		return p.getAllTblNames();
	}

	@Override
	public Plan getParentPlan() {
		return father;
	}
}
