package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expression;
import fatworm.util.Compare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-7.
 */
public class RenameScan implements Scan {
    private Scan s, father;
    private String newName;
    private List<Const> nowRecord;
    private Map<String, Integer> idxMap;

    public RenameScan(Scan s, String newName, Scan father) {
        this.s = s;
        this.newName = newName;
        this.father = father;

        idxMap = new HashMap<String, Integer>();
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            String tbl = getTableName(i);
            Expression fieldNameExpr = getFieldName(i);
            if (fieldNameExpr instanceof ColNameExpr) {
                Expression exp = new ColNameExpr(tbl, ((ColNameExpr)fieldNameExpr).getFldName());
                idxMap.put(exp.toHashString(), i);
                exp = new ColNameExpr(null, ((ColNameExpr)fieldNameExpr).getFldName());
                idxMap.put(exp.toHashString(), i);
            } else {
                idxMap.put(fieldNameExpr.toHashString(), i);
            }
        }
    }

    public String getNewName() {
        return newName;
    }

    public Scan getScan() {
        return s;
    }

    @Override
    public void beforeFirst() {
        s.beforeFirst();
    }

    @Override
    public boolean next() {
        if (s.next()) {
            nowRecord = s.getNowRecord();
            return true;
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
    public Expression getFieldName(int columnIndex) {
        return s.getFieldName(columnIndex);
    }

    @Override
    public String getTableName(int columnIndex) {
        return newName;
    }

    @Override
    public Const getColumn(Expression expr, boolean findParent) {
        int index = getColumnIndex(expr);
        if (index != notFound) return getColumn(index);
        if (father == null || !findParent) return null;
        return father.getColumn(expr, true);
    }

    @Override
    public int getColumnType(Expression expr, boolean findParent) {
        int index = getColumnIndex(expr);
        if (index != notFound) return getColumnType(index);
        if (father == null || !findParent) return notFound;
        return father.getColumnType(expr, true);
    }

    @Override
    public int getColumnIndex(Expression expr) {
        int columnCount = s.getColumnCount();
        int index = notFound;
        for (int i = 0; i < columnCount; ++i) {
            if (Compare.equalCol(getTableName(i), getFieldName(i), expr)) {
                index = i;
                break;
            }
        }
        return index;
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
        if (!findFather || father == null) return null;
        return father.get(expr, true);
    }

}
