package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.Expression;

import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class DistinctScan implements Scan {
    public DistinctScan(Scan s, Scan father) {
        super();
    }

    //TODO unimplemented
    @Override
    public void beforeFirst() {

    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public int getColumnType(int columnIndex) {
        return 0;
    }

    @Override
    public int getColumnType(Expression expr, boolean findFather) {
        return 0;
    }

    @Override
    public int getColumnIndex(Expression expr) {
        return 0;
    }

    @Override
    public Const getColumn(int columnIndex) {
        return null;
    }

    @Override
    public Const getColumn(Expression expr, boolean findFather) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public Expression getFieldName(int columnIndex) {
        return null;
    }

    @Override
    public String getTableName(int columnIndex) {
        return null;
    }

    @Override
    public int getOriginColNum() {
        return 0;
    }

    @Override
    public Scan getParent() {
        return null;
    }

    @Override
    public List<Const> getNowRecord() {
        return null;
    }

    @Override
    public Const get(int columnIndex) {
        return null;
    }

    @Override
    public Const get(Expression expr, boolean findFather) {
        return null;
    }
}
