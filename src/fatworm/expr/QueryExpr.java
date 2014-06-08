package fatworm.expr;

import fatworm.constant.Const;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-7.
 */
public class QueryExpr implements Expression {
	
	private Plan p;
	
	public QueryExpr(Plan p) {
		this.p = p;
	}

	@Override
	public Const getResult(Scan s) {
		Scan qs = p.open(s);
		if (!qs.next()) return null;
		int colNum = qs.getColumnCount();
		if (colNum != 1) return null;
		Const res = qs.get(qs.getFieldName(0), true);
		if (qs.next()) return null;
		return res;
	}

	@Override
	public int getType(Scan s) {
		Scan qs = p.open(s);
		return qs.getColumnType(qs.getFieldName(0), true);
	}
	
	public String toString() {
        return "query: (" + p.toString() + " )";
	}

	@Override
	public void renameTable(String from, String to) {
		p.renameTable(from, to);
	}

	@Override
	public HashSet<String> getTblNames(Plan plan) {
//		not sure
		return p.getAllUsedTblNames();
//		return new HashSet<String>();
	}

	@Override
	public String toHashString() {
		return null;
	}
	
}
