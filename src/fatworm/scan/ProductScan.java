package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductScan implements Scan {
	
	private Scan s1, s2, father;
	private boolean hasNext;
	private ArrayList<Const> nowRecord;
	private Map<String, Integer> idxMap;
	private int left, right;
	
	public ProductScan(Scan s1, Scan s2, Scan father) {
		this.s1 = s1;
		this.s2 = s2;
		this.father = father;
		hasNext = s1.next();
        nowRecord = new ArrayList<Const>(s1.getNowRecord());
//		nowRecord = (ArrayList<Const>) ((ArrayList<Const>) s1.getNowRecord()).clone();
		
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
		left = s1.getColumnCount();
		right = getColumnCount();
	}

//	@SuppressWarnings("unchecked")
	@Override
	public void beforeFirst() {
		s1.beforeFirst();
		s2.beforeFirst();
		hasNext = s1.next();
        nowRecord = new ArrayList<>(s1.getNowRecord());
//		nowRecord = (ArrayList<Const>) ((ArrayList<Const>) s1.getNowRecord()).clone();
	}

	public boolean next() {
		if (!hasNext) return false;
		if (s2.next()) {
			if (nowRecord.size() > left) {
				nowRecord.subList(left, right).clear();
			}
			nowRecord.addAll(s2.getNowRecord());
			return true;
		}
		s2.beforeFirst();
		hasNext = s1.next();
		if (hasNext && s2.next()) {
			nowRecord = new ArrayList<Const>();
			nowRecord.addAll(s1.getNowRecord());
			nowRecord.addAll(s2.getNowRecord());
			return true;
		}
		return false;
	}

	@Override
	public int getColumnCount() {
		return s1.getColumnCount() + s2.getColumnCount();
	}

	@Override
	public int getColumnType(int columnIndex) {
		if (columnIndex < s1.getColumnCount()) {
			return s1.getColumnType(columnIndex);
		} else {
			return s2.getColumnType(columnIndex - s1.getColumnCount());
		}
	}

	@Override
	public Const getColumn(int columnIndex) {
		if (columnIndex < s1.getColumnCount()) {
			return s1.getColumn(columnIndex);
		} else {
			return s2.getColumn(columnIndex - s1.getColumnCount());
		}
	}

	@Override
	public void close() {
		s1.close();
		s2.close();
	}

	@Override
	public Expression getFieldName(int columnIndex) {
		if (columnIndex < s1.getColumnCount()) {
			return s1.getFieldName(columnIndex);
		} else {
			return s2.getFieldName(columnIndex - s1.getColumnCount());
		}
	}

	@Override
	public String getTableName(int columnIndex) {
		if (columnIndex < s1.getColumnCount()) {
			return s1.getTableName(columnIndex);
		} else {
			return s2.getTableName(columnIndex - s1.getColumnCount());
		}
	}

    @Override
	public Const getColumn(Expression expr, boolean findParent) {
		Const c = s1.getColumn(expr, false);
		if (c != null) return c;
		c = s2.getColumn(expr, findParent);
		if (c != null) return c;
		if (findParent) {
			c = s1.getColumn(expr, true);
			if (c != null) return c;
		}
		return null;
	}

	@Override
	public int getColumnType(Expression expr, boolean findParent) {
		int type = s1.getColumnType(expr, false);
		if (type != notFound) return type;
		type = s2.getColumnType(expr, findParent);
		if (type != notFound) return type;
		if (findParent) {
			type = s1.getColumnType(expr, true);
			if (type != notFound) return type;
		}
		return notFound;
//		if (father == null || !(s1 instanceof TableScan && s2 instanceof TableScan)) return notFound;
//		return father.getColumnType(expr);
	}

	@Override
	public int getColumnIndex(Expression expr) {
		int index = s1.getColumnIndex(expr);
		if (index != notFound) return index;
		index = s2.getColumnIndex(expr);
		if (index != notFound) return index + s1.getColumnCount();
		return notFound;
	}

	@Override
	public int getOriginColNum() {
		return s1.getOriginColNum() + s2.getOriginColNum();
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
