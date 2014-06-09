package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expr;
import fatworm.util.Compare;

import java.util.*;

/**
 * Created by lostleaf on 14-6-8.
 */
public class OrderScan implements Scan {

    private Scan s;
    private ColNameExpr colName;
    private boolean asc;
    private List<List<Const>> allItem = null;
    private Iterator<List<Const>> iter;
    private List<Const> nowItem;
    private Scan father;

    private List<Const> nowRecord;
    private Map<String, Integer> idxMap;

    public OrderScan(Scan s, ColNameExpr colName, boolean asc, Scan father) {
        this.s = s;
        this.colName = colName;
        this.asc = asc;
        this.father = father;

        idxMap = new HashMap<String, Integer>();
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            String tbl = getTableName(i);
            Expr fld = getFieldName(i);
            if (fld instanceof ColNameExpr) {
                Expr exp = new ColNameExpr(tbl, ((ColNameExpr)fld).getFldName());
                idxMap.put(exp.toHashString(), i);
                exp = new ColNameExpr(null, ((ColNameExpr)fld).getFldName());
                idxMap.put(exp.toHashString(), i);
            } else {
                idxMap.put(fld.toHashString(), i);
            }
        }
    }

    private class MyCamparator implements Comparator<List<Const>> {

        private int compareIndex;
        private boolean asc;

        public MyCamparator(int compareIndex, boolean asc) {
            this.compareIndex = compareIndex;
            this.asc = asc;
        }

        @Override
        public int compare(List<Const> a1, List<Const> a2) {
            if (asc)
                return a1.get(compareIndex).compareTo(a2.get(compareIndex));
            else
                return -a1.get(compareIndex).compareTo(a2.get(compareIndex));
        }

    }

    @Override
    public void beforeFirst() {
        iter = allItem.iterator();
    }

    @Override
    public boolean next() {
        if (allItem == null) {
            allItem = new ArrayList<List<Const>>();
            while (s.next()) {
//				int colNum = s.getColumnCount();
                ArrayList<Const> item = new ArrayList<Const>();
                item.addAll(s.getNowRecord());
//				for (int i = 0; i < colNum; ++i) {
//					item.add(s.getColumn(i));
//				}
                allItem.add(item);
            }
            if (allItem.isEmpty()) {
                iter = allItem.iterator();
            } else {
                int index = getColumnIndex(colName);
                Collections.sort(allItem, new MyCamparator(index, asc));
                iter = allItem.iterator();
            }
        }
        if (iter.hasNext()) {
            nowItem = iter.next();
            nowRecord = nowItem;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getColumnCount() {
        return s.getColumnCount();
    }

    @Override
    public int getColumnType(int columnIndex) {
        return s.getColumnType(columnIndex);
    }

    @Override
    public Const getColumn(int columnIndex) {
        return nowItem.get(columnIndex);
    }

    @Override
    public void close() {
        s.close();
    }

    @Override
    public Expr getFieldName(int columnIndex) {
        return s.getFieldName(columnIndex);
    }

    @Override
    public String getTableName(int columnIndex) {
        return s.getTableName(columnIndex);
    }

    @Override
    public Const getColumn(Expr expr, boolean findParent) {
        int idx = getColumnIndex(expr);
        if (idx != notFound) return nowItem.get(idx);
        if (father == null || !findParent) return null;
        return father.getColumn(expr, true);
//		if (!(s instanceof TableScan)) return null;
//		if (father == null) return null;
//		return father.getColumn(expr);
    }

    @Override
    public int getColumnType(Expr expr, boolean findParent) {
        int idx = getColumnIndex(expr);
        if (idx != notFound) return getColumnType(idx);
        if (father == null || !findParent) return notFound;
        return father.getColumnType(expr, true);
//		if (!(s instanceof TableScan)) return notFound;
//		if (father == null) return notFound;
//		return father.getColumnType(expr);
    }

    @Override
    public int getColumnIndex(Expr expr) {
        int columnCount = s.getColumnCount();
        int index = -1;
        for (int i = 0; i < columnCount; ++i) {
            if (Compare.equalCol(getTableName(i), getFieldName(i), expr)) {
                index = i;
                break;
            }
        }
        if (index >= 0) return index;
        return notFound;
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
    public Const get(Expr expr, boolean findFather) {
        if (idxMap.containsKey(expr.toHashString())) {
            return nowRecord.get(idxMap.get(expr.toHashString()));
        }
        if (!findFather || father == null) return null;
        return father.get(expr, true);
    }

}