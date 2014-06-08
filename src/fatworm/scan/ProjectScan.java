package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-5.
 */
public class ProjectScan implements Scan {

    private Scan scan;
    private List<ColNameExpr> projs;
    private List<Integer> idx;
    private Scan parent;
    private int firstItem;

    private ArrayList<Const> nowRecord;
    private Map<String, Integer> idxMap;

    public ProjectScan(Scan scan, List<ColNameExpr> prs, Scan parentScan) {
        this.scan = scan;
        this.projs = new ArrayList<ColNameExpr>(prs);
        this.parent = parentScan;
        firstItem = 0;
        if (projs.get(0).getFldName().equals("*")) {
            firstItem = scan.getOriginColNum();
            projs.remove(0);
        }
        idx = new ArrayList<Integer>();
        for (ColNameExpr e : projs) {
            int i = scan.getColumnIndex(e);
            idx.add(i);
        }

        idxMap = new HashMap<String, Integer>();
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            String tbl = getTableName(i);
            Expression fld = getFieldName(i);
            if (fld instanceof ColNameExpr) {
                Expression exp = new ColNameExpr(tbl, ((ColNameExpr) fld).getFldName());
                idxMap.put(exp.toHashString(), i);
                exp = new ColNameExpr(null, ((ColNameExpr) fld).getFldName());
                idxMap.put(exp.toHashString(), i);
            } else {
                idxMap.put(fld.toHashString(), i);
            }
        }
    }

    public List<ColNameExpr> getProjs() {
        return projs;
    }

    @Override
    public void beforeFirst() {
        scan.beforeFirst();
    }

    public boolean next() {
        if (scan.next()) {
            if (firstItem > 0) {
                nowRecord = new ArrayList<Const>();
                nowRecord.addAll(scan.getNowRecord().subList(0, firstItem));
            } else {
                nowRecord = new ArrayList<Const>();
            }
            int size = idx.size();
            for (int i = 0; i < size; ++i) {
                nowRecord.add(scan.get(projs.get(i), true));
            }
            return true;
        }
        return false;
    }

    @Override
    public int getColumnCount() {
        return firstItem + projs.size();
    }

    @Override
    public int getColumnType(int columnIndex) {
        if (columnIndex < firstItem) return scan.getColumnType(columnIndex);
        return scan.getColumnType(idx.get(columnIndex - firstItem));
    }

    @Override
    public int getColumnType(Expression expr, boolean findParent) {
        int type = scan.getColumnType(expr, findParent);
        if (type != notFound) return type;
        return notFound;
    }

    @Override
    public int getColumnIndex(Expression expr) {
        int index = scan.getColumnIndex(expr);
        if (index < firstItem) return index;
        for (int i = 0; i < projs.size(); ++i) {
            if (idx.get(i) == index) return firstItem + i;
        }
        return notFound;
    }

    @Override
    public Const getColumn(int columnIndex) {
        if (columnIndex < firstItem) return scan.getColumn(columnIndex);
        return scan.getColumn(idx.get(columnIndex - firstItem));
    }

    @Override
    public Const getColumn(Expression expr, boolean findParent) {
        Const c = scan.getColumn(expr, findParent);
        if (c != null) return c;
        return null;
//		if (parent == null || !(s instanceof TableScan)) return null;
//		return parent.getColumn(expr);
    }

    @Override
    public void close() {
        scan.close();
    }

    @Override
    public Expression getFieldName(int columnIndex) {
        if (columnIndex < firstItem) return scan.getFieldName(columnIndex);
        return new ColNameExpr(null, projs.get(columnIndex - firstItem).getFldName());
    }

    @Override
    public String getTableName(int columnIndex) {
        if (columnIndex < firstItem) return scan.getTableName(columnIndex);
        return projs.get(columnIndex - firstItem).getTblName();
    }

    @Override
    public int getOriginColNum() {
        return getColumnCount();
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
        if (!findFather || parent == null) return null;
        return parent.get(expr, findFather);
    }

}
