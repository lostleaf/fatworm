package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expr;
import fatworm.handler.Fucker;
import fatworm.util.Compare;
import fatworm.memory.MemoryRecordFile;
import fatworm.meta.Attribute;
import fatworm.meta.RecordFile;
import fatworm.meta.Schema;
import fatworm.meta.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-6.
 */
public class TableScan implements UpdateScan {

    private String tblName;
    private Scan father;
    private RecordFile rf;
    private Table table;
    private Schema schema;
    private List<Attribute> attrs;
    private int columnCount;
    private List<Const> nowRecord;
    private Map<String, Integer> idxMap;

    public TableScan(String tblName, Scan father) {
        this.tblName = tblName;
        this.father = father;
        rf = new MemoryRecordFile(tblName);
        table = Fucker.getDBManager().getCurrentDB().getTable(tblName);
        schema = table.getSchema();
        attrs = schema.getAttributes();
        columnCount = attrs.size();

        idxMap = new HashMap<String, Integer>();
        for (int i = 0; i < columnCount; ++i) {
            String tbl = getTableName(i);
            Expr fld = getFieldName(i);
            Expr exp = new ColNameExpr(tbl, ((ColNameExpr) fld).getFldName());
            idxMap.put(exp.toHashString(), i);
            exp = new ColNameExpr(null, ((ColNameExpr) fld).getFldName());
            idxMap.put(exp.toHashString(), i);
        }
    }

    public String getTblName() {
        return tblName;
    }

    @Override
    public void beforeFirst() {
        rf.beforeFirst();
    }

    @Override
    public boolean next() {
        if (rf.next()) {
            nowRecord = rf.getRecord();
            return true;
        }
        return false;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public int getColumnType(int columnIndex) {
        return attrs.get(columnIndex).getType().getType();
    }

    @Override
    public Const getColumn(int columnIndex) {
        return nowRecord.get(columnIndex);
    }

    @Override
    public void close() {
        rf.close();
    }

    @Override
    public ColNameExpr getFieldName(int columnIndex) {
        return new ColNameExpr(null, attrs.get(columnIndex).getAttrName());
    }

    @Override
    public String getTableName(int columnIndex) {
        return tblName;
    }

    @Override
    public Const getColumn(Expr expr, boolean findParent) {
        for (int i = 0; i < columnCount; ++i) {
            if (Compare.equalCol(getTableName(i), getFieldName(i), expr)) {
                return nowRecord.get(i);
            }
        }
        if (father == null || !findParent) return null;
        return father.getColumn(expr, findParent);
    }

    @Override
    public int getColumnType(Expr expr, boolean findParent) {
        for (int i = 0; i < columnCount; ++i) {
            if (Compare.equalCol(getTableName(i), getFieldName(i), expr)) {
                return getColumnType(i);
            }
        }
        if (father == null || !findParent) return notFound;
        return father.getColumnType(expr, findParent);
    }

    @Override
    public int getColumnIndex(Expr expr) {
        for (int i = 0; i < columnCount; ++i) {
            if (Compare.equalCol(getTableName(i), getFieldName(i), expr)) {
                return i;
            }
        }
        return notFound;
    }

    @Override
    public int getOriginColNum() {
        return columnCount;
    }

    @Override
    public void setValue(Expr expr, Const val) {
        int idx = schema.getIdx(((ColNameExpr) expr).getFldName());
        setValue(idx, val);
    }

    @Override
    public void setValue(int idx, Const val) {
        nowRecord.set(idx, val);
        rf.setValue(idx, val);
    }

    @Override
    public void insert(List<Const> values) {
        rf.insert(values);
    }

    @Override
    public void delete() {
        rf.delete();
    }

    public String toString() {
        return "Table Scan: " + tblName;
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
        return father.get(expr, findFather);
    }

}
