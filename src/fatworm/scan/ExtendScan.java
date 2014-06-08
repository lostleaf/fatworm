package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expression;
import fatworm.util.Compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendScan implements Scan {
	
	private Scan s;
	private Expression expr;
	private String newName;
	private Scan father;
	
	private ArrayList<Const> nowRecord;
	private Map<String, Integer> idxMap;
	
	public ExtendScan(Scan s, Expression expr, String newName, Scan father) {
		this.s = s;
		this.expr = expr;
		this.newName = newName;
		this.father = father;
		
		idxMap = new HashMap<String, Integer>();
		int columnCount = getColumnCount();
		for (int i = 0; i < columnCount; ++i) {
			String tbl = getTableName(i);
			Expression fld = getFieldName(i);
			if (fld instanceof ColNameExpr) {
				Expression exp = new ColNameExpr(tbl, ((ColNameExpr)fld).getFldName());
				idxMap.put(exp.toHashString(), i);
				exp = new ColNameExpr(null, ((ColNameExpr)fld).getFldName());
				idxMap.put(exp.toHashString(), i);
			} else {
				idxMap.put(fld.toHashString(), i);
			}
		}
	}

	@Override
	public void beforeFirst() {
		s.beforeFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean next() {
		if (s.next()) {
			nowRecord = (ArrayList<Const>) ((ArrayList<Const>) s.getNowRecord()).clone();
			nowRecord.add(expr.getResult(s));
			return true;
		}
		return false;
	}

	@Override
	public int getColumnCount() {
		return s.getColumnCount() + 1;
	}

	@Override
	public int getColumnType(int columnIndex) {
		if (columnIndex < s.getColumnCount()) return s.getColumnType(columnIndex);
		return expr.getType(s);
	}

	@Override
	public Const getColumn(int columnIndex) {
		if (columnIndex < s.getColumnCount()) return s.getColumn(columnIndex);
		return expr.getResult(s);
	}

	@Override
	public void close() {
		s.close();
	}

	@Override
	public Expression getFieldName(int columnIndex) {
		if (columnIndex < s.getColumnCount()) return s.getFieldName(columnIndex);
		return new ColNameExpr(null, newName);
	}

	@Override
	public String getTableName(int columnIndex) {
		if (columnIndex < s.getColumnCount()) return s.getTableName(columnIndex);
		return null;
	}

	@Override
	public Scan getParent() {
		return father;
	}

	@Override
	public Const getColumn(Expression exp, boolean findFather) {
		int colCount = getColumnCount();
		if (Compare.equalCol(getTableName(colCount - 1), getFieldName(colCount - 1), exp)) {
			return expr.getResult(s);
		}
		Const c = s.getColumn(exp, findFather);
		return c;
//		if (c != null || (!(s instanceof TableScan))) return c;
//		if (father == null) return null;
//		return father.getColumn(exp);
	}

	@Override
	public int getColumnType(Expression exp, boolean findFather) {
		int colCount = getColumnCount();
		if (Compare.equalCol(getTableName(colCount - 1), getFieldName(colCount - 1), exp)) {
			return expr.getType(s);
		}
		int t = s.getColumnType(exp, findFather);
		return t;
//		if (t != notFound || (!(s instanceof TableScan))) return t;
//		if (father == null) return notFound;
//		return father.getColumnType(exp);
	}

	@Override
	public int getColumnIndex(Expression exp) {
		int colCount = getColumnCount();
		if (Compare.equalCol(getTableName(colCount - 1), getFieldName(colCount - 1), exp)) {
			return colCount - 1;
		}
		return s.getColumnIndex(exp);
	}

	@Override
	public int getOriginColNum() {
		return s.getOriginColNum();
	}

	@Override
	public List<Const> getNowRecord() {
		return nowRecord;
	}

	@Override
	public Const get(int columnIndex) {
		return nowRecord.get(columnIndex);
	}

	@Override
	public Const get(Expression expr, boolean findFather) {
		if (idxMap.containsKey(expr.toHashString())) {
			return nowRecord.get(idxMap.get(expr.toHashString()));
		}
		if (!findFather || father == null) return null;
		return father.get(expr, findFather);
	}

}
