package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expr;

import java.util.*;

/**
 * Created by lostleaf on 14-6-5.
 */
public class DistinctScan implements Scan {

    private Scan s;
    private Set<Item> allItem = null;
    private List<Item> itemList;
    private Iterator<Item> iterator;
    private Item nowItem;
    private Scan parentScan;

    private List<Const> nowRecord;
    private Map<String, Integer> idxMap;

    public DistinctScan(Scan s, Scan parentScan) {
        this.s = s;
        this.parentScan = parentScan;

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

    private class Item {
        public List<Const> rec;

        public Item(List<Const> rec) {
            this.rec = rec;
        }

        public String toString() {
            return rec.toString();
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public boolean equals(Object obj) {
            return obj instanceof Item && toString().equals(obj.toString());
        }

    }

    @Override
    public void beforeFirst() {
        iterator = itemList.iterator();
    }

    @Override
    public boolean next() {
        if (allItem == null) {
            itemList = new ArrayList<Item>();
            allItem = new HashSet<Item>();
            while (s.next()) {
                ArrayList<Const> item = new ArrayList<Const>();
                item.addAll(s.getNowRecord());
                Item newItem = new Item(item);
                if (allItem.contains(newItem)) continue;
                allItem.add(newItem);
                itemList.add(newItem);
            }
            iterator = itemList.iterator();
        }
        if (iterator.hasNext()) {
            nowItem = iterator.next();
            nowRecord = nowItem.rec;
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
        return nowItem.rec.get(columnIndex);
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
        Const c = s.getColumn(expr, findParent);
        return c;
//		if (c != null || (!(s instanceof TableScan))) return c;
//		if (parentScan == null) return null;
//		return parentScan.getColumn(expr);
    }

    @Override
    public int getColumnType(Expr expr, boolean findParent) {
        int t = s.getColumnType(expr, findParent);
        return t;
//		if (t != notFound || (!(s instanceof TableScan))) return t;
//		if (parentScan == null) return notFound;
//		return parentScan.getColumnType(expr);
    }

    @Override
    public int getColumnIndex(Expr expr) {
        return s.getColumnIndex(expr);
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
        if (!findFather || parentScan == null) return null;
        return parentScan.get(expr, true);
    }

}
