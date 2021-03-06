package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expr;
import fatworm.pred.Predicate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-5.
 */
public class SelectScan implements UpdateScan {

    private Scan s, father;
    private Predicate pred;
    private List<Const> nowRecord;
    private Map<String, Integer> idxMap;

    public SelectScan(Scan s, Predicate pred, Scan father) {
        this.s = s;
        this.pred = pred;
        this.father = father;

        idxMap = new HashMap<String, Integer>();
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            String tbl = getTableName(i);
            Expr fld = getFieldName(i);
            if (fld instanceof ColNameExpr) {
                Expr exp = new ColNameExpr(tbl, ((ColNameExpr) fld).getFldName());
                idxMap.put(exp.toHashString(), i);
                exp = new ColNameExpr(null, ((ColNameExpr) fld).getFldName());
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

    @Override
    public boolean next() {
        while (s.next()) {
            nowRecord = s.getNowRecord();
            if (pred.isSatisfied(this)) {
                return true;
            }
        }
        return false;
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
        return s.getColumn(columnIndex);
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
        return s.getColumn(expr, findParent);
    }

    @Override
    public int getColumnType(Expr expr, boolean findParent) {
        return s.getColumnType(expr, findParent);
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
    public void setValue(Expr expr, Const val) {
        ((TableScan) s).setValue(expr, val);
    }

    @Override
    public void setValue(int idx, Const val) {
        ((TableScan) s).setValue(idx, val);
    }

    @Override
    public void insert(List<Const> values) {
        ((TableScan) s).insert(values);
    }

    @Override
    public void delete() {
        ((TableScan) s).delete();
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
